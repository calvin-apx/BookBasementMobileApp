package com.example.bookbasement_02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Adapters.UserAdapter;
import com.example.bookbasement_02.Models.ChatList;
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
import java.util.List;

public class ChatFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<FirebaseUsers> mUsers;
    private List<ChatList> chatLists;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        chatLists = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatLists").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                chatLists.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    chatLists.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void chatList(){
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    FirebaseUsers user = dataSnapshot.getValue(FirebaseUsers.class);
                   for(ChatList chatList: chatLists){
                       if(user.getId().equals(chatList.getId()) && !user.getType().equals("Recycling Facility")){
                           mUsers.add(user);
                       }
                   }
                }
                userAdapter = new UserAdapter(getContext() , mUsers ,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
    }


}
