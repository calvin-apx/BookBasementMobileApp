package com.example.bookbasement_02.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.bookbasement_02.Models.SearchFilter;
import com.example.bookbasement_02.R;

import java.io.Serializable;

public class FilterDialog extends DialogFragment {
    public FragmentAListener listener;
    EditText title_edit_text;
    EditText genre_edit_text;
    EditText author_edit_text;
    EditText publisher_edit_text;
    TextView clear_text_view;
    SeekBar price_range;
    private TextView price;

    public interface FragmentAListener {
        void setSearchData(SearchFilter searchData);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_filter, null);

        title_edit_text = (EditText) view.findViewById(R.id.title_id);
        genre_edit_text = (EditText) view.findViewById(R.id.genre_id);
        author_edit_text = (EditText) view.findViewById(R.id.author_id);
        publisher_edit_text = (EditText) view.findViewById(R.id.publisher_id);
        price_range = (SeekBar) view.findViewById(R.id.price_range_id);
        price = (TextView) view.findViewById(R.id.price_id);
        clear_text_view = (TextView) view.findViewById(R.id.clear_all_id);

        price.setText(0 + "");
        price_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
                price.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar) {

            }
        });

        builder.setView(view);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Save Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                SearchFilter searchFilter = new SearchFilter();
                searchFilter.setTitle(title_edit_text.getText()+"");
                searchFilter.setAuthor(author_edit_text.getText()+"");
                searchFilter.setGenre(genre_edit_text.getText()+"");
                searchFilter.setPublisher(publisher_edit_text.getText()+"");
                searchFilter.setPrice(Integer.parseInt(price.getText()+""));
                listener.setSearchData(searchFilter);


            }
        });

        clear_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                title_edit_text.setText(null);
                author_edit_text.setText(null);
                genre_edit_text.setText(null);
                publisher_edit_text.setText(null);
                price.setText(null);
                price_range.setProgress(0);
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      try {
          listener = (FragmentAListener) getTargetFragment();
      }catch (Exception e){
          System.out.println("ERROR: "+e);
      }


    }





}

