package com.byrd.gmtools;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.byrd.gmtools.databinding.ActivityDiceRollerBinding;

import java.util.Random;

public class DiceRollerActivity extends AppCompatActivity {

    private ActivityDiceRollerBinding binding;

    private EditText etNumberOfDice;
    private EditText etModifier;
    private TextView tvResults;

    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDiceRollerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Inputs
        etNumberOfDice = findViewById(R.id.etNumberOfDice);
        etModifier = findViewById(R.id.etModifier);
        tvResults = findViewById(R.id.tvResults);

        // Dice Buttons
        Button btnD4 = findViewById(R.id.btnD4);
        Button btnD6 = findViewById(R.id.btnD6);
        Button btnD8 = findViewById(R.id.btnD8);
        Button btnD10 = findViewById(R.id.btnD10);
        Button btnD12 = findViewById(R.id.btnD12);
        Button btnD20 = findViewById(R.id.btnD20);

        // Click Listeners
        btnD4.setOnClickListener(v -> rollDice(4));
        btnD6.setOnClickListener(v -> rollDice(6));
        btnD8.setOnClickListener(v -> rollDice(8));
        btnD10.setOnClickListener(v -> rollDice(10));
        btnD12.setOnClickListener(v -> rollDice(12));
        btnD20.setOnClickListener(v -> rollDice(20));
    }

    private void rollDice(int sides) {

        int numberOfDice = 1;
        int modifier = 0;

        // Get number of dice
        String diceText = etNumberOfDice.getText().toString();

        if (!diceText.isEmpty()) {
            numberOfDice = Integer.parseInt(diceText);
        }

        // Get modifier
        String modifierText = etModifier.getText().toString();

        if (!modifierText.isEmpty()) {
            modifier = Integer.parseInt(modifierText);
        }

        int total = 0;

        StringBuilder rollsText = new StringBuilder();

        for (int i = 0; i < numberOfDice; i++) {

            int roll = random.nextInt(sides) + 1;

            total += roll;

            rollsText.append(roll);

            if (i < numberOfDice - 1) {
                rollsText.append(", ");
            }
        }

        total += modifier;

        String result =
                "Rolling " + numberOfDice + "d" + sides +
                        "\nRolls: " + rollsText +
                        "\nModifier: " + modifier +
                        "\n\nTotal: " + total;

        tvResults.setText(result);
    }
}