package com.example.genshinhelper.selections;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.genshinhelper.adapters.CharacterAdapter;
import com.example.genshinhelper.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterSelectionActivity extends AppCompatActivity {

    private List<String> characterNames = new ArrayList<>();
    private CharacterAdapter adapter;
    private ImageView pyroImageView;
    private ImageView hydroImageView;
    private ImageView electroImageView;
    private ImageView cryoImageView;
    private ImageView anemoImageView;
    private ImageView dendroImageView;
    private ImageView geoImageView;

    private ImageView selectedCharacter = null;
    public APIHandler api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_character_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText searchInput = findViewById(R.id.search_input);
        RecyclerView recyclerView = findViewById(R.id.character_list);

        View.OnClickListener listener = v -> {
            if (selectedCharacter != null && selectedCharacter == v) {
                selectedCharacter.setBackground(null);
                selectedCharacter = null;
                fetchByFilter("names");
            } else {
                if (selectedCharacter != null) {
                    selectedCharacter.setBackground(null);
                }
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                selectedCharacter = (ImageView) v;

                int id = v.getId();
                if (id == R.id.pyro) {
                    fetchByFilter("pyro");
                } else if (id == R.id.hydro) {
                    fetchByFilter("hydro");
                } else if (id == R.id.electro) {
                    fetchByFilter("electro");
                } else if (id == R.id.cryo) {
                    fetchByFilter("cryo");
                } else if (id == R.id.anemo) {
                    fetchByFilter("anemo");
                } else if (id == R.id.dendro) {
                    fetchByFilter("dendro");
                }else if (id == R.id.geo) {
                    fetchByFilter("geo");
                }
            }
        };

        pyroImageView = findViewById(R.id.pyro);
        hydroImageView = findViewById(R.id.hydro);
        electroImageView = findViewById(R.id.electro);
        cryoImageView = findViewById(R.id.cryo);
        anemoImageView = findViewById(R.id.anemo);
        dendroImageView = findViewById(R.id.dendro);
        geoImageView = findViewById(R.id.geo);
        pyroImageView.setOnClickListener(listener);
        hydroImageView.setOnClickListener(listener);
        electroImageView.setOnClickListener(listener);
        cryoImageView.setOnClickListener(listener);
        anemoImageView.setOnClickListener(listener);
        dendroImageView.setOnClickListener(listener);
        geoImageView.setOnClickListener(listener);

        adapter = new CharacterAdapter(this, characterNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genshin-db-api.vercel.app/api/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(APIHandler.class);

        Call<List<String>> call = api.getCharacterNames("names", true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateCharacterList(response.body());
                } else {
                    Toast.makeText(CharacterSelectionActivity.this, "No response from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(CharacterSelectionActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fetchByFilter(String element) {
        Call<List<String>> call = api.getCharacterNames(element, true);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateCharacterList(response.body());
                } else {
                    Toast.makeText(CharacterSelectionActivity.this, "Brak wyników dla filtra " + element, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(CharacterSelectionActivity.this, "Błąd API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
