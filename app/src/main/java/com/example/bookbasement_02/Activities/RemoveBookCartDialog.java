package com.example.bookbasement_02.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.R;
import com.squareup.picasso.Picasso;

public class RemoveBookCartDialog extends AppCompatDialogFragment {
    private RemoveBookCartDialogListener listener;
    private CartList cartList;
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.remove_cart_dialog,null);


        builder.setView(view);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                listener.setDataPassed(cartList);
            }
        });

        return builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RemoveBookCartDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement RemoveBookCartDialogListener");
        }
    }

    public interface RemoveBookCartDialogListener{
        void setDataPassed (CartList cartList);
    }

    public void setCart(CartList cartList){
        this.cartList = cartList;
    }

}
