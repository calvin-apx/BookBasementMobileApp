package com.example.bookbasement_02.Activities;

import android.app.Activity;
import android.content.Context;

import com.example.bookbasement_02.R;
import com.tapadoo.alerter.Alerter;

public class AlertMessageHelper {
    private Activity activity;

    public AlertMessageHelper (Context context) {
        this.activity = (Activity) context;
    }

    public void success (String message) {
        Alerter.create(activity)
                .setTitle("Success!")
                .setText(message)
                .setDuration(5000)
                .setBackgroundColorRes(R.color.colorSuccess)
                .setIcon(R.drawable.alerter_ic_notifications)
                .show();
    }

    public void error (String message) {
        Alerter.create(activity)
                .setTitle("Error!")
                .setText(message)
                .setDuration(5000)
                .setBackgroundColorRes(R.color.colorError)
                .setIcon(R.drawable.alerter_ic_notifications)
                .show();
    }

}
