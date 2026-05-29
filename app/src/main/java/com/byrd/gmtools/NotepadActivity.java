package com.byrd.gmtools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.byrd.gmtools.databinding.ActivityNotepadBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NotepadActivity extends AppCompatActivity {

    private ActivityNotepadBinding binding;

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

        // SAVE TO TEXT FILE BUTTON
        binding.contentMain.saveFileButton.setOnClickListener(v -> {

            String noteText = binding.contentMain.notepadEditText
                    .getText()
                    .toString();

            try {

                File file = new File(
                        getExternalFilesDir(null),
                        "session_notes.txt"
                );

                FileWriter writer = new FileWriter(file);
                writer.write(noteText);
                writer.close();

                Toast.makeText(
                        this,
                        "Saved to: " + file.getAbsolutePath(),
                        Toast.LENGTH_LONG
                ).show();

                // Optional share intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        androidx.core.content.FileProvider.getUriForFile(
                                this,
                                getPackageName() + ".provider",
                                file
                        )
                );

                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(
                        shareIntent,
                        "Share Notes"
                ));

            } catch (IOException e) {
                e.printStackTrace();

                Toast.makeText(
                        this,
                        "Failed to save file",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // CLEAR BUTTON
        binding.contentMain.clearButton.setOnClickListener(v -> {
            binding.contentMain.notepadEditText.setText("");

            Toast.makeText(
                    this,
                    "Notes Cleared",
                    Toast.LENGTH_SHORT
            ).show();
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
                binding.contentMain.notepadEditText
                        .getText()
                        .toString()
        );

        editor.apply();
    }
}