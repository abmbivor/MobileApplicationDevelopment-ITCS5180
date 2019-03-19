package net.ddns.bivor.group14_hw03;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    ArrayList<Song> mData;
    String keyword, state;
    int progressValue;
    static final String SONG_KEY = "SONG_KEY";
    static final String SONGS_KEY = "SONGS_KEY";
    static final String KEYWORD = "KEYWORD";
    static final String STATE = "STATE";
    static final String PROGRESS_VALUE = "PROGRESS_VALUE";

//    public SongAdapter(ArrayList<Song> mData, String keyword, String state, int progressValue)
    public SongAdapter(ArrayList<Song> mData)
    {
        this.mData = mData;
//        this.keyword = keyword;
//        this.state = state;
//        this.progressValue = progressValue;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = mData.get(i);
        viewHolder.textViewItemArtist.setText("Artist: "+song.artistName);
        viewHolder.textViewItemTrack.setText("Track: "+song.trackName);
        viewHolder.textViewItemPrice.setText("Price: "+song.trackPrice+" $");
        String date = song.dateTime;
        viewHolder.textViewItemDate.setText("Date: "+date.substring(5,7)+"-"+date.substring(8,10)+"-"+date.substring(0,4));
        viewHolder.song = song;
        viewHolder.songList = mData;
//        viewHolder.keyword = keyword;
//        viewHolder.state = state;
//        viewHolder.progressValue = progressValue;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewItemTrack, textViewItemPrice, textViewItemArtist, textViewItemDate;
        Song song;
        ArrayList<Song> songList;
//        String keyword, state;
//        int progressValue;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewItemTrack = itemView.findViewById(R.id.textViewItemTrack);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemArtist = itemView.findViewById(R.id.textViewItemArtist);
            textViewItemDate = itemView.findViewById(R.id.textViewItemDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Display.class);
                    intent.putExtra(SONG_KEY,song);
                    intent.putExtra(SONGS_KEY,songList);
//                    intent.putExtra(KEYWORD,keyword);
//                    intent.putExtra(STATE,state);
//                    intent.putExtra(PROGRESS_VALUE,progressValue);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }


}
