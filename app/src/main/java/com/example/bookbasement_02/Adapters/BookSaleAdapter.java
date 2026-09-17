package com.example.bookbasement_02.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookbasement_02.Activities.ViewBook;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookSaleAdapter extends RecyclerView.Adapter<BookSaleAdapter.MyViewHolder> {
    private Context context;
    private List<Product> mData;

    public BookSaleAdapter (Context context, List<Product> mData, List<Users> mUsers) {
        this.context = context;
        this.mData = mData;
    }


    @NonNull
    @Override
    public BookSaleAdapter.MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull BookSaleAdapter.MyViewHolder holder, final int position) {
        Picasso.get()
                .load(URL.IMAGE_URL_ADDRESS + "" + mData.get(position).getImage_url())
                .resize(160, 240)
                .centerCrop()
                .into(holder.productImage);
        holder.bookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(context, ViewBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("book_id", mData.get(position).getBook_id());
                intent.putExtra("genre_id", mData.get(position).getGenre_id());
                intent.putExtra("title", mData.get(position).getTitle());
                intent.putExtra("sub_title", mData.get(position).getSub_title());
                intent.putExtra("genre", mData.get(position).getGenre());
                intent.putExtra("description", mData.get(position).getDescription());
                intent.putExtra("price", mData.get(position).getPrice());
                intent.putExtra("qty", mData.get(position).getQty());
                intent.putExtra("total", mData.get(position).getTotal());
                intent.putExtra("publisher", mData.get(position).getPublisher());
                intent.putExtra("authors", mData.get(position).getAuthors());
                intent.putExtra("page_count", mData.get(position).getPage_count());
                intent.putExtra("image_url", mData.get(position).getImage_url());
                intent.putExtra("rating", mData.get(position).getRating());
                intent.putExtra("book_status", mData.get(position).getStatus());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount ( ) {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage;
        CardView bookCardView;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            productImage  =  itemView.findViewById(R.id.image_product_id);
            bookCardView  =  itemView.findViewById(R.id.card_product_id);
        }
    }
}
