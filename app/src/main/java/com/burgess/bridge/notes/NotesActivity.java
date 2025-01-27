package com.burgess.bridge.notes;

import static com.burgess.bridge.Constants.INSPECTION_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import data.Tables.InspectionHistory_Table;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "NOTES";

    private NotesViewModel notesVM;
    private ConstraintLayout constraintLayout;
    private RecyclerView rNotes;
    private Toolbar toolbar;

    private int inspectionId;
    private NotesListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setSupportActionBar(findViewById(R.id.notes_toolbar));
        notesVM = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(NotesViewModel.class);

        // Prepare Logger
        BridgeLogger.getInstance(this);

        Intent intent = getIntent();
        inspectionId = intent.getIntExtra(INSPECTION_ID, 0);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.notes_constraint_layout);
        rNotes = findViewById(R.id.notes_recycler);
        toolbar = findViewById(R.id.notes_toolbar);
    }

    private void initializeDisplayContent() {
        try {
            listAdapter = new NotesListAdapter(new NotesListAdapter.NoteDiff());
            rNotes.setAdapter(listAdapter);
            rNotes.setLayoutManager(new LinearLayoutManager(this));
            List<InspectionHistory_Table> notes = notesVM.getNotes(inspectionId);
            listAdapter.submitList(notesVM.getNotes(inspectionId));
        } catch (Exception e) {
            Snackbar.make(constraintLayout, "Error loading notes, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
    }
}