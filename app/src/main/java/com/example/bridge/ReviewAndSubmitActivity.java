package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import data.DataManager;
import data.Inspection;
import data.InspectionDefect;
import data.Location;

public class ReviewAndSubmitActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar((Toolbar) findViewById(R.id.review_and_submit_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.review_and_submit_text_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);

        displayAddress(textAddress);

        RecyclerView recyclerInspectionDefects = (RecyclerView) findViewById(R.id.review_and_submit_recycler_inspection_defects);
        recyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));

        List<InspectionDefect> inspectionDefects = DataManager.getInstance().getInspectionDefects();
        recyclerInspectionDefects.setAdapter(new ReviewAndSubmitRecyclerAdapter(this, inspectionDefects));
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }
}