package com.example.bookbasement_02.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MessageContainerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragments;

    public MessageContainerAdapter (@NonNull FragmentManager fm) {
        super(fm);
        titleList = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    public void addFragment (Fragment fragment, String title) {
        fragments.add(fragment);
        titleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem (int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount ( ) {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
        return titleList.get(position);
    }

}
