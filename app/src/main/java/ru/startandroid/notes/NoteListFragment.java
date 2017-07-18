package ru.startandroid.notes;

import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static ru.startandroid.notes.Constants.COLUMN_ID;


public class NoteListFragment extends Fragment implements NoteListAdapter.OnEventClickListener_ {

    private Button mFab;
    private View mRootView;
    private List<Note> mNotes;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String TAG_NOTE = "NOTE";

    public NoteListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        mFab = (Button) mRootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoteEditorActivity.class));
            }
        });
        setupList();
        return mRootView;
    }


    private void setupList() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector =
                new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);
                    Note selectedNote = mNotes.get(position);
                    Intent editorIntent = new Intent(getActivity(), NoteEditorActivity.class);
                    editorIntent.putExtra("id", selectedNote.getId());
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        mNotes = NoteManager.newInstance(getActivity()).getAllNotes();
        mAdapter = new NoteListAdapter(mNotes, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLongClickListener(final Note note) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Вы уверены, что хотите удалить эту заметку" + "?");
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteManager.newInstance(getActivity()).delete(note);
                mNotes.remove(note);
                mAdapter = new NoteListAdapter(mNotes, getActivity(), NoteListFragment.this);
                mRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onClickListener(Note note) {
        Intent intent = new Intent(this.getContext(), FullNote.class);
        intent.putExtra(TAG_NOTE, note);
        startActivity(intent);
    }
}