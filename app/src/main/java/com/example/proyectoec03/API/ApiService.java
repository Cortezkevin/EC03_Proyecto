package com.example.proyectoec03.API;

import android.widget.Toast;

import com.example.proyectoec03.API.Service.CharactersService;
import com.example.proyectoec03.API.Service.EpisodesService;
import com.example.proyectoec03.Adapter.CharacterAdapter;
import com.example.proyectoec03.Application.ProyectoEC03Application;
import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.Model.ResponseList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kotlin.Lazy;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiService {

    private static final String BASE_URL = "https://rickandmortyapi.com/api/";

    public CharactersService characterService = getRetrofit().create(CharactersService.class);
    public EpisodesService episodesService = getRetrofit().create(EpisodesService.class);

    private static Interceptor getInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {  //interceptor sirve para obtener algo y devolvemos lo mismo pero modificado
                Request original = chain.request(); // obtenemos peticion original que esta haciendo retrofit
                Request request = original.newBuilder() // recontrostruimos la peticion y le añadimos nuestra configuracion
                        .header("Accept", "application/json") //que acepte solo archivos json
                        .method(original.method(), original.body()) //el metodo y el cuerpo son los mismos
                        .build(); //construimos
                return chain.proceed(request); // devolvemos
            }
        };
    }

    public static Retrofit getRetrofit(){
        Retrofit.Builder builder;
        builder = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(getInterceptor())
                        .readTimeout(60, TimeUnit.SECONDS) //tiempo de lectura de la peticion
                        .connectTimeout(60, TimeUnit.SECONDS) //tiempo de conexion de la peticion
                        .build())  //construimos
                .baseUrl(BASE_URL)  //indicamos la url donde hara la peticion
                .addConverterFactory(GsonConverterFactory.create()); //convertimos a json
        return builder.build();
    }

}
