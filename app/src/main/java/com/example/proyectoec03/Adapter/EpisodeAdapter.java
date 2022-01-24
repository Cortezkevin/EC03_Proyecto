package com.example.proyectoec03.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoec03.EpisodeDetailsFragment;
import com.example.proyectoec03.Model.Episode;
import com.example.proyectoec03.NavigationHost;
import com.example.proyectoec03.R;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.LocationViewHolder> {

    List<Episode> episodeList;
    Fragment fragment;

    public EpisodeAdapter(List<Episode> episodeList, Fragment fragment){
        this.episodeList = episodeList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodes, parent, false);
        return new LocationViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if(episodeList != null && position < episodeList.size()){
            Episode episode = episodeList.get(position);
            holder.episode_number.setText("EPISODE "+episode.getId());
            holder.episode_name.setText(episode.getName());
            holder.episode_air_date.setText(episode.getAirDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_episode", episode.getId());
                    fragment.getParentFragmentManager().setFragmentResult("param_id",bundle);
                    ((NavigationHost) fragment.getActivity()).hideShowFragment(fragment, new EpisodeDetailsFragment(), "EPISODE_FRAGMENT");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder{

        public TextView episode_number;
        public TextView episode_name;
        public TextView episode_air_date;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            episode_number = itemView.findViewById(R.id.episode_number);
            episode_name = itemView.findViewById(R.id.episode_name);
            episode_air_date = itemView.findViewById(R.id.episode_air_date);
        }
    }
}
