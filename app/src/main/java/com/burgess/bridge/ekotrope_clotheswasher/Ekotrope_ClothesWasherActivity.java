package com.burgess.bridge.ekotrope_clotheswasher;

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
import data.Tables.Ekotrope_ClothesWasher_Table;

public class Ekotrope_ClothesWasherActivity extends AppCompatActivity {
    private Ekotrope_ClothesWasherViewModel mClothesWasherViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CheckBox mCheckBoxAvailable;
    private Spinner mSpinnerDefaultsType;
    private Spinner mSpinnerLoadType;
    private EditText mTextLabeledEnergyRating;
    private EditText mTextIntegratedModifiedEnergyFactor;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_ClothesWasher_Table mClothesWasher;
    private List<String> mDefaultsTypes;
    private List<String> mLoadTypes;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_clothes_washer);
        setSupportActionBar(findViewById(R.id.clothes_washer_toolbar));
        mClothesWasherViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_ClothesWasherViewModel.class);

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
        mClothesWasher = mClothesWasherViewModel.getClothesWasher(mPlanId);

        // Set spinner lists...
        mDefaultsTypes = new ArrayList<>();
        mDefaultsTypes.add("Custom");
        mDefaultsTypes.add("HersReference");
        mDefaultsTypes.add("EnergyStar");
        mDefaultsTypes.add("HighEfficiency");
        mDefaultsTypes.add("MediumEfficiency");
        mDefaultsTypes.add("Standard2018ToPresent");

        mLoadTypes = new ArrayList<>();
        mLoadTypes.add("FrontLoad");
        mLoadTypes.add("TopLoad");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.clothes_washer_constraint_layout);
        mCheckBoxAvailable = findViewById(R.id.clothes_washer_checkbox_available);
        mSpinnerDefaultsType = findViewById(R.id.clothes_washer_spinner_defaults_type);
        mSpinnerLoadType = findViewById(R.id.clothes_washer_spinner_load_type);
        mTextLabeledEnergyRating = findViewById(R.id.clothes_washer_text_labeled_energy_rating);
        mTextIntegratedModifiedEnergyFactor = findViewById(R.id.clothes_washer_text_integrated_modified_energy_factor);
        mButtonSave = findViewById(R.id.clothes_washer_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newAvailable = mCheckBoxAvailable.isChecked();
            String newDefaultsType = mSpinnerDefaultsType.getSelectedItem().toString();
            String newLoadType = mSpinnerLoadType.getSelectedItem().toString();
            Double newLabeledEnergyRating = Double.parseDouble(mTextLabeledEnergyRating.getText().toString());
            Double newIntegratedModifiedEnergyFactor = Double.parseDouble(mTextIntegratedModifiedEnergyFactor.getText().toString());

            mCheckBoxAvailable.clearFocus();
            mSpinnerDefaultsType.clearFocus();
            mSpinnerLoadType.clearFocus();
            mTextLabeledEnergyRating.clearFocus();
            mTextIntegratedModifiedEnergyFactor.clearFocus();

            if ((boolean)mCheckBoxAvailable.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesWasher", "N/A", "Available",
                        Boolean.toString(mClothesWasher.available), Boolean.toString(newAvailable));
                mClothesWasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerDefaultsType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesWasher", "N/A", "Defaults Type",
                        mClothesWasher.defaults_type, newDefaultsType);
                mClothesWasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerLoadType.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesWasher", "N/A", "Load Type",
                        mClothesWasher.load_type, newLoadType);
                mClothesWasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextLabeledEnergyRating.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesWasher", "N/A", "Labeled Energy Rating",
                        Double.toString(mClothesWasher.labeled_energy_rating), Double.toString(newLabeledEnergyRating));
                mClothesWasherViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextIntegratedModifiedEnergyFactor.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "ClothesWasher", "N/A", "Integrated Modified Energy Factor",
                        Double.toString(mClothesWasher.integrated_modified_energy_factor), Double.toString(newIntegratedModifiedEnergyFactor));
                mClothesWasherViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_ClothesWasher_Table newClothesWasher = new Ekotrope_ClothesWasher_Table(mPlanId,
                    newAvailable, newDefaultsType, newLoadType, newLabeledEnergyRating, newIntegratedModifiedEnergyFactor, true);
            mClothesWasherViewModel.updateClothesWasher(newClothesWasher);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mCheckBoxAvailable.setChecked(mClothesWasher.available);
        mTextLabeledEnergyRating.setText(String.format(mClothesWasher.labeled_energy_rating.toString()));
        mTextIntegratedModifiedEnergyFactor.setText(String.format(mClothesWasher.integrated_modified_energy_factor.toString()));

        ArrayAdapter<String> defaultsTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDefaultsTypes);
        defaultsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDefaultsType.setAdapter(defaultsTypeAdapter);
        mSpinnerDefaultsType.setSelection(mDefaultsTypes.indexOf(mClothesWasher.defaults_type));

        ArrayAdapter<String> loadTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLoadTypes);
        loadTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLoadType.setAdapter(loadTypeAdapter);
        mSpinnerLoadType.setSelection(mLoadTypes.indexOf(mClothesWasher.load_type));
    }

    private void initializeTextValidators() {
        mTextLabeledEnergyRating.addTextChangedListener(new TextWatcher() {
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
                        mTextLabeledEnergyRating.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextLabeledEnergyRating.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextIntegratedModifiedEnergyFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextIntegratedModifiedEnergyFactor.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextIntegratedModifiedEnergyFactor.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mCheckBoxAvailable, mClothesWasher.available);
        BridgeHelper.setChangeTracker(mSpinnerDefaultsType, mClothesWasher.defaults_type);
        BridgeHelper.setChangeTracker(mSpinnerLoadType, mClothesWasher.load_type);
        BridgeHelper.setChangeTracker(mTextLabeledEnergyRating, mClothesWasher.labeled_energy_rating);
        BridgeHelper.setChangeTracker(mTextIntegratedModifiedEnergyFactor, mClothesWasher.integrated_modified_energy_factor);
    }
}