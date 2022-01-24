package com.example.proyectoec03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoec03.API.ApiService;
import com.example.proyectoec03.Adapter.EpisodeAdapter;
import com.example.proyectoec03.Model.Episode;
import com.example.proyectoec03.Model.ResponseList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Season4Fragment extends Fragment {

    RecyclerView rv_season04;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance) {
        View view = inflater.inflate(R.layout.fragment_season4, parent, false);

        rv_season04 = view.findViewById(R.id.rv_season04);

        ApiService apiService = new ApiService();
        Call<ResponseList<Episode>> call = apiService.episodesService.getEpisodes(2);
        call.enqueue(new Callback<ResponseList<Episode>>() {
            @Override
            public void onResponse(Call<ResponseList<Episode>> call, Response<ResponseList<Episode>> response) {
                if(response.isSuccessful()){
                    rv_season04.setHasFixedSize(true);
                    rv_season04.setLayoutManager(new GridLayoutManager(getContext(), 1,RecyclerView.VERTICAL, false));

                    List<Episode> episodeListSeason04 = new ArrayList<>();
                    for(Episode e: response.body().getResults()){
                        String season = e.getEpisode().substring(0,3);
                        if(season.equals("S04")){
                            episodeListSeason04.add(e);
                        }
                    }
                    EpisodeAdapter episodeAdapter = new EpisodeAdapter((List<Episode>) episodeListSeason04, Season4Fragment.this);
                    rv_season04.setAdapter(episodeAdapter);
                }else{
                    Toast.makeText(getContext(),"ERROR",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseList<Episode>> call, Throwable t) {
                Toast.makeText(getContext(),"ERROR",Toast.LENGTH_SHORT);
            }
        });

        return view;
    }
}
