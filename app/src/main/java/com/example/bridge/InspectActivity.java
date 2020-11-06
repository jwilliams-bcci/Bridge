package com.example.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.DataManager;
import data.DefectCategory;
import data.DefectItem;
import data.InspectDefectListItem;
import data.Inspection;
import data.InspectionResolution;
import data.Location;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private Spinner mSpinnerDefectCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        setSupportActionBar((Toolbar) findViewById(R.id.inspect_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspect_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);

        displayAddress(textAddress);
        fillSpinner(mSpinnerDefectCategories);
        displayDefectItems();
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        Location location = DataManager.getInstance().getLocation(mLocationId);
        textAddress.append(location.getCommunity() + "\n");
        textAddress.append(location.getFullAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void fillSpinner(Spinner spinnerDefectCategories) {
        List<DefectCategory> defectCategories = DataManager.getInstance().getDefectCategories();
        ArrayAdapter<DefectCategory> adapterDefectCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defectCategories);
        adapterDefectCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDefectCategories.setAdapter(adapterDefectCategories);
    }

    private void displayDefectItems() {
        final RecyclerView recyclerDefectItems = (RecyclerView) findViewById(R.id.inspect_list_defect_items);
        final LinearLayoutManager defectItemsLayoutManager = new LinearLayoutManager(this);
        recyclerDefectItems.setLayoutManager(defectItemsLayoutManager);

        List defectItems = DataManager.getInstance().getInspectDefectList();
        final DefectItemRecyclerAdapter defectItemRecyclerAdapter = new DefectItemRecyclerAdapter(this, defectItems);
        recyclerDefectItems.setAdapter(defectItemRecyclerAdapter);
    }
}