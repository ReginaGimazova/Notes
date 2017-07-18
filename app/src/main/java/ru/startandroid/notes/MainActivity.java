package ru.startandroid.notes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private com.mikepenz.materialdrawer.Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(

                        new PrimaryDrawerItem().withName(R.string.title_geometry).withIcon(FontAwesome.Icon.faw_superscript).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.title_table).withIcon(FontAwesome.Icon.faw_table).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.title_notes).withIcon(FontAwesome.Icon.faw_edit).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.title_chat).withIcon(FontAwesome.Icon.faw_envelope_o).withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            String name = ((Nameable)drawerItem).getName().getText(MainActivity.this);
                            mToolbar.setTitle(name);
                        }

                        if (drawerItem != null){
                            int selectedScren = drawerItem.getIdentifier();
                            switch (selectedScren){
                                case 1:
                                    Toast.makeText(getApplication(), "Приведение к каноническому виду", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    openFragment(new Table(), "Таблица");
                                    break;
                                case 3:
                                    openFragment(new NoteListFragment(), "Заметки");
                                    break;
                                case 4:
                                    Toast.makeText(getApplication(), "Чат", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }


                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);

                    }

                    @Override
                    public void onDrawerClosed(View view) {}

                    @Override
                    public void onDrawerSlide(View view, float v) {}
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();
        if (savedInstanceState == null){
            result.setSelection(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void openFragment(final Fragment fragment, String title){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }
}
