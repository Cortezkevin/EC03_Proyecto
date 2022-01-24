package com.example.proyectoec03.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.proyectoec03.CharacterDetailsFragment;
import com.example.proyectoec03.ImageRequester;
import com.example.proyectoec03.Model.Character;
import com.example.proyectoec03.NavigationHost;
import com.example.proyectoec03.R;
import com.example.proyectoec03.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    List<Character> characterList;
    ImageRequester imageRequester;
    Fragment charactersFragment;
    List<Character> characterListFilter;

    public CharacterAdapter(List<Character> characterList, Fragment charactersFragment) {
        this.characterList = characterList;
        imageRequester = ImageRequester.getInstance();
        this.charactersFragment = charactersFragment;
        characterListFilter = new ArrayList<>();
        characterListFilter.addAll(characterList);
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = 0;
        if (Util.Visualizacion == Util.LIST) {
            layout = R.layout.item_characters;
        } else {
            layout = R.layout.character_card;
        }
        View viewLayout = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new CharacterViewHolder(viewLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        if (characterList != null && position < characterList.size()) {
            Character character = characterList.get(position);
            holder.card_name.setText(character.getName());
            holder.card_status.setText(character.getStatus());
            holder.card_species_gender.setText(character.getSpecies() + ", " + character.getGender());
            if (character.getStatus().equals("Alive")) {
                holder.card_status.setTextColor(Color.GREEN);
            } else if (character.getStatus().equals("Dead")) {
                holder.card_status.setTextColor(Color.RED);
            } else {
                holder.card_status.setTextColor(Color.GRAY);
            }
            imageRequester.setImageFromUrl(holder.card_image, character.getImage());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle param = new Bundle();
                    param.putInt("character_id", character.getId());

                    charactersFragment.getParentFragmentManager().setFragmentResult("params", param);
                    ((NavigationHost) charactersFragment.getActivity()).hideShowFragment(charactersFragment, new CharacterDetailsFragment(), "CHARACTER DETAILS");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {

        public TextView card_name;
        public TextView card_status;
        public TextView card_species_gender;
        public NetworkImageView card_image;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);

            if (Util.Visualizacion == Util.LIST) {
                card_image = itemView.findViewById(R.id.character_image);
                card_name = itemView.findViewById(R.id.character_name);
                card_status = itemView.findViewById(R.id.character_status);
                card_species_gender = itemView.findViewById(R.id.character_species_gender);
            } else {
                card_image = itemView.findViewById(R.id.card_image);
                card_name = itemView.findViewById(R.id.card_name);
                card_status = itemView.findViewById(R.id.card_status);
                card_species_gender = itemView.findViewById(R.id.card_species_gender);
            }
        }
    }

    public void characterListRefresh(List<Character> list) {
        characterList.clear();
        characterList.addAll(list);
        characterListFilter.clear();
        characterListFilter.addAll(list);
        notifyDataSetChanged();
    }

    public void filterCharacters(String text) {
        int length_text = text.length();
        if (length_text == 0) {
            characterList.clear();
            characterList.addAll(characterListFilter);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Character> list = characterList.stream().filter(i -> i.getName()
                        .toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
                characterList.clear();
                characterList.addAll(list);
            } else {
                for (Character c : characterListFilter) {
                    if (c.getName().toLowerCase().contains(text.toLowerCase())) {
                        characterList.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
