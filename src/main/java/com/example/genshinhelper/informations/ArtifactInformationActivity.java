package com.example.genshinhelper.informations;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.genshinhelper.APIHandler;
import com.example.genshinhelper.APIResponses;
import com.example.genshinhelper.R;
import com.example.genshinhelper.Setters;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtifactInformationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView storyTextView;
    private TextView effectTextView;
    private LinearLayout rarityContainer;
    private ImageView flowerImageView;
    private ImageView plumeImageView;
    private ImageView sandsImageView;
    private ImageView gobletImageView;
    private ImageView circletImageView;
    private TextView linkTextView;

    private String artifactName;
    private APIResponses.ArtifactResponse artifact;
    private ImageView selectedArtifactPart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_information);

        nameTextView = findViewById(R.id.name);
        flowerImageView = findViewById(R.id.flower);
        plumeImageView = findViewById(R.id.plume);
        sandsImageView = findViewById(R.id.sands);
        gobletImageView = findViewById(R.id.goblet);
        circletImageView = findViewById(R.id.circlet);
        storyTextView = findViewById(R.id.story);
        descriptionTextView = findViewById(R.id.description);
        rarityContainer = findViewById(R.id.rarity_container);
        effectTextView = findViewById(R.id.effect);
        linkTextView = findViewById(R.id.link);

        artifactName = getIntent().getStringExtra("artifact_name");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHandler api = retrofit.create(APIHandler.class);

        Call<APIResponses.ArtifactResponse> call = api.getArtifactInfo(artifactName.toLowerCase());
        call.enqueue(new Callback<APIResponses.ArtifactResponse>() {
            @Override
            public void onResponse(Call<APIResponses.ArtifactResponse> call, Response<APIResponses.ArtifactResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    artifact = response.body();
                    Log.d("ArtifactInfo", "Received artifact: " + new Gson().toJson(artifact));

                    nameTextView.setText(artifact.name);
                    descriptionTextView.setText(artifact.description);
                    storyTextView.setText(artifact.story);
                    Setters.setArtifactRarityImages(rarityContainer, artifact.rarityList);
                    Setters.setLink(linkTextView, artifact.name);

                    loadImageAndSetClick(flowerImageView, artifact.images.flower, "flower");
                    loadImageAndSetClick(plumeImageView, artifact.images.plume, "plume");
                    loadImageAndSetClick(sandsImageView, artifact.images.sands, "sands");
                    loadImageAndSetClick(gobletImageView, artifact.images.goblet, "goblet");
                    loadImageAndSetClick(circletImageView, artifact.images.circlet, "circlet");

                    String text = "<b>2-Piece effect: </b>" + artifact.effect2Pc + " <br><br>" +
                            "<b>4-Piece effect: </b>" + artifact.effect4Pc;
                    effectTextView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));

                } else {
                    Log.e("ArtifactInformationActivity", "Błąd w odpowiedzi: " + response.code());
                    linkTextView.setText("Brak danych.");
                }
            }

            @Override
            public void onFailure(Call<APIResponses.ArtifactResponse> call, Throwable t) {
                Log.e("ArtifactInformationActivity", "Błąd zapytania API: " + t.getMessage(), t);
                linkTextView.setText("Failed to load data.");
            }
        });
    }

    private void loadImageAndSetClick(ImageView imageView, String url, String type) {
        int placeholderRes = 0;
        switch (type) {
            case "flower":
                placeholderRes = R.drawable.icon_flower;
                break;
            case "plume":
                placeholderRes = R.drawable.icon_plume;
                break;
            case "sands":
                placeholderRes = R.drawable.icon_sands;
                break;
            case "goblet":
                placeholderRes = R.drawable.icon_goblet;
                break;
            case "circlet":
                placeholderRes = R.drawable.icon_circlet;
                break;

        }
        Glide.with(this)
                .load(url)
                .error(placeholderRes)
                .into(imageView);
        imageView.setBackground(null);
        imageView.setOnClickListener(v -> {
            if (selectedArtifactPart != null && selectedArtifactPart == v) {
                selectedArtifactPart.setBackground(null);
                selectedArtifactPart = null;
                nameTextView.setText(artifactName);
                descriptionTextView.setText(artifact.description);
                storyTextView.setText(artifact.story);
            } else {
                if (selectedArtifactPart != null) {
                    selectedArtifactPart.setBackground(null);
                }
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                selectedArtifactPart = (ImageView) v;

                onArtifactPartClicked(type);
            }
        });
    }

    private void onArtifactPartClicked(String pieceName) {
        if (artifact == null) return;

        APIResponses.ArtifactResponse.ArtifactPiece piece = null;
        switch (pieceName) {
            case "flower":
                piece = artifact.flower;
                break;
            case "plume":
                piece = artifact.plume;
                break;
            case "sands":
                piece = artifact.sands;
                break;
            case "goblet":
                piece = artifact.goblet;
                break;
            case "circlet":
                piece = artifact.circlet;
                break;
        }

        if (piece != null) {
            nameTextView.setText(piece.name);
            descriptionTextView.setText(piece.description);
            storyTextView.setText(piece.story);
        }
    }
}
