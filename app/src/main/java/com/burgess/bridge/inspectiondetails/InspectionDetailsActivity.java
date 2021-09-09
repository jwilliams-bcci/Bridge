package com.burgess.bridge.inspectiondetails;

import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.AssignTraineeActivity;
import com.burgess.bridge.EditResolutionActivity;
import com.burgess.bridge.InspectionHistoryActivity;
import com.burgess.bridge.R;
import com.burgess.bridge.TransferInspectionActivity;
import com.burgess.bridge.inspect.InspectActivity;
import com.burgess.bridge.multifamily.MultifamilyDetailsActivity;

import java.time.OffsetDateTime;

import data.Tables.Inspection_Table;

public class InspectionDetailsActivity extends AppCompatActivity {
    private InspectionDetailsViewModel mInspectionDetailsViewModel;
    private SharedPreferences mSharedPreferences;
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private TextView mTextAddress;
    private TextView mTextBuilder;
    private TextView mTextSuperintendent;
    private TextView mTextNotes;
    private Button mInspectButton;
    private Button mViewInspectionHistoryButton;
    private Button mTransferInspectionButton;
    private Button mAssignTraineeButton;
    private Button mEditResolutionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        setSupportActionBar(findViewById(R.id.inspection_details_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectionDetailsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectionDetailsViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mInspection = mInspectionDetailsViewModel.getInspectionSync(mInspectionId);
        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mTextAddress = findViewById(R.id.inspection_details_text_inspection_address);
        mTextBuilder = findViewById(R.id.inspection_details_text_builder);
        mTextSuperintendent = findViewById(R.id.inspection_details_text_superintendent);
        mTextNotes = findViewById(R.id.inspection_details_text_notes);

        mInspectButton = findViewById(R.id.inspection_details_button_inspect);
        mViewInspectionHistoryButton = findViewById(R.id.inspection_details_button_view_inspection_history);
        mTransferInspectionButton = findViewById(R.id.inspection_details_button_transfer_inspection);
        mAssignTraineeButton = findViewById(R.id.inspection_details_button_assign_trainee);
        mEditResolutionButton = findViewById(R.id.inspection_details_button_edit_resolution);
    }
    private void initializeButtonListeners() {
        mInspectButton.setOnClickListener(view -> {
            OffsetDateTime startTime = OffsetDateTime.now();
            mInspectionDetailsViewModel.startInspection(startTime, mInspectionId);

            Intent inspectIntent = new Intent(InspectionDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);

            Intent multifamilyInformationIntent = new Intent(InspectionDetailsActivity.this, MultifamilyDetailsActivity.class);
            multifamilyInformationIntent.putExtra(MultifamilyDetailsActivity.INSPECTION_ID, mInspectionId);

            if (mInspection.division_id == 20) {
                startActivity(multifamilyInformationIntent);
            } else {
                startActivity(inspectIntent);
            }
        });


        mViewInspectionHistoryButton.setOnClickListener(view -> {
            Intent viewInspectionHistoryIntent = new Intent(InspectionDetailsActivity.this, InspectionHistoryActivity.class);
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
    }
    private void initializeDisplayContent() {
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");

        mTextBuilder.setText(mInspection.builder_name);
        mTextSuperintendent.setText(mInspection.super_name);
        mTextNotes.setText(mInspection.notes);
    }
}