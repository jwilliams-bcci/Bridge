package com.burgess.bridge.ekotrope_ceilings;

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
import android.widget.TextView;

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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_Ceiling_Table;
import data.Tables.Ekotrope_ChangeLog_Table;

public class Ekotrope_CeilingsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String CEILING_INDEX = "com.burgess.bridge.CEILING_INDEX";
    public static final int CEILING_INDEX_NOT_FOUND = -1;

    private Ekotrope_CeilingsViewModel mCeilingsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private TextView mTextName;
    private Spinner mSpinnerCavityInsulationGrade;
    private EditText mTextCavityInsulationR;
    private EditText mTextContinuousInsulationR;
    private EditText mTextStudSpacing;
    private EditText mTextStudWidth;
    private EditText mTextStudDepth;
    private Spinner mSpinnerStudMaterial;
    private CheckBox mCheckBoxRadiantBarrier;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mCeilingIndex;
    private Ekotrope_Ceiling_Table mCeiling;
    private List<String> mCavityInsulationGrades;
    private List<String> mStudMaterials;
    private boolean valid = true;

    public static final String TAG = "CEILINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_ceilings);
        setSupportActionBar(findViewById(R.id.ekotrope_ceilings_toolbar));
        mCeilingsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_CeilingsViewModel.class);

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
        mCeilingIndex = intent.getIntExtra(CEILING_INDEX, CEILING_INDEX_NOT_FOUND);
        mCeiling = mCeilingsViewModel.getCeiling(mPlanId, mCeilingIndex);

        // Set spinner lists...
        mCavityInsulationGrades = new ArrayList<>();
        mCavityInsulationGrades.add("I");
        mCavityInsulationGrades.add("II");
        mCavityInsulationGrades.add("III");

        mStudMaterials = new ArrayList<>();
        mStudMaterials.add("Wood");
        mStudMaterials.add("Steel");
        mStudMaterials.add("Concrete");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_ceilings_constraint_layout);
        mToolbar = findViewById(R.id.ekotrope_ceilings_toolbar);
        mTextName = findViewById(R.id.ekotrope_ceilings_text_name);
        mSpinnerCavityInsulationGrade = findViewById(R.id.ekotrope_ceilings_spinner_cavity_insulation_grade);
        mTextCavityInsulationR = findViewById(R.id.ekotrope_ceilings_text_cavity_insulation_r);
        mTextContinuousInsulationR = findViewById(R.id.ekotrope_ceilings_text_continuous_insulation_r);
        mTextStudSpacing = findViewById(R.id.ekotrope_ceilings_text_stud_spacing);
        mTextStudWidth = findViewById(R.id.ekotrope_ceilings_text_stud_width);
        mTextStudDepth = findViewById(R.id.ekotrope_ceilings_text_stud_depth);
        mSpinnerStudMaterial = findViewById(R.id.ekotrope_ceilings_spinner_stud_material);
        mCheckBoxRadiantBarrier = findViewById(R.id.ekotrope_ceilings_checkbox_radiant_barrier);
        mButtonSave = findViewById(R.id.ekotrope_ceilings_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            String newCavityInsulationGrade = mSpinnerCavityInsulationGrade.getSelectedItem().toString();
            double newCavityInsulationR = Double.parseDouble(mTextCavityInsulationR.getText().toString());
            double newContinuousInsulationR = Double.parseDouble(mTextContinuousInsulationR.getText().toString());
            double newStudSpacing = Double.parseDouble(mTextStudSpacing.getText().toString());
            double newStudWidth = Double.parseDouble(mTextStudWidth.getText().toString());
            double newStudDepth = Double.parseDouble(mTextStudDepth.getText().toString());
            String newStudMaterial = mSpinnerStudMaterial.getSelectedItem().toString();
            boolean newRadiantBarrier = mCheckBoxRadiantBarrier.isChecked();

            mSpinnerCavityInsulationGrade.clearFocus();
            mTextCavityInsulationR.clearFocus();
            mTextContinuousInsulationR.clearFocus();
            mTextStudSpacing.clearFocus();
            mTextStudWidth.clearFocus();
            mTextStudDepth.clearFocus();
            mSpinnerStudMaterial.clearFocus();
            mCheckBoxRadiantBarrier.clearFocus();

            if((boolean)mSpinnerCavityInsulationGrade.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Insulation Grade",
                        mCeiling.cavityInsulationGrade, newCavityInsulationGrade);
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextCavityInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Cavity Insulation R",
                        Double.toString(mCeiling.cavityInsulationR), Double.toString(newCavityInsulationR));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextContinuousInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Continuous Insulation R",
                        Double.toString(mCeiling.continuousInsulationR), Double.toString(newContinuousInsulationR));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextStudSpacing.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Stud Spacing",
                        Double.toString(mCeiling.studSpacing), Double.toString(newStudSpacing));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextStudWidth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Stud Width",
                        Double.toString(mCeiling.studWidth), Double.toString(newStudWidth));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextStudDepth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Stud Depth",
                        Double.toString(mCeiling.studDepth), Double.toString(newStudDepth));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mSpinnerStudMaterial.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Stud Material",
                        mCeiling.studMaterial, newStudMaterial);
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mCheckBoxRadiantBarrier.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Ceiling", mCeiling.name, "Radiant Barrier",
                        Boolean.toString(mCeiling.hasRadiantBarrier), Boolean.toString(newRadiantBarrier));
                mCeilingsViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Ceiling_Table newCeiling = new Ekotrope_Ceiling_Table(mPlanId, mCeilingIndex,
                    mTextName.getText().toString(), mCeiling.typeName, newCavityInsulationGrade,
                    newCavityInsulationR, newContinuousInsulationR, newStudSpacing, newStudWidth,
                    newStudDepth, newStudMaterial, newRadiantBarrier, true);

            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mCeilingsViewModel.updateCeiling(newCeiling);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Ceiling - %s", mCeiling.name));
        mTextName.setText(mCeiling.typeName);
        mTextCavityInsulationR.setText(String.format(mCeiling.cavityInsulationR.toString()));
        mTextContinuousInsulationR.setText(String.format(mCeiling.continuousInsulationR.toString()));
        mTextStudSpacing.setText(String.format(mCeiling.studSpacing.toString()));
        mTextStudWidth.setText(String.format(mCeiling.studWidth.toString()));
        mTextStudDepth.setText(String.format(mCeiling.studDepth.toString()));
        mCheckBoxRadiantBarrier.setChecked(mCeiling.hasRadiantBarrier);

        ArrayAdapter<String> cavityInsulationGradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCavityInsulationGrades);
        cavityInsulationGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCavityInsulationGrade.setAdapter(cavityInsulationGradeAdapter);
        mSpinnerCavityInsulationGrade.setSelection(mCavityInsulationGrades.indexOf(mCeiling.cavityInsulationGrade));

        ArrayAdapter<String> studMaterialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStudMaterials);
        studMaterialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStudMaterial.setAdapter(studMaterialAdapter);
        mSpinnerStudMaterial.setSelection(mStudMaterials.indexOf(mCeiling.studMaterial));
    }

    private void initializeTextValidators() {
        mTextCavityInsulationR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextCavityInsulationR.getText().toString()) < 0) {
                        mTextCavityInsulationR.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextCavityInsulationR.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextContinuousInsulationR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextContinuousInsulationR.getText().toString()) < 0) {
                        mTextContinuousInsulationR.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextContinuousInsulationR.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextStudSpacing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextStudSpacing.getText().toString()) < 7) {
                        mTextStudSpacing.setError("Must be greater than or equal to 7");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextStudSpacing.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextStudWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextStudWidth.getText().toString()) <= 0) {
                        mTextStudWidth.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextStudWidth.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextStudDepth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextStudDepth.getText().toString()) <= 0) {
                        mTextStudDepth.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                        }
                } catch (NumberFormatException e) {
                    mTextStudDepth.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mSpinnerCavityInsulationGrade, mCeiling.cavityInsulationGrade);
        BridgeHelper.setChangeTracker(mTextCavityInsulationR, mCeiling.cavityInsulationR);
        BridgeHelper.setChangeTracker(mTextContinuousInsulationR, mCeiling.continuousInsulationR);
        BridgeHelper.setChangeTracker(mTextStudSpacing, mCeiling.studSpacing);
        BridgeHelper.setChangeTracker(mTextStudWidth, mCeiling.studWidth);
        BridgeHelper.setChangeTracker(mTextStudDepth, mCeiling.studDepth);
        BridgeHelper.setChangeTracker(mSpinnerStudMaterial, mCeiling.studMaterial);
        BridgeHelper.setChangeTracker(mCheckBoxRadiantBarrier, mCeiling.hasRadiantBarrier);
    }
}