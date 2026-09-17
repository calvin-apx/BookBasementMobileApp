package com.example.bookbasement_02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Activities.RemoveFavoritesDialog;
import com.example.bookbasement_02.Adapters.FavoritesAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Favorite;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment implements RemoveFavoritesDialog.RemoveFavoriteListener {
    Api api = new Api();
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private List<Favorite> favoriteList;
    private List<Users> usersList;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_favorites);
        recyclerView.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        favoriteList = new ArrayList<>();
        usersList = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(FavoritesFragment.this, getContext(), favoriteList, usersList, getFragmentManager());
        recyclerView.setAdapter(favoritesAdapter);
        getFavorites();
        return view;
    }

    public void getFavorites ( ) {
        Call<List<Favorite>> call = api.apiInterface.getFavorites(URL.USER_ID);
        call.enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse (Call<List<Favorite>> call, Response<List<Favorite>> response) {
                List<Favorite> favorites = response.body();
                favoriteList.clear();
                favoriteList.addAll(favorites);
                favoritesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure (Call<List<Favorite>> call, Throwable t) {

            }
        });
    }

    @Override
    public void setDataPassed (Favorite favorite) {
        Call<Favorite> call = api.apiInterface.removeFavorite(favorite);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse (Call<Favorite> call, Response<Favorite> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                getFavorites();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Removed from favorites")
                        .show();

            }

            @Override
            public void onFailure (Call<Favorite> call, Throwable t) {

            }
        });
    }


}
