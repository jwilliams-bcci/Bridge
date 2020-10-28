package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class RouteSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar((Toolbar) findViewById(R.id.route_sheet_toolbar));

        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        final RecyclerView recyclerInspections = (RecyclerView) findViewById(R.id.route_sheet_list_inspections);
        final LinearLayoutManager inspectionsLayoutManager = new LinearLayoutManager(this);
        recyclerInspections.setLayoutManager(inspectionsLayoutManager);

        List<Inspection> inspections = DataManager.getInstance().getInspections();
        final InspectionRecyclerAdapter inspectionRecyclerAdapter = new InspectionRecyclerAdapter(this, inspections);
        recyclerInspections.setAdapter(inspectionRecyclerAdapter);
    }
}