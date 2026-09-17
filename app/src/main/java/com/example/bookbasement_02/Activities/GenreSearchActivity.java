package com.example.bookbasement_02.Activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Adapters.BookToSearchAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreSearchActivity extends AppCompatActivity implements AddToCartDialog.AddQuantityListener {

    int genre_id = 0;
    LinearLayoutManager linearLayoutManager;
    int visibleItemCount, totalItemCount, pastVisibleItems;
    Boolean loadingBook = false;
    Api api = new Api();
    private BookToSearchAdapter bookToSearchAdapter;
    private List<Users> mUsers;
    private List<Product> mBookRecommended;
    private RecyclerView recyclerViewRecommended;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerViewRecommended = findViewById(R.id.item_recyclerview);
        recyclerViewRecommended.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        genre_id = Integer.parseInt(getIntent().getStringExtra("genre_id"));
        getSupportActionBar().setTitle("Genre: " + getIntent().getStringExtra("genre_name"));

        mBookRecommended = new ArrayList<>();
        mUsers = new ArrayList<>();

        bookToSearchAdapter = new BookToSearchAdapter(this, mBookRecommended, mUsers, getSupportFragmentManager() , GenreSearchActivity.this);
        recyclerViewRecommended.setAdapter(bookToSearchAdapter);
        getBookRecommendation(genre_id);

    }

    /**
     * Get Book list of books
     *
     * @return void
     * @params none
     */
    public void getBookRecommendation (int id) {

        Call<List<Product>> callBook = api.apiInterface.getBookGenreRecommendation(id);
        callBook.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse (Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> books = response.body();
                loadingBook = false;
                if (mBookRecommended.size() > 0) {
                    if (mBookRecommended.size() != books.size()) {
                        mBookRecommended.clear();
                        mBookRecommended.addAll(books);
                        bookToSearchAdapter.notifyDataSetChanged();
                    }
                } else {
                    mBookRecommended.addAll(books);
                    bookToSearchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure (Call<List<Product>> call, Throwable t) {

            }
        });
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
                    new SweetAlertDialog(GenreSearchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(GenreSearchActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to cart")
                        .show();

            }

            @Override
            public void onFailure (Call<CartList> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }

}
