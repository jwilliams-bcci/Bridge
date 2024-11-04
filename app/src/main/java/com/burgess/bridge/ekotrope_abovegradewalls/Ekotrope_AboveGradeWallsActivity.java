package com.burgess.bridge.ekotrope_abovegradewalls;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_AboveGradeWall_Table;

public class Ekotrope_AboveGradeWallsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String ABOVE_GRADE_WALLS_INDEX = "com.burgess.bridge.ABOVE_GRADE_WALLS_INDEX";
    public static final int ABOVE_GRADE_WALLS_INDEX_NOT_FOUND = -1;

    private Ekotrope_AboveGradeWallsViewModel mAboveGradeWallsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextIndex;
    private EditText mTextName;
    private EditText mTextCavityInsulationR;
    private EditText mTextContinuousInsulationR;
    private EditText mTextStudSpacing;
    private EditText mTextStudWidth;
    private EditText mTextStudDepth;
    private Spinner mSpinnerCavityInsulationGrade;
    private Spinner mSpinnerStudMaterial;
    private Button mButtonSave;

    private String mPlanId;
    private int mAboveGradeWallIndex;
    private Ekotrope_AboveGradeWall_Table mAboveGradeWall;
    ArrayAdapter<String> mInsulationGradeAdapter;
    ArrayAdapter<String> mStudMaterialAdapter;
    List<String> mInsulationGrades;
    List<String> mStudMaterials;
    private boolean valid = true;

    public static final String TAG = "ABOVE_GRADE_WALLS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_above_grade_walls);
        setSupportActionBar(findViewById(R.id.above_grade_walls_toolbar));
        mAboveGradeWallsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_AboveGradeWallsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);
        mAboveGradeWallIndex = intent.getIntExtra(ABOVE_GRADE_WALLS_INDEX, ABOVE_GRADE_WALLS_INDEX_NOT_FOUND);
        mAboveGradeWall = mAboveGradeWallsViewModel.getAboveGradeWall(mPlanId, mAboveGradeWallIndex);

        // Set spinner lists...
        mInsulationGrades = new ArrayList<String>();
        mStudMaterials = new ArrayList<String>();
        mInsulationGrades.add("I");
        mInsulationGrades.add("II");
        mInsulationGrades.add("III");
        mStudMaterials.add("Wood");
        mStudMaterials.add("Steel");
        mStudMaterials.add("Concrete");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        intializeTextValidators();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.above_grade_walls_constraint_layout);
        mTextIndex = findViewById(R.id.above_grade_walls_text_index);
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

            Ekotrope_AboveGradeWall_Table newAboveGradeWall = new Ekotrope_AboveGradeWall_Table(mPlanId,
                    mAboveGradeWallIndex, mTextName.getText().toString(), mAboveGradeWall.typeName,
                    newCavityInsulationGrade, newCavityInsulationR, newContinuousInsulationR,
                    newStudSpacing, newStudWidth, newStudDepth, newStudMaterial);
            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mAboveGradeWallsViewModel.updateAboveGradeWall(newAboveGradeWall);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextIndex.setText(Integer.toString(mAboveGradeWall.index));
        mTextName.setText(mAboveGradeWall.name);
        mTextCavityInsulationR.setText(Double.toString(mAboveGradeWall.cavityInsulationR));
        mTextContinuousInsulationR.setText(Double.toString(mAboveGradeWall.continuousInsulationR));
        mTextStudSpacing.setText(Double.toString(mAboveGradeWall.studSpacing));
        mTextStudWidth.setText(Double.toString(mAboveGradeWall.studWidth));
        mTextStudDepth.setText(Double.toString(mAboveGradeWall.studDepth));

        mInsulationGradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mInsulationGrades);
        mInsulationGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCavityInsulationGrade.setAdapter(mInsulationGradeAdapter);
        mSpinnerCavityInsulationGrade.setSelection(mInsulationGrades.indexOf(mAboveGradeWall.cavityInsulationGrade));

        mStudMaterialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStudMaterials);
        mStudMaterialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStudMaterial.setAdapter(mStudMaterialAdapter);
        mSpinnerStudMaterial.setSelection(mStudMaterials.indexOf(mAboveGradeWall.studMaterial));
    }

    private void intializeTextValidators() {
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
}