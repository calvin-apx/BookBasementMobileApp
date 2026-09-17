package com.example.bookbasement_02.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookbasement_02.Helpers.IntentHelper;
import com.example.bookbasement_02.Helpers.StringHelper;
import com.example.bookbasement_02.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddUserDonateBookActivity extends AppCompatActivity implements View.OnClickListener{

    EditText donateQty;
    Button addDonate;
    StringHelper stringHelper = new StringHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__donate__book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        donateQty = findViewById(R.id.book_quantity_id);
        addDonate = findViewById(R.id.button_donate_id);
        addDonate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_donate_id:
                if(donateQty.getText().toString().equals("")){
                    new SweetAlertDialog(AddUserDonateBookActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!\n" +
                                    "Please check your inputs")
                            .show();
                }else{
                    donateProcess();
                }
                break;
        }
    }

    public void donateProcess(){
        int qty = stringHelper.toInt(donateQty.getText().toString());
        Map<String,String> map = new HashMap();
        map.put("donateQty",qty+"");
        map.put("purpose","donate");
        (new IntentHelper(getBaseContext())).setIntent(map,AppointmentSetterActivity.class);
    }


}
