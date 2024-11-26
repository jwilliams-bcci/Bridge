package com.burgess.bridge.ekotrope_mechanicalequipment;

import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.Constants.MECHANICAL_EQUIPMENT_INDEX;
import static com.burgess.bridge.Constants.MECHANICAL_EQUIPMENT_INDEX_NOT_FOUND;
import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;

public class Ekotrope_MechanicalEquipmentActivity extends AppCompatActivity {
    private Ekotrope_MechanicalEquipmentViewModel mMechanicalEquipmentViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private TextView mTextModelNumber;
    private Spinner mSpinnerLocation;
    private EditText mTextHeatingLoad;
    private EditText mTextCoolingLoad;
    private EditText mTextHotWaterLoad;
    private EditText mTextAHRIReferenceNumber;
    private Spinner mSpinnerAHRIReferenceFuelType;
    private View mDividerLeft;
    private View mDividerRight;
    private TextView mLabelRefrigerantCharge;
    private TextView mLabelTestConducted;
    private CheckBox mCheckBoxTestConducted;
    private TextView mLabelTestMethod;
    private TextView mLabelMeteringDevice;
    private TextView mLabelDifferenceDTD;
    private TextView mLabelDifferenceCTOA;
    private TextView mLabelWeightDeviation;
    private Spinner mSpinnerTestMethod;
    private Spinner mSpinnerMeteringDevice;
    private EditText mTextDifferenceDTD;
    private EditText mTextDifferenceCTOA;
    private EditText mTextWeightDeviation;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mMechanicalEquipmentIndex;
    private Ekotrope_MechanicalEquipment_Table mMechanicalEquipment;
    List<String> mLocations;
    List<String> mFuelTypes;
    List<String> mTestMethods;
    List<String> mMeteringDevices;
    private boolean valid = true;

