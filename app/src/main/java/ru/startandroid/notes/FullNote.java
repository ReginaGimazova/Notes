package ru.startandroid.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FullNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_note);

        TextView title_view = (TextView)findViewById(R.id.title_view);
        TextView content_view = (TextView)findViewById(R.id.content_view);

        if (getIntent().getExtras() != null){
            Note note = (Note)getIntent().getExtras().getSerializable(NoteListFragment.TAG_NOTE);
            title_view.setText(note.getTitle());
            content_view.setText(note.getContent());
        }
    }
}
