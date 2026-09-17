package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Adapters.BookToSearchAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Favorite;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBook extends AppCompatActivity implements AddToCartDialog.AddQuantityListener {
    private ImageView image,addFavoriteImage;
    private TextView titleTextView, authorsTextView, priceTextView, pagesTextView, ratingTextView, genreTextView;
    private RatingBar ratingbar;
    private Button cart_btn;
    private List<Product> mBookRecommended;
    private List<Users> list_user;
    private BookToSearchAdapter book_adapter = null;
    private RecyclerView recyclerview_recommended = null;
    private Boolean loadingBook = false;
    private Api api = new Api();
    private int book_id;
    private String title;
    private String sub_title;
    private String genre;
    private double price;
    private String book_image;
    private int qty;
    private float rating;
    private int pages;
    private String book_status;
    private String publisher;
    private String description;
    private String authors;
    private Integer genre_id;
    private Product product;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("View Books");

        initialize();
        setIntentPassed();
        setDataPassed();

        mBookRecommended = new ArrayList<>();
        list_user = new ArrayList<>();


        book_adapter = new BookToSearchAdapter(getApplicationContext(), mBookRecommended, list_user, getSupportFragmentManager(), ViewBook.this);
        recyclerview_recommended.setAdapter(book_adapter);
        getBookRecommendation(genre_id);

        recyclerview_recommended.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled (@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                    int totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                    int pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (!loadingBook) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadingBook = true;
                            //page = page+1;
                            getBookRecommendation(genre_id);
                        }
                    }
                }
            }
        });

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                AddToCartDialog addToCartDialog = new AddToCartDialog();
                addToCartDialog.setBook(product);
                addToCartDialog.show(getSupportFragmentManager(), "add quantity dialog");
            }
        });

        addFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Favorite favorite = new Favorite();
                favorite.setUser_id(URL.USER_ID);
                favorite.setBook_id(book_id);
                favorite.setStatus("AC");
                Call<Favorite> call = api.apiInterface.storeFavorites(favorite);
                call.enqueue(new Callback<Favorite>() {
                    @Override
                    public void onResponse (Call<Favorite> call, Response<Favorite> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Error111", Toast.LENGTH_SHORT).show();
                        }
                        new SweetAlertDialog(ViewBook.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Added to favorites")
                                .show();
                    }

                    @Override
                    public void onFailure (Call<Favorite> call, Throwable t) {
                        System.out.println("Error Response101 : " + t.getMessage());
                    }
                });
            }
        });


    }

    /**
     * Get Book list of books
     *
     * @param genre_id
     * @return void
     */
    private void getBookRecommendation (Integer genre_id) {

        Call<List<Product>> callBook = api.apiInterface.getBookGenreRecommendation(genre_id);
        callBook.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse (Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> books = response.body();
                loadingBook = false;
                if (mBookRecommended.size() > 0) {
                    if (mBookRecommended.size() != books.size()) {
                        mBookRecommended.clear();
                        mBookRecommended.addAll(books);
                        book_adapter.notifyDataSetChanged();
                    }
                } else {
                    mBookRecommended.addAll(books);
                    for (int i = 0; i < mBookRecommended.size(); i++) {
                        if (mBookRecommended.get(i).getBook_id() == book_id) {
                            mBookRecommended.remove(i);
                        }
                    }
                    book_adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure (Call<List<Product>> call, Throwable t) {

            }
        });
    }

    /**
     * @return void
     * @author Calvin
     */
    private void initialize ( ) {

        image = findViewById(R.id.item_img);
        cart_btn = findViewById(R.id.add_cart_btn);
        titleTextView = findViewById(R.id.item_title);
        authorsTextView = findViewById(R.id.item_author);
        priceTextView = findViewById(R.id.item_price);
        pagesTextView = findViewById(R.id.item_pages);
        genreTextView = findViewById(R.id.item_genre);
        ratingbar = findViewById(R.id.item_ratingbar);
        ratingTextView = findViewById(R.id.item_genre);
        addFavoriteImage = findViewById(R.id.add_to_favorite_id);

        recyclerview_recommended = findViewById(R.id.item_recyclerview);
        recyclerview_recommended.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        recyclerview_recommended.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }


    /**
     * @return void
     * @author Calvin
     */
    private void setIntentPassed ( ) {

        book_id = getIntent().getIntExtra("book_id", 0);
        genre_id = getIntent().getIntExtra("genre_id", 0);
        title = getIntent().getStringExtra("title");
        sub_title = getIntent().getStringExtra("sub_title");
        genre = getIntent().getStringExtra("genre");
        description = getIntent().getStringExtra("description");
        price = getIntent().getDoubleExtra("price", 0);
        qty = getIntent().getIntExtra("qty", 0);
        publisher = getIntent().getStringExtra("publisher");
        authors = getIntent().getStringExtra("authors");
        pages = getIntent().getIntExtra("page_count", 0);
        book_image = getIntent().getStringExtra("image_url");
        rating = getIntent().getFloatExtra("rating", 0);
        book_status = getIntent().getStringExtra("status");

        product = new Product(book_id, genre_id, title, sub_title, genre, description, price, qty, 0, publisher, authors, pages, book_image, rating, book_status);
    }

    @SuppressLint("SetTextI18n")
    private void setDataPassed ( ) {
        if (sub_title.length() >= 30) {
            sub_title = sub_title.substring(0, 30) + "...";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        titleTextView.setText(title);
        authorsTextView.setText("By ".concat(sub_title));
        priceTextView.setText("\u20B1 " + decimalFormat.format(price));
        ratingTextView.setText(rating + "");
        genreTextView.setText(genre);
        pagesTextView.setText(pages + " Pages");
        ratingbar.setRating(Float.parseFloat(String.valueOf(rating)));
        Picasso.get()
                .load(URL.IMAGE_URL_ADDRESS + "" + book_image)
                .resize(130, 200)
                .centerCrop()
                .into(image);

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
                    new SweetAlertDialog(ViewBook.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }

                new SweetAlertDialog(ViewBook.this,SweetAlertDialog.SUCCESS_TYPE)
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
