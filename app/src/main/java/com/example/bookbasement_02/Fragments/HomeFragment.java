package com.example.bookbasement_02.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bookbasement_02.Activities.CartListActivity;
import com.example.bookbasement_02.Activities.NotificationListActivity;
import com.example.bookbasement_02.Activities.ViewBook;
import com.example.bookbasement_02.Adapters.BookSaleAdapter;
import com.example.bookbasement_02.Adapters.GenreAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Genre;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class HomeFragment extends Fragment {

    ImageView recommendedImageView;
    TextView titleTextView;
    TextView descriptionTextView;
    int visibleItemCount, totalItemCount, pastVisibleItems;
    Button bookButton;
    LinearLayoutManager linearLayoutManager;
    Boolean loadingGenre = false;
    Boolean loadingBook = false;
    Api api = new Api();
    private List<Genre> mBookGenre;
    private List<Product> mBookRecommended;
    private List<Users> mUsers, mUsersRating;
    private RecyclerView recyclerViewGenre, recyclerViewRecommended;
    private GenreAdapter genreAdapter;
    private BookSaleAdapter bookSaleAdapter;
    private EditText editText_search_books;

    public HomeFragment ( ) {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance ( ) {
        return new HomeFragment();
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init ViewModel


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragments, container, false);
    }


    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTextView = view.findViewById(R.id.title_id);
        descriptionTextView = view.findViewById(R.id.description_id);
        bookButton = view.findViewById(R.id.book_button_view_id);
        editText_search_books = view.findViewById(R.id.editText_search_books);
        recommendedImageView = view.findViewById(R.id.recommended_image_id);
        recyclerViewGenre = view.findViewById(R.id.recyclerview_genre_product_id);
        recyclerViewRecommended = view.findViewById(R.id.recyclerview_recomended_product_id);
        recyclerViewRecommended.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        linearLayoutManager = new LinearLayoutManager(getContext(), HORIZONTAL, true);
        recyclerViewRecommended.setLayoutManager(linearLayoutManager);
        recyclerViewGenre.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));


        mBookGenre = new ArrayList<>();
        mBookRecommended = new ArrayList<>();
        mUsers = new ArrayList<>();

        genreAdapter = new GenreAdapter(getContext(), mBookGenre);
        recyclerViewGenre.setAdapter(genreAdapter);
        getGenre();

        bookSaleAdapter = new BookSaleAdapter(getContext(), mBookRecommended, mUsers);
        recyclerViewRecommended.setAdapter(bookSaleAdapter);
        getBookRecommendation();

        recyclerViewGenre.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled (@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                    System.out.println("visibleItemCount " + visibleItemCount);
                    totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                    System.out.println("totalItemCount " + totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    System.out.println("pastVisibleItems " + pastVisibleItems);
                    if (!loadingGenre) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadingGenre = true;
                            //page = page+1;
                            getGenre();
                        }
                    }
                }
            }
        });

        recyclerViewRecommended.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled (@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                    System.out.println("visibleItemCount " + visibleItemCount);
                    totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                    System.out.println("totalItemCount " + totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    System.out.println("pastVisibleItems " + pastVisibleItems);
                    if (!loadingBook) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadingBook = true;
                            //page = page+1;
                            getBookRecommendation();
                        }
                    }
                }
            }
        });

        editText_search_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();

            }
        });


    }


    /**
     * Get genre list of books
     *
     * @return void
     * @params none
     */
    public void getGenre ( ) {

        Call<List<Genre>> call = api.apiInterface.getGenre();

        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse (Call<List<Genre>> call, retrofit2.Response<List<Genre>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingGenre = false;
                List<Genre> genres = response.body();
                if(mBookGenre.size() > 0){
                    if(mBookGenre.size() != genres.size()) {
                        mBookGenre.clear();
                        mBookGenre.addAll(genres);
                        genreAdapter.notifyDataSetChanged();
                    }
                }else{
                    mBookGenre.addAll(genres);
                    genreAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure (Call<List<Genre>> call, Throwable t) {

            }
        });
    }

    /**
     * Get Book list of books
     *
     * @return void
     * @params none
     */
    public void getBookRecommendation ( ) {

        Call<List<Product>> callBook = api.apiInterface.getBookRecommendation();
        callBook.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse (Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                loadingBook = false;
                final List<Product> books = response.body();
                assert books!=null;
                if(mBookRecommended.size() > 0){
                    if(mBookRecommended.size() != books.size()) {
                        mBookRecommended.clear();
                        mBookRecommended.addAll(books);
                        bookSaleAdapter.notifyDataSetChanged();
                    }
                }else{
                    mBookRecommended.addAll(books);
                    bookSaleAdapter.notifyDataSetChanged();
                    titleTextView.setText(mBookRecommended.get(0).getTitle());
                    if(mBookRecommended.get(0).getDescription().length() >= 200){
                        descriptionTextView.setText(mBookRecommended.get(0).getDescription().substring(0 , 200).concat("..."));
                    }else{
                        descriptionTextView.setText(mBookRecommended.get(0).getDescription());
                    }

                    Glide.with(getContext())
                            .load(URL.IMAGE_URL_ADDRESS.concat(mBookRecommended.get(0).getImage_url()))
                            .transform(new CenterCrop(), new RoundedCorners(12))
                            .into(recommendedImageView);
                    bookButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent(getContext(), ViewBook.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("book_id", mBookRecommended.get(0).getBook_id());
                            intent.putExtra("genre_id", mBookRecommended.get(0).getGenre_id());
                            intent.putExtra("title", mBookRecommended.get(0).getTitle());
                            intent.putExtra("sub_title", mBookRecommended.get(0).getSub_title());
                            intent.putExtra("genre", mBookRecommended.get(0).getGenre());
                            intent.putExtra("description", mBookRecommended.get(0).getDescription());
                            intent.putExtra("price", mBookRecommended.get(0).getPrice());
                            intent.putExtra("qty", mBookRecommended.get(0).getQty());
                            intent.putExtra("total", mBookRecommended.get(0).getTotal());
                            intent.putExtra("publisher", mBookRecommended.get(0).getPublisher());
                            intent.putExtra("authors", mBookRecommended.get(0).getAuthors());
                            intent.putExtra("page_count", mBookRecommended.get(0).getPage_count());
                            intent.putExtra("image_url", mBookRecommended.get(0).getImage_url());
                            intent.putExtra("rating", mBookRecommended.get(0).getRating());
                            intent.putExtra("book_status", mBookRecommended.get(0).getStatus());
                            getContext().startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure (Call<List<Product>> call, Throwable t) {

            }
        });
    }


}
