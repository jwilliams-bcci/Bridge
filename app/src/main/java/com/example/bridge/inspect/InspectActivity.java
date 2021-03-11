package com.example.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.DefectItemRecyclerAdapter;
import com.example.bridge.R;
import com.example.bridge.ReviewAndSubmitActivity;

import java.util.List;

import data.DataManager;
import data.DefectCategory;
import data.Inspection;
import data.Location;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private Spinner mSpinnerDefectCategories;
    private InspectViewModel mInspectViewModel;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        setSupportActionBar((Toolbar) findViewById(R.id.inspect_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspect_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);

        mInspectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);

        //displayAddress(textAddress);
        //fillSpinner(mSpinnerDefectCategories);
        displayDefectItems();

        Button buttonReviewAndSubmit = findViewById(R.id.inspect_button_review_and_submit);
        buttonReviewAndSubmit.setOnClickListener(v -> {
            Intent reviewAndSubmitIntent = new Intent(InspectActivity.this, ReviewAndSubmitActivity.class);
            reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, mInspectionId);
            reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.LOCATION_ID, mLocationId);
            startActivity(reviewAndSubmitIntent);
        });
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
        RecyclerView recyclerDefectItems = findViewById(R.id.inspect_list_defect_items);
        final InspectListAdapter adapter = new InspectListAdapter(new InspectListAdapter.InspectDiff());
        recyclerDefectItems.setAdapter(adapter);
        recyclerDefectItems.setLayoutManager(new LinearLayoutManager(this));

        mInspectViewModel.getAllDefectItems().observe(this, defectItems ->
                adapter.submitList(defectItems));
    }
}