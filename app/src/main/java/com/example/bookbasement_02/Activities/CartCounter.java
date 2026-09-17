package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartCounter {
    private int count = 0;
    private ImageView imageView;
    private CardView cardView;
    private TextView textView;
    private Api api = new Api();
    private Context context;
    private ContainerActivity2 containerActivity2;
    public CartCounter (View view, final Context context, ContainerActivity2 containerActivity2){
        imageView = view.findViewById(R.id.cart_notification_id);
        textView = view.findViewById(R.id.cart_count_notificiation);
        cardView = view.findViewById(R.id.cart_card_count_id);
        cardView.setVisibility(View.GONE);
        this.context = context;
        this.containerActivity2 = containerActivity2;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(context.getApplicationContext(), CartListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void getNumberOfCarts(){
        Call<List<CartList>> callCartList = api.apiInterface.getCartList(URL.USER_ID);
        callCartList.enqueue(new Callback<List<CartList>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse (Call<List<CartList>> call, Response<List<CartList>> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(containerActivity2, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }else{
                    List<CartList> data = response.body();
                    System.out.println("Size cart");
                    System.out.println(data.size());
                    if(data.size() > 0){
                        textView.setText(data.size()+"");
                        cardView.setVisibility(View.VISIBLE);
                    }else {
                        textView.setText("");
                        cardView.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void onFailure (Call<List<CartList>> call, Throwable t) {

            }
        });
    }



}
