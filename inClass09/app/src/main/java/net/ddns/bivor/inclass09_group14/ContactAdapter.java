package net.ddns.bivor.inclass09_group14;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    ArrayList<Contact> mData;

    private OnFragmentListener mCommunicator;

    public ContactAdapter(ArrayList<Contact> mData, OnFragmentListener mCommunicator) {
        this.mData = mData;
        this.mCommunicator = mCommunicator;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Contact contact = mData.get(i);
        viewHolder.contact = contact;
        viewHolder.contacts = mData;
        viewHolder.textViewName.setText(contact.name);
        viewHolder.textViewEmail.setText(contact.email);
        viewHolder.textViewPhone.setText(contact.phone);
        Picasso.get().load(contact.imageURL).into(viewHolder.imageViewContact);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewPhone,textViewEmail;
        Contact contact;
        ArrayList<Contact> contacts;
        ImageView imageViewContact;
        OnFragmentListener mCommunication;

        public ViewHolder(@NonNull final View itemView, OnFragmentListener communicator) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageViewContact = itemView.findViewById(R.id.imageViewContact);

            mCommunication = communicator;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCommunication.delete(contact);
                    return false;
                }
            });

        }



    }
}
