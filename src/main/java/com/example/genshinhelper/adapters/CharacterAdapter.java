package com.example.genshinhelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.genshinhelper.R;
import com.example.genshinhelper.informations.CharacterInformationActivity;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private Context context;
    private List<String> characterList;
    private List<String> filteredList;

    public CharacterAdapter(Context context, List<String> characterList) {
        this.context = context;
        this.characterList = characterList;
        this.filteredList = new ArrayList<>(characterList);
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        String characterName = filteredList.get(position);
        holder.characterNameTextView.setText(characterName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CharacterInformationActivity.class);
            intent.putExtra("character_name", characterName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(characterList);
        } else {
            for (String name : characterList) {
                if (name.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(name);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView characterNameTextView;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            characterNameTextView = itemView.findViewById(R.id.name);
        }
    }

    public void updateCharacterList(List<String> newList) {
        characterList.clear();
        characterList.addAll(newList);

        filteredList.clear();
        filteredList.addAll(newList);

        notifyDataSetChanged();
    }

}
