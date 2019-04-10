package net.ddns.bivor.group14_ic10;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    FragmentCommunication mCommunicator;

    ArrayList<Note> mData;

    public NoteAdapter(ArrayList<Note> mData, FragmentCommunication mCommunicator) {
        this.mCommunicator = mCommunicator;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note note =mData.get(i);
        viewHolder.note = note;
        viewHolder.textViewNote.setText(note.text);
        viewHolder.selectedIndex = i;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNote;
        ImageView imageViewDelete;

        int selectedIndex;

        Note note;

       // ArrayList<Note> notes;

        FragmentCommunication mCommunication;

        public ViewHolder(@NonNull final View itemView, FragmentCommunication communicator) {
            super(itemView);

            textViewNote = itemView.findViewById(R.id.textViewNote);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);

            mCommunication = communicator;

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewDelete.setEnabled(false);
                    mCommunication.deleteNote(note, selectedIndex);
                }
            });

            textViewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommunication.goToDisplayNote(note);
                }
            });

        }
    }

}
