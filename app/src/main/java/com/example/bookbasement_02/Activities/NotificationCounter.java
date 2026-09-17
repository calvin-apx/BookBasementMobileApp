package com.example.bookbasement_02.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Appointment;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookbasement_02.App.CHANNEL_1_ID;

public class NotificationCounter {
    private int count = 0;
    private ImageView imageView;
    private CardView cardView;
    private TextView textView;
    private Api api = new Api();
    private Context mContext;
    private NotificationManagerCompat notificationManager;
    public NotificationCounter(View view , Context context, NotificationManagerCompat notificationManager){
        imageView = view.findViewById(R.id.notifcation_id);
        textView = view.findViewById(R.id.count_notificiation);
        cardView = view.findViewById(R.id.card_count_id);
        this.notificationManager = notificationManager;
        cardView.setVisibility(View.GONE);
        this.mContext = context;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(mContext, NotificationListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    public void getNumberOfNotifications(){
        Call<List<Appointment>> call = api.apiInterface.getAppointments(URL.USER_ID);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse (Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (!response.isSuccessful()) {

                }
                List<Appointment> newsItems = response.body();
                String notifcationMessage = "";
                for (Appointment appointment: newsItems) {
                        if(appointment.getStatus().equalsIgnoreCase("pending")) {
                            notifcationMessage += "=========================\n" +
                                    "Date: " + appointment.getDate() + " " + appointment.getTime() + " \n Location: " + appointment.getLocation() + "\n Purpose: " + appointment.getPurpose() + "" +
                                    "\n";
                            count++;
                        }
                        if(count > 0){
                            textView.setText(count+"");
                            cardView.setVisibility(View.VISIBLE);
                        }else {
                            textView.setText("");
                            cardView.setVisibility(View.GONE);
                        }
                }
                Intent intent1 = new Intent(mContext, NotificationListActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent1, 0);

                Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book_view);
                if(newsItems.size() > 0){
                    Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.calendar)
                            .setContentTitle("Added to appointments")
                            .setContentText("Thank you for making an appointments")
                            .setLargeIcon(largeIcon)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notifcationMessage)
                                    .setBigContentTitle("You have appointments on")
                                    .setSummaryText("Summary Text"))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .build();
                    notificationManager.notify(1, notification);
                }

            }

            @Override
            public void onFailure (Call<List<Appointment>> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }



}
