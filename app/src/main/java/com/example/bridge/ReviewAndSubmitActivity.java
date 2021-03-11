package com.example.bridge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bridge.routesheet.RouteSheetActivity;

import java.util.List;

import data.DataManager;
import data.Inspection;
import data.InspectionDefect;
import data.InspectionResolution;
import data.Location;

public class ReviewAndSubmitActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private boolean mSupervisorPresent;
    private boolean mStatusCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar((Toolbar) findViewById(R.id.review_and_submit_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.review_and_submit_text_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSupervisorPresent = false;
        mStatusCorrect = false;

        displayAddress(textAddress);

        RecyclerView recyclerInspectionDefects = (RecyclerView) findViewById(R.id.review_and_submit_recycler_inspection_defects);
        recyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));
        List<InspectionDefect> inspectionDefects = DataManager.getInstance().getInspectionDefects();
        recyclerInspectionDefects.setAdapter(new ReviewAndSubmitRecyclerAdapter(this, inspectionDefects));

        Button buttonSubmit = findViewById(R.id.review_and_submit_button_submit);
        buttonSubmit.setOnClickListener(v -> {
            Log.d("SUBMIT", "Submit clicked, show dialog");
            new AlertDialog.Builder(this)
                    .setTitle("Is the following resolution accurate?")
                    .setMessage("FAILED")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mStatusCorrect = true;
                            DataManager.getInstance().getInspection(1).setIsComplete(true);
                            returnToRouteSheet();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showEditResolutionDialog();
                        }
                    })
                    .show();

            new AlertDialog.Builder(this)
                    .setTitle("Supervisor Present?")
                    .setMessage("Was the supervisor present?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSupervisorPresent = true;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void showEditResolutionDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_resolution, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Change Resolution");
        builder.create().show();

        Spinner spinnerResolutions = (Spinner) view.findViewById(R.id.dialog_edit_resolution_spinner_resolutions);
        List<InspectionResolution> inspectionResolutions = DataManager.getInstance().getInspectionResolutions();
        ArrayAdapter<InspectionResolution> adapterInspectionResolutions = new ArrayAdapter<>(this, R.layout.item_spinner_item, inspectionResolutions);
        spinnerResolutions.setAdapter(adapterInspectionResolutions);

        Button buttonSaveResolution = (Button) view.findViewById(R.id.dialog_edit_resolution_button_save);
        buttonSaveResolution.setOnClickListener(v -> {
            returnToRouteSheet();
        });
    }

    private void returnToRouteSheet() {
        Intent routeSheetIntent = new Intent(ReviewAndSubmitActivity.this, RouteSheetActivity.class);
        startActivity(routeSheetIntent);
    }
}