package com.byrd.gmtools;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        Button diceRollerButton = findViewById(R.id.diceRollerButton);

        diceRollerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiceRollerActivity.class);
            startActivity(intent);
        });

        Button notepadButton = findViewById(R.id.notepadButton);

        notepadButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotepadActivity.class);
            startActivity(intent);
        });

        Button sessionNotesButton = findViewById(R.id.sessionNotesButton);

        sessionNotesButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SessionNotesActivity.class);
            startActivity(intent);
        });
    }

}

