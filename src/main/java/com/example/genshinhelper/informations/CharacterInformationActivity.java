package com.example.genshinhelper.informations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.genshinhelper.APIHandler;
import com.example.genshinhelper.APIResponses;
import com.example.genshinhelper.R;
import com.example.genshinhelper.Setters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterInformationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView voiceActorTextView;
    private TextView birthdayTextView;
    private TextView constellationsTextView;
    private TextView linkTextView;
    private ImageView characterImageView;
    private ImageView rarityImageView;
    private ImageView weaponTypeImageView;
    private ImageView elementImageView;
    public String characterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_information);

        int placeholderRes = R.drawable.icon_character;
        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.description);
        voiceActorTextView = findViewById(R.id.voice_actor);
        birthdayTextView = findViewById(R.id.birthday);
        constellationsTextView = findViewById(R.id.constellations);
        linkTextView = findViewById(R.id.link);
        characterImageView = findViewById(R.id.image);
        rarityImageView = findViewById(R.id.rarity);
        weaponTypeImageView = findViewById(R.id.weapon_type);
        elementImageView = findViewById(R.id.element);

        characterName = getIntent().getStringExtra("character_name");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHandler api = retrofit.create(APIHandler.class);

        Call<APIResponses.CharacterResponse> call = api.getCharacterInfo(characterName.toLowerCase());
        call.enqueue(new Callback<APIResponses.CharacterResponse>() {
            @Override
            public void onResponse(Call<APIResponses.CharacterResponse> call, Response<APIResponses.CharacterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponses.CharacterResponse character = response.body();

                    nameTextView.setText(character.name);
                    descriptionTextView.setText(character.description);
                    voiceActorTextView.setText("Voice Actor (EN): " + character.getCv());
                    birthdayTextView.setText("Birthday: " + character.birthday);

                    Glide.with(CharacterInformationActivity.this)
                            .load(character.getImageUrl())
                            .error(placeholderRes)
                            .into(characterImageView);

                    Setters.setRarityImage(rarityImageView,character.rarity);
                    Setters.setWeaponTypeImage(weaponTypeImageView ,character.weaponText);
                    Setters.setElementImage(elementImageView, nameTextView,character.element);
                    Setters.setLink(linkTextView,characterName);
                    getConstellations(api);
                } else {
                    Log.e("CharacterInformationActivity", "Błąd w odpowiedzi: " + response.code());
                    linkTextView.setText("Brak danych.");
                }
            }

            @Override
            public void onFailure(Call<APIResponses.CharacterResponse> call, Throwable t) {
                Log.e("CharacterInformationActivity", "Błąd zapytania API: " + t.getMessage(), t);
                linkTextView.setText("Failed to load data.");
            }
        });

    }
    private void getConstellations(APIHandler api) {
        Call<APIResponses.ConstellationResponse> constellationsCall = api.getConstellations(characterName.toLowerCase());
        constellationsCall.enqueue(new Callback<APIResponses.ConstellationResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<APIResponses.ConstellationResponse> call, Response<APIResponses.ConstellationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponses.ConstellationResponse constellation = response.body();

                    String text =
                            "<b>C1: " + constellation.c1.name + "</b>: " + constellation.c1.description + "<br>" + "<br>" +
                                    "<b>C2: " + constellation.c2.name + "</b>: " + constellation.c2.description + "<br>" + "<br>" +
                                    "<b>C3: " + constellation.c3.name + "</b>: " + constellation.c3.description + "<br>" + "<br>" +
                                    "<b>C4: " + constellation.c4.name + "</b>: " + constellation.c4.description + "<br>" + "<br>" +
                                    "<b>C5: " + constellation.c5.name + "</b>: " + constellation.c5.description + "<br>" + "<br>" +
                                    "<b>C6: " + constellation.c6.name + "</b>: " + constellation.c6.description + "<br>";

                    constellationsTextView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    Log.e("Constellations", "Nieudana odpowiedź: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponses.ConstellationResponse> call, Throwable t) {
                Log.e("Constellations", "Błąd zapytania: " + t.getMessage(), t);
            }
        });
    }
}
