package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import data.DataManager;
import data.Inspection;
import data.Inspector;
import data.Location;

public class AssignTraineeActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private Spinner mSpinnerInspectors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_trainee);
        setSupportActionBar(findViewById(R.id.assign_trainee_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.assign_trainee_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerInspectors = findViewById(R.id.assign_trainee_spinner_inspectors);

        displayAddress(textAddress);
        fillSpinner(mSpinnerInspectors);
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void fillSpinner(Spinner spinnerInspectors) {
        List<Inspector> inspectors = DataManager.getInstance().getInspectors();
        ArrayAdapter<Inspector> adapterInspectors = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, inspectors);
        adapterInspectors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerInspectors.setAdapter(adapterInspectors);
    }
}