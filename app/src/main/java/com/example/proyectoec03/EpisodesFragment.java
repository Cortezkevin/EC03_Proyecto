package com.example.proyectoec03;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class EpisodesFragment extends Fragment {

    TabLayout tab_layout_episodes;
    ViewPager vp_episodes;
    TabItem tab_season_1, tab_season_2, tab_season_3, tab_season_4, tab_season_5;
    PageAdapterEpisodes pageAdapterEpisodes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.fragment_episodes, parent, false);

        tab_layout_episodes = view.findViewById(R.id.tab_layout_episodes);
        vp_episodes = view.findViewById(R.id.vp_episodes);
        tab_season_1 = view.findViewById(R.id.tab_season_1);
        tab_season_2 = view.findViewById(R.id.tab_season_2);
        tab_season_3 = view.findViewById(R.id.tab_season_3);
        tab_season_4 = view.findViewById(R.id.tab_season_4);
        tab_season_5 = view.findViewById(R.id.tab_season_5);



        pageAdapterEpisodes = new PageAdapterEpisodes(getParentFragmentManager(), tab_layout_episodes.getTabCount());
        vp_episodes.setAdapter(pageAdapterEpisodes);

        tab_layout_episodes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_episodes.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp_episodes.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout_episodes));
        return view;
    }
}
