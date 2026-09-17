package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Fragments.HomeFragment;
import com.example.bookbasement_02.Fragments.ProfileFragment;
import com.example.bookbasement_02.Fragments.SearchFragment;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.FirebaseUsers;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainerActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddToCartDialog.AddQuantityListener {
    private Api api = new Api();
    private Intent intent;
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private TextView username, email;
    private CircleImageView circleImageView;
    private CartCounter cartCounter;
    private NotificationCounter notificationCounter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        notificationManager =  NotificationManagerCompat.from(this);
        notificationCounter = new NotificationCounter(findViewById(R.id.bell_logo_id), getBaseContext() , notificationManager);
        notificationCounter.getNumberOfNotifications();
        cartCounter = new CartCounter(findViewById(R.id.cart_logo_id) , getBaseContext(),ContainerActivity2.this);
        cartCounter.getNumberOfCarts();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.username_id);
        email = header.findViewById(R.id.user_email_id);
        circleImageView = header.findViewById(R.id.user_image_id);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new HomeFragment())
                .commit();

        drawer = findViewById(R.id.drawer_layout);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                FirebaseUsers firebaseUser = snapshot.getValue(FirebaseUsers.class);
                assert firebaseUser != null;
                username.setText(firebaseUser.getUsername());
                email.setText(firebaseUser.getEmail());
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.action_bar));

        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.search_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.cart_id:
                intent = new Intent(getBaseContext(), CartListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.profile_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.notification_id:
                intent = new Intent(getBaseContext(), NotificationListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.message_id:
                intent = new Intent(getBaseContext(), MessageContainerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.donate_book_id:
                intent = new Intent(getBaseContext(), AddUserDonateBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.sell_book_id:
                Intent intent = new Intent(getBaseContext(), AddUserSellBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.setting_id:

                break;
            case R.id.logout_id:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ContainerActivity2.this, LoginActivity.class));
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    @Override
//    public boolean onCreateOptionsMenu (Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.container2, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected (MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.cart_id:
//                intent = new Intent(getBaseContext(), CartListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                return true;
//            case R.id.notification_btn:
//                intent = new Intent(getBaseContext(), NotificationListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    @Override
    public void onBackPressed ( ) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setDataPassed (int qty, Product book) {
        CartList cartList = new CartList();
        cartList.setCart_qty(qty);
        cartList.setBook_id(book.getBook_id());
        cartList.setStatus("AC");
        cartList.setUser_id(URL.USER_ID);
        Call<CartList> callCartList = api.apiInterface.storeCartList(cartList);
        callCartList.enqueue(new Callback<CartList>() {

            @Override
            public void onResponse (Call<CartList> call, Response<CartList> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(ContainerActivity2.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(ContainerActivity2.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to cart")
                        .show();
                cartCounter = new CartCounter(findViewById(R.id.cart_logo_id) , getBaseContext(), ContainerActivity2.this);
                cartCounter.getNumberOfCarts();
            }

            @Override
            public void onFailure (Call<CartList> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
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
        cartCounter = new CartCounter(findViewById(R.id.cart_logo_id) , getBaseContext(), ContainerActivity2.this);
        cartCounter.getNumberOfCarts();
        status("online");
    }

    @Override
    protected void onPause ( ) {
        super.onPause();
        status("offline");
    }
}