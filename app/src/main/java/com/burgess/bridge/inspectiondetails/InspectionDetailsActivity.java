package com.burgess.bridge.inspectiondetails;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.attachments.AttachmentsActivity;
import com.burgess.bridge.pastinspections.PastInspectionsActivity;
import com.burgess.bridge.R;
import com.burgess.bridge.assigntrainee.AssignTraineeActivity;
import com.burgess.bridge.editresolution.EditResolutionActivity;
import com.burgess.bridge.inspect.InspectActivity;
import com.burgess.bridge.multifamily.MultifamilyDetailsActivity;
import com.burgess.bridge.transferinspection.TransferInspectionActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.OffsetDateTime;

import data.Tables.Inspection_Table;

public class InspectionDetailsActivity extends AppCompatActivity {
    private InspectionDetailsViewModel mInspectionDetailsViewModel;
    private SharedPreferences mSharedPreferences;
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mTextAddress;
    private TextView mTextBuilder;
    private TextView mTextSuperintendent;
    private TextView mTextNotes;
    private Button mInspectButton;
    private Button mViewInspectionHistoryButton;
    private Button mTransferInspectionButton;
    private Button mAssignTraineeButton;
    private Button mEditResolutionButton;
    private Button mAttachmentsButton;

    private static final String TAG = "INSPECTION_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        setSupportActionBar(findViewById(R.id.inspection_details_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectionDetailsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectionDetailsViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mInspection = mInspectionDetailsViewModel.getInspectionSync(mInspectionId);
        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.inspection_details_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mTextAddress = findViewById(R.id.inspection_details_text_inspection_address);
        mTextBuilder = findViewById(R.id.inspection_details_text_builder);
        mTextSuperintendent = findViewById(R.id.inspection_details_text_superintendent);
        mTextNotes = findViewById(R.id.inspection_details_text_notes);
        mInspectButton = findViewById(R.id.inspection_details_button_inspect);
        mViewInspectionHistoryButton = findViewById(R.id.inspection_details_button_view_inspection_history);
        mTransferInspectionButton = findViewById(R.id.inspection_details_button_transfer_inspection);
        mAssignTraineeButton = findViewById(R.id.inspection_details_button_assign_trainee);
        mEditResolutionButton = findViewById(R.id.inspection_details_button_edit_resolution);
        mAttachmentsButton = findViewById(R.id.inspection_details_button_attachments);
    }
    private void initializeButtonListeners() {
        mInspectButton.setOnClickListener(view -> {
            OffsetDateTime startTime = OffsetDateTime.now();
            if (mInspection.start_time == null) {
                mInspectionDetailsViewModel.startInspection(startTime, mInspectionId);
            }

            Intent inspectIntent = new Intent(InspectionDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);

            Intent multifamilyInformationIntent = new Intent(InspectionDetailsActivity.this, MultifamilyDetailsActivity.class);
            multifamilyInformationIntent.putExtra(MultifamilyDetailsActivity.INSPECTION_ID, mInspectionId);

            BridgeLogger.getInstance().log('I', TAG, "Starting inspection " + mInspectionId + " at " + startTime.toString());

            if (mInspection.division_id == 20) {
                startActivity(multifamilyInformationIntent);
            } else {
                startActivity(inspectIntent);
            }
        });


        mViewInspectionHistoryButton.setOnClickListener(view -> {
            Intent viewInspectionHistoryIntent = new Intent(InspectionDetailsActivity.this, PastInspectionsActivity.class);
            viewInspectionHistoryIntent.putExtra(PastInspectionsActivity.INSPECTION_ID, mInspectionId);
            startActivity(viewInspectionHistoryIntent);
        });

        mTransferInspectionButton.setOnClickListener(view -> {
            Intent transferInspectionHistoryIntent = new Intent(InspectionDetailsActivity.this, TransferInspectionActivity.class);
            transferInspectionHistoryIntent.putExtra(TransferInspectionActivity.INSPECTION_ID, mInspectionId);
            startActivity(transferInspectionHistoryIntent);
        });

        mAssignTraineeButton.setOnClickListener(view -> {
            Intent assignTraineeIntent = new Intent(InspectionDetailsActivity.this, AssignTraineeActivity.class);
            assignTraineeIntent.putExtra(AssignTraineeActivity.INSPECTION_ID, mInspectionId);
            startActivity(assignTraineeIntent);
        });

        mEditResolutionButton.setOnClickListener(view -> {
            Intent editResolutionIntent = new Intent(InspectionDetailsActivity.this, EditResolutionActivity.class);
            editResolutionIntent.putExtra(EditResolutionActivity.INSPECTION_ID, mInspectionId);
            startActivity(editResolutionIntent);
        });

        mAttachmentsButton.setOnClickListener(view -> {
            Intent attachmentsIntent = new Intent(InspectionDetailsActivity.this, AttachmentsActivity.class);
            attachmentsIntent.putExtra(AttachmentsActivity.INSPECTION_ID, mInspectionId);
            attachmentsIntent.putExtra(AttachmentsActivity.LOCATION_ID, mInspection.location_id);
            startActivity(attachmentsIntent);
        });
    }
    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");

        mTextBuilder.setText(mInspection.builder_name);
        mTextSuperintendent.setText(mInspection.super_name + "\n" + mInspection.super_phone);
        mTextSuperintendent.setMovementMethod(LinkMovementMethod.getInstance());
        if (mInspection.notes.equals("null") || mInspection.notes.isEmpty()) {
            mTextNotes.setText("No notes");
        } else {
            mTextNotes.setText(mInspection.notes);
        }
    }
}