package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

public class RouteSheetActivity extends AppCompatActivity {
    private InspectionViewModel mInspectionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar((Toolbar) findViewById(R.id.route_sheet_toolbar));

        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        RecyclerView recyclerInspections = findViewById(R.id.route_sheet_list_inspections);
        final InspectionListAdapter adapter = new InspectionListAdapter(new InspectionListAdapter.InspectionDiff());
        recyclerInspections.setAdapter(adapter);
        recyclerInspections.setLayoutManager(new LinearLayoutManager(this));

        mInspectionViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectionViewModel.class);
        mInspectionViewModel.getAllInspectionsForRouteSheet().observe(this, inspections -> {
            adapter.submitList(inspections);
        });
    }
}