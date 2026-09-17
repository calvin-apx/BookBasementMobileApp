package com.example.bookbasement_02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Adapters.MessageContainerAdapter;
import com.example.bookbasement_02.Fragments.ChatFragment;
import com.example.bookbasement_02.Fragments.UserFragment;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageContainerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView usernameTextView;
    CircleImageView circleImageView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        usernameTextView = findViewById(R.id.name_id);
        circleImageView = findViewById(R.id.image_id);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUser = snapshot.getValue(FirebaseUsers.class);
                usernameTextView.setText(firebaseUser.getUsername());
                if(firebaseUser.getImageURL().equals("default")){
                    circleImageView.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(getApplicationContext()).load(firebaseUser.getImageURL()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

        MessageContainerAdapter messageContainerAdapter = new MessageContainerAdapter(getSupportFragmentManager());
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("Chat");
        titleList.add("Users");

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new UserFragment());

        for(int index = 0; index < titleList.size(); index++){
            messageContainerAdapter.addFragment(fragments.get(index) , titleList.get(index));
        }
        viewPager.setAdapter(messageContainerAdapter);
        tabLayout.setupWithViewPager(viewPager);


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
        status("offline");
    }
}