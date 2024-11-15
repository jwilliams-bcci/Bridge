package com.burgess.bridge.ekotrope_rangeoven;

import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.R;
import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_RangeOven_Table;

public class Ekotrope_RangeOvenActivity extends AppCompatActivity {
    private Ekotrope_RangeOvenViewModel mRangeOvenViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Spinner mSpinnerFuelType;
    private CheckBox mCheckBoxInductionRange;
    private CheckBox mCheckBoxConvectionOven;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_RangeOven_Table mRangeOven;
    private List<String> mFuelTypes;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_range_oven);
        setSupportActionBar(findViewById(R.id.range_oven_toolbar));
        mRangeOvenViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_RangeOvenViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(EKOTROPE_PLAN_ID);
        mRangeOven = mRangeOvenViewModel.getRangeOven(mPlanId);

        // Set spinner lists...
        mFuelTypes = new ArrayList<>();
        mFuelTypes.add("NATURAL_GAS");
        mFuelTypes.add("ELECTRIC");
        mFuelTypes.add("PROPANE");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.range_oven_constraint_layout);
        mSpinnerFuelType = findViewById(R.id.range_oven_spinner_fuel_type);
        mCheckBoxInductionRange = findViewById(R.id.range_oven_checkbox_induction_range);
        mCheckBoxConvectionOven = findViewById(R.id.range_oven_checkbox_convection_oven);
        mButtonSave = findViewById(R.id.range_oven_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            String newFuelType = mSpinnerFuelType.getSelectedItem().toString();
            boolean newInductionRange = mCheckBoxInductionRange.isChecked();
            boolean newConvectionOven = mCheckBoxConvectionOven.isChecked();

            mSpinnerFuelType.clearFocus();
            mCheckBoxInductionRange.clearFocus();
            mCheckBoxConvectionOven.clearFocus();

            if ((boolean)mSpinnerFuelType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "RangeOven", "N/A", "Fuel Type",
                        mRangeOven.fuel_type, newFuelType);
                mRangeOvenViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mCheckBoxInductionRange.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "RangeOven", "N/A", "Induction Range",
                        Boolean.toString(mRangeOven.is_induction_range), Boolean.toString(newInductionRange));
                mRangeOvenViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mCheckBoxConvectionOven.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "RangeOven", "N/A", "Convection Oven",
                        Boolean.toString(mRangeOven.is_convection_oven), Boolean.toString(newConvectionOven));
                mRangeOvenViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_RangeOven_Table newRangeOven = new Ekotrope_RangeOven_Table(mPlanId,
                    newFuelType, newInductionRange, newConvectionOven, true);
            mRangeOvenViewModel.updateRangeOven(newRangeOven);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mCheckBoxInductionRange.setChecked(mRangeOven.is_induction_range);
        mCheckBoxConvectionOven.setChecked(mRangeOven.is_convection_oven);

        ArrayAdapter<String> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mFuelTypes);
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFuelType.setAdapter(fuelTypeAdapter);
        mSpinnerFuelType.setSelection(mFuelTypes.indexOf(mRangeOven.fuel_type));
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerFuelType, mRangeOven.fuel_type);
        BridgeHelper.setChangeTracker(mCheckBoxInductionRange, mRangeOven.is_induction_range);
        BridgeHelper.setChangeTracker(mCheckBoxConvectionOven, mRangeOven.is_convection_oven);
    }
}