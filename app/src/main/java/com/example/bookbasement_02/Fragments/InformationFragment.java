package com.example.bookbasement_02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public  class InformationFragment extends Fragment {
    TextView nameTextView,emailTextview,genderTextView,mobileNumberTextView,birthdayTextView;


    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment,container,false);
        nameTextView = view.findViewById(R.id.name_id);
        emailTextview = view.findViewById(R.id.email_id);
        genderTextView = view.findViewById(R.id.gender_id);
        mobileNumberTextView = view.findViewById(R.id.mobile_num_id);
        birthdayTextView = view.findViewById(R.id.birthday_id);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUsers = snapshot.getValue(FirebaseUsers.class);
                nameTextView.setText(firebaseUsers.getUsername());
                emailTextview.setText(firebaseUsers.getEmail());
                genderTextView.setText(firebaseUsers.getGender());
                mobileNumberTextView.setText(firebaseUsers.getPhone());
                birthdayTextView.setText(firebaseUsers.getBirthday());
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
