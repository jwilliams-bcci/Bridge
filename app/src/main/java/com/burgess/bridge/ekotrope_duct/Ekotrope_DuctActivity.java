package com.burgess.bridge.ekotrope_duct;

import static com.burgess.bridge.Constants.DISTRIBUTION_SYSTEM_INDEX;
import static com.burgess.bridge.Constants.DISTRIBUTION_SYSTEM_INDEX_NOT_FOUND;
import static com.burgess.bridge.Constants.DUCT_INDEX;
import static com.burgess.bridge.Constants.DUCT_INDEX_NOT_FOUND;
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
import data.Tables.Ekotrope_Duct_Table;

public class Ekotrope_DuctActivity extends AppCompatActivity {
    private Ekotrope_DuctViewModel mDuctViewModel;
    private ConstraintLayout mConstraintLayout;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Spinner mSpinnerLocation;
    private EditText mTextSupplyArea;
    private EditText mTextReturnArea;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mDsId;
    private int mDuctIndex;
    private Ekotrope_Duct_Table mDuct;
    List<String> mLocations;
    private boolean valid = true;

    public static final String TAG = "DUCT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_duct);
        setSupportActionBar(findViewById(R.id.duct_toolbar));
        mDuctViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DuctViewModel.class);

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
        mDsId = intent.getIntExtra(DISTRIBUTION_SYSTEM_INDEX, DISTRIBUTION_SYSTEM_INDEX_NOT_FOUND);
        mDuctIndex = intent.getIntExtra(DUCT_INDEX, DUCT_INDEX_NOT_FOUND);
        mDuct = mDuctViewModel.getDuct(mPlanId, mDsId, mDuctIndex);

        // Set spinner lists...
        mLocations = new ArrayList<>();
        mLocations.add("ConditionedSpace");
        mLocations.add("ConditionedAttic");
        mLocations.add("AtticWellVented");
        mLocations.add("AtticPoorlyVented");
        mLocations.add("AtticWellVentedRadiantBarrier");
        mLocations.add("AtticPoorlyVentedRadiantBarrier");
        mLocations.add("AtticUnderInsulationWellVented");
        mLocations.add("AtticUnderInsulationPoorlyVented");
        mLocations.add("AtticUnderInsulationWellVentedRadiantBarrier");
        mLocations.add("AtticUnderInsulationPoorlyVentedRadiantBarrier");
        mLocations.add("Garage");
        mLocations.add("CrawlspaceUnventedUninsulated");
        mLocations.add("CrawlspaceUnventedInsulated");
        mLocations.add("CrawlspaceUnventedInsulatedFloorOnly");
        mLocations.add("CrawlspaceVentedUninsulated");
        mLocations.add("CrawlspaceVentedInsulated");
        mLocations.add("CrawlspaceVentedInsulatedFloorOnly");
        mLocations.add("BasementUninsulated");
        mLocations.add("BasementInsulatedWalls");
        mLocations.add("BasementInsulatedBasementCeiling");
        mLocations.add("Underslab");
        mLocations.add("ExteriorWalls");
        mLocations.add("ExposedExterior");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.duct_constraint_layout);
        mToolbar = findViewById(R.id.duct_toolbar);
        mSpinnerLocation = findViewById(R.id.duct_spinner_location);
        mTextSupplyArea = findViewById(R.id.duct_text_supply_area);
        mTextReturnArea = findViewById(R.id.duct_text_return_area);
        mButtonSave = findViewById(R.id.duct_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors before saving.", Snackbar.LENGTH_LONG).show();
                return;
            }
            String newLocation = mSpinnerLocation.getSelectedItem().toString();
            Double newSupplyArea = Double.parseDouble(mTextSupplyArea.getText().toString());
            Double newReturnArea = Double.parseDouble(mTextReturnArea.getText().toString());

            mSpinnerLocation.clearFocus();
            mTextSupplyArea.clearFocus();
            mTextReturnArea.clearFocus();

            if ((boolean)mSpinnerLocation.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Duct", mDuct.location,
                        "Location", mDuct.location, newLocation);
                mDuctViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextSupplyArea.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Duct", mDuct.location,
                        "Supply Area", mDuct.percent_supply_area.toString(), newSupplyArea.toString());
                mDuctViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextReturnArea.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Duct", mDuct.location,
                        "Return Area", mDuct.percent_return_area.toString(), newReturnArea.toString());
                mDuctViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Duct_Table newDuct = new Ekotrope_Duct_Table(mPlanId, mDsId, mDuctIndex,
                    newLocation, newSupplyArea, newReturnArea, true);
            mDuctViewModel.updateDuct(newDuct);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Duct - %s", mDuct.location));
        mTextSupplyArea.setText(String.format(mDuct.percent_supply_area.toString()));
        mTextReturnArea.setText(String.format(mDuct.percent_return_area.toString()));

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLocation.setAdapter(locationAdapter);
        mSpinnerLocation.setSelection(mLocations.indexOf(mDuct.location));
    }

    private void initializeTextValidators() {
        mTextSupplyArea.addTextChangedListener(new TextWatcher() {
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
                            mTextSupplyArea.setError("Value must be between 0 and 100");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextSupplyArea.setError("Value must be a number");
                    valid = false;
                }
            }
        });
        mTextReturnArea.addTextChangedListener(new TextWatcher() {
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
                            mTextReturnArea.setError("Value must be between 0 and 100");
                            valid = false;
                        } else {
                            valid = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    mTextReturnArea.setError("Value must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerLocation, mDuct.location);
        BridgeHelper.setChangeTracker(mTextSupplyArea, mDuct.percent_supply_area);
        BridgeHelper.setChangeTracker(mTextReturnArea, mDuct.percent_return_area);
    }
}