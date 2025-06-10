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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genshinhelper.APIHandler;
import com.example.genshinhelper.R;
import com.example.genshinhelper.SpinnerAdapter;
import com.example.genshinhelper.adapters.ArtifactAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtifactSelectionActivity extends AppCompatActivity {

    private List<String> artifactNames = new ArrayList<>();
    private ArtifactAdapter adapter;
    public APIHandler api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_artifact_selection);

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
                switch (position) {
                    case 0:
                        fetchByFilter("names");
                        break;
                    case 1:
                        fetchByFilter("1");
                        break;
                    case 2:
                        fetchByFilter("2");
                        break;
                    case 3:
                        fetchByFilter("3");
                        break;
                    case 4:
                        fetchByFilter("4");
                        break;
                    case 5:
                        fetchByFilter("5");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText searchInput = findViewById(R.id.search_input);
        RecyclerView recyclerView = findViewById(R.id.artifact_list);
        adapter = new ArtifactAdapter(this, artifactNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(APIHandler.class);

        Call<List<String>> call = api.getArtifactNames("names", true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateArtifactList(response.body());
                } else {
                    Toast.makeText(ArtifactSelectionActivity.this, "No response from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ArtifactSelectionActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void fetchByFilter(String query) {
        Call<List<String>> call = api.getArtifactNames(query, true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateArtifactList(response.body());
                } else {
                    Toast.makeText(ArtifactSelectionActivity.this, "Brak wyników dla filtra " + query, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ArtifactSelectionActivity.this, "Błąd API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
