package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Adapters.BookToCartAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity implements RemoveBookCartDialog.RemoveBookCartDialogListener , AddToCartDialog.AddQuantityListener {
    private List<CartList> cartList;
    private BookToCartAdapter bookToCartAdapter = null;
    private Api api = new Api();
    private TextView subTotalTextView, discountTextView,estimatedTextView;
    private Button btn_place_order;
    private double subTotal;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Shopping Cart List");

        subTotalTextView  = (TextView) findViewById(R.id.subtotal_id);
        discountTextView  = (TextView) findViewById(R.id.discount_id);
        estimatedTextView = (TextView) findViewById(R.id.tax_id);
        btn_place_order   = (Button)   findViewById(R.id.btn_place_order_id);

        cartList = new ArrayList<>();
        List<Users> mUsers = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        bookToCartAdapter = new BookToCartAdapter(getApplicationContext(), cartList, mUsers,getSupportFragmentManager());
        recyclerView.setAdapter(bookToCartAdapter);
        getCartList();

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(cartList.size() > 0){
                    Intent intent = new Intent(getBaseContext(), AppointmentSetterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("purpose","buy");
                    getBaseContext().startActivity(intent);
                }else{
                    Alerter.create(CartListActivity.this)
                            .setTitle("INFO!")
                            .setText("No CartList Available")
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorInfo)
                            .setIcon(R.drawable.alerter_ic_notifications)
                            .show();
                }

            }
        });

    }

    /**
     * Get Cartlist
     * @return void
     */
    private void getCartList(){

        Call<List<CartList>> callCartList = api.apiInterface.getCartList(URL.USER_ID);
        callCartList.enqueue(new Callback<List<CartList>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse (Call<List<CartList>> call, Response<List<CartList>> response) {
                List<CartList> data = response.body();
                cartList.clear();
                cartList.addAll(data);
                bookToCartAdapter.notifyDataSetChanged();
                subTotal = 0;
                for (CartList list : cartList) {
                    subTotal += (list.getPrice() * list.getCart_qty());
                }
                subTotalTextView.setText("\u20B1 " + decimalFormat.format(subTotal));
            }
            @Override
            public void onFailure (Call<List<CartList>> call, Throwable t) {

            }
        });

    }


    @Override
    public void setDataPassed (CartList cartList) {
        Call<CartList> callCartList = api.apiInterface.removeCartList(cartList);
        callCartList.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse (Call<CartList> call, Response<CartList> response) {
                if (!response.isSuccessful()) {
                    Alerter.create(CartListActivity.this)
                            .setTitle("Error!")
                            .setText("Error")
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorError)
                            .setIcon(R.drawable.alerter_ic_notifications)
                            .show();
                }
                getCartList();
                Alerter.create(CartListActivity.this)
                        .setTitle("Success!")
                        .setText("Successfully Deleted to Cart List")
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.colorSuccess)
                        .setIcon(R.drawable.alerter_ic_notifications)
                        .show();
            }

            @Override
            public void onFailure (Call<CartList> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }

    @Override
    public void setDataPassed (int quantity, Product book) {
        CartList cartList = new CartList();
        cartList.setCart_qty(quantity);
        cartList.setBook_id(book.getBook_id());
        cartList.setStatus("AC");
        cartList.setUser_id(URL.USER_ID);
        Call<CartList> callCartList = api.apiInterface.storeCartList(cartList);
        callCartList.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse (Call<CartList> call, Response<CartList> response) {
                if (!response.isSuccessful()) {
                    Alerter.create(CartListActivity.this)
                            .setTitle("Error!")
                            .setText("Error")
                            .setDuration(5000)
                            .setBackgroundColorRes(R.color.colorError)
                            .setIcon(R.drawable.alerter_ic_notifications)
                            .show();
                }
                getCartList();
                Alerter.create(CartListActivity.this)
                        .setTitle("Success!")
                        .setText("Successfully Updated Cart List")
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.colorSuccess)
                        .setIcon(R.drawable.alerter_ic_notifications)
                        .show();
            }

            @Override
            public void onFailure (Call<CartList> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });

    }
}
