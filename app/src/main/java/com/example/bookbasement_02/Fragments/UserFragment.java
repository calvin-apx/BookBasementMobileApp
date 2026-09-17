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
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.Models.Users;
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

public class UserFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<FirebaseUsers> usersList;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.users_layout , container , false);
      recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_id);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      usersList = new ArrayList<>();

      readUsers();
      return  view;
    }

    private void readUsers ( ) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    FirebaseUsers user = dataSnapshot.getValue(FirebaseUsers.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid()) && !user.getType().equals("Recycling Facility")){
                        usersList.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext() , usersList,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });
    }
}
