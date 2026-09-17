package com.example.bookbasement_02.Fragments;

import androidx.fragment.app.Fragment;

public class Fragments {
    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    Fragment fragment;

    public Fragments(Fragment fragment) {
        this.fragment = fragment;
    }
}
