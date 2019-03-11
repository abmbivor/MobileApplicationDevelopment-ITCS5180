package net.ddns.bivor.a801028228_midterm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder>{

    ArrayList<Track> mData;

    public TrackAdapter(ArrayList<Track> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewItemTrack, textViewItemPrice, textViewItemArtist, textViewItemDate;
        Track song;
        ArrayList<Track> songList;
//        String keyword, state;
//        int progressValue;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

//            textViewItemTrack = itemView.findViewById(R.id.textViewItemTrack);
//            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
//            textViewItemArtist = itemView.findViewById(R.id.textViewItemArtist);
//            textViewItemDate = itemView.findViewById(R.id.textViewItemDate);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(), Display.class);
//                    intent.putExtra(SONG_KEY,song);
//                    intent.putExtra(SONGS_KEY,songList);
////                    intent.putExtra(KEYWORD,keyword);
////                    intent.putExtra(STATE,state);
////                    intent.putExtra(PROGRESS_VALUE,progressValue);
//                    itemView.getContext().startActivity(intent);
//                }
//            });

        }
    }
}
