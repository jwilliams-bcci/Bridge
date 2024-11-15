package com.burgess.bridge.ekotrope_dishwasher;

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
import data.Tables.Ekotrope_Dishwasher_Table;

public class Ekotrope_DishwasherActivity extends AppCompatActivity {
    private Ekotrope_DishwasherViewModel mDishwasherViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CheckBox mCheckBoxAvailable;
    private Spinner mSpinnerDefaultsType;
    private Spinner mSpinnerSize;
    private Spinner mSpinnerEfficiencyType;
    private EditText mTextEfficiency;
    private EditText mTextAnnualGasCost;
    private EditText mTextGasRate;
    private EditText mTextElectricRate;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_Dishwasher_Table mDishwasher;
    private List<String> mDefaultsTypes;
    private List<String> mSizes;
    private List<String> mEfficiencyTypes;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_dishwasher);
        setSupportActionBar(findViewById(R.id.dishwasher_toolbar));
        mDishwasherViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DishwasherViewModel.class);

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
        mDishwasher = mDishwasherViewModel.getDishwasher(mPlanId);

        // Set spinner lists...
        mDefaultsTypes = new ArrayList<>();
        mDefaultsTypes.add("EnergyStarCompact");
        mDefaultsTypes.add("EnergyStarStandard");
        mDefaultsTypes.add("NaecaMinimum");
        mDefaultsTypes.add("HersReference");
        mDefaultsTypes.add("Custom");

        mSizes = new ArrayList<>();
        mSizes.add("Compact");
        mSizes.add("Standard");

        mEfficiencyTypes = new ArrayList<>();
        mEfficiencyTypes.add("EF");
        mEfficiencyTypes.add("kWh");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.dishwasher_constraint_layout);
        mCheckBoxAvailable = findViewById(R.id.dishwasher_checkbox_available);
        mSpinnerDefaultsType = findViewById(R.id.dishwasher_spinner_defaults_type);
        mSpinnerSize = findViewById(R.id.dishwasher_spinner_size);
        mSpinnerEfficiencyType = findViewById(R.id.dishwasher_spinner_efficiency_type);
        mTextEfficiency = findViewById(R.id.dishwasher_text_efficiency);
        mTextAnnualGasCost = findViewById(R.id.dishwasher_text_annual_gas_cost);
        mTextGasRate = findViewById(R.id.dishwasher_text_gas_rate);
        mTextElectricRate = findViewById(R.id.dishwasher_text_electric_rate);
        mButtonSave = findViewById(R.id.dishwasher_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newAvailable = mCheckBoxAvailable.isChecked();
            String newDefaultsType = mSpinnerDefaultsType.getSelectedItem().toString();
            String newSize = mSpinnerSize.getSelectedItem().toString();
            String newEfficiencyType = mSpinnerEfficiencyType.getSelectedItem().toString();
            Double newEfficiency = Double.parseDouble(mTextEfficiency.getText().toString());
            Double newAnnualGasCost = Double.parseDouble(mTextAnnualGasCost.getText().toString());
            Double newGasRate = Double.parseDouble(mTextGasRate.getText().toString());
            Double newElectricRate = Double.parseDouble(mTextElectricRate.getText().toString());

            mCheckBoxAvailable.clearFocus();
            mSpinnerDefaultsType.clearFocus();
            mSpinnerSize.clearFocus();
            mSpinnerEfficiencyType.clearFocus();
            mTextEfficiency.clearFocus();
            mTextAnnualGasCost.clearFocus();
            mTextGasRate.clearFocus();
            mTextElectricRate.clearFocus();

            if ((boolean)mCheckBoxAvailable.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Available", Boolean.toString(mDishwasher.dishwasher_available),
                        Boolean.toString(newAvailable));
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerDefaultsType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Defaults Type", mDishwasher.dishwasher_defaults_type,
                        newDefaultsType);
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerSize.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Size", mDishwasher.dishwasher_size,
                        newSize);
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerEfficiencyType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Efficiency Type", mDishwasher.dishwasher_efficiency_type,
                        newEfficiencyType);
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextEfficiency.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Efficiency", Double.toString(mDishwasher.dishwasher_efficiency),
                        Double.toString(newEfficiency));
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextAnnualGasCost.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Annual Gas Cost", Double.toString(mDishwasher.dishwasher_annual_gas_cost),
                        Double.toString(newAnnualGasCost));
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextGasRate.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Gas Rate", Double.toString(mDishwasher.dishwasher_gas_rate),
                        Double.toString(newGasRate));
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextElectricRate.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Dishwasher", "N/A",
                        "Dishwasher Electric Rate", Double.toString(mDishwasher.dishwasher_electric_rate),
                        Double.toString(newElectricRate));
                mDishwasherViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Dishwasher_Table newDishwasher = new Ekotrope_Dishwasher_Table(mPlanId,
                    newAvailable, newDefaultsType, newSize, newEfficiencyType, newEfficiency,
                    newAnnualGasCost, newGasRate, newElectricRate, true);
            mDishwasherViewModel.updateDishwasher(newDishwasher);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mCheckBoxAvailable.setChecked(mDishwasher.dishwasher_available);
        mTextEfficiency.setText(String.format(mDishwasher.dishwasher_efficiency.toString()));
        mTextAnnualGasCost.setText(String.format(mDishwasher.dishwasher_annual_gas_cost.toString()));
        mTextGasRate.setText(String.format(mDishwasher.dishwasher_gas_rate.toString()));
        mTextElectricRate.setText(String.format(mDishwasher.dishwasher_electric_rate.toString()));

        ArrayAdapter<String> defaultsTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDefaultsTypes);
        defaultsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDefaultsType.setAdapter(defaultsTypeAdapter);
        mSpinnerDefaultsType.setSelection(mDefaultsTypes.indexOf(mDishwasher.dishwasher_defaults_type));

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSize.setAdapter(sizeAdapter);
        mSpinnerSize.setSelection(mSizes.indexOf(mDishwasher.dishwasher_size));

        ArrayAdapter<String> efficiencyTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mEfficiencyTypes);
        efficiencyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEfficiencyType.setAdapter(efficiencyTypeAdapter);
        mSpinnerEfficiencyType.setSelection(mEfficiencyTypes.indexOf(mDishwasher.dishwasher_efficiency_type));
    }

    private void initializeTextValidators() {
        mTextEfficiency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0.1 || value > 2000) {
                        mTextEfficiency.setError("Must be between 0.1 and 2000");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextEfficiency.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextAnnualGasCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextAnnualGasCost.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextAnnualGasCost.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextGasRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextGasRate.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextGasRate.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextElectricRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextElectricRate.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextElectricRate.setError("Must be a number");
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mCheckBoxAvailable, mDishwasher.is_changed);
        BridgeHelper.setChangeTracker(mSpinnerDefaultsType, mDishwasher.dishwasher_defaults_type);
        BridgeHelper.setChangeTracker(mSpinnerSize, mDishwasher.dishwasher_size);
        BridgeHelper.setChangeTracker(mSpinnerEfficiencyType, mDishwasher.dishwasher_efficiency_type);
        BridgeHelper.setChangeTracker(mTextEfficiency, mDishwasher.dishwasher_efficiency);
        BridgeHelper.setChangeTracker(mTextAnnualGasCost, mDishwasher.dishwasher_annual_gas_cost);
        BridgeHelper.setChangeTracker(mTextGasRate, mDishwasher.dishwasher_gas_rate);
        BridgeHelper.setChangeTracker(mTextElectricRate, mDishwasher.dishwasher_electric_rate);
    }
}