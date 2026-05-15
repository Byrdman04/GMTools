package com.byrd.gmtools;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.byrd.gmtools.databinding.ActivityNotepadBinding;
import com.google.android.material.snackbar.Snackbar;

public class NotepadActivity extends AppCompatActivity {

    private ActivityNotepadBinding binding;
    private int currentFontSize = 15;

    // SharedPreferences constants
    private static final String PREFS_NAME = "NotepadPrefs";
    private static final String NOTE_KEY = "saved_note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotepadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Load saved note
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = prefs.getString(NOTE_KEY, "");
        binding.contentMain.notepadEditText.setText(savedText);

        // --- Logic for the Notepad ---

        // 1. Change Font Size on click
        binding.contentMain.fontSizeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFontSize < 30) {
                    currentFontSize += 2;
                } else {
                    currentFontSize = 12;
                }

                binding.contentMain.notepadEditText.setTextSize(currentFontSize);
                binding.contentMain.fontSizeLabel.setText("Font size: " + currentFontSize);
            }
        });

        // 2. Change Font Style on click
        binding.contentMain.fontStyleLabel.setOnClickListener(new View.OnClickListener() {

            boolean isSerif = true;

            @Override
            public void onClick(View v) {

                if (isSerif) {
                    binding.contentMain.notepadEditText.setTypeface(Typeface.MONOSPACE);
                    binding.contentMain.fontStyleLabel.setText("Font: Monospace");
                } else {
                    binding.contentMain.notepadEditText.setTypeface(Typeface.SERIF);
                    binding.contentMain.fontStyleLabel.setText("Font: Serif");
                }

                isSerif = !isSerif;
            }
        });

        // 3. FAB Save Button
        binding.fab.setOnClickListener(view -> {

            String noteText = binding.contentMain.notepadEditText
                    .getText()
                    .toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(NOTE_KEY, noteText);
            editor.apply();

            Snackbar.make(view, "Notes Saved", Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Auto-save when leaving activity
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(
                NOTE_KEY,
                binding.contentMain.notepadEditText.getText().toString()
        );

        editor.apply();
    }
}