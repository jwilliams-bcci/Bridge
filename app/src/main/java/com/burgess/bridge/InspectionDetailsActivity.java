package com.burgess.bridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.inspect.InspectActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import data.Tables.Inspection_Table;

public class InspectionDetailsActivity extends AppCompatActivity {
    private InspectionDetailsViewModel mInspectionDetailsViewModel;
    private SharedPreferences mSharedPreferences;
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String INSPECTION_TYPE_ID = "com.example.bridge.INSPECTION_TYPE_ID";
    public static final String BUILDER_ID = "com.example.bridge.BUILDER_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    public static final int INSPECTION_TYPE_ID_NOT_SET = -1;
    public static final int BUILDER_ID_NOT_SET = -1;
    public static final int LOCATION_ID_NOT_SET = -1;
    private int mInspectionId;
    private int mInspectionTypeId;
    private LiveData<Inspection_Table> mInspection;
    private int mBuilderId;
    private int mLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        setSupportActionBar(findViewById(R.id.inspection_details_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        mInspectionDetailsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectionDetailsViewModel.class);
        TextView textAddress = findViewById(R.id.inspection_details_text_inspection_address);
        TextView textBuilder = findViewById(R.id.inspection_details_text_builder);
        TextView textSuperintendent = findViewById(R.id.inspection_details_text_superintendent);
        TextView textNotes = findViewById(R.id.inspection_details_text_notes);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mInspectionTypeId = intent.getIntExtra(INSPECTION_TYPE_ID, INSPECTION_TYPE_ID_NOT_SET);
        mInspection = mInspectionDetailsViewModel.getInspection(mInspectionId);


        Button inspectButton = findViewById(R.id.inspection_details_button_inspect);
        inspectButton.setOnClickListener(view -> {
            Date startTime = Calendar.getInstance().getTime();
            mInspectionDetailsViewModel.startInspection(startTime, mInspectionId);

            Intent inspectIntent = new Intent(InspectionDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            inspectIntent.putExtra(InspectActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
            inspectIntent.putExtra(InspectActivity.LOCATION_ID, mLocationId);
            startActivity(inspectIntent);
        });

        Button viewInspectionHistoryButton = findViewById(R.id.inspection_details_button_view_inspection_history);
        viewInspectionHistoryButton.setOnClickListener(view -> {
            Intent viewInspectionHistoryIntent = new Intent(InspectionDetailsActivity.this, InspectionHistoryActivity.class);
            viewInspectionHistoryIntent.putExtra(InspectionHistoryActivity.LOCATION_ID, mLocationId);
            startActivity(viewInspectionHistoryIntent);
        });

        Button transferInspectionButton = findViewById(R.id.inspection_details_button_transfer_inspection);
        transferInspectionButton.setOnClickListener(view -> {
            Intent transferInspectionHistoryIntent = new Intent(InspectionDetailsActivity.this, TransferInspectionActivity.class);
            transferInspectionHistoryIntent.putExtra(TransferInspectionActivity.INSPECTION_ID, mInspectionId);
            transferInspectionHistoryIntent.putExtra(TransferInspectionActivity.LOCATION_ID, mLocationId);
            startActivity(transferInspectionHistoryIntent);
        });

        Button assignTraineeButton = findViewById(R.id.inspection_details_button_assign_trainee);
        assignTraineeButton.setOnClickListener(view -> {
            Intent assignTraineeIntent = new Intent(InspectionDetailsActivity.this, AssignTraineeActivity.class);
            assignTraineeIntent.putExtra(AssignTraineeActivity.INSPECTION_ID, mInspectionId);
            assignTraineeIntent.putExtra(AssignTraineeActivity.LOCATION_ID, mLocationId);
            startActivity(assignTraineeIntent);
        });

        Button editResolutionButton = findViewById(R.id.inspection_details_button_edit_resolution);
        editResolutionButton.setOnClickListener(view -> {
            Intent editResolutionIntent = new Intent(InspectionDetailsActivity.this, EditResolutionActivity.class);
            editResolutionIntent.putExtra(EditResolutionActivity.INSPECTION_ID, mInspectionId);
            editResolutionIntent.putExtra(EditResolutionActivity.LOCATION_ID, mLocationId);
            startActivity(editResolutionIntent);
        });

        displayAddress(textAddress);
        displayInspectionSummary(textBuilder, textSuperintendent, textNotes);
    }

    private void displayAddress(TextView textAddress) {
        mInspection.observe(this, inspection -> {
            textAddress.append(inspection.community + "\n");
            textAddress.append(inspection.address + "\n");
            textAddress.append(inspection.inspection_type);
        });
    }

    private void displayInspectionSummary(TextView textBuilder, TextView textSuperintendent, TextView textNotes) {
        mInspection.observe(this, insp -> {
            textBuilder.setText(insp.builder_name);
            textSuperintendent.setText(insp.super_name);
            textNotes.setText(insp.notes);
        });
    }
}