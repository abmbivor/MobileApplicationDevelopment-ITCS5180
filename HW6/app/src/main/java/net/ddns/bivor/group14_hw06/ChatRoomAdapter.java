package net.ddns.bivor.group14_hw06;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>{

    FragmentCommunication mCommunicator;

    ArrayList<Message> mData;

    PrettyTime p = new PrettyTime();

    public ChatRoomAdapter(ArrayList<Message> mData, FragmentCommunication mCommunicator) {
        this.mCommunicator = mCommunicator;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Message message = mData.get(position);
            holder.message = message;
            holder.messages = mData;
            holder.textViewMessage.setText(message.message);
            holder.textViewMessageFirstName.setText(message.firstName);
            holder.textViewMessagePrettyTime.setText(p.format(message.prettyTime));
            if(message.imageURL.equals("NO_IMAGE"))holder.imageViewMessage.setVisibility(View.GONE);
            else{
                holder.imageViewMessage.setVisibility(View.VISIBLE);
                Picasso.get().load(message.imageURL).into(holder.imageViewMessage);
            }
            holder.imageViewMessageDelete.setEnabled(true);
            Log.d("l","kk");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView textViewMessage, textViewMessageFirstName, textViewMessagePrettyTime;
    ImageView imageViewMessage, imageViewMessageDelete;

    Message message;
    ArrayList<Message> messages;

    FragmentCommunication mCommunication;

    public ViewHolder(@NonNull final View itemView, FragmentCommunication communicator) {
        super(itemView);

            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewMessageFirstName = itemView.findViewById(R.id.textViewMessageFirstName);
            textViewMessagePrettyTime = itemView.findViewById(R.id.textViewMessagePrettyTime);
            imageViewMessage = itemView.findViewById(R.id.imageViewMessage);
            imageViewMessageDelete = itemView.findViewById(R.id.imageViewMessageDelete);

            mCommunication = communicator;

            imageViewMessageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewMessageDelete.setEnabled(false);
                    mCommunication.deletePost(message);
                }
            });

    }
}

}