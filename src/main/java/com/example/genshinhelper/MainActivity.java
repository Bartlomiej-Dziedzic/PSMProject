package com.example.genshinhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.genshinhelper.selections.CharacterSelectionActivity;

public class MainActivity extends AppCompatActivity {

    Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        View.OnClickListener goToCharacterSelection = v -> {
            Intent intent = new Intent(MainActivity.this, CharacterSelectionActivity.class);
            startActivity(intent);
        };

        button1.setOnClickListener(v -> openCharacterSelection(1));
        button2.setOnClickListener(v -> openCharacterSelection(2));
        button3.setOnClickListener(v -> openCharacterSelection(3));
    }
    private void openCharacterSelection(int slot) {
        Intent intent;
        switch(slot)
        {
            case 1:
                intent = new Intent(MainActivity.this, CharacterSelectionActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MainActivity.this, CharacterSelectionActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(MainActivity.this, CharacterSelectionActivity.class);
                startActivity(intent);
                break;
        }
    }

}