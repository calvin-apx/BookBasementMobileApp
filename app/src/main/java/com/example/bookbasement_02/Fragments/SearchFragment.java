package com.example.bookbasement_02.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookbasement_02.Activities.FilterDialog;
import com.example.bookbasement_02.Activities.GenreSearchActivity;
import com.example.bookbasement_02.Adapters.BookToSearchAdapter;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.SearchFilter;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements FilterDialog.FragmentAListener{
    Intent intent;
    List<Product> mBookRecommended;
    List<Users> mUsers;
    BookToSearchAdapter bookToSearchAdapter = null;
    RecyclerView recyclerView = null;
    TextView emptyMessage1 = null;
    Api api = new Api();
    ImageView filter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragments, container, false);
        intent = getActivity().getIntent();

        emptyMessage1 = view.findViewById(R.id.empty_view_productview_id);
        EditText searchProduct = view.findViewById(R.id.editTextSearch_id);

        recyclerView = view.findViewById(R.id.recyclerview_product_search_id);
        recyclerView.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        filter = view.findViewById(R.id.filter_id);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                FilterDialog filterDialog = new FilterDialog();
                filterDialog.setTargetFragment(SearchFragment.this ,1);
                filterDialog.show(getFragmentManager(), "filter");
            }
        });

        mBookRecommended = new ArrayList<>();
        mUsers = new ArrayList<>();
        bookToSearchAdapter = new BookToSearchAdapter(getContext(), mBookRecommended, mUsers,getFragmentManager(), getActivity());
        recyclerView.setAdapter(bookToSearchAdapter);

        getBookRecommendation();


        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                bookToSearchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });


        return view;
    }

    /**
     * Get Recommendation Book list of books
     *
     * @return void
     * @params none
     */
    public void getBookRecommendation() {

        Call<List<Product>> callBook = api.apiInterface.getBookRecommendation();
        callBook.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> books = response.body();
                mBookRecommended.clear();
                mBookRecommended.addAll(books);
                bookToSearchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }


    @Override
    public void setSearchData (SearchFilter searchData) {
        Call<List<Product>> searchCall = api.apiInterface.setSearch(searchData);
        searchCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse (Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> books = response.body();
                mBookRecommended.clear();
                mBookRecommended.addAll(books);
                bookToSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure (Call<List<Product>> call, Throwable t) {

            }
        });
    }
}



