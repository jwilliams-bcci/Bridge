package com.burgess.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;

import java.util.List;
import java.util.Objects;

import data.Tables.DefectItem_Table;
import data.Tables.Inspection_Table;

import static com.burgess.bridge.Constants.PREF;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String INSPECTION_TYPE_ID = "com.example.bridge.INSPECTION_TYPE_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int INSPECTION_TYPE_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    public static final String TAG = "INSPECT";

    public int mInspectionId;
    private int mLocationId;
    public int mInspectionTypeId;
    public boolean mReinspection;
    private Spinner mSpinnerDefectCategories;
    private InspectViewModel mInspectViewModel;
    private SharedPreferences mSharedPreferences;
    private LiveData<Inspection_Table> mInspection;
    private Button mButtonSaveAndExit;
    private Button mButtonSortItemNumber;
    private Button mButtonSortDescription;
    private Button mButtonAddNote;
    private RecyclerView mRecyclerDefectItems;
    private InspectListAdapter mAdapter;
    private AddNoteFragment mAddNoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspect_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspectionTypeId = intent.getIntExtra(INSPECTION_TYPE_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);
        mInspectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);
        mInspection = mInspectViewModel.getInspection(mInspectionId);
        mReinspection = mInspectViewModel.getReinspect(mInspectionId);
        mButtonSaveAndExit = findViewById(R.id.inspect_button_save_and_exit);
        mButtonSortItemNumber = findViewById(R.id.inspect_button_sort_by_defect_number);
        mButtonSortDescription = findViewById(R.id.inspect_button_sort_by_description);
        mButtonAddNote = findViewById(R.id.inspect_button_add_note);
        mRecyclerDefectItems = findViewById(R.id.inspect_list_defect_items);

        displayAddress(textAddress);
        fillSpinner(mSpinnerDefectCategories);

        mAdapter = new InspectListAdapter(new InspectListAdapter.InspectDiff());
        mAdapter.setInspectionId(mInspectionId);
        mAdapter.setInspectionTypeId(mInspectionTypeId);
        mRecyclerDefectItems.setAdapter(mAdapter);
        mRecyclerDefectItems.setLayoutManager(new LinearLayoutManager(this));

        displayDefectItems("ALL");

        Button buttonReviewAndSubmit = findViewById(R.id.inspect_button_review_and_submit);
        buttonReviewAndSubmit.setOnClickListener(v -> {
            Intent reviewAndSubmitIntent = new Intent(InspectActivity.this, ReviewAndSubmitActivity.class);
            reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, mInspectionId);
            startActivity(reviewAndSubmitIntent);
        });

        mButtonSaveAndExit.setOnClickListener(v -> {
            Intent routeSheetIntent = new Intent(InspectActivity.this, RouteSheetActivity.class);
            startActivity(routeSheetIntent);
        });

        mButtonSortItemNumber.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredNumberSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mReinspection, mInspectionId).observe(this, defectItems ->
                    mAdapter.submitList(defectItems));
        });

        mButtonSortDescription.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mReinspection, mInspectionId).observe(this, defectItems ->
                    mAdapter.submitList(defectItems));
        });

        mButtonAddNote.setOnClickListener(v -> {
            mAddNoteFragment = AddNoteFragment.newInstance();
            mAddNoteFragment.show(getSupportFragmentManager(), "TAG");
        });
    }

    private void displayAddress(TextView textAddress) {
        textAddress.setText("");
        mInspection.observe(this, inspection -> {
            textAddress.append(inspection.community + "\n");
            textAddress.append(inspection.address + "\n");
            textAddress.append(inspection.inspection_type);
        });
    }

    private void fillSpinner(Spinner spinnerDefectCategories) {
        mInspectViewModel.getDefectCategories(mInspectionTypeId).observe(this, defectCategories -> {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, defectCategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDefectCategories.setAdapter(adapter);
        });

        spinnerDefectCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayDefectItems(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayDefectItems(String filter) {
        mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(filter, mInspectionTypeId, mReinspection, mInspectionId).observe(this, defectItems ->
                mAdapter.submitList(defectItems));
    }
}