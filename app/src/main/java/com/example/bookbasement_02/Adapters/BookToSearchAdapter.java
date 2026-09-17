package com.example.bookbasement_02.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bookbasement_02.Activities.AddToCartDialog;
import com.example.bookbasement_02.Activities.GenreSearchActivity;
import com.example.bookbasement_02.Activities.ViewBook;
import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Favorite;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookToSearchAdapter extends RecyclerView.Adapter<BookToSearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Product> mData;
    private List<Product> mDataFiltered;
    private List<Users> mUsers;
    private FragmentManager fragmentManager;
    private Activity activity;
    private Api api = new Api();

    public BookToSearchAdapter (Context context, List<Product> mData, List<Users> mUsers, FragmentManager fragmentManager, Activity activity) {
        this.context = context;
        this.mData = mData;
        this.mDataFiltered = mData;
        this.mUsers = mUsers;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = mData ;
                }
                else {
                    List<Product> lstFiltered = new ArrayList<>();
                    for (Product row : mData) {
                        if (
                                row.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                        row.getAuthors().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                        row.getGenre().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                        row.getPublisher().toLowerCase().contains(constraint.toString().toLowerCase())
                        ){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDataFiltered = (List<Product>) results.values;
                notifyDataSetChanged();

            }
        };

    }


    @NonNull
    @Override
    public BookToSearchAdapter.MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (@NonNull final BookToSearchAdapter.MyViewHolder holder, final int position) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String sub_title = mDataFiltered.get(position).getSub_title();

        String book_title =  mDataFiltered.get(position).getTitle();
        if (sub_title.length() >= 30) {
            sub_title = sub_title.substring(0, 30) + "....";
        }
        if (book_title.length() >= 30) {
            book_title = book_title.substring(0, 20) + "....";
        }


        holder.item_title_book.setText(book_title);
        holder.item_book_author.setText(sub_title);
        holder.item_book_rating.setText(mDataFiltered.get(position).getRating() + "");
        holder.item_book_ratingBar.setRating(mDataFiltered.get(position).getRating());
        holder.item_book_price.setText("\u20B1 " + decimalFormat.format(mDataFiltered.get(position).getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(URL.IMAGE_URL_ADDRESS + "" + mDataFiltered.get(position).getImage_url())
                .transforms(new CenterCrop(), new RoundedCorners(12))
                .into(holder.item_book_img);

        holder.item_book_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(context, ViewBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("book_id", mDataFiltered.get(position).getBook_id());
                intent.putExtra("genre_id", mDataFiltered.get(position).getGenre_id());
                intent.putExtra("title", mDataFiltered.get(position).getTitle());
                intent.putExtra("sub_title", mDataFiltered.get(position).getSub_title());
                intent.putExtra("genre", mDataFiltered.get(position).getGenre());
                intent.putExtra("description", mDataFiltered.get(position).getDescription());
                intent.putExtra("price", mDataFiltered.get(position).getPrice());
                intent.putExtra("qty", mDataFiltered.get(position).getQty());
                intent.putExtra("total", mDataFiltered.get(position).getTotal());
                intent.putExtra("publisher", mDataFiltered.get(position).getPublisher());
                intent.putExtra("authors", mDataFiltered.get(position).getAuthors());
                intent.putExtra("page_count", mDataFiltered.get(position).getPage_count());
                intent.putExtra("image_url", mDataFiltered.get(position).getImage_url());
                intent.putExtra("rating", mDataFiltered.get(position).getRating());
                intent.putExtra("book_status", mDataFiltered.get(position).getStatus());
                context.startActivity(intent);
            }
        });

        holder.cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                AddToCartDialog addToCartDialog = new AddToCartDialog();
                addToCartDialog.setBook(mDataFiltered.get(position));
                addToCartDialog.show(fragmentManager, "add quantity dialog");
            }
        });

        holder.item_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Favorite favorite = new Favorite();
                favorite.setUser_id(URL.USER_ID);
                favorite.setBook_id(mDataFiltered.get(position).getBook_id());
                favorite.setStatus("AC");
                Call<Favorite> call = api.apiInterface.storeFavorites(favorite);
                call.enqueue(new Callback<Favorite>() {
                    @Override
                    public void onResponse (Call<Favorite> call, Response<Favorite> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error111", Toast.LENGTH_SHORT).show();
                        }
                        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to favorites")
                        .show();
                    }

                    @Override
                    public void onFailure (Call<Favorite> call, Throwable t) {
                        System.out.println("Error Response101 : " + t.getMessage());
                    }
                });

            }
        });

        holder.item_book_img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount ( ) {
        return mDataFiltered.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title_book, item_book_author, item_book_price, item_book_rating;
        ImageView item_book_img, item_favorite;
        RatingBar item_book_ratingBar;
        Button cart_btn;
        CardView container;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            item_book_img = itemView.findViewById(R.id.item_img);
            item_title_book = itemView.findViewById(R.id.item_title);
            item_book_author = itemView.findViewById(R.id.item_author);
            item_book_price = itemView.findViewById(R.id.item_price);
            item_book_rating = itemView.findViewById(R.id.item_genre);
            container = itemView.findViewById(R.id.container_id);
            item_book_ratingBar = itemView.findViewById(R.id.item_ratingbar);
            cart_btn = itemView.findViewById(R.id.btn_add_to_cart_id);
            item_favorite = itemView.findViewById(R.id.item_remove);
        }
    }
}
