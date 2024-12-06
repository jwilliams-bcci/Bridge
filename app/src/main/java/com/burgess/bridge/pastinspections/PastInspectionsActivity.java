package com.burgess.bridge.pastinspections;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import data.Tables.Inspection_Table;
import data.Tables.PastInspection_Table;

public class PastInspectionsActivity extends AppCompatActivity {
    private static final String TAG = "INSPECTION_HISTORY";
    private PastInspectionsViewModel mPastInspectionsViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int LOCATION_ID_NOT_SET = -1;
    public static final int INSPECTION_ID_NOT_SET = -1;
    private int mLocationId;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private LiveData<List<PastInspection_Table>> mPastInspections;

    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mTextEmptyLabel;
    private TextView mTextAddress;
    private RecyclerView mRecyclerPastInspections;
    private PastInspectionListAdapter mPastInspectionsListAdapter;
    private static BridgeAPIQueue apiQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_inspections);
        setSupportActionBar((Toolbar) findViewById(R.id.past_inspections_toolbar));
        mPastInspectionsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PastInspectionsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger and API Queue...
        BridgeLogger.getInstance(this);
        apiQueue = BridgeAPIQueue.getInstance(this);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mInspection = mPastInspectionsViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.past_inspections_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mTextEmptyLabel = findViewById(R.id.past_inspections_text_empty_label);
        mTextAddress = findViewById(R.id.past_inspections_text_inspection_address);
        mRecyclerPastInspections = findViewById(R.id.past_inspections_list);
    }

    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        mTextAddress.setText("");
        mTextAddress.append(mInspection.Community + "\n");
        mTextAddress.append(mInspection.Address + "\n");
        mTextAddress.append(mInspection.InspectionType + "\n");

        try {
            mPastInspections = mPastInspectionsViewModel.getPastInspections(mInspection.LocationID);
            mPastInspectionsListAdapter = new PastInspectionListAdapter(new PastInspectionListAdapter.PastInspectionsDiff());
            mRecyclerPastInspections.setAdapter(mPastInspectionsListAdapter);
            mRecyclerPastInspections.setLayoutManager(new LinearLayoutManager(this));
            mPastInspectionsViewModel.getPastInspections(mInspection.LocationID).observe(this, pastInspections -> {
                mPastInspectionsListAdapter.setCurrentList(pastInspections);
                if (pastInspections.isEmpty()) {
                    mTextEmptyLabel.setVisibility(View.VISIBLE);
                    mRecyclerPastInspections.setVisibility(View.GONE);
                } else {
                    mTextEmptyLabel.setVisibility(View.GONE);
                    mRecyclerPastInspections.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Snackbar.make(mConstraintLayout, "Error in loading past inspections, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
    }
}