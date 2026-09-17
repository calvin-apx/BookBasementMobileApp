package com.example.bookbasement_02.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AddToCartDialog extends AppCompatDialogFragment {
    private TextView titleTextView, descriptionTextView, genreTextView, authorsTextView,priceTextView , qtyTextView;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Button negativeBtn , positiveBtn;
    private AddQuantityListener listener;
    private Product book;
    private int qty = 1;

    private AlphaAnimation buttonClick = new AlphaAnimation(0.5F, 0.8F);
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.view_book_add_cart,null);

        initialize(view);
        setItems();

        builder.setView(view);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                listener.setDataPassed(qty,book);
            }
        });

        return builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddQuantityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddQuantityListener");
        }
    }

    public interface AddQuantityListener{
        void setDataPassed (int qty, Product book);
    }

    public void setBook(Product book){
        this.book = book;
    }

    public void initialize(View view){
        qtyTextView         =  (TextView) view.findViewById(R.id.qty_id);
        titleTextView       =  (TextView) view.findViewById(R.id.title_id);
        descriptionTextView =  (TextView) view.findViewById(R.id.description_id);
        genreTextView       =  (TextView) view.findViewById(R.id.genre_id);
        authorsTextView     =  (TextView) view.findViewById(R.id.authors_id);
        priceTextView       =  (TextView) view.findViewById(R.id.price_id);
        ratingBar           =  (RatingBar) view.findViewById(R.id.item_ratingbar);
        negativeBtn         =  (Button)   view.findViewById(R.id.btn_negative_id);
        positiveBtn         =  (Button)   view.findViewById(R.id.btn_positive_id);
        imageView           =  (ImageView) view.findViewById(R.id.image_view_id);
    }

    @SuppressLint("SetTextI18n")
    public void setItems(){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String description = book.getDescription();

        if (description.length() >= 190) {
            description = description.substring(0, 190) + "...";
        }
        qtyTextView.setText(qty+"");
        titleTextView.setText(book.getTitle());
        descriptionTextView.setText(description);
        genreTextView.setText(book.getGenre());
        authorsTextView.setText(book.getAuthors());
        priceTextView.setText("\u20B1 "+decimalFormat.format(book.getPrice()));
        ratingBar.setRating(book.getRating());

        Picasso.get()
                .load(URL.IMAGE_URL_ADDRESS.concat(book.getImage_url()))
                .fit()
                .into(imageView);

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                v.startAnimation(buttonClick);
                if(qty > 1){
                    qty--;
                    qtyTextView.setText(qty+"");
                }
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                v.startAnimation(buttonClick);
                if(qty != book.getQty()){
                    qty++;
                    qtyTextView.setText(qty+"");
                }
            }
        });
    }
}
