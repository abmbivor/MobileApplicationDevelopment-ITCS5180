package net.ddns.bivor.group14_inclass12;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder>{

    ArrayList<Trip> mData;
    private FragmentCommunication mCommunicator;

    public TripsAdapter(ArrayList<Trip> mData, FragmentCommunication mCommunicator) {
        this.mData = mData;
        this.mCommunicator = mCommunicator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mData.get(position);
        holder.trip = trip;
        holder.textView.setText(trip.destination.name);
        holder.selectedIndex = position;
        holder.trips = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        Trip trip;
        int selectedIndex;
        ArrayList<Trip> trips;
        FragmentCommunication mCommunication;

        public ViewHolder(@NonNull final View itemView, FragmentCommunication Communicator) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);


            mCommunication = Communicator;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCommunication.goToShowTripfromTrip(trip);
        }

    }

}
