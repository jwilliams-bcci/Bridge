package com.burgess.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import data.DataManager;
import data.InspectionHistory;
import data.Location;

public class InspectionHistoryActivity extends AppCompatActivity {
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int LOCATION_ID_NOT_SET = -1;
    private int mLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_history);
        setSupportActionBar((Toolbar) findViewById(R.id.inspection_history_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspection_history_text_inspection_address);

        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_SET);

        displayAddress(textAddress);
        displayInspectionHistory();
    }

    private void displayAddress(TextView textAddress) {
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress());
    }

    private void displayInspectionHistory() {
        final RecyclerView recyclerInspectionHistories = (RecyclerView) findViewById(R.id.inspection_history_list_inspection_histories);
        final LinearLayoutManager inspectionHistoriesLayoutManager = new LinearLayoutManager(this);
        recyclerInspectionHistories.setLayoutManager(inspectionHistoriesLayoutManager);

        List<InspectionHistory> inspectionHistories = DataManager.getInstance().getInspectionHistories(mLocationId);
        final InspectionHistoryRecyclerAdapter inspectionHistoryRecyclerAdapter = new InspectionHistoryRecyclerAdapter(this, inspectionHistories);
        recyclerInspectionHistories.setAdapter(inspectionHistoryRecyclerAdapter);
    }
}