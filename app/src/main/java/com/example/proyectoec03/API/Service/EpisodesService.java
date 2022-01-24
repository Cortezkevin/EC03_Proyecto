package com.example.proyectoec03.API.Service;

import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.Model.Episode;
import com.example.proyectoec03.Model.ResponseList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EpisodesService {

    @GET("episode")
    Call<ResponseList<Episode>> getEpisodes(@Query("page") Integer page);

    @GET("episode/{ids}")
    Call<List<Episode>> getMultipleEpisodes(@Path("ids") String ids);

    @GET("episode/{id}")
    Call<Episode> getEpisodeById(@Path("id") Integer id);
}
