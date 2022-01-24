package com.example.proyectoec03;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoec03.API.ApiService;
import com.example.proyectoec03.API.Service.CharactersService;
import com.example.proyectoec03.Adapter.CharacterAdapter;
import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.Model.ResponseList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharactersFragment extends Fragment implements SearchView.OnQueryTextListener{

    RecyclerView rv_characters;
    CharacterAdapter characterAdapter;
    List<Character> characterList = new ArrayList<>();
    TextView total_characters, page_characters;
    MaterialButton prev_page, next_page;

    SearchView sv_characters;
    ProgressDialog mDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.fragment_characters, parent, false);

        Util.Visualizacion = Util.GRID;

        total_characters = view.findViewById(R.id.total_characters);
        page_characters = view.findViewById(R.id.page_characters);
        prev_page = view.findViewById(R.id.prev_page);
        next_page = view.findViewById(R.id.next_page);

        rv_characters = view.findViewById(R.id.rv_characters);
        rv_characters.setHasFixedSize(true);
        rv_characters.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));

        sv_characters = view.findViewById(R.id.sv_characters);

        sv_characters.setOnQueryTextListener(this);

        mDialog = new ProgressDialog(getContext());

        mDialog.setMessage("Cargando informacion...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        page_characters.setText("PAGE: 1");
        prev_page.setEnabled(false);
        ApiService apiService = new ApiService();
        Call<ResponseList<Character>> call = apiService.characterService.getCharacters(1);
        call.enqueue(new Callback<ResponseList<Character>>() {
            @Override
            public void onResponse(Call<ResponseList<Character>> call, Response<ResponseList<Character>> response) {
                if(response.isSuccessful()){
                    List<Character> list = response.body().getResults();
                    characterAdapter = new CharacterAdapter(list, CharactersFragment.this);
                    rv_characters.setAdapter(characterAdapter);
                    total_characters.setText("TOTAL CHARACTERS: "+response.body().info.getCount().toString());
                    mDialog.dismiss();
                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseList<Character>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        prev_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page --;
                mDialog.show();
                if(page > 1){
                    page_characters.setText("PAGE: "+page);
                    getCharacters(page);
                }else{
                    page_characters.setText("PAGE: "+page);
                    prev_page.setEnabled(false);
                    getCharacters(1);
                }
            }
        });

        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page ++;
                mDialog.show();
                page_characters.setText("PAGE: "+page);
                prev_page.setEnabled(true);
                getCharacters(page);
            }
        });
        return view;
    }

    int page = 1;

    public void getCharacters(int page){
        ApiService apiService = new ApiService();
        Call<ResponseList<Character>> call = apiService.characterService.getCharacters(page);
        call.enqueue(new Callback<ResponseList<Character>>() {
            @Override
            public void onResponse(Call<ResponseList<Character>> call, Response<ResponseList<Character>> response) {
                if(response.isSuccessful()){
                    List<Character> list = response.body().getResults();
                    characterAdapter.characterListRefresh(list);
                    total_characters.setText("TOTAL CHARACTERS: "+response.body().info.getCount().toString());
                    mDialog.dismiss();
                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseList<Character>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        characterAdapter.filterCharacters(newText);
        return false;
    }
}
