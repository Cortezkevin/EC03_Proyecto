package com.example.proyectoec03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterEpisodes extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterEpisodes(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Season1Fragment();
            case 1:
                return new Season2Fragment();
            case 2:
                return new Season3Fragment();
            case 3:
                return new Season4Fragment();
            case 4:
                return new Season5Fragment();
            default:
                return new Season1Fragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
