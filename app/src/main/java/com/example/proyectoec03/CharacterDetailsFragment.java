package com.example.proyectoec03;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoec03.Adapter.EpisodeAdapter;
import com.example.proyectoec03.Model.Character;
import com.android.volley.toolbox.NetworkImageView;
import com.example.proyectoec03.API.ApiService;
import com.example.proyectoec03.Model.Episode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterDetailsFragment extends Fragment {

    NetworkImageView image_character;
    TextView name_character, gender_character, species_character, origin_character, location_character, status_character;
    RecyclerView rv_episodes_character;
    ProgressDialog mDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.fragment_character_details, parent, false);

        getParentFragmentManager().setFragmentResultListener("params", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                getCharacterById(bundle.getInt("character_id"));
            }
        });
        mDialog = new ProgressDialog(getContext());

        mDialog.setMessage("Cargando informacion..."); //mensaje que tedra el dialog
        mDialog.setCanceledOnTouchOutside(false); //para que el usuario no pueda quitarlo
        mDialog.show();

        image_character = view.findViewById(R.id.image_character);
        name_character = view.findViewById(R.id.name_character);
        gender_character = view.findViewById(R.id.gender_character);
        species_character = view.findViewById(R.id.species_character);
        origin_character = view.findViewById(R.id.origin_character);
        location_character = view.findViewById(R.id.location_character);
        status_character = view.findViewById(R.id.status_character);

        rv_episodes_character = view.findViewById(R.id.rv_episodes_character);

        return view;
    }

    public void getCharacterById(int id){
        ApiService apiService = new ApiService();
        Call<Character> call =  apiService.characterService.getCharacterById(id);
        call.enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if(response.isSuccessful()){
                    Character obj = response.body();
                    ImageRequester imageRequester = ImageRequester.getInstance();
                    imageRequester.setImageFromUrl(image_character, obj.getImage());
                    name_character.setText(obj.getName());
                    gender_character.setText(obj.getGender());
                    species_character.setText(obj.getSpecies());
                    origin_character.setText(obj.getOrigin().getName());
                    location_character.setText(obj.getLocation().getName());
                    status_character.setText(obj.getStatus());
                    if(obj.getStatus().equals("Alive")){
                        status_character.setTextColor(Color.GREEN);
                    }else if(obj.getStatus().equals("Dead")){
                        status_character.setTextColor(Color.RED);
                    }else{
                        status_character.setTextColor(Color.GRAY);
                    }
                    List<String> listEpisodeUrl = obj.getEpisode();

                    String num = "0";
                    for (int i=0; i < listEpisodeUrl.size(); i++){
                        num = num + ","+listEpisodeUrl.get(i).replaceAll("[^0-9]+", "").trim();
                    }

                    getEpisodes(num);
                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR AL CONECTAR CON EL SERVICIO", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getEpisodes(String ids){
        ApiService apiService = new ApiService();
        Call<List<Episode>> call = apiService.episodesService.getMultipleEpisodes(ids);
        call.enqueue(new Callback<List<Episode>>() {
            @Override
            public void onResponse(Call<List<Episode>> call, Response<List<Episode>> response) {
                if(response.isSuccessful()){
                    rv_episodes_character.setHasFixedSize(true);
                    rv_episodes_character.setLayoutManager(new GridLayoutManager(getContext(), 1,RecyclerView.VERTICAL, false));
                    EpisodeAdapter episodeAdapter = new EpisodeAdapter((List<Episode>) response.body(), CharacterDetailsFragment.this);
                    rv_episodes_character.setAdapter(episodeAdapter);
                    mDialog.dismiss();
                }else{
                    Toast.makeText(getContext(),"ERROR",Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onFailure(Call<List<Episode>> call, Throwable t) {
                Toast.makeText(getContext(),"ERROR AL CONECTAR AL SERVIDOR",Toast.LENGTH_SHORT);
            }
        });
    }
}
