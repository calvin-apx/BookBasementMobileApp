package com.example.bookbasement_02.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bookbasement_02.Constants.URL;
import com.example.bookbasement_02.Models.Appointment;
import com.example.bookbasement_02.Models.Donate;
import com.example.bookbasement_02.Models.Product;
import com.example.bookbasement_02.Models.Recycle;
import com.example.bookbasement_02.R;
import com.example.bookbasement_02.RestApi.Api;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookbasement_02.App.CHANNEL_1_ID;

public class AppointmentSetterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AppointmentSetterActivity.class.getSimpleName();
    Product product;
    private NotificationManagerCompat notificationManager;
    private Button btnTime1,
            btnTime2,
            btnTime3,
            btnConfirmAppointment;
    private Spinner locationSpinner;
    private CalendarView calendarView;
    private String date = "";
    private String time = "";
    private String purpose = "";
    private Api api = new Api();
    private int quantity;
    private String location;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_layout);
        getSupportActionBar().hide();
        initialize();
        setIntentPassed();

        date = getCurrentDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange (@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "/" + month + "/" + dayOfMonth;
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                location = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

    }

    private String getCurrentDate ( ) {
        return Calendar.getInstance().get(Calendar.YEAR) + "/" + Calendar.getInstance().get(Calendar.MONTH) + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Initialize the views
     *
     * @return void
     */
    private void initialize ( ) {
        notificationManager = NotificationManagerCompat.from(this);
        calendarView = findViewById(R.id.calendar_id);
        btnTime1 = findViewById(R.id.btn_time1);
        btnTime2 = findViewById(R.id.btn_time2);
        btnTime3 = findViewById(R.id.btn_time3);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);
        locationSpinner = findViewById(R.id.location_id);

        btnTime1.setOnClickListener(this);
        btnTime2.setOnClickListener(this);
        btnTime3.setOnClickListener(this);
        btnConfirmAppointment.setOnClickListener(this);
    }


    /**
     * get the intent passed parameters
     *
     * @return void
     */
    private void setIntentPassed ( ) {
        Intent intent = getIntent();
        purpose = intent.getStringExtra("purpose");
        assert purpose != null;
        if (purpose.equals("donate")) {
            quantity = Integer.parseInt(intent.getStringExtra("donateQty"));
        } else if (purpose.equals("sell")) {
            product = (Product) intent.getSerializableExtra("product");
        } else if (purpose.equals("recycle")) {
            quantity = Integer.parseInt(intent.getStringExtra("recycleQty"));
        }


    }


    private void processBuyAppointment ( ) {
        Appointment appointment = new Appointment();
        appointment.setUser_id(URL.USER_ID);
        appointment.setTime(time);
        appointment.setDate(date);
        appointment.setPurpose(purpose);
        appointment.setLocation(location);
        appointment.setStatus("pending");


        Call<Appointment> call = api.apiInterface.storeBuyAppointment(appointment);
        call.enqueue(new Callback<Appointment>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse (final Call<Appointment> call, Response<Appointment> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to appointments")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick (SweetAlertDialog sDialog) {
                                Intent intent1 = new Intent(getBaseContext(), NotificationListActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent1, 0);

                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.book_view);
                                Notification notification = new NotificationCompat.Builder(getBaseContext(), CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.calendar)
                                        .setContentTitle("Added to appointments")
                                        .setContentText("Thank you for making an appointments")
                                        .setLargeIcon(largeIcon)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Thank You for buying from Book Basement \n" +
                                                        "We look forward for your next purchase.\n")
                                                .setBigContentTitle("Buying Book Appointment")
                                                .setSummaryText("Summary Text"))
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notificationManager.notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getBaseContext().startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }

            @Override
            public void onFailure (Call<Appointment> call, Throwable t) {
                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }


    /**
     * Process the Donation...
     *
     * @return null
     */
    private void processDonationAppointment ( ) {

        Donate donate = new Donate();
        donate.setUser_id(URL.USER_ID);
        donate.setTime(time);
        donate.setDate(date);
        donate.setPurpose(purpose);
        donate.setQty(quantity);
        donate.setLocation(location);
        donate.setStatus("pending");


        Call<Donate> call = api.apiInterface.storeDonateAppointment(donate);
        call.enqueue(new Callback<Donate>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse (Call<Donate> call, Response<Donate> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to appointments")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick (SweetAlertDialog sDialog) {
                                Intent intent1 = new Intent(getBaseContext(), NotificationListActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent1, 0);

                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.book_view);
                                Notification notification = new NotificationCompat.Builder(getBaseContext(), CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.calendar)
                                        .setContentTitle("Added to appointments")
                                        .setContentText("Thank you for making an appointments")
                                        .setLargeIcon(largeIcon)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Thank You for donating your book. \n" +
                                                        "Hope to see you soon.\n")
                                                .setBigContentTitle("Donating Book Appointment")
                                                .setSummaryText("Summary Text"))
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notificationManager.notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getBaseContext().startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }

            @Override
            public void onFailure (Call<Donate> call, Throwable t) {

                System.out.println("Error Response101 : " + t.getMessage());
            }
        });
    }

    private void processSellAppointment ( ) {
        Appointment appointment = new Appointment();
        appointment.setUser_id(URL.USER_ID);
        appointment.setTime(time);
        appointment.setDate(date);
        appointment.setPurpose(purpose);
        appointment.setLocation(location);
        appointment.setStatus("pending");
        appointment.setTitle(product.getTitle());
        appointment.setSub_title(product.getSub_title());
        appointment.setAuthors(product.getAuthors());
        appointment.setQty(product.getQty());
        appointment.setDescription(product.getDescription());
        appointment.setPrice(product.getPrice());
        appointment.setGenre(product.getGenre());
        appointment.setPublisher(product.getPublisher());
        appointment.setImage(product.getImage_url());
        appointment.setTotal(product.getPrice() * product.getQty());


        Call<Appointment> call = api.apiInterface.sellStore(appointment);
        call.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse (Call<Appointment> call, Response<Appointment> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to appointments")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick (SweetAlertDialog sDialog) {
                                Intent intent1 = new Intent(getBaseContext(), NotificationListActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent1, 0);

                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.book_view);
                                Notification notification = new NotificationCompat.Builder(getBaseContext(), CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.calendar)
                                        .setContentTitle("Added to appointments")
                                        .setContentText("Thank you for making an appointments")
                                        .setLargeIcon(largeIcon)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Thank You for Selling from Book Basement \n" +
                                                        "We look forward for you..\n")
                                                .setBigContentTitle("Selling Book Appointment")
                                                .setSummaryText("Summary Text"))
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notificationManager.notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), ContainerActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getBaseContext().startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }

            @Override
            public void onFailure (Call<Appointment> call, Throwable t) {

            }
        });

    }

    private void processRecycleAppointment ( ) {
        Recycle recycle = new Recycle();
        recycle.setUser_id(URL.USER_ID);
        recycle.setTime(time);
        recycle.setDate(date);
        recycle.setPurpose(purpose);
        recycle.setQty(quantity);
        recycle.setLocation(location);
        recycle.setStatus("pending");


        Call<Recycle> call = api.apiInterface.storeRecycle(recycle);
        call.enqueue(new Callback<Recycle>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse (Call<Recycle> call, Response<Recycle> response) {
                if (!response.isSuccessful()) {
                    new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
                new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Added to appointments")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick (SweetAlertDialog sDialog) {
                                Intent intent1 = new Intent(getBaseContext(), NotificationListActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent1, 0);

                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.book_view);
                                Notification notification = new NotificationCompat.Builder(getBaseContext(), CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.calendar)
                                        .setContentTitle("Added to appointments")
                                        .setContentText("Thank you for making an appointments")
                                        .setLargeIcon(largeIcon)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Thank You for setting appointment")
                                                .setBigContentTitle("Recycle Appointment")
                                                .setSummaryText("Summary Text"))
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setContentIntent(pendingIntent)
                                        .build();
                                notificationManager.notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), RecylingFacilitiesActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getBaseContext().startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }

            @Override
            public void onFailure (Call<Recycle> call, Throwable t) {

                System.out.println("Error Response101 : " + t.getMessage());
            }
        });

    }


    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn_time1:
                setActive(btnTime1, btnTime2, btnTime3);
                break;
            case R.id.btn_time2:
                setActive(btnTime2, btnTime1, btnTime3);
                break;
            case R.id.btn_time3:
                setActive(btnTime3, btnTime1, btnTime2);
                break;
            case R.id.btn_confirm_appointment:
                if (time == "" || time.length() == 0) {
                    new SweetAlertDialog(AppointmentSetterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Please set your time of appointment.")
                            .show();
                    break;
                }
                if (purpose.equals("donate")) {
                    processDonationAppointment();
                } else if (purpose.equals("buy")) {
                    processBuyAppointment();
                } else if (purpose.equals("sell")) {
                    processSellAppointment();
                } else if (purpose.equals("recycle")) {
                    processRecycleAppointment();
                }
                break;
        }
    }

    private void setActive (Button active, Button inActive1, Button inActive2) {
        time = active.getText().toString();
        active.setBackgroundColor(Color.parseColor("#5A5A5A"));
        active.setTextColor(Color.parseColor("#FFFFFF"));

        inActive1.setBackgroundColor(Color.parseColor("#CFCFCF"));
        inActive1.setTextColor(Color.parseColor("#757171"));

        inActive2.setBackgroundColor(Color.parseColor("#CFCFCF"));
        inActive2.setTextColor(Color.parseColor("#757171"));
    }
}
