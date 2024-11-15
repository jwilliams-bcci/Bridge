package com.burgess.bridge.ekotrope_abovegradewalls;

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
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_ChangeLog_Table;

public class Ekotrope_AboveGradeWallsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String ABOVE_GRADE_WALLS_INDEX = "com.burgess.bridge.ABOVE_GRADE_WALLS_INDEX";
    public static final int ABOVE_GRADE_WALLS_INDEX_NOT_FOUND = -1;

    private Ekotrope_AboveGradeWallsViewModel mAboveGradeWallsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private TextView mTextName;
    private EditText mTextCavityInsulationR;
    private EditText mTextContinuousInsulationR;
    private EditText mTextStudSpacing;
    private EditText mTextStudWidth;
    private EditText mTextStudDepth;
    private Spinner mSpinnerCavityInsulationGrade;
    private Spinner mSpinnerStudMaterial;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mAboveGradeWallIndex;
    private Ekotrope_AboveGradeWall_Table mAboveGradeWall;
    List<String> mInsulationGrades;
    List<String> mStudMaterials;
    private boolean valid = true;

    public static final String TAG = "ABOVE_GRADE_WALLS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_above_grade_walls);
        setSupportActionBar(findViewById(R.id.above_grade_walls_toolbar));
        mAboveGradeWallsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_AboveGradeWallsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);
        mAboveGradeWallIndex = intent.getIntExtra(ABOVE_GRADE_WALLS_INDEX, ABOVE_GRADE_WALLS_INDEX_NOT_FOUND);
        mAboveGradeWall = mAboveGradeWallsViewModel.getAboveGradeWall(mPlanId, mAboveGradeWallIndex);

        // Set spinner lists...
        mInsulationGrades = new ArrayList<>();
        mStudMaterials = new ArrayList<>();
        mInsulationGrades.add("I");
        mInsulationGrades.add("II");
        mInsulationGrades.add("III");
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
        mConstraintLayout = findViewById(R.id.above_grade_walls_constraint_layout);
        mToolbar = findViewById(R.id.above_grade_walls_toolbar);
        mTextName = findViewById(R.id.above_grade_walls_text_name);
        mTextCavityInsulationR = findViewById(R.id.above_grade_walls_text_cavity_insulation_r);
        mTextContinuousInsulationR = findViewById(R.id.above_grade_walls_text_continuous_insulation_r);
        mTextStudSpacing = findViewById(R.id.above_grade_walls_text_stud_spacing);
        mTextStudWidth = findViewById(R.id.above_grade_walls_text_stud_width);
        mTextStudDepth = findViewById(R.id.above_grade_walls_text_stud_depth);
        mSpinnerCavityInsulationGrade = findViewById(R.id.above_grade_walls_spinner_cavity_insulation_grade);
        mSpinnerStudMaterial = findViewById(R.id.above_grade_walls_spinner_stud_material);
        mButtonSave = findViewById(R.id.above_grade_walls_button_save);
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

            mSpinnerCavityInsulationGrade.clearFocus();
            mTextCavityInsulationR.clearFocus();
            mTextContinuousInsulationR.clearFocus();
            mTextStudSpacing.clearFocus();
            mTextStudWidth.clearFocus();
            mTextStudDepth.clearFocus();
            mSpinnerStudMaterial.clearFocus();

            if ((boolean)mSpinnerCavityInsulationGrade.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Insulation Grade", mAboveGradeWall.cavityInsulationGrade,
                        newCavityInsulationGrade);
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextCavityInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Cavity Insulation R", Double.toString(mAboveGradeWall.cavityInsulationR),
                        Double.toString(newCavityInsulationR));
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextContinuousInsulationR.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Continuous Insulation R", Double.toString(mAboveGradeWall.continuousInsulationR),
                        Double.toString(newContinuousInsulationR));
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextStudSpacing.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Stud Spacing", Double.toString(mAboveGradeWall.studSpacing),
                        Double.toString(newStudSpacing));
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextStudWidth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Stud Width", Double.toString(mAboveGradeWall.studWidth),
                        Double.toString(newStudWidth));
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextStudDepth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Stud Depth", Double.toString(mAboveGradeWall.studDepth),
                        Double.toString(newStudDepth));
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mSpinnerStudMaterial.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Above Grade Wall", mAboveGradeWall.name,
                        "Stud Material", mAboveGradeWall.studMaterial, newStudMaterial);
                mAboveGradeWallsViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_AboveGradeWall_Table newAboveGradeWall = new Ekotrope_AboveGradeWall_Table(mPlanId,
                    mAboveGradeWallIndex, mTextName.getText().toString(), mAboveGradeWall.typeName,
                    newCavityInsulationGrade, newCavityInsulationR, newContinuousInsulationR,
                    newStudSpacing, newStudWidth, newStudDepth, newStudMaterial, true);
            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mAboveGradeWallsViewModel.updateAboveGradeWall(newAboveGradeWall);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Above Grade Wall - %s", mAboveGradeWall.name));
        mTextName.setText(mAboveGradeWall.typeName);
        mTextCavityInsulationR.setText(String.format(mAboveGradeWall.cavityInsulationR.toString()));
        mTextContinuousInsulationR.setText(String.format(mAboveGradeWall.continuousInsulationR.toString()));
        mTextStudSpacing.setText(String.format(mAboveGradeWall.studSpacing.toString()));
        mTextStudWidth.setText(String.format(mAboveGradeWall.studWidth.toString()));
        mTextStudDepth.setText(String.format(mAboveGradeWall.studDepth.toString()));

        ArrayAdapter<String> insulationGradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mInsulationGrades);
        insulationGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCavityInsulationGrade.setAdapter(insulationGradeAdapter);
        mSpinnerCavityInsulationGrade.setSelection(mInsulationGrades.indexOf(mAboveGradeWall.cavityInsulationGrade));

        ArrayAdapter<String> studMaterialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStudMaterials);
        studMaterialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStudMaterial.setAdapter(studMaterialAdapter);
        mSpinnerStudMaterial.setSelection(mStudMaterials.indexOf(mAboveGradeWall.studMaterial));
    }

    private void initializeTextValidators() {
        mTextCavityInsulationR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextCavityInsulationR.getText().toString()) < 0) {
                        mTextCavityInsulationR.setError("Cannot be less than 0");
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
                    if (Double.parseDouble(mTextCavityInsulationR.getText().toString()) < 0) {
                        mTextCavityInsulationR.setError("Cannot be less than 0");
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
                        mTextStudSpacing.setError("Must be greater or equal to 7");
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
        BridgeHelper.setChangeTracker(mSpinnerCavityInsulationGrade, mAboveGradeWall.cavityInsulationGrade);
        BridgeHelper.setChangeTracker(mTextCavityInsulationR, mAboveGradeWall.cavityInsulationR);
        BridgeHelper.setChangeTracker(mTextContinuousInsulationR, mAboveGradeWall.continuousInsulationR);
        BridgeHelper.setChangeTracker(mTextStudSpacing, mAboveGradeWall.studSpacing);
        BridgeHelper.setChangeTracker(mTextStudWidth, mAboveGradeWall.studWidth);
        BridgeHelper.setChangeTracker(mTextStudDepth, mAboveGradeWall.studDepth);
        BridgeHelper.setChangeTracker(mSpinnerStudMaterial, mAboveGradeWall.studMaterial);
    }
}