package com.example.genshinhelper.selections;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genshinhelper.APIHandler;
import com.example.genshinhelper.R;
import com.example.genshinhelper.SpinnerAdapter;
import com.example.genshinhelper.adapters.WeaponAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeaponSelectionActivity extends AppCompatActivity {

    private List<String> weaponNames = new ArrayList<>();
    private WeaponAdapter adapter;
    public ImageView weaponSword;
    public ImageView weaponClaymore;
    public ImageView weaponPolearm;
    public ImageView weaponCatalyst;
    public ImageView weaponBow;

    public APIHandler api;
    private ImageView selectedWeapon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weapon_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner raritySpinner = findViewById(R.id.rarity_spinner);

        int[] rarityImages = {
                0,
                R.drawable.icon_1_star,
                R.drawable.icon_2_star,
                R.drawable.icon_3_star,
                R.drawable.icon_4_star,
                R.drawable.icon_5_star
        };

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, rarityImages);
        raritySpinner.setAdapter(spinnerAdapter);

        raritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedWeapon != null) {
                    selectedWeapon.setBackground(null);
                    selectedWeapon = null;
                }
                switch (position) {
                    case 0:
                        fetchWeaponsByFilter("names");
                        break;
                    case 1:
                        fetchWeaponsByFilter("1");
                        break;
                    case 2:
                        fetchWeaponsByFilter("2");
                        break;
                    case 3:
                        fetchWeaponsByFilter("3");
                        break;
                    case 4:
                        fetchWeaponsByFilter("4");
                        break;
                    case 5:
                        fetchWeaponsByFilter("5");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText searchInput = findViewById(R.id.search_input);
        RecyclerView recyclerView = findViewById(R.id.weapon_list);
        weaponSword = findViewById(R.id.imageViewSword);
        weaponClaymore = findViewById(R.id.imageViewClaymore);
        weaponPolearm = findViewById(R.id.imageViewPolearm);
        weaponCatalyst = findViewById(R.id.imageViewCatalyst);
        weaponBow = findViewById(R.id.imageViewBow);

        View.OnClickListener listener = v -> {
            if (selectedWeapon != null && selectedWeapon == v) {
                selectedWeapon.setBackground(null);
                selectedWeapon = null;
                fetchWeaponsByFilter("names");
            } else {
                if (selectedWeapon != null) {
                    selectedWeapon.setBackground(null);
                }
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                selectedWeapon = (ImageView) v;
                raritySpinner.setSelection(0);

                int id = v.getId();
                if (id == R.id.imageViewSword) {
                    fetchWeaponsByFilter("weapon_sword_one_hand");
                } else if (id == R.id.imageViewClaymore) {
                    fetchWeaponsByFilter("weapon_claymore");
                } else if (id == R.id.imageViewPolearm) {
                    fetchWeaponsByFilter("weapon_pole");
                } else if (id == R.id.imageViewCatalyst) {
                    fetchWeaponsByFilter("weapon_catalyst");
                } else if (id == R.id.imageViewBow) {
                    fetchWeaponsByFilter("weapon_bow");
                }
            }
        };

        weaponSword.setOnClickListener(listener);
        weaponClaymore.setOnClickListener(listener);
        weaponPolearm.setOnClickListener(listener);
        weaponCatalyst.setOnClickListener(listener);
        weaponBow.setOnClickListener(listener);


        adapter = new WeaponAdapter(this, weaponNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(APIHandler.class);

        Call<List<String>> call = api.getWeaponNames("names", true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateWeaponList(response.body());
                } else {
                    Toast.makeText(WeaponSelectionActivity.this, "No response from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(WeaponSelectionActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void fetchWeaponsByFilter(String query) {
        Call<List<String>> call = api.getWeaponNames(query, true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateWeaponList(response.body());
                } else {
                    Toast.makeText(WeaponSelectionActivity.this, "Brak wyników dla filtra " + query, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(WeaponSelectionActivity.this, "Błąd API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
