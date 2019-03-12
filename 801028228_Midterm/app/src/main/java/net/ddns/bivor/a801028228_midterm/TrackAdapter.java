package net.ddns.bivor.a801028228_midterm;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder>{

    ArrayList<Track> mData;

    public TrackAdapter(ArrayList<Track> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Track track = mData.get(i);
        viewHolder.textViewItemArtist.setText("Artist: "+track.artist_name);
        viewHolder.textViewItemTrack.setText("Track: "+track.track_name);
        viewHolder.textViewItemAlbum.setText("Album: "+track.album_name);
        String date = track.updated_time;
        viewHolder.textViewItemDate.setText("Date: "+date.substring(5,7)+"-"+date.substring(8,10)+"-"+date.substring(0,4));
        viewHolder.track = track;
        viewHolder.trackList = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewItemTrack, textViewItemArtist, textViewItemAlbum, textViewItemDate;
        Track track;
        ArrayList<Track> trackList;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewItemTrack = itemView.findViewById(R.id.textViewItemTrack);
            textViewItemArtist = itemView.findViewById(R.id.textViewItemArtist);
            textViewItemAlbum = itemView.findViewById(R.id.textViewItemAlbum);
            textViewItemDate = itemView.findViewById(R.id.textViewItemDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if(track.track_share_url!="N/A") {
                        intent.setData(Uri.parse(track.track_share_url));
                        itemView.getContext().startActivity(intent);
                    }
                    else{
                        Toast.makeText(itemView.getContext(), "No Track Share URL Found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
