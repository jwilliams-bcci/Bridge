package com.burgess.bridge.ekotrope_infiltration;

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
import data.Tables.Ekotrope_Infiltration_Table;

public class Ekotrope_InfiltrationActivity extends AppCompatActivity {
    private Ekotrope_InfiltrationViewModel mEkotropeInfiltrationViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Spinner mSpinnerUnit;
    private EditText mTextValue;
    private Spinner mSpinnerMeasurementType;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_Infiltration_Table mInfiltration;
    private List<String> mUnits;
    private List<String> mMeasurementTypes;
    private boolean addNew = false;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_infiltration);
        setSupportActionBar(findViewById(R.id.infiltration_toolbar));
        mEkotropeInfiltrationViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_InfiltrationViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = getIntent().getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = getIntent().getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = getIntent().getStringExtra(EKOTROPE_PLAN_ID);
        mInfiltration = mEkotropeInfiltrationViewModel.getInfiltration(mPlanId);
        if (mInfiltration == null) {
            mInfiltration = new Ekotrope_Infiltration_Table(mPlanId, "CFM_50", 0.0, "Untested", false);
            addNew = true;
        }

        // Set spinner lists...
        mUnits = new ArrayList<>();
        mUnits.add("CFM_50");
        mUnits.add("ACH_50");
        mUnits.add("ACH_4");
        mUnits.add("ACH_NATURAL");
        mUnits.add("SPECIFIC_LEAKAGE_AREA");
        mUnits.add("EFFECTIVE_LEAKAGE_AREA");
        mUnits.add("ELA_PER_100_SF_SHELL");
        mUnits.add("CFM50_PER_SF_SHELL");

        mMeasurementTypes = new ArrayList<>();
        mMeasurementTypes.add("Untested");
        mMeasurementTypes.add("Tested");
        mMeasurementTypes.add("ToBeTested");
        mMeasurementTypes.add("ThresholdOrSampled");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.infiltration_constraint_layout);
        mSpinnerUnit = findViewById(R.id.infiltration_spinner_unit);
        mTextValue = findViewById(R.id.infiltration_text_value);
        mSpinnerMeasurementType = findViewById(R.id.infiltration_spinner_measurement_type);
        mButtonSave = findViewById(R.id.infiltration_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(view -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            String newUnit = mSpinnerUnit.getSelectedItem().toString();
            Double newValue = Double.parseDouble(mTextValue.getText().toString());
            String newMeasurementType = mSpinnerMeasurementType.getSelectedItem().toString();

            mSpinnerUnit.clearFocus();
            mTextValue.clearFocus();
            mSpinnerMeasurementType.clearFocus();

            if ((boolean)mSpinnerUnit.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A", "Unit",
                        mInfiltration.infiltration_unit, newUnit);
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }
            if ((boolean)mTextValue.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A", "Value",
                        Double.toString(mInfiltration.infiltration_value), Double.toString(newValue));
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }
            if ((boolean)mSpinnerMeasurementType.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A", "Measurement Type",
                        mInfiltration.measurement_type, newMeasurementType);
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }

            Ekotrope_Infiltration_Table infiltration = new Ekotrope_Infiltration_Table(mPlanId,
                    newUnit, newValue, newMeasurementType, true);
            if (addNew) {
                mEkotropeInfiltrationViewModel.insert(infiltration);
            } else {
                mEkotropeInfiltrationViewModel.update(infiltration);
            }
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextValue.setText(Double.toString(mInfiltration.infiltration_value));

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mUnits);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerUnit.setAdapter(unitAdapter);
        mSpinnerUnit.setSelection(mUnits.indexOf(mInfiltration.infiltration_unit));

        ArrayAdapter<String> measurementTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mMeasurementTypes);
        measurementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMeasurementType.setAdapter(measurementTypeAdapter);
        mSpinnerMeasurementType.setSelection(mMeasurementTypes.indexOf(mInfiltration.measurement_type));
    }

    private void initializeTextValidators() {
        mTextValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextValue.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextValue.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerUnit, mInfiltration.infiltration_unit);
        BridgeHelper.setChangeTracker(mTextValue, Double.toString(mInfiltration.infiltration_value));
        BridgeHelper.setChangeTracker(mSpinnerMeasurementType, mInfiltration.measurement_type);
    }
}