    public static final String TAG = "MECHANICAL_EQUIPMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_mechanical_equipment);
        setSupportActionBar(findViewById(R.id.mechanical_equipment_toolbar));
        mMechanicalEquipmentViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_MechanicalEquipmentViewModel.class);

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
        mMechanicalEquipmentIndex = intent.getIntExtra(MECHANICAL_EQUIPMENT_INDEX, MECHANICAL_EQUIPMENT_INDEX_NOT_FOUND);
        mMechanicalEquipment = mMechanicalEquipmentViewModel.getMechanicalEquipment(mPlanId, mMechanicalEquipmentIndex);

        // Set spinner lists...
        mLocations = new ArrayList<>();
        mFuelTypes = new ArrayList<>();
        mTestMethods = new ArrayList<>();
        mMeteringDevices = new ArrayList<>();
        mLocations.add("Ambient");
        mLocations.add("UnconditionedAttic");
        mLocations.add("ConditionedAttic");
        mLocations.add("ConditionedCrawlspace");
        mLocations.add("ConditionedSpace");
        mLocations.add("Garage");
        mLocations.add("UnconditionedBasementOrCrawlspace");
        mLocations.add("Unspecified");
        mFuelTypes.add("NATURAL_GAS");
        mFuelTypes.add("ELECTRIC");
        mFuelTypes.add("OIL");
        mFuelTypes.add("PROPANE");
        mFuelTypes.add("WOOD");
        mTestMethods.add("NON_INVASIVE");
        mTestMethods.add("WEIGH_IN");
        mMeteringDevices.add("PISTON_CAP_TUBE");
        mMeteringDevices.add("TXV_EEV");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.mechanical_equipment_constraint_layout);
        mToolbar = findViewById(R.id.mechanical_equipment_toolbar);
        mTextModelNumber = findViewById(R.id.mechanical_equipment_text_model_number);
        mSpinnerLocation = findViewById(R.id.mechanical_equipment_spinner_location);
        mTextHeatingLoad = findViewById(R.id.mechanical_equipment_text_heating_load);
        mTextCoolingLoad = findViewById(R.id.mechanical_equipment_text_cooling_load);
        mTextHotWaterLoad = findViewById(R.id.mechanical_equipment_text_hot_water_load);
        mTextAHRIReferenceNumber = findViewById(R.id.mechanical_equipment_text_ahri_ref_number);
        mSpinnerAHRIReferenceFuelType = findViewById(R.id.mechanical_equipment_spinner_ahri_ref_fuel_type);
        mDividerLeft = findViewById(R.id.mechanical_equipment_divider_left);
        mDividerRight = findViewById(R.id.mechanical_equipment_divider_right);
        mLabelRefrigerantCharge = findViewById(R.id.mechanical_equipment_label_refrigerant_charge);
        mLabelTestConducted = findViewById(R.id.mechanical_equipment_label_test_conducted);
        mCheckBoxTestConducted = findViewById(R.id.mechanical_equipment_checkbox_test_conducted);
        mLabelTestMethod = findViewById(R.id.mechanical_equipment_label_test_method);
        mLabelMeteringDevice = findViewById(R.id.mechanical_equipment_label_metering_device);
        mLabelDifferenceDTD = findViewById(R.id.mechanical_equipment_label_difference_dtd);
        mLabelDifferenceCTOA = findViewById(R.id.mechanical_equipment_label_difference_ctoa);
        mLabelWeightDeviation = findViewById(R.id.mechanical_equipment_label_weight_deviation);
        mSpinnerTestMethod = findViewById(R.id.mechanical_equipment_spinner_test_method);
        mSpinnerMeteringDevice = findViewById(R.id.mechanical_equipment_spinner_metering_device);
        mTextDifferenceDTD = findViewById(R.id.mechanical_equipment_text_difference_dtd);
        mTextDifferenceCTOA = findViewById(R.id.mechanical_equipment_text_difference_ctoa);
        mTextWeightDeviation = findViewById(R.id.mechanical_equipment_text_weight_deviation);
        mButtonSave = findViewById(R.id.mechanical_equipment_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            String newLocation = mSpinnerLocation.getSelectedItem().toString();
            Double newHeatingLoad = Double.parseDouble(mTextHeatingLoad.getText().toString());
            Double newCoolingLoad = Double.parseDouble(mTextCoolingLoad.getText().toString());
            Double newHotWaterLoad = Double.parseDouble(mTextHotWaterLoad.getText().toString());
            String newAHRIReferenceNumber = mTextAHRIReferenceNumber.getText().toString();
            String newAHRIReferenceFuelType = mSpinnerAHRIReferenceFuelType.getSelectedItem().toString();
            boolean newTestConducted = mCheckBoxTestConducted.isChecked();
            String newTestMethod = "NON_INVASIVE";
            String newMeteringDevice = "PISTON_CAP_TUBE";
            Double newDifferenceDTD = 0.0;
            Double newDifferenceCTOA = 0.0;
            Double newWeightDeviation = 0.0;

            if (mCheckBoxTestConducted.isChecked()) {
                newTestMethod = mSpinnerTestMethod.getSelectedItem().toString();
                newMeteringDevice = mSpinnerMeteringDevice.getSelectedItem().toString();
                newDifferenceDTD = Double.parseDouble(mTextDifferenceDTD.getText().toString());
                newDifferenceCTOA = Double.parseDouble(mTextDifferenceCTOA.getText().toString());
                newWeightDeviation = Double.parseDouble(mTextWeightDeviation.getText().toString());

                mSpinnerTestMethod.clearFocus();
                mSpinnerMeteringDevice.clearFocus();
                mTextDifferenceDTD.clearFocus();
                mTextDifferenceCTOA.clearFocus();
                mTextWeightDeviation.clearFocus();

                if ((boolean)mSpinnerTestMethod.getTag()) {
                    Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                            mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                            "Test Method", "N/A", newTestMethod);
                    mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
                }
                if ((boolean)mSpinnerMeteringDevice.getTag()) {
                    Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                            mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                            "Metering Device", "N/A", newMeteringDevice);
                    mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
                }
                if ((boolean)mTextDifferenceDTD.getTag()) {
                    Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                            mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                            "Difference DTD", "N/A", Double.toString(newDifferenceDTD));
                    mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
                }
                if ((boolean)mTextDifferenceCTOA.getTag()) {
                    Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                            mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                            "Difference CTOA", "N/A", Double.toString(newDifferenceCTOA));
                    mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
                }
                if ((boolean)mTextWeightDeviation.getTag()) {
                    Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                            mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                            "Weight Deviation", "N/A", Double.toString(newWeightDeviation));
                    mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
                }
            }

            mSpinnerLocation.clearFocus();
            mTextHeatingLoad.clearFocus();
            mTextCoolingLoad.clearFocus();
            mTextHotWaterLoad.clearFocus();
            mTextAHRIReferenceNumber.clearFocus();
            mSpinnerAHRIReferenceFuelType.clearFocus();
            mCheckBoxTestConducted.clearFocus();

            if ((boolean)mSpinnerLocation.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "Location", mMechanicalEquipment.location, newLocation);
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextHeatingLoad.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "Heating Load", Double.toString(mMechanicalEquipment.percent_heating_load),
                        Double.toString(newHeatingLoad));
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);

            }
            if ((boolean)mTextCoolingLoad.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "Cooling Load", Double.toString(mMechanicalEquipment.percent_cooling_load),
                        Double.toString(newCoolingLoad));
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextHotWaterLoad.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "Hot Water Load", Double.toString(mMechanicalEquipment.percent_hot_water_load),
                        Double.toString(newHotWaterLoad));
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextAHRIReferenceNumber.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "AHRI Reference Number", mMechanicalEquipment.ahri_reference_number,
                        newAHRIReferenceNumber);
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerAHRIReferenceFuelType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "AHRI Reference Fuel Type", mMechanicalEquipment.ahri_reference_fuel_type,
                        newAHRIReferenceFuelType);
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mCheckBoxTestConducted.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Mechanical Equipment", mMechanicalEquipment.name,
                        "Test Conducted", Boolean.toString(mMechanicalEquipment.rc_test_conducted),
                        Boolean.toString(newTestConducted));
                mMechanicalEquipmentViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_MechanicalEquipment_Table newMechanicalEquipment = new Ekotrope_MechanicalEquipment_Table(
                    mPlanId, mMechanicalEquipmentIndex, mMechanicalEquipment.name,
                    mMechanicalEquipment.equipment_type, mMechanicalEquipment.model_number,
                    newLocation, newHeatingLoad, newCoolingLoad, newHotWaterLoad, newAHRIReferenceNumber,
                    newAHRIReferenceFuelType, newTestConducted, newTestMethod, newMeteringDevice,
                    newDifferenceDTD, newDifferenceCTOA, newWeightDeviation, true);
            mMechanicalEquipmentViewModel.updateMechanicalEquipment(newMechanicalEquipment);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Mechanical Equipment - %s", mMechanicalEquipment.name));
        mTextModelNumber.setText(mMechanicalEquipment.model_number);
        mTextHeatingLoad.setText(String.format(mMechanicalEquipment.percent_heating_load.toString()));
        mTextCoolingLoad.setText(String.format(mMechanicalEquipment.percent_cooling_load.toString()));
        mTextHotWaterLoad.setText(String.format(mMechanicalEquipment.percent_hot_water_load.toString()));
        mTextAHRIReferenceNumber.setText(mMechanicalEquipment.ahri_reference_number);
        mCheckBoxTestConducted.setChecked(mMechanicalEquipment.rc_test_conducted);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLocation.setAdapter(locationAdapter);
        mSpinnerLocation.setSelection(locationAdapter.getPosition(mMechanicalEquipment.location));

        ArrayAdapter<String> ahriReferenceFuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mFuelTypes);
        ahriReferenceFuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAHRIReferenceFuelType.setAdapter(ahriReferenceFuelTypeAdapter);
        mSpinnerAHRIReferenceFuelType.setSelection(ahriReferenceFuelTypeAdapter.getPosition(mMechanicalEquipment.ahri_reference_fuel_type));

        if (mMechanicalEquipment.equipment_type.equals("AIR_CONDITIONER") || mMechanicalEquipment.equipment_type.equals("AIR_SOURCE_HEAT_PUMP")) {
            mDividerLeft.setVisibility(View.GONE);
            mDividerRight.setVisibility(View.GONE);
            mLabelRefrigerantCharge.setVisibility(View.GONE);
            mLabelTestConducted.setVisibility(View.GONE);
            mCheckBoxTestConducted.setVisibility(View.GONE);
        }

        if (mCheckBoxTestConducted.isChecked()) {
            setRefrigerantChangeVisibility(true);
        }

        if (mMechanicalEquipment.model_number != null || !mMechanicalEquipment.model_number.isEmpty()) {
            mTextAHRIReferenceNumber.setEnabled(false);
            Snackbar.make(mConstraintLayout, "Model number is present, AHRI ref number cannot be modified.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void initializeTextValidators() {
        mTextHeatingLoad.addTextChangedListener(new TextWatcher() {
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
                        if (value < 0 || value > 100) {
                            mTextHeatingLoad.setError("Please enter a infiltration_value between 0 and 100");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextHeatingLoad.setError("Please enter a number");
                    valid = false;
                }
            }
        });
        mTextCoolingLoad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextCoolingLoad.setError("Please enter a infiltration_value between 0 and 100");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextCoolingLoad.setError("Please enter a number");
                    valid = false;
                }
            }
        });
        mTextHotWaterLoad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextHotWaterLoad.setError("Please enter a infiltration_value between 0 and 100");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextHotWaterLoad.setError("Please enter a number");
                    valid = false;
                }
            }
        });
        if (mCheckBoxTestConducted.isChecked()) {
            mTextDifferenceDTD.addTextChangedListener(new TextWatcher() {
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
                        }
                    } catch (NumberFormatException e) {
                        mTextDifferenceDTD.setError("Please enter a number");
                        valid = false;
                    }
                }
            });
            mTextDifferenceCTOA.addTextChangedListener(new TextWatcher() {
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
                        }
                    } catch (NumberFormatException e) {
                        mTextDifferenceCTOA.setError("Please enter a number");
                        valid = false;
                    }
                }
            });
            mTextWeightDeviation.addTextChangedListener(new TextWatcher() {
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
                        }
                    } catch (NumberFormatException e) {
                        mTextWeightDeviation.setError("Please enter a number");
                        valid = false;
                    }
                }
            });
        }
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerLocation, mMechanicalEquipment.location);
        BridgeHelper.setChangeTracker(mTextHeatingLoad, mMechanicalEquipment.percent_heating_load);
        BridgeHelper.setChangeTracker(mTextCoolingLoad, mMechanicalEquipment.percent_cooling_load);
        BridgeHelper.setChangeTracker(mTextHotWaterLoad, mMechanicalEquipment.percent_hot_water_load);
        BridgeHelper.setChangeTracker(mTextAHRIReferenceNumber, mMechanicalEquipment.ahri_reference_number);
        BridgeHelper.setChangeTracker(mSpinnerAHRIReferenceFuelType, mMechanicalEquipment.ahri_reference_fuel_type);

        Drawable originalBackground = mCheckBoxTestConducted.getBackground();
        boolean fieldChanged = true;
        mCheckBoxTestConducted.setTag(!fieldChanged);
        mCheckBoxTestConducted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setRefrigerantChangeVisibility(true);
            } else {
                setRefrigerantChangeVisibility(false);
            }
            if (!isChecked == mMechanicalEquipment.rc_test_conducted) {
                mCheckBoxTestConducted.setBackgroundResource(R.drawable.green_border);
                mCheckBoxTestConducted.setTag(fieldChanged);
            } else {
                mCheckBoxTestConducted.setBackground(originalBackground);
                mCheckBoxTestConducted.setTag(!fieldChanged);
            }
        });
    }

    private void setRefrigerantChangeVisibility(boolean visible) {
        mLabelTestMethod.setVisibility(visible ? View.VISIBLE : View.GONE);
        mLabelMeteringDevice.setVisibility(visible ? View.VISIBLE : View.GONE);
        mLabelDifferenceDTD.setVisibility(visible ? View.VISIBLE : View.GONE);
        mLabelDifferenceCTOA.setVisibility(visible ? View.VISIBLE : View.GONE);
        mLabelWeightDeviation.setVisibility(visible ? View.VISIBLE : View.GONE);
        mSpinnerTestMethod.setVisibility(visible ? View.VISIBLE : View.GONE);
        mSpinnerMeteringDevice.setVisibility(visible ? View.VISIBLE : View.GONE);
        mTextDifferenceDTD.setVisibility(visible ? View.VISIBLE : View.GONE);
        mTextDifferenceCTOA.setVisibility(visible ? View.VISIBLE : View.GONE);
        mTextWeightDeviation.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            mTextDifferenceDTD.setText(String.format(mMechanicalEquipment.rc_difference_dtd.toString()));
            mTextDifferenceCTOA.setText(String.format(mMechanicalEquipment.rc_difference_ctoa.toString()));
            mTextWeightDeviation.setText(String.format(mMechanicalEquipment.rc_weight_deviation.toString()));

            ArrayAdapter<String> testMethodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTestMethods);
            testMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerTestMethod.setAdapter(testMethodAdapter);
            mSpinnerTestMethod.setSelection(testMethodAdapter.getPosition(mMechanicalEquipment.rc_test_method));

            ArrayAdapter<String> meteringDeviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mMeteringDevices);
            meteringDeviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMeteringDevice.setAdapter(meteringDeviceAdapter);
            mSpinnerMeteringDevice.setSelection(meteringDeviceAdapter.getPosition(mMechanicalEquipment.rc_metering_device));

            BridgeHelper.setChangeTracker(mSpinnerTestMethod, mMechanicalEquipment.rc_test_method);
            BridgeHelper.setChangeTracker(mSpinnerMeteringDevice, mMechanicalEquipment.rc_metering_device);
            BridgeHelper.setChangeTracker(mTextDifferenceDTD, mMechanicalEquipment.rc_difference_dtd);
            BridgeHelper.setChangeTracker(mTextDifferenceCTOA, mMechanicalEquipment.rc_difference_ctoa);
            BridgeHelper.setChangeTracker(mTextWeightDeviation, mMechanicalEquipment.rc_weight_deviation);
        }
    }
}