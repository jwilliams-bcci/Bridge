package com.burgess.bridge.ekotrope_mechanicalventilation;

import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.Constants.MECHANICAL_VENTILATION_INDEX;
import static com.burgess.bridge.Constants.MECHANICAL_VENTILATION_INDEX_NOT_FOUND;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;

public class Ekotrope_MechanicalVentilationActivity extends AppCompatActivity {
    private Ekotrope_MechanicalVentilationViewModel mMechanicalVentilationViewModel;
    private ConstraintLayout mConstraintLayout;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Spinner mSpinnerVentilationType;
    private EditText mTextMeasuredFlowRate;
    private EditText mTextFanWatts;
    private EditText mTextHours;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mMechanicalVentilationIndex;
    private Ekotrope_MechanicalVentilation_Table mMechanicalVentilation;
    List<String> mVentilationTypes;
    private boolean valid = true;

    public static final String TAG = "MECHANICAL_VENTILATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_mechanical_ventilation);
        setSupportActionBar(findViewById(R.id.mechanical_ventilation_toolbar));
        mMechanicalVentilationViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_MechanicalVentilationViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(EKOTROPE_PLAN_ID);
        mMechanicalVentilationIndex = intent.getIntExtra(MECHANICAL_VENTILATION_INDEX, MECHANICAL_VENTILATION_INDEX_NOT_FOUND);
        mMechanicalVentilation = mMechanicalVentilationViewModel.getMechanicalVentilation(mPlanId, mMechanicalVentilationIndex);

        // Set spinner lists...
        mVentilationTypes = new ArrayList<>();
        mVentilationTypes.add("ExhaustOnly");
        mVentilationTypes.add("SupplyOnly");
        mVentilationTypes.add("AirCycler");
        mVentilationTypes.add("AirCyclerWithSupplementalFan");
        mVentilationTypes.add("Balanced");
        mVentilationTypes.add("ERV");
        mVentilationTypes.add("HRV");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.mechanical_ventilation_constraint_layout);
        mToolbar = findViewById(R.id.mechanical_ventilation_toolbar);
        mSpinnerVentilationType = findViewById(R.id.mechanical_ventilation_spinner_ventilation_type);
        mTextMeasuredFlowRate = findViewById(R.id.mechanical_ventilation_text_measured_flow_rate);
        mTextFanWatts = findViewById(R.id.mechanical_ventilation_text_fan_watts);
        mTextHours = findViewById(R.id.mechanical_ventilation_text_operational_hours_per_day);
        mButtonSave = findViewById(R.id.mechanical_ventilation_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors before saving.", Snackbar.LENGTH_LONG).show();
                return;
            }
            String newVentilationType = mSpinnerVentilationType.getSelectedItem().toString();
            Double newMeasuredFlowRate = Double.parseDouble(mTextMeasuredFlowRate.getText().toString());
            Double newFanWatts = Double.parseDouble(mTextFanWatts.getText().toString());
            Double newHours = Double.parseDouble(mTextHours.getText().toString());

            mSpinnerVentilationType.clearFocus();
            mTextMeasuredFlowRate.clearFocus();
            mTextFanWatts.clearFocus();
            mTextHours.clearFocus();

            if ((boolean)mSpinnerVentilationType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "MechanicalVentilation", mMechanicalVentilation.motor_type,
                        "Ventilation Type", mMechanicalVentilation.ventilation_type, newVentilationType);
                mMechanicalVentilationViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextMeasuredFlowRate.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "MechanicalVentilation", mMechanicalVentilation.motor_type,
                        "Measured Flow Rate", mMechanicalVentilation.measured_flow_rate.toString(), newMeasuredFlowRate.toString());
                mMechanicalVentilationViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextFanWatts.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "MechanicalVentilation", mMechanicalVentilation.motor_type,
                        "Fan Watts", mMechanicalVentilation.fan_watts.toString(), newFanWatts.toString());
                mMechanicalVentilationViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextHours.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "MechanicalVentilation", mMechanicalVentilation.motor_type,
                        "Operational Hours Per Day", mMechanicalVentilation.operational_hours_per_day.toString(), newHours.toString());
                mMechanicalVentilationViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_MechanicalVentilation_Table newMechanicalVentilation = new Ekotrope_MechanicalVentilation_Table(
                    mPlanId, mMechanicalVentilationIndex, mMechanicalVentilation.motor_type,
                    newVentilationType, newMeasuredFlowRate, newFanWatts, newHours, true);
            mMechanicalVentilationViewModel.updateMechanicalVentilation(newMechanicalVentilation);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Mechanical Ventilation - %s", mMechanicalVentilation.motor_type));
        mTextMeasuredFlowRate.setText(String.format(mMechanicalVentilation.measured_flow_rate.toString()));
        mTextFanWatts.setText(String.format(mMechanicalVentilation.fan_watts.toString()));
        mTextHours.setText(String.format(mMechanicalVentilation.operational_hours_per_day.toString()));

        ArrayAdapter<String> ventilationTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mVentilationTypes);
        ventilationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerVentilationType.setAdapter(ventilationTypeAdapter);
        mSpinnerVentilationType.setSelection(mVentilationTypes.indexOf(mMechanicalVentilation.ventilation_type));
    }

    private void initializeTextValidators() {
        mTextMeasuredFlowRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().isEmpty()) {
                        valid = true;
                    } else {
                        Double value = Double.parseDouble(editable.toString());
                        if (value < 0) {
                            mTextMeasuredFlowRate.setError("Value must be greater than 0");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextMeasuredFlowRate.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextFanWatts.addTextChangedListener(new TextWatcher() {
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
                            mTextFanWatts.setError("Value must be greater than 0");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextFanWatts.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextHours.addTextChangedListener(new TextWatcher() {
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
                        if (value < 0 || value > 24) {
                            mTextHours.setError("Value must be between 0 and 24");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextHours.setError("Value must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerVentilationType, mMechanicalVentilation.ventilation_type);
        BridgeHelper.setChangeTracker(mTextMeasuredFlowRate, mMechanicalVentilation.measured_flow_rate);
        BridgeHelper.setChangeTracker(mTextFanWatts, mMechanicalVentilation.fan_watts);
        BridgeHelper.setChangeTracker(mTextHours, mMechanicalVentilation.operational_hours_per_day);
    }
}