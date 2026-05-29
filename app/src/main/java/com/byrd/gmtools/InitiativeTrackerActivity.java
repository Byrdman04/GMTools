package com.byrd.gmtools;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author nicholsonja
 */
public class InitiativeTrackerActivity extends AppCompatActivity {

    private EditText etNameInput;
    private EditText etRollInput;
    private Button btnAdd;
    private Button btnNext;
    private Button btnClear;
    private Button btnPrevious;
    private RecyclerView rvInitiativeList;

    private InitiativeAdapter adapter;
    private List<InitiativeEntity> initiativeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initative_tracker);

        // Initialize UI Elements
        etNameInput = findViewById(R.id.etNameInput);
        etRollInput = findViewById(R.id.etRollInput);
        btnAdd = findViewById(R.id.btnAdd);
        btnNext = findViewById(R.id.btnNext);
        btnClear = findViewById(R.id.btnClear);
        btnPrevious = findViewById(R.id.btnPrevious);
        rvInitiativeList = findViewById(R.id.rvInitiativeList);

        // Initialize Data Structures
        initiativeList = new ArrayList<>();
        adapter = new InitiativeAdapter(initiativeList);

        // Setup RecyclerView
        rvInitiativeList.setLayoutManager(new LinearLayoutManager(this));
        rvInitiativeList.setAdapter(adapter);

        // Add Button Logic
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etNameInput.getText().toString().trim();
                String rollStr = etRollInput.getText().toString().trim();

                if (name.isEmpty() || rollStr.isEmpty()) {
                    Toast.makeText(InitiativeTrackerActivity.this, "Please enter both a name and a roll", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int roll = Integer.parseInt(rollStr);

                    // Add entity and clear fields
                    initiativeList.add(new InitiativeEntity(name, roll));
                    etNameInput.setText("");
                    etRollInput.setText("");

                    // Auto-sort list by highest roll whenever an object is added
                    sortInitiativeList();
                    adapter.notifyDataSetChanged();

                } catch (NumberFormatException e) {
                    Toast.makeText(InitiativeTrackerActivity.this, "Invalid roll number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Next Button Logic: Shift top entity (highest) to the bottom
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initiativeList.size() > 1) {
                    InitiativeEntity topEntity = initiativeList.remove(0);
                    initiativeList.add(topEntity);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // Previous Button Logic: Shift bottom entity to the top
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initiativeList.size() > 1) {
                    InitiativeEntity bottomEntity = initiativeList.remove(initiativeList.size() - 1);
                    initiativeList.add(0, bottomEntity);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // Clear Button Logic: Wipe out the tracking group
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiativeList.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Sorts initiative items descending from highest roll value to lowest.
     */
    private void sortInitiativeList() {
        Collections.sort(initiativeList, new Comparator<InitiativeEntity>() {
            @Override
            public int compare(InitiativeEntity o1, InitiativeEntity o2) {
                return Integer.compare(o2.getRoll(), o1.getRoll());
            }
        });
    }

    // --- Internal Domain Objects & Adapters ---

    /**
     * Entity data model representing a combatant in the queue.
     */
    private static class InitiativeEntity {
        private final String name;
        private final int roll;

        public InitiativeEntity(String name, int roll) {
            this.name = name;
            this.roll = roll;
        }

        public String getName() { return name; }
        public int getRoll() { return roll; }
    }

    /**
     * Custom lightweight view holder adapter tailored for text specifications.
     */
    private static class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder> {
        private final List<InitiativeEntity> list;

        public InitiativeAdapter(List<InitiativeEntity> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inline creation of text item views styled with font matches
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            textView.setTextSize(34);
            textView.setTextColor(android.graphics.Color.BLACK);
            textView.setTypeface(android.graphics.Typeface.create("casual", android.graphics.Typeface.BOLD));

            // Enforce explicit padding/indentation structure matches within inner boundary
            int paddingPx = (int) (16 * parent.getContext().getResources().getDisplayMetrics().density);
            textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);

            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            InitiativeEntity item = list.get(position);
            holder.textView.setText(item.getName() + ": " + item.getRoll());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.textView = (TextView) itemView;
            }
        }
    }
}