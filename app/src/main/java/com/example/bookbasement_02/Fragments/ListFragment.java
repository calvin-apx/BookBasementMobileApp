package com.example.bookbasement_02.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.bookbasement_02.Activities.AddUserDonateBookActivity;
import com.example.bookbasement_02.Activities.AddUserSellBookActivity;
import com.example.bookbasement_02.R;

public class ListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_fragment,container,false);

       CardView sell_book = (CardView) view.findViewById(R.id.sell_book_id);
       CardView donate_book = (CardView) view.findViewById(R.id.donate_book_id);

        sell_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUserSellBookActivity.class);
                startActivity(intent);
            }
        });

        donate_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUserDonateBookActivity.class);
                startActivity(intent);
            }
        });

       return view;
    }
}
