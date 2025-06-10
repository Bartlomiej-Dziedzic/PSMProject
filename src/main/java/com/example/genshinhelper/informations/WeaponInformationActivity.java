package com.example.genshinhelper.informations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

public class WeaponInformationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView storyTextView;
    private TextView effectTextView;
    private TextView linkTextView;
    private TextView mainStatTextView;
    private ImageView weaponImageView;
    private ImageView rarityImageView;
    private ImageView weaponTypeImageView;
    public String weaponName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon_information);
        int placeholderRes = R.drawable.icon_weapon_sword;

        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.description);
        storyTextView = findViewById(R.id.story);
        effectTextView = findViewById(R.id.effect);
        mainStatTextView = findViewById(R.id.mainStat);
        linkTextView = findViewById(R.id.link);
        weaponImageView = findViewById(R.id.image);
        rarityImageView = findViewById(R.id.rarity);
        weaponTypeImageView = findViewById(R.id.weapon_type);

        weaponName = getIntent().getStringExtra("weapon_name");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHandler api = retrofit.create(APIHandler.class);

        Call<APIResponses.WeaponResponse> call = api.getWeaponInfo(weaponName.toLowerCase());
        call.enqueue(new Callback<APIResponses.WeaponResponse>() {
            @Override
            public void onResponse(Call<APIResponses.WeaponResponse> call, Response<APIResponses.WeaponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponses.WeaponResponse weapon = response.body();

                    nameTextView.setText(weapon.name);
                    descriptionTextView.setText(weapon.description);
                    storyTextView.setText(weapon.story);
                    mainStatTextView.setText(weapon.mainStatText);
                    Glide.with(WeaponInformationActivity.this)
                            .load(weapon.getImageUrl())
                            .error(placeholderRes)
                            .into(weaponImageView);

                    Setters.setRarityImage(rarityImageView, weapon.rarity);
                    Setters.setWeaponTypeImage(weaponTypeImageView, weapon.weaponText);
                    Setters.setLink(linkTextView, weaponName);
                    String effect = "<b>" + weapon.effectName + "</b><br>" + "<br>" +
                            "<b> R1: </b>" + weapon.r1.description + "<br>" + "<br>" +
                            "<b> R2: </b>" + weapon.r2.description + "<br>" + "<br>" +
                            "<b> R3: </b>" + weapon.r3.description + "<br>" + "<br>" +
                            "<b> R4: </b>" + weapon.r4.description + "<br>" + "<br>" +
                            "<b> R5: </b>" + weapon.r5.description;
                    effectTextView.setText(HtmlCompat.fromHtml(effect, HtmlCompat.FROM_HTML_MODE_LEGACY));

                } else {
                    Log.e("CharacterInformationActivity", "Błąd w odpowiedzi: " + response.code());
                    linkTextView.setText("Brak danych.");
                }
            }

            @Override
            public void onFailure(Call<APIResponses.WeaponResponse> call, Throwable t) {
                Log.e("WeaponInformationActivity", "Błąd zapytania API: " + t.getMessage(), t);
                linkTextView.setText("Failed to load data.");
            }
        });

    }
}
