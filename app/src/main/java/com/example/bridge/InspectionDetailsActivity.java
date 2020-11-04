package com.example.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import data.Builder;
import data.DataManager;
import data.Inspection;
import data.Location;

public class InspectionDetailsActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String BUILDER_ID = "com.example.bridge.BUILDER_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    public static final int BUILDER_ID_NOT_SET = -1;
    public static final int LOCATION_ID_NOT_SET = -1;
    private int mInspectionId;
    private int mBuilderId;
    private int mLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        setSupportActionBar((Toolbar) findViewById(R.id.inspection_details_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspection_details_text_inspection_address);
        TextView textBuilder = findViewById(R.id.inspection_details_text_builder);
        TextView textSuperintendent = findViewById(R.id.inspection_details_text_superintendent);
        TextView textNotes = findViewById(R.id.inspection_details_text_notes);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mBuilderId = intent.getIntExtra(BUILDER_ID, BUILDER_ID_NOT_SET);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_SET);

        Button inspectButton = findViewById(R.id.inspection_details_button_inspect);
        inspectButton.setOnClickListener(view -> {
            Intent inspectIntent = new Intent(InspectionDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
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
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(inspection.getLocationId());
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void displayInspectionSummary(TextView textBuilder, TextView textSuperintendent, TextView textNotes) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Builder builder = DataManager.getInstance().getBuilder(mBuilderId);
        textBuilder.setText(builder.getBuilderName());
        textSuperintendent.setText(inspection.getSuperintendent());
        textNotes.setText(inspection.getNotes());
    }
}