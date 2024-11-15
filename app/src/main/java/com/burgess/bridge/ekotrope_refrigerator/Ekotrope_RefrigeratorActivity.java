package com.burgess.bridge.ekotrope_refrigerator;

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
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.R;
import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.google.android.material.snackbar.Snackbar;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Refrigerator_Table;

public class Ekotrope_RefrigeratorActivity extends AppCompatActivity {
    private Ekotrope_RefrigeratorViewModel mRefrigeratorViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private EditText mTextConsumption;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_Refrigerator_Table mRefrigerator;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_refrigerator);
        setSupportActionBar(findViewById(R.id.refrigerator_toolbar));
        mRefrigeratorViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_RefrigeratorViewModel.class);

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
        mRefrigerator = mRefrigeratorViewModel.getRefrigerator(mPlanId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.refrigerator_constraint_layout);
        mToolbar = findViewById(R.id.refrigerator_toolbar);
        mTextConsumption = findViewById(R.id.refrigerator_text_refrigerator_consumption);
        mButtonSave = findViewById(R.id.refrigerator_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            Double newConsumption = Double.parseDouble(mTextConsumption.getText().toString());

            mTextConsumption.clearFocus();

            if ((boolean)mTextConsumption.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Refrigerator", "N/A",
                        "Refrigerator Consumption", Double.toString(mRefrigerator.refrigerator_consumption),
                        Double.toString(newConsumption));
                mRefrigeratorViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Refrigerator_Table newRefrigerator = new Ekotrope_Refrigerator_Table(mPlanId,
                    newConsumption, true);
            mRefrigeratorViewModel.updateRefrigerator(newRefrigerator);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextConsumption.setText(String.format(mRefrigerator.refrigerator_consumption.toString()));
    }

    private void initializeTextValidators() {
        mTextConsumption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0) {
                        mTextConsumption.setError("Value must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextConsumption.setError("Please enter a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mTextConsumption, mRefrigerator.refrigerator_consumption);
    }
}