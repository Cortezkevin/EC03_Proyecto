package com.example.proyectoec03.API.Service;

import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.Model.Episode;
import com.example.proyectoec03.Model.ResponseList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CharactersService {

    @GET("character")
    Call<ResponseList<Character>> getCharacters(@Query("page") Integer page);

    @GET("character/{id}")
    Call<Character> getCharacterById(@Path("id") Integer id);

    @GET("character/{ids}")
    Call<List<Character>> getMultipleCharacters(@Path("ids") String ids);
}
