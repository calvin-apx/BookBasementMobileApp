package com.example.bookbasement_02.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookbasement_02.R;

public class TestFragment extends Fragment {

    final String TAG = "fragment_lifecyle";
    public TestFragment(){}

    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG , "onAttach");
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG , "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG , "onCreateView");
        return inflater.inflate(R.layout.sample_fragment_layout, container ,false);
    }

    @Override
    public void onResume ( ) {
        super.onResume();
        Log.i(TAG , "onResume");
    }

    @Override
    public void onPause ( ) {
        super.onPause();
        Log.i(TAG , "onPause");
    }
}
