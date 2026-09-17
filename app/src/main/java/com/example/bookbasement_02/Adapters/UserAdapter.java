package com.example.bookbasement_02.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Activities.MessageActivity;
import com.example.bookbasement_02.Models.Chat;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<FirebaseUsers> users;
    private  boolean isChat;
    String lastMessage;

    public UserAdapter(Context context , List<FirebaseUsers> users , boolean isChat){
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(context).inflate(R.layout.user_card,parent,false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder (@NonNull UserAdapter.ViewHolder holder, int position) {
        final FirebaseUsers user = users.get(position);

        if(user.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.drawable.profile);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.imageView);
        }
        holder.textView.setText(user.getUsername());
        if (isChat){
            lastMessage(user.getId() , holder.last_message);
        }else{
            holder.last_message.setVisibility(View.GONE);
        }
        if (isChat){
            if(user.getStatus().equals("online")){
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }else{
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.image_on.setVisibility(View.VISIBLE);
            holder.image_off.setVisibility(View.GONE);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(context , MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount ( ) {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView image_on;
        ImageView image_off;
        TextView last_message;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_id);
            image_on = (ImageView) itemView.findViewById(R.id.image_on);
            image_off = (ImageView) itemView.findViewById(R.id.image_off);
            textView = (TextView) itemView.findViewById(R.id.name_id);
            last_message = (TextView) itemView.findViewById(R.id.last_message);
        }
    }
    private void lastMessage(final String userid , final TextView last_mesage){
        lastMessage = "default";
        final FirebaseUser firebaseUsers = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUsers.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUsers.getUid())){
                            lastMessage = chat.getMessage();
                    }
                }
                switch(lastMessage){
                    case "default":
                        last_mesage.setText("No Message");
                        break;
                    default:
                        last_mesage.setText(lastMessage);
                        break;
                }
                lastMessage = "default";
            }



            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

    }
}
