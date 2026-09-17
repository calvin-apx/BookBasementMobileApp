package com.example.bookbasement_02.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecylingFacilitiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CardView availableBookCardView, messageUsCardView, appointmentsCardView;
    private DrawerLayout drawer;
    private TextView username, email , welcomeTextView;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyling_facilities);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        availableBookCardView = findViewById(R.id.available_book_card_id);
        messageUsCardView = findViewById(R.id.message_card_id);
        appointmentsCardView = findViewById(R.id.appointment_card_id);
        welcomeTextView = findViewById(R.id.welcome_id);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.username_id);
        email = header.findViewById(R.id.user_email_id);
        circleImageView = header.findViewById(R.id.user_image_id);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.action_bar));

        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUser = snapshot.getValue(FirebaseUsers.class);
                assert firebaseUser != null;
                username.setText(firebaseUser.getUsername());
                email.setText(firebaseUser.getEmail());
                welcomeTextView.setText("Welcome "+firebaseUser.getUsername());
                if (firebaseUser.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.default_image);
                } else {
                    Glide.with(getBaseContext()).load(firebaseUser.getImageURL()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {

            }
        });

        availableBookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent1 = new Intent(getBaseContext(), AvailableBookActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent1);

            }
        });

        messageUsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getBaseContext(), MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", "FjaQXRiK2UaZARgUvqxrWQHrFT53");
                getBaseContext().startActivity(intent);

            }
        });

        appointmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getBaseContext(), RecycleAppointmentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.available_book_id:
                intent = new Intent(getBaseContext(), AvailableBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);

                break;
            case R.id.message_us_id:
                intent = new Intent(getBaseContext(), MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", "FjaQXRiK2UaZARgUvqxrWQHrFT53");
                getBaseContext().startActivity(intent);

                break;
            case R.id.appointment_id:
                intent = new Intent(getBaseContext(), RecycleAppointmentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                break;
            case R.id.setting_id:

                break;
            case R.id.logout_id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(RecylingFacilitiesActivity.this, LoginActivity.class));
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}