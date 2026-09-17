package com.example.bookbasement_02.Adapters;

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


import com.example.bookbasement_02.Activities.GenreSearchActivity;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Genre;
import com.example.bookbasement_02.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    private Context context;
    private List<Genre> mData;

    public GenreAdapter(Context context,List<Genre> mData) {
        this.context = context;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        Picasso.get()
                .load(URL.IMAGE_GENRE_URL_ADDRESS + "" + mData.get(position).getImage_url())
                .resize(160, 240)
                .centerCrop()
                .into(holder.productImage);
        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GenreSearchActivity.class);
                intent.putExtra("genre_name",mData.get(position).getName());
                intent.putExtra("genre_id",mData.get(position).getGenre_id());
                intent.putExtra("image_url",mData.get(position).getImage_url()+"");
                context.startActivity(intent);
            }
        });
    }
    public void setGenre(List<Genre> genres){
        this.mData = genres;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        CardView productCard;
        ImageView productImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product_id);
            productCard = itemView.findViewById(R.id.card_product_id);
        }
    }
}
