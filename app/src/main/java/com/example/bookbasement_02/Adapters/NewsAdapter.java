package com.example.bookbasement_02.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookbasement_02.Models.Appointment;
import com.example.bookbasement_02.Models.Users;
import com.example.bookbasement_02.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {
    Context mContext;
    List<Appointment> mData ;
    List<Appointment> mDataFiltered ;
    List<Users> mUser;
    public NewsAdapter(Context mContext, List<Appointment> mData , List<Users> mUser) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_news,viewGroup,false);
        return new NewsViewHolder(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        String content = null;
        String title = mDataFiltered.get(position).getPurpose();
        String status = mDataFiltered.get(position).getStatus();
        String colorTextTitle = "#FFFFFF";
        String colorTextDate =  "#FFFFFF";

        if (title.equals("buy")) {
            colorTextTitle = "#2D82B5";
            colorTextDate = "#FB6602";
            content = "Thank You for buying from Book Basement \n" +
                    "We look forward for your next purchase.\n";
            newsViewHolder.img_user.setImageResource(R.drawable.uservoyager);
        }else if(title.equals("sell")){
            colorTextTitle = "#D87F81";
            colorTextDate = "#fcba03";
            content = "Thank You for selling your book \n" +
                    "We're sure many readers would enjoy this too..\n";
            newsViewHolder.img_user.setImageResource(R.drawable.circul6);
        }else if(title.equals("donate")){
            content = "Thank You for donating your book. \n" +
                    "Hope to see you soon.\n";
            colorTextTitle = "#C73866";
            colorTextDate = "#FE676E";
            newsViewHolder.img_user.setImageResource(R.drawable.useillust);
        }else{
            content = "Thank You for setting-up an appointment. \n" +
                    "Hope to see you soon.\n";
            colorTextTitle = "#C73866";
            colorTextDate = "#FE676E";
            newsViewHolder.img_user.setImageResource(R.drawable.useillust);
        }
        if(status.equals("pending")){
            newsViewHolder.status.setBackgroundColor(parseColor("#097bed"));
        }else if(status.equals("done")){
            newsViewHolder.status.setBackgroundColor(parseColor("#11ba57"));
        }else if(status.equals("cancel")){
            newsViewHolder.status.setBackgroundColor(parseColor("#eb0000"));
        }

        newsViewHolder.purpose.setTextColor(parseColor(colorTextTitle));

        newsViewHolder.dateAndTime.setTextColor(parseColor(colorTextDate));

        newsViewHolder.img_user.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        newsViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        newsViewHolder.purpose.setText("Purpose: "+mDataFiltered.get(position).getPurpose());
        newsViewHolder.description.setText(content);
        newsViewHolder.dateAndTime.setText(mDataFiltered.get(position).getDate() +" "+mDataFiltered.get(position).getTime() );
        newsViewHolder.status.setText(mData.get(position).getStatus());
        newsViewHolder.location.setText(mData.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
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
                    List<Appointment> lstFiltered = new ArrayList<>();
                    for (Appointment row : mData) {
                        if (row.getPurpose().toLowerCase().contains(Key.toLowerCase())){
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

                mDataFiltered = (List<Appointment>) results.values;
                notifyDataSetChanged();

            }
        };




    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView purpose, description, dateAndTime , location,status;
        ImageView img_user;
        CardView container;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            container   = itemView.findViewById(R.id.container_id);
            purpose     = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            dateAndTime = itemView.findViewById(R.id.tv_date);
            img_user    = itemView.findViewById(R.id.img_user);
            status      = itemView.findViewById(R.id.status_id);
            location    = itemView.findViewById(R.id.location_id);
        }
    }
}
