package com.byrd.gmtools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.byrd.gmtools.databinding.ActivitySessionNotesBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SessionNotesActivity extends AppCompatActivity {

    private ActivitySessionNotesBinding binding;

    private final ArrayList<String> sessionNames = new ArrayList<>();
    private final ArrayList<String> sessionUris = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    private ActivityResultLauncher<String[]> filePickerLauncher;

    private static final String PREFS_NAME = "SessionNotesPrefs";
    private static final String NAMES_KEY = "session_names";
    private static final String URIS_KEY = "session_uris";

    private boolean viewingFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivitySessionNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));



        // -----------------------------
        // Adapter
        // -----------------------------
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                sessionNames
        );

        binding.contentMain.sessionListView.setAdapter(adapter);

        // -----------------------------
        // File Picker (FIXED)
        // -----------------------------
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {

                    if (uri != null) {

                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        String fileName = getFileName(uri);

                        if (!sessionUris.contains(uri.toString())) {
                            sessionNames.add(fileName);
                            sessionUris.add(uri.toString());

                            adapter.notifyDataSetChanged();
                            saveSessions();
                        }
                    }
                }
        );

        // -----------------------------
        // Load saved data
        // -----------------------------
        loadSessions();

        // -----------------------------
        // List click
        // -----------------------------
        binding.contentMain.sessionListView.setOnItemClickListener((parent, view, position, id) -> {
            openFile(Uri.parse(sessionUris.get(position)));
        });

        // -----------------------------
        // Toolbar (ONLY system used)
        // -----------------------------
        binding.toolbar.inflateMenu(R.menu.session_notes_menu);

        binding.toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_upload) {

                if (!viewingFile) {
                    filePickerLauncher.launch(new String[]{"text/plain"});
                } else {
                    showListView();
                }

                return true;
            }

            return false;
        });

        for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
            MenuItem item = binding.toolbar.getMenu().getItem(i);
            item.getIcon().setTint(
                    getResources().getColor(android.R.color.white, getTheme())
            );
        }
    }

    // =====================================================
    // Open File
    // =====================================================
    private void openFile(Uri uri) {

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            reader.close();

            binding.contentMain.fileContentText.setText(builder.toString());
            showFileView();

        } catch (Exception e) {
            binding.contentMain.fileContentText.setText("Failed to open file.");
            showFileView();
        }
    }

    // =====================================================
    // Show File View
    // =====================================================
    private void showFileView() {

        viewingFile = true;

        binding.contentMain.sessionListContainer.setVisibility(View.GONE);
        binding.contentMain.fileViewerContainer.setVisibility(View.VISIBLE);
    }

    // =====================================================
    // Show List View
    // =====================================================
    private void showListView() {

        viewingFile = false;

        binding.contentMain.fileViewerContainer.setVisibility(View.GONE);
        binding.contentMain.sessionListContainer.setVisibility(View.VISIBLE);
    }

    // =====================================================
    // Save
    // =====================================================
    private void saveSessions() {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(NAMES_KEY, String.join(";;", sessionNames));
        editor.putString(URIS_KEY, String.join(";;", sessionUris));

        editor.apply();
    }

    // =====================================================
    // Load
    // =====================================================
    private void loadSessions() {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String names = prefs.getString(NAMES_KEY, "");
        String uris = prefs.getString(URIS_KEY, "");

        if (!names.isEmpty()) {
            for (String s : names.split(";;")) {
                sessionNames.add(s);
            }
        }

        if (!uris.isEmpty()) {
            for (String s : uris.split(";;")) {
                sessionUris.add(s);
            }
        }
    }

    // =====================================================
    // File name helper
    // =====================================================
    private String getFileName(Uri uri) {

        String path = uri.getLastPathSegment();

        if (path == null) return "Unknown File";

        int cut = path.lastIndexOf('/');

        return (cut != -1) ? path.substring(cut + 1) : path;
    }
}