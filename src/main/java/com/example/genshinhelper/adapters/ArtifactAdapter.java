package com.example.genshinhelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.genshinhelper.R;
import com.example.genshinhelper.informations.ArtifactInformationActivity;

import java.util.ArrayList;
import java.util.List;

public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ArtifactViewHolder> {

    private Context context;
    private List<String> artifactList;
    private List<String> filteredList;

    public ArtifactAdapter(Context context, List<String> artifactList) {
        this.context = context;
        this.artifactList = artifactList;
        this.filteredList = new ArrayList<>(artifactList);
    }

    @Override
    public ArtifactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ArtifactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtifactViewHolder holder, int position) {
        String artifactName = filteredList.get(position);
        holder.artifactNameTextView.setText(artifactName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArtifactInformationActivity.class);
            intent.putExtra("artifact_name", artifactName);
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
            filteredList.addAll(artifactList);
        } else {
            for (String name : artifactList) {
                if (name.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(name);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ArtifactViewHolder extends RecyclerView.ViewHolder {
        TextView artifactNameTextView;

        public ArtifactViewHolder(View itemView) {
            super(itemView);
            artifactNameTextView = itemView.findViewById(R.id.name);
        }
    }

    public void updateArtifactList(List<String> newList) {
        artifactList.clear();
        artifactList.addAll(newList);

        filteredList.clear();
        filteredList.addAll(newList);

        notifyDataSetChanged();
    }

}
