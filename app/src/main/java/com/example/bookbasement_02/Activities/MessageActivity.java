package com.example.bookbasement_02.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Adapters.MessageAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView textView;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    ImageButton sendBtn;
    EditText sendEditText;
    Intent intent;
    String userId;

    List<Chat> chats;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;

    ValueEventListener seenListener;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(MessageActivity.this , MessageContainerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        circleImageView = findViewById(R.id.image_id);
        textView = findViewById(R.id.name_id);
        sendBtn = findViewById(R.id.send_btn);
        sendEditText = findViewById(R.id.text_send);


        intent = getIntent();
        userId = intent.getStringExtra("id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers user = snapshot.getValue(FirebaseUsers.class);
                textView.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(circleImageView);
                }
                readMessage(firebaseUser.getUid(), userId ,user.getImageURL());
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String message = sendEditText.getText().toString();
                if (!message.equals("")) {
                    String sender = firebaseUser.getUid();
                    String receiver = userId;
                    sendMessage(sender, receiver, message);
                } else {
                    Toast.makeText(getBaseContext(), "You can't send empty message ", Toast.LENGTH_LONG).show();
                }
                sendEditText.setText("");

            }
        });
        seenMessage(userId);
    }
    private void seenMessage(final String userId){
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)){
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("isseen" , true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage (String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen",false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatLists")
                .child(firebaseUser.getUid())
                .child(userId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(final String myId, final String userId, final String imageUrl){
        chats = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        chats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,chats , imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
    }

    private void status(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);

    }

    @Override
    protected void onResume ( ) {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause ( ) {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        status("offline");
    }
}