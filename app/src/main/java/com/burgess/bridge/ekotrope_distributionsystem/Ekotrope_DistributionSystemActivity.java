package com.burgess.bridge.ekotrope_distributionsystem;

import static com.burgess.bridge.Constants.COMPONENT_DISTRIBUTION_SYSTEM;
import static com.burgess.bridge.Constants.COMPONENT_DUCT;
import static com.burgess.bridge.Constants.COMPONENT_TYPE;
import static com.burgess.bridge.Constants.DISTRIBUTION_SYSTEM_INDEX;
import static com.burgess.bridge.Constants.DISTRIBUTION_SYSTEM_INDEX_NOT_FOUND;
import static com.burgess.bridge.Constants.DUCT_INDEX;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_componentlist.Ekotrope_ComponentListActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_DistributionSystem_Table;

public class Ekotrope_DistributionSystemActivity extends AppCompatActivity {
    private Ekotrope_DistributionSystemViewModel mDistributionSystemViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private CheckBox mCheckBoxLeakageToOutsideTested;
    private EditText mTextLeakageToOutside;
    private EditText mTextTotalLeakage;
    private Spinner mSpinnerTotalDuctLeakageTestCondition;
    private EditText mTextNumberOfReturns;
    private EditText mTextSqFeetServed;
    private Button mButtonDuctsList;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mDistributionSystemIndex;
    private Ekotrope_DistributionSystem_Table mDistributionSystem;
    List<String> mTestConditions;
    private boolean valid = true;

