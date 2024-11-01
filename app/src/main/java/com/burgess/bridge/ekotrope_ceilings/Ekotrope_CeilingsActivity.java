package com.burgess.bridge.ekotrope_ceilings;

import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import data.Tables.Ekotrope_Ceiling_Table;

public class Ekotrope_CeilingsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String CEILING_INDEX = "com.burgess.bridge.CEILING_INDEX";
    public static final int CEILING_INDEX_NOT_FOUND = -1;

    private Ekotrope_CeilingsViewModel mCeilingsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextName;
    private Spinner mSpinnerCavityInsulationGrade;
    private EditText mTextCavityInsulationR;
    private EditText mTextContinuousInsulationR;
    private EditText mTextStudSpacing;
    private EditText mTextStudWidth;
    private EditText mTextStudDepth;
    private Spinner mSpinnerStudMaterial;
    private CheckBox mCheckBoxRadiantBarrier;
    private Button mButtonSave;

    private String mPlanId;
    private int mCeilingIndex;
    private Ekotrope_Ceiling_Table mCeiling;
    private ArrayAdapter<String> mCavityInsulationGradeAdapter;
    private ArrayAdapter<String> mStudMaterialAdapter;
    private List<String> mCavityInsulationGrades;
    private List<String> mStudMaterials;

    public static final String TAG = "CEILINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_ceilings);
        setSupportActionBar(findViewById(R.id.ekotrope_ceilings_toolbar));
        mCeilingsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_CeilingsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);
        mCeilingIndex = intent.getIntExtra(CEILING_INDEX, CEILING_INDEX_NOT_FOUND);
        mCeiling = mCeilingsViewModel.getCeiling(mPlanId, mCeilingIndex);

        // Set spinner lists...
        mCavityInsulationGrades = new ArrayList<String>();
        mCavityInsulationGrades.add("I");
        mCavityInsulationGrades.add("II");
        mCavityInsulationGrades.add("III");

        mStudMaterials = new ArrayList<String>();
        mStudMaterials.add("Wood");
        mStudMaterials.add("Steel");
        mStudMaterials.add("Concrete");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_ceilings_constraint_layout);
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
            String newCavityInsulationGrade = mSpinnerCavityInsulationGrade.getSelectedItem().toString();
            double newCavityInsulationR = Double.parseDouble(mTextCavityInsulationR.getText().toString());
            double newContinuousInsulationR = Double.parseDouble(mTextContinuousInsulationR.getText().toString());
            double newStudSpacing = Double.parseDouble(mTextStudSpacing.getText().toString());
            double newStudWidth = Double.parseDouble(mTextStudWidth.getText().toString());
            double newStudDepth = Double.parseDouble(mTextStudDepth.getText().toString());
            String newStudMaterial = mSpinnerStudMaterial.getSelectedItem().toString();
            boolean newRadiantBarrier = mCheckBoxRadiantBarrier.isChecked();

            Ekotrope_Ceiling_Table newCeiling = new Ekotrope_Ceiling_Table(mPlanId, mCeilingIndex,
                    mTextName.getText().toString(), mCeiling.typeName, newCavityInsulationGrade,
                    newCavityInsulationR, newContinuousInsulationR, newStudSpacing, newStudWidth,
                    newStudDepth, newStudMaterial, newRadiantBarrier);

            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mCeilingsViewModel.updateCeiling(newCeiling);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextName.setText(mCeiling.name);
        mTextCavityInsulationR.setText(Double.toString(mCeiling.cavityInsulationR));
        mTextContinuousInsulationR.setText(Double.toString(mCeiling.continuousInsulationR));
        mTextStudSpacing.setText(Double.toString(mCeiling.studSpacing));
        mTextStudWidth.setText(Double.toString(mCeiling.studWidth));
        mTextStudDepth.setText(Double.toString(mCeiling.studDepth));
        mCheckBoxRadiantBarrier.setChecked(mCeiling.hasRadiantBarrier);

        mCavityInsulationGradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCavityInsulationGrades);
        mCavityInsulationGradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCavityInsulationGrade.setAdapter(mCavityInsulationGradeAdapter);

        mStudMaterialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStudMaterials);
        mStudMaterialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStudMaterial.setAdapter(mStudMaterialAdapter);
    }
}