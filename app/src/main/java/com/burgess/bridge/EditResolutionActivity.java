package com.burgess.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import data.DataManager;
import data.Inspection;
import data.InspectionResolution;
import data.Location;

public class EditResolutionActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private Spinner mSpinnerResolutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resolution);
        setSupportActionBar((Toolbar) findViewById(R.id.edit_resolution_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.edit_resolution_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerResolutions = findViewById(R.id.edit_resolution_spinner_resolutions);

        displayAddress(textAddress);
        fillSpinner(mSpinnerResolutions);
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void fillSpinner(Spinner spinnerResolutions) {
        List<InspectionResolution> inspectionResolutions = DataManager.getInstance().getInspectionResolutions();
        ArrayAdapter<InspectionResolution> adapterInspectionResolutions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, inspectionResolutions);
        adapterInspectionResolutions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerResolutions.setAdapter(adapterInspectionResolutions);
    }
}