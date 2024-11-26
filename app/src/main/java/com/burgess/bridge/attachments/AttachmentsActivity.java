package com.burgess.bridge.attachments;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import data.Tables.Attachment_Table;
import data.Tables.Inspection_Table;

public class AttachmentsActivity extends AppCompatActivity {
    private static final String TAG = "ATTACHMENTS";
    private AttachmentsViewModel mAttachmentsViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_SET = -2;
    public static final int LOCATION_ID_NOT_SET = -2;
    private int mInspectionId;
    private int mLocationId;
    private Inspection_Table mInspection;
    private LiveData<List<Attachment_Table>> mAttachments;
    private AttachmentsListAdapter mAttachmentsListAdapter;

    // Activity Results
    private ActivityResultLauncher<Intent> chooseFileLauncher;

    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mTextRefreshLabel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerAttachments;
    private static BridgeAPIQueue apiQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments);
        setSupportActionBar(findViewById(R.id.attachments_toolbar));
        mAttachmentsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AttachmentsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger and APIQueue
        BridgeLogger.getInstance(this);
        apiQueue = BridgeAPIQueue.getInstance(this);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_SET);
        mInspection = mAttachmentsViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.attachments_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mTextRefreshLabel = findViewById(R.id.attachments_text_refresh_label);
        mSwipeRefreshLayout = findViewById(R.id.attachments_swipe_refresh);
        mRecyclerAttachments = findViewById(R.id.attachments_list);
    }

    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        mSwipeRefreshLayout.setOnRefreshListener(this::updateAttachments);
        try {
            mAttachments = mAttachmentsViewModel.getAttachments(mInspectionId, mLocationId);
            mAttachmentsListAdapter = new AttachmentsListAdapter(new AttachmentsListAdapter.AttachmentsDiff());
            mRecyclerAttachments.setAdapter(mAttachmentsListAdapter);
            mRecyclerAttachments.setLayoutManager(new LinearLayoutManager(this));
            mAttachmentsViewModel.getAttachments(mInspectionId, mLocationId).observe(this, attachments -> {
                mAttachmentsListAdapter.setCurrentList(attachments);
                if (attachments.isEmpty()) {
                    mRecyclerAttachments.setVisibility(View.GONE);
                    mTextRefreshLabel.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerAttachments.setVisibility(View.VISIBLE);
                    mTextRefreshLabel.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Snackbar.make(mConstraintLayout, "Error in loading attachments, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
    }

    private void updateAttachments() {
        apiQueue.getRequestQueue().add(apiQueue.updateAttachments(mAttachmentsViewModel, mInspectionId, mLocationId));
        mSwipeRefreshLayout.setRefreshing(false);
    }
}