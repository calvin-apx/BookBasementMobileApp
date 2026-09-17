package com.example.bookbasement_02.Activities;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookbasement_02.Adapters.NewsAdapter;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Appointment;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {
    RecyclerView newsRecyclerview;
    NewsAdapter newsAdapter;
    List<Appointment> mData;
    List<Users> mUsers;
    Api api = new Api();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsRecyclerview = findViewById(R.id.recycler_view_notification);
        newsRecyclerview.setHasFixedSize(true);//every item of the RecyclerView has a fix size
        newsRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mData = new ArrayList<>();
        mUsers = new ArrayList<>();

        newsAdapter = new NewsAdapter(getApplicationContext(), mData, mUsers);
        newsRecyclerview.setAdapter(newsAdapter);

        loadNewsFeeds();
    }


    private void loadNewsFeeds ( ) {
        Call<List<Appointment>> call = api.apiInterface.getAppointments(URL.USER_ID);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse (Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                List<Appointment> newsItems = response.body();
                System.out.println("Success News Item " + response.body());
                mData.clear();
                assert newsItems != null;
                for(Appointment appointment: newsItems){
                    if(!appointment.getPurpose().equalsIgnoreCase("recycle")){
                        mData.add(appointment);
                    }
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure (Call<List<Appointment>> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }
}
