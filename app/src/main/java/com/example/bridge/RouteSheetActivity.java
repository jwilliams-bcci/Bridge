package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.List;

import data.DataManager;
import data.Inspection;
import data.Room;

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
        recyclerInspections.setLayoutManager(new LinearLayoutManager(this));

        List<Inspection> inspections = DataManager.getInstance().getInspections();
        List<Room> rooms = DataManager.getInstance().getRooms();
        recyclerInspections.setAdapter(new InspectionRecyclerAdapter(this, inspections));
    }
}