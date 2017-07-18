package ru.startandroid.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

    private List<Note> mNotes;
    private Context mContext;
    private OnEventClickListener_ onEventClickListener_;


    public NoteListAdapter(List<Note> notes, Context context, OnEventClickListener_ onEventClickListener_){
        mNotes = notes;
        mContext = context;
        this.onEventClickListener_ = onEventClickListener_;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.noteTitle.setText(mNotes.get(position).getTitle());
        holder.noteCreateDate.setText(getReadableModifiedDate(mNotes.get(position).getDateModified()));
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onEventClickListener_.onLongClickListener(mNotes.get(position));
                return true;
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onEventClickListener_.onClickListener(mNotes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView noteTitle, noteCreateDate;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView)itemView.findViewById(R.id.text_view_note_title);
            noteCreateDate = (TextView)itemView.findViewById(R.id.text_view_note_date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
        }
    }

    public static String getReadableModifiedDate(long date){
        String displayDate = new SimpleDateFormat("MMM dd, yyyy - h:mm a").format(new Date(date));
        return displayDate;
    }

    public interface OnEventClickListener_ {
        void onLongClickListener(Note note);
        void onClickListener(Note note);
    }
}