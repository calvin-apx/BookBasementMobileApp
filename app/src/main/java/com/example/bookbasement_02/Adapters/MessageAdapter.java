package com.example.bookbasement_02.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Models.Chat;
import com.example.bookbasement_02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;
    private Context context;
    private List<Chat> chats;
    private String imageURL;

    public MessageAdapter (Context context, List<Chat> chats, String imageURL) {
        this.context = context;
        this.chats = chats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View layout = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(layout);
        } else {
            View layout = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(layout);
        }

    }

    @Override
    public void onBindViewHolder (@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        holder.textView.setText(chat.getMessage());
        if (imageURL.equals("default")) {
            holder.imageView.setImageResource(R.drawable.profile);
        } else {
            Glide.with(context).load(imageURL).into(holder.imageView);
        }

        if (position == this.chats.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("Delivered");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount ( ) {
        return chats.size();
    }

    @Override
    public int getItemViewType (int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView txt_seen;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_id);
            textView = (TextView) itemView.findViewById(R.id.show_message);
            txt_seen = (TextView) itemView.findViewById(R.id.text_seen);
        }
    }
}
