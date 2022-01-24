package com.example.proyectoec03;

import android.app.ProgressDialog;
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

import com.example.proyectoec03.API.ApiService;
import com.example.proyectoec03.Adapter.CharacterAdapter;
import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.Model.Episode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeDetailsFragment extends Fragment {

    ApiService apiService = new ApiService();

    RecyclerView rv_character_episode;
    CharacterAdapter characterAdapter;
    TextView name_episode, number_episode, air_date_episode;

    ProgressDialog mDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance) {
        View view = inflater.inflate(R.layout.fragment_episodes_details, parent, false);

        Util.Visualizacion = Util.LIST;

        rv_character_episode = view.findViewById(R.id.rv_character_episode);
        name_episode = view.findViewById(R.id.name_episode);
        number_episode = view.findViewById(R.id.number_episode);
        air_date_episode = view.findViewById(R.id.air_date_episode);

        mDialog = new ProgressDialog(getContext());

        mDialog.setMessage("Cargando informacion...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        getParentFragmentManager().setFragmentResultListener("param_id", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getEpisodeById(result.getInt("id_episode"));
            }
        });
        return view;
    }

    public void getEpisodeById(Integer id) {
        Call<Episode> call = apiService.episodesService.getEpisodeById(id);
        call.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Call<Episode> call, Response<Episode> response) {
                if (response.isSuccessful()) {
                    Episode episode = response.body();
                    name_episode.setText(episode.getName());
                    number_episode.setText("EPISODE " + episode.getId());
                    air_date_episode.setText(episode.getAirDate());

                    List<String> listCharacterUrl = episode.getCharacters();
                    String num = "0";
                    for (int i=0; i < listCharacterUrl.size(); i++){
                        num = num + ","+listCharacterUrl.get(i).replaceAll("[^0-9]+", "").trim();
                    }

                    getCharacters(num);

                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Episode> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR AL CONECTAR AL SERVICIO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCharacters(String ids){
        Call<List<Character>> call = apiService.characterService.getMultipleCharacters(ids);
        call.enqueue(new Callback<List<Character>>() {
            @Override
            public void onResponse(Call<List<Character>> call, Response<List<Character>> response) {
                if (response.isSuccessful()){

                    rv_character_episode.setHasFixedSize(true);
                    rv_character_episode.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
                    CharacterAdapter characterAdapter = new CharacterAdapter(response.body(), EpisodeDetailsFragment.this);
                    rv_character_episode.setAdapter(characterAdapter);
                    mDialog.dismiss();
                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Character>> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR AL CONECTAR AL SERVICIO", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
