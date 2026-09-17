package com.example.bookbasement_02.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookbasement_02.Helpers.IntentHelper;
import com.example.bookbasement_02.Models.Recycle;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableBookActivity extends AppCompatActivity {
    private Api api = new Api();
    TextView quantityTextView;
    EditText quantityEditText;
    Button submitButton;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quantityTextView = findViewById(R.id.recycle_quantity_id);
        quantityEditText = findViewById(R.id.quantity_id);
        submitButton = findViewById(R.id.submit_id);

        Call<List<Recycle>> recycleCall = api.apiInterface.getRecycle();

        recycleCall.enqueue(new Callback<List<Recycle>>() {
            @Override
            public void onResponse (Call<List<Recycle>> call, Response<List<Recycle>> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(getBaseContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }else{
                    int total = 0;
                    List<Recycle> recycles = response.body();
                    System.out.println("recycle111");
                    System.out.println(recycles);
                    assert recycles != null;
                    for (Recycle recycle :  recycles){
                        if(recycle.getStatus().equals("AC")){
                            total +=  recycle.getQty();
                        }

                    }
                    quantityTextView.setText(total+"");
                }

            }

            @Override
            public void onFailure (Call<List<Recycle>> call, Throwable t) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(quantityEditText.getText().toString().equals("")){
                    new SweetAlertDialog(AvailableBookActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!\n" +
                                    "Please check your inputs")
                            .show();
                }else{
                    processRecycle();
                }
            }
        });
    }
    public void processRecycle(){
        int qty = Integer.parseInt(quantityEditText.getText()+"");
        Map<String,String> map = new HashMap();
        map.put("recycleQty",qty+"");
        map.put("purpose","recycle");
        (new IntentHelper(getBaseContext())).setIntent(map,AppointmentSetterActivity.class);
    }
}