package com.example.genshinhelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.genshinhelper.R;
import com.example.genshinhelper.informations.WeaponInformationActivity;

import java.util.ArrayList;
import java.util.List;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.WeaponViewHolder> {

    private Context context;
    private List<String> weaponList;
    private List<String> filteredList;

    public WeaponAdapter(Context context, List<String> weaponList) {
        this.context = context;
        this.weaponList = weaponList;
        this.filteredList = new ArrayList<>(weaponList);
    }

    @Override
    public WeaponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new WeaponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeaponViewHolder holder, int position) {
        String weaponName = filteredList.get(position);
        holder.weaponNameTextView.setText(weaponName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WeaponInformationActivity.class);
            intent.putExtra("weapon_name", weaponName);
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
            filteredList.addAll(weaponList);
        } else {
            for (String name : weaponList) {
                if (name.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(name);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class WeaponViewHolder extends RecyclerView.ViewHolder {
        TextView weaponNameTextView;

        public WeaponViewHolder(View itemView) {
            super(itemView);
            weaponNameTextView = itemView.findViewById(R.id.name);
        }
    }

    public void updateWeaponList(List<String> newList) {
        weaponList.clear();
        weaponList.addAll(newList);

        filteredList.clear();
        filteredList.addAll(newList);

        notifyDataSetChanged();
    }

}