    public static final String TAG = "DISTRIBUTION_SYSTEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_distribution_system);
        setSupportActionBar(findViewById(R.id.distribution_system_toolbar));
        mDistributionSystemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DistributionSystemViewModel.class);

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
        mDistributionSystemIndex = intent.getIntExtra(DISTRIBUTION_SYSTEM_INDEX, DISTRIBUTION_SYSTEM_INDEX_NOT_FOUND);
        mDistributionSystem = mDistributionSystemViewModel.getDistributionSystem(mPlanId, mDistributionSystemIndex);

        // Set spinner lists...
        mTestConditions = new ArrayList<>();
        mTestConditions.add("NoTest");
        mTestConditions.add("PostConstruction");
        mTestConditions.add("RoughInWithAirHandler");
        mTestConditions.add("RoughInNoAirHandler");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.distribution_system_constraint_layout);
        mToolbar = findViewById(R.id.distribution_system_toolbar);
        mCheckBoxLeakageToOutsideTested = findViewById(R.id.distribution_system_checkbox_leakage_to_outside_tested);
        mTextLeakageToOutside = findViewById(R.id.distribution_system_text_leakage_to_outside);
        mTextTotalLeakage = findViewById(R.id.distribution_system_text_total_leakage);
        mSpinnerTotalDuctLeakageTestCondition = findViewById(R.id.distribution_system_spinner_test_condition);
        mTextNumberOfReturns = findViewById(R.id.distribution_system_text_number_of_returns);
        mTextSqFeetServed = findViewById(R.id.distribution_system_text_sq_feet_served);
        mButtonDuctsList = findViewById(R.id.distribution_system_button_ducts_list);
        mButtonSave = findViewById(R.id.distribution_system_button_save);
    }

    private void initializeButtonListeners() {
        mButtonDuctsList.setOnClickListener(v -> {
            Intent intent = new Intent(this, Ekotrope_ComponentListActivity.class);
            intent.putExtra(INSPECTION_ID, mInspectionId);
            intent.putExtra(EKOTROPE_PROJECT_ID, mProjectId);
            intent.putExtra(EKOTROPE_PLAN_ID, mPlanId);
            intent.putExtra(DUCT_INDEX, mDistributionSystemIndex);
            intent.putExtra(COMPONENT_TYPE, COMPONENT_DUCT);
            startActivity(intent);
        });
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newLeakageToOutsideTested = mCheckBoxLeakageToOutsideTested.isChecked();
            Double newLeakageToOutside = Double.parseDouble(mTextLeakageToOutside.getText().toString());
            Double newTotalLeakage = Double.parseDouble(mTextTotalLeakage.getText().toString());
            String newTestCondition = mSpinnerTotalDuctLeakageTestCondition.getSelectedItem().toString();
            Integer newNumberOfReturns = Integer.parseInt(mTextNumberOfReturns.getText().toString());
            Double newSqFeetServed = Double.parseDouble(mTextSqFeetServed.getText().toString());

            mCheckBoxLeakageToOutsideTested.clearFocus();
            mTextLeakageToOutside.clearFocus();
            mTextTotalLeakage.clearFocus();
            mSpinnerTotalDuctLeakageTestCondition.clearFocus();
            mTextNumberOfReturns.clearFocus();
            mTextSqFeetServed.clearFocus();

            if ((boolean)mCheckBoxLeakageToOutsideTested.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Leakage To Outside Tested", Boolean.toString(mDistributionSystem.is_leakage_to_outside_tested),
                        Boolean.toString(newLeakageToOutsideTested));
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextLeakageToOutside.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Leakage To Outside", Double.toString(mDistributionSystem.leakage_to_outside),
                        Double.toString(newLeakageToOutside));
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextTotalLeakage.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Total Leakage", Double.toString(mDistributionSystem.total_leakage),
                        Double.toString(newTotalLeakage));
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerTotalDuctLeakageTestCondition.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Test Condition", mDistributionSystem.total_duct_leakage_test_condition,
                        newTestCondition);
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextNumberOfReturns.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Number Of Returns", Double.toString(mDistributionSystem.number_of_returns),
                        Double.toString(newNumberOfReturns));
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextSqFeetServed.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Distribution System", mDistributionSystem.system_type,
                        "Sq Feet Served", Double.toString(mDistributionSystem.sq_feet_served),
                        Double.toString(newSqFeetServed));
                mDistributionSystemViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_DistributionSystem_Table newDistributionSystem = new Ekotrope_DistributionSystem_Table(
                    mPlanId, mDistributionSystemIndex, mDistributionSystem.system_type,
                    newLeakageToOutsideTested, newLeakageToOutside, newTotalLeakage,
                    newTestCondition, newNumberOfReturns, newSqFeetServed, true);
            mDistributionSystemViewModel.updateDistributionSystem(newDistributionSystem);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Distribution System - %s", mDistributionSystem.system_type));
        mCheckBoxLeakageToOutsideTested.setChecked(mDistributionSystem.is_leakage_to_outside_tested);
        mTextLeakageToOutside.setText(String.format(mDistributionSystem.leakage_to_outside.toString()));
        mTextTotalLeakage.setText(String.format(mDistributionSystem.total_leakage.toString()));
        mTextNumberOfReturns.setText(String.format(mDistributionSystem.number_of_returns.toString()));
        mTextSqFeetServed.setText(String.format(mDistributionSystem.sq_feet_served.toString()));

        ArrayAdapter<String> testConditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTestConditions);
        testConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTotalDuctLeakageTestCondition.setAdapter(testConditionAdapter);
        mSpinnerTotalDuctLeakageTestCondition.setSelection(mTestConditions.indexOf(mDistributionSystem.total_duct_leakage_test_condition));
    }

    private void initializeTextValidators() {
        mTextLeakageToOutside.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().isEmpty()) {
                        valid = true;
                    } else {
                        Double value = Double.parseDouble(editable.toString());
                        if (value < 0) {
                            mTextLeakageToOutside.setError("Value must be greater than or equal to 0");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextLeakageToOutside.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextTotalLeakage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().isEmpty()) {
                        valid = true;
                    } else {
                        Double value = Double.parseDouble(editable.toString());
                        if (value < 0) {
                            mTextTotalLeakage.setError("Value must be greater than or equal to 0");
                            valid = false;
                            } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextTotalLeakage.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextNumberOfReturns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().isEmpty()) {
                        valid = true;
                    } else {
                        Integer value = Integer.parseInt(editable.toString());
                        if (value < 0) {
                            mTextNumberOfReturns.setError("Value must be greater than or equal to 0");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextNumberOfReturns.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextSqFeetServed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().isEmpty()) {
                        valid = true;
                    } else {
                        Double value = Double.parseDouble(editable.toString());
                        if (value < 0) {
                            mTextSqFeetServed.setError("Value must be greater than or equal to 0");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextSqFeetServed.setError("Value must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mCheckBoxLeakageToOutsideTested, mDistributionSystem.is_leakage_to_outside_tested);
        BridgeHelper.setChangeTracker(mTextLeakageToOutside, mDistributionSystem.leakage_to_outside);
        BridgeHelper.setChangeTracker(mTextTotalLeakage, mDistributionSystem.total_leakage);
        BridgeHelper.setChangeTracker(mSpinnerTotalDuctLeakageTestCondition, mDistributionSystem.total_duct_leakage_test_condition);
        BridgeHelper.setChangeTracker(mTextNumberOfReturns, mDistributionSystem.number_of_returns);
        BridgeHelper.setChangeTracker(mTextSqFeetServed, mDistributionSystem.sq_feet_served);
    }
}