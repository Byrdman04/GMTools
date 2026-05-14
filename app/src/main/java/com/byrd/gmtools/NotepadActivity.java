package com.byrd.gmtools;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.byrd.gmtools.databinding.ActivityNotepadBinding;
import com.google.android.material.snackbar.Snackbar;

public class NotepadActivity extends AppCompatActivity {

    private ActivityNotepadBinding binding;
    private int currentFontSize = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotepadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

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

        // 3. FAB Save Feedback
        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Notes Cached", Snackbar.LENGTH_SHORT).show()
        );
    }
}