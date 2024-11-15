package com.burgess.bridge.ekotrope_clothesdryer;

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
import data.Tables.Ekotrope_ClothesDryer_Table;

public class Ekotrope_ClothesDryerActivity extends AppCompatActivity {
    private Ekotrope_ClothesDryerViewModel mClothesDryerViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CheckBox mCheckBoxAvailable;
    private Spinner mSpinnerDefaultsType;
    private EditText mTextCombinedEnergyFactor;
    private Spinner mSpinnerUtilizationFactor;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_ClothesDryer_Table mClothesDryer;
    private List<String> mDefaultsTypes;
    private List<String> mUtilizationFactors;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_clothes_dryer);
        setSupportActionBar(findViewById(R.id.clothes_dryer_toolbar));
        mClothesDryerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_ClothesDryerViewModel.class);

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
        mClothesDryer = mClothesDryerViewModel.getClothesDryer(mPlanId);

        // Set spinner lists...
        mDefaultsTypes = new ArrayList<>();
        mDefaultsTypes.add("Custom");
        mDefaultsTypes.add("HersReference");

        mUtilizationFactors = new ArrayList<>();
        mUtilizationFactors.add("TIMER_CONTROLS");
        mUtilizationFactors.add("MOISTURE_SENSING");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.clothes_dryer_constraint_layout);
        mCheckBoxAvailable = findViewById(R.id.clothes_dryer_checkbox_available);
        mSpinnerDefaultsType = findViewById(R.id.clothes_dryer_spinner_defaults_type);
        mTextCombinedEnergyFactor = findViewById(R.id.clothes_dryer_text_combined_energy_factor);
        mSpinnerUtilizationFactor = findViewById(R.id.clothes_dryer_spinner_utilization_factor);
        mButtonSave = findViewById(R.id.clothes_dryer_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newAvailable = mCheckBoxAvailable.isChecked();
            String newDefaultsType = mSpinnerDefaultsType.getSelectedItem().toString();
            Double newCombinedEnergyFactor = Double.parseDouble(mTextCombinedEnergyFactor.getText().toString());
            String newUtilizationFactor = mSpinnerUtilizationFactor.getSelectedItem().toString();

            mCheckBoxAvailable.clearFocus();
            mSpinnerDefaultsType.clearFocus();
            mTextCombinedEnergyFactor.clearFocus();
            mSpinnerUtilizationFactor.clearFocus();

            if ((boolean)mCheckBoxAvailable.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesDryer", "N/A", "Available",
                        Boolean.toString(mClothesDryer.available), Boolean.toString(newAvailable));
                mClothesDryerViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerDefaultsType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesDryer", "N/A", "Defaults Type",
                        mClothesDryer.defaults_type, newDefaultsType);
                mClothesDryerViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextCombinedEnergyFactor.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesDryer", "N/A", "Combined Energy Factor",
                        Double.toString(mClothesDryer.combined_energy_factor), Double.toString(newCombinedEnergyFactor));
                mClothesDryerViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerUtilizationFactor.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesDryer", "N/A", "Utilization Factor",
                        mClothesDryer.utilization_factor, newUtilizationFactor);
                mClothesDryerViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_ClothesDryer_Table newClothesDryer = new Ekotrope_ClothesDryer_Table(mPlanId,
                    newAvailable, newDefaultsType, newCombinedEnergyFactor, newUtilizationFactor, true);
            mClothesDryerViewModel.updateClothesDryer(newClothesDryer);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mCheckBoxAvailable.setChecked(mClothesDryer.available);
        mTextCombinedEnergyFactor.setText(String.format(mClothesDryer.combined_energy_factor.toString()));

        ArrayAdapter<String> defaultsTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDefaultsTypes);
        defaultsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDefaultsType.setAdapter(defaultsTypeAdapter);
        mSpinnerDefaultsType.setSelection(mDefaultsTypes.indexOf(mClothesDryer.defaults_type));

        ArrayAdapter<String> utilizationFactorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mUtilizationFactors);
        utilizationFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerUtilizationFactor.setAdapter(utilizationFactorAdapter);
        mSpinnerUtilizationFactor.setSelection(mUtilizationFactors.indexOf(mClothesDryer.utilization_factor));
    }

    private void initializeTextValidators() {
        mTextCombinedEnergyFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextCombinedEnergyFactor.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextCombinedEnergyFactor.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mCheckBoxAvailable, mClothesDryer.available);
        BridgeHelper.setChangeTracker(mSpinnerDefaultsType, mClothesDryer.defaults_type);
        BridgeHelper.setChangeTracker(mTextCombinedEnergyFactor, mClothesDryer.combined_energy_factor);
        BridgeHelper.setChangeTracker(mSpinnerUtilizationFactor, mClothesDryer.utilization_factor);
    }
}