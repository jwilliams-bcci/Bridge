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
    private EditText mTextCfm50;
    private EditText mTextAch50;
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
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(EKOTROPE_PLAN_ID);
        mInfiltration = mEkotropeInfiltrationViewModel.getInfiltration(mPlanId);

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
        mTextCfm50 = findViewById(R.id.infiltration_text_cfm_50);
        mTextAch50 = findViewById(R.id.infiltration_text_ach_50);
        mSpinnerMeasurementType = findViewById(R.id.infiltration_spinner_measurement_type);
        mButtonSave = findViewById(R.id.infiltration_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(view -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors.", Snackbar.LENGTH_SHORT).show();
                return;
            }
            Double newCfm50 = Double.parseDouble(mTextCfm50.getText().toString());
            Double newAch50 = Double.parseDouble(mTextAch50.getText().toString());
            String newMeasurementType = mSpinnerMeasurementType.getSelectedItem().toString();

            mTextCfm50.clearFocus();
            mTextAch50.clearFocus();
            mSpinnerMeasurementType.clearFocus();

            if ((boolean)mTextCfm50.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A",
                        "CFM 50", mInfiltration.cfm_50.toString(),
                        newCfm50.toString());
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }
            if ((boolean) mTextAch50.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A",
                        "ACH 50", mInfiltration.ach_50.toString(), newAch50.toString());
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }
            if ((boolean)mSpinnerMeasurementType.getTag()) {
                Ekotrope_ChangeLog_Table changeLog = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Infiltration", "N/A", "Measurement Type",
                        mInfiltration.measurement_type, newMeasurementType);
                mEkotropeInfiltrationViewModel.insertChangeLog(changeLog);
            }

            Ekotrope_Infiltration_Table infiltration = new Ekotrope_Infiltration_Table(mPlanId,
                    newCfm50, newAch50, newMeasurementType, true);
            if (addNew) {
                mEkotropeInfiltrationViewModel.insert(infiltration);
            } else {
                mEkotropeInfiltrationViewModel.update(infiltration);
            }
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextCfm50.setText(String.format(mInfiltration.cfm_50.toString()));
        mTextAch50.setText(String.format(mInfiltration.ach_50.toString()));

        ArrayAdapter<String> measurementTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mMeasurementTypes);
        measurementTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMeasurementType.setAdapter(measurementTypeAdapter);
        mSpinnerMeasurementType.setSelection(mMeasurementTypes.indexOf(mInfiltration.measurement_type));
    }

    private void initializeTextValidators() {
        mTextAch50.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextAch50.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextAch50.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mTextCfm50, Double.toString(mInfiltration.cfm_50));
        BridgeHelper.setChangeTracker(mTextAch50, Double.toString(mInfiltration.ach_50));
        BridgeHelper.setChangeTracker(mSpinnerMeasurementType, mInfiltration.measurement_type);
    }
}