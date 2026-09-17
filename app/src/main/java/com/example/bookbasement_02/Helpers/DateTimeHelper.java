package com.example.bookbasement_02.Helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.bookbasement_02.R;

import java.util.Calendar;

public class DateTimeHelper {
    private Context context;

    public DateTimeHelper(Context context){
        this.context = context;
    }

    public void handleDate(final Button dateText) {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                String monthString = "";
                try {
                    monthString = context.getResources().getStringArray(R.array.month_names)[month];
                } catch (ArrayIndexOutOfBoundsException e) {
                    monthString = Integer.toString(month);
                }
                String dateString = monthString + " " + date + " , " + year;
                dateText.setText(dateString);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }



}
