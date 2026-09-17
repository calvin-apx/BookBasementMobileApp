package com.example.bookbasement_02.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookbasement_02.Activities.AddToCartDialog;
import com.example.bookbasement_02.Activities.RemoveFavoritesDialog;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Favorite;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private Context context;
    private List<Favorite> mData;
//    private List<Users> mUsers;
    private FragmentManager fragmentManager;
    private Object object;

    public FavoritesAdapter (Object object, Context context, List<Favorite> mData, List<Users> mUsers, FragmentManager fragmentManager) {
        this.object = object;
        this.context = context;
        this.mData = mData;
//        this.mUsers = mUsers;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_card, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (@NonNull FavoritesAdapter.FavoritesViewHolder holder, final int position) {
        holder.item_title_book.setText(mData.get(position).getTitle());
        holder.item_book_author.setText("By: "+mData.get(position).getAuthors());
        holder.item_description.setText(mData.get(position).getDescription());
        Picasso.get()
                .load(URL.IMAGE_URL_ADDRESS + "" + mData.get(position).getImage_url())
                .resize(130, 180)
                .centerCrop()
                .into(holder.item_book_img);
        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                RemoveFavoritesDialog removeFavoritesDialog = new RemoveFavoritesDialog();
                removeFavoritesDialog.setRemoveFavoritesDialog((RemoveFavoritesDialog.RemoveFavoriteListener) object);
                removeFavoritesDialog.setFavorite(mData.get(position));
                removeFavoritesDialog.show(fragmentManager,"remove favorite item");
            }
        });
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                AddToCartDialog addToCartDialog = new AddToCartDialog();
                addToCartDialog.setBook(mData.get(position));
                addToCartDialog.show(fragmentManager, "add quantity dialog");
            }
        });

        holder.item_book_img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
    }


    @Override
    public int getItemCount ( ) {
        return mData.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_book, item_book_author, item_description;
        ImageView item_book_img;
        Button remove_item, item_view;
        CardView container;

        public FavoritesViewHolder (@NonNull View itemView) {
            super(itemView);
            item_book_img = itemView.findViewById(R.id.item_img);
            item_title_book = itemView.findViewById(R.id.item_title);
            item_book_author = itemView.findViewById(R.id.item_author);
            item_description = itemView.findViewById(R.id.item_description);
            remove_item = itemView.findViewById(R.id.item_remove);
            container = itemView.findViewById(R.id.container_id);
            item_view = itemView.findViewById(R.id.item_view);
        }
    }
}
