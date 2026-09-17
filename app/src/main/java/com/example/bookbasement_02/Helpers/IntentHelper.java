package com.example.bookbasement_02.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Map;

public class IntentHelper {

    private Context context;
    public IntentHelper(Context context){
        this.context = context;
    }


    /**
     *
     * @param data
     * @return void
     */
    public void setIntent(Map<String, String> data , final Class<? extends Activity> ActivityToOpen){
        Intent intent = new Intent(context, ActivityToOpen);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        for (Map.Entry<String, String> item : data.entrySet()) {
            intent.putExtra(item.getKey(),item.getValue());
        }
        context.startActivity(intent);
    }
}
