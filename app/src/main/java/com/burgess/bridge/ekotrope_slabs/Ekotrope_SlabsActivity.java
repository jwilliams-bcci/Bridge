package com.burgess.bridge.ekotrope_slabs;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String SLAB_INDEX = "com.burgess.bridge.SLAB_INDEX";
    public static final int SLAB_INDEX_NOT_FOUND = -1;

    private Ekotrope_SlabsViewModel mSlabsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private TextView mTextName;
    private EditText mTextUnderSlabInsulationR;
    private CheckBox mCheckBoxFullyInsulated;
    private EditText mTextUnderSlabInsulationWidth;
    private EditText mTextPerimeterInsulationDepth;
    private EditText mTextPerimeterInsulationR;
    private CheckBox mCheckBoxThermalBreak;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mSlabIndex;
    private Ekotrope_Slab_Table mSlab;
    private boolean valid = true;

    public static final String TAG = "SLABS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_slabs);
        setSupportActionBar(findViewById(R.id.ekotrope_slabs_toolbar));
        mSlabsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_SlabsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);
        mSlabIndex = intent.getIntExtra(SLAB_INDEX, SLAB_INDEX_NOT_FOUND);
        mSlab = mSlabsViewModel.getSlab(mPlanId, mSlabIndex);

        intializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void intializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_slabs_constraint_layout);
        mToolbar = findViewById(R.id.ekotrope_slabs_toolbar);
        mTextName = findViewById(R.id.ekotrope_slabs_text_name);
        mTextUnderSlabInsulationR = findViewById(R.id.ekotrope_slabs_text_underslab_insulation_r);
        mCheckBoxFullyInsulated = findViewById(R.id.ekotrope_slabs_checkbox_fully_insulated);
        mTextUnderSlabInsulationWidth = findViewById(R.id.ekotrope_slabs_text_underslab_insulation_width);
        mTextPerimeterInsulationDepth = findViewById(R.id.ekotrope_slabs_text_perimeter_insulation_depth);
        mTextPerimeterInsulationR = findViewById(R.id.ekotrope_slabs_text_perimeter_insulation_r);
        mCheckBoxThermalBreak = findViewById(R.id.ekotrope_slabs_checkbox_thermal_break);
        mButtonSave = findViewById(R.id.ekotrope_slabs_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            double newUnderSlabInsulationR = Double.parseDouble(mTextUnderSlabInsulationR.getText().toString());
            double newUnderSlabInsulationWidth = Double.parseDouble(mTextUnderSlabInsulationWidth.getText().toString());
            double newPerimeterInsulationDepth = Double.parseDouble(mTextPerimeterInsulationDepth.getText().toString());
            double newPerimeterInsulationR = Double.parseDouble(mTextPerimeterInsulationR.getText().toString());
            boolean newFullyInsulated = mCheckBoxFullyInsulated.isChecked();
            boolean newThermalBreak = mCheckBoxThermalBreak.isChecked();

            mTextUnderSlabInsulationR.clearFocus();
            mCheckBoxFullyInsulated.clearFocus();
            mTextUnderSlabInsulationWidth.clearFocus();
            mTextPerimeterInsulationDepth.clearFocus();
            mTextPerimeterInsulationR.clearFocus();
            mCheckBoxThermalBreak.clearFocus();

            if((boolean)mTextUnderSlabInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "UnderSlabInsulationR",
                        Double.toString(mSlab.underslabInsulationR), Double.toString(newUnderSlabInsulationR));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mCheckBoxFullyInsulated.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "FullyInsulated",
                        Boolean.toString(mSlab.isFullyInsulated), Boolean.toString(newFullyInsulated));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextUnderSlabInsulationWidth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "UnderSlabInsulationWidth",
                        Double.toString(mSlab.underslabInsulationWidth), Double.toString(newUnderSlabInsulationWidth));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextPerimeterInsulationDepth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "PerimeterInsulationDepth",
                        Double.toString(mSlab.perimeterInsulationDepth), Double.toString(newPerimeterInsulationDepth));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextPerimeterInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "PerimeterInsulationR",
                        Double.toString(mSlab.perimeterInsulationR), Double.toString(newPerimeterInsulationR));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mCheckBoxThermalBreak.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Slab", mSlab.name, "ThermalBreak",
                        Boolean.toString(mSlab.thermalBreak), Boolean.toString(newThermalBreak));
                mSlabsViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Slab_Table newSlab = new Ekotrope_Slab_Table(mPlanId, mSlabIndex,
                    mTextName.getText().toString(), mSlab.typeName, newUnderSlabInsulationR,
                    newFullyInsulated, newUnderSlabInsulationWidth, newPerimeterInsulationDepth,
                    newPerimeterInsulationR, newThermalBreak, true);
            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mSlabsViewModel.updateSlab(newSlab);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Slab - %s", mSlab.name));
        mTextName.setText(mSlab.typeName);
        mTextUnderSlabInsulationR.setText(String.format(mSlab.underslabInsulationR.toString()));
        mTextUnderSlabInsulationWidth.setText(String.format(mSlab.underslabInsulationWidth.toString()));
        mTextPerimeterInsulationDepth.setText(String.format(mSlab.perimeterInsulationDepth.toString()));
        mTextPerimeterInsulationR.setText(String.format(mSlab.perimeterInsulationR.toString()));
        mCheckBoxFullyInsulated.setChecked(mSlab.isFullyInsulated);
        mCheckBoxThermalBreak.setChecked(mSlab.thermalBreak);
    }

    private void initializeTextValidators() {
        mTextUnderSlabInsulationR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextUnderSlabInsulationR.getText().toString()) < 0) {
                        mTextUnderSlabInsulationR.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextUnderSlabInsulationR.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextUnderSlabInsulationWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextUnderSlabInsulationWidth.getText().toString()) < 0) {
                        mTextUnderSlabInsulationWidth.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextUnderSlabInsulationWidth.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextPerimeterInsulationDepth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextPerimeterInsulationDepth.getText().toString()) < 0) {
                        mTextPerimeterInsulationDepth.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextPerimeterInsulationDepth.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextPerimeterInsulationR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextPerimeterInsulationR.getText().toString()) < 0) {
                        mTextPerimeterInsulationR.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextPerimeterInsulationR.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mTextUnderSlabInsulationR, mSlab.underslabInsulationR);
        BridgeHelper.setChangeTracker(mCheckBoxFullyInsulated, mSlab.isFullyInsulated);
        BridgeHelper.setChangeTracker(mTextUnderSlabInsulationWidth, mSlab.underslabInsulationWidth);
        BridgeHelper.setChangeTracker(mTextPerimeterInsulationDepth, mSlab.perimeterInsulationDepth);
        BridgeHelper.setChangeTracker(mTextPerimeterInsulationR, mSlab.perimeterInsulationR);
        BridgeHelper.setChangeTracker(mCheckBoxThermalBreak, mSlab.thermalBreak);
    }
}