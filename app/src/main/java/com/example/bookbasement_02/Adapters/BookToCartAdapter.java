package com.example.bookbasement_02.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bookbasement_02.Activities.AddToCartDialog;
import com.example.bookbasement_02.Activities.RemoveBookCartDialog;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.CartList;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;


import java.text.DecimalFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookToCartAdapter extends RecyclerView.Adapter<BookToCartAdapter.CartViewHolder> {
    private Context context;
    private List<CartList> mData;
    private List<Users> mUsers;
    private FragmentManager fragmentManager;
    public BookToCartAdapter (Context context, List<CartList> mData, List<Users> mUsers,FragmentManager fragmentManager) {
        this.context = context;
        this.mData = mData;
        this.mUsers = mUsers;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_cart, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (@NonNull CartViewHolder holder, final int position) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String sub_title = mData.get(position).getSub_title();
        if (sub_title.length() >= 30) {
            sub_title = sub_title.substring(0, 30) + "....";
        }

        holder.item_title_book.setText(mData.get(position).getTitle());
        holder.item_book_author.setText(sub_title);

        Glide.with(holder.itemView.getContext())
                .load(URL.IMAGE_URL_ADDRESS.concat(mData.get(position).getImage_url()))
                .transform(new CenterCrop(), new RoundedCorners(12))
                .into(holder.item_book_img);

        holder.item_qty.setText("Quantity: " + mData.get(position).getCart_qty());
        holder.item_price.setText("\u20B1 " + decimalFormat.format(mData.get(position).getPrice()));

        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                RemoveBookCartDialog removeBookCartDialog = new RemoveBookCartDialog();
                removeBookCartDialog.setCart(mData.get(position));
                removeBookCartDialog.show(fragmentManager,"remove cart item");

            }
        });
        holder.item_edit.setOnClickListener(new View.OnClickListener() {
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

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_book, item_book_author, item_qty, item_price ;
        ImageView item_book_img, remove_item , item_edit;
        CardView container;

        public CartViewHolder (@NonNull View itemView) {
            super(itemView);
            item_book_img    =  itemView.findViewById(R.id.item_img);
            item_title_book  =  itemView.findViewById(R.id.item_title);
            item_book_author =  itemView.findViewById(R.id.item_author);
            remove_item      =  itemView.findViewById(R.id.item_remove);
            container        =  itemView.findViewById(R.id.container_id);
            item_price       =  itemView.findViewById(R.id.item_price);
            item_qty         =  itemView.findViewById(R.id.item_genre);
            item_edit        =  itemView.findViewById(R.id.item_view);
        }
    }
}
