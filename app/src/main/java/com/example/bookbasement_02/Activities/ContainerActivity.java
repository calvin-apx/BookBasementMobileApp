package com.example.bookbasement_02.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bookbasement_02.Fragments.HomeFragment;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Fragments.ListFragment;
import com.example.bookbasement_02.Fragments.ProfileFragment;
import com.example.bookbasement_02.Fragments.SearchFragment;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContainerActivity extends AppCompatActivity implements AddToCartDialog.AddQuantityListener {
    Api api = new Api();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_id);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_id,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_appointments:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new ListFragment();
                            break;
                        case R.id.nav_info:
                            selectedFragment = new SearchFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_id,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void setDataPassed (int qty, Product book) {
        CartList cartList = new CartList();
        cartList.setCart_qty(qty);
        cartList.setBook_id(book.getBook_id());
        cartList.setStatus("AC");
        cartList.setUser_id(URL.USER_ID);
        Call<CartList> callCartList = api.apiInterface.storeCartList(cartList);
        callCartList.enqueue(new Callback<CartList>() {
            AlertMessageHelper alertMessageHelper = new AlertMessageHelper(getApplicationContext());
            @Override
            public void onResponse (Call<CartList> call, Response<CartList> response) {
                if (!response.isSuccessful()) {
                    alertMessageHelper.error("error");
                }
                alertMessageHelper.success("Successfully Added to Cart List");
            }

            @Override
            public void onFailure (Call<CartList> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }

}
