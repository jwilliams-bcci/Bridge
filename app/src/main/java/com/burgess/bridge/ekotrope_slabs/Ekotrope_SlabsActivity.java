package com.burgess.bridge.ekotrope_slabs;

import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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

import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String SLAB_INDEX = "com.burgess.bridge.SLAB_INDEX";
    public static final int SLAB_INDEX_NOT_FOUND = -1;

    private Ekotrope_SlabsViewModel mSlabsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextName;
    private EditText mTextUnderSlabInsulationR;
    private CheckBox mTextFullyInsulated;
    private EditText mTextUnderSlabInsulationWidth;
    private EditText mTextPerimeterInsulationDepth;
    private EditText mTextPerimeterInsulationR;
    private CheckBox mTextThermalBreak;
    private Button mButtonSave;

    private String mPlanId;
    private int mSlabIndex;
    private Ekotrope_Slab_Table mSlab;

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
        mPlanId = intent.getStringExtra(PLAN_ID);
        mSlabIndex = intent.getIntExtra(SLAB_INDEX, SLAB_INDEX_NOT_FOUND);
        mSlab = mSlabsViewModel.getSlab(mPlanId, mSlabIndex);

        intializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void intializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_slabs_constraint_layout);
        mTextName = findViewById(R.id.ekotrope_slabs_text_name);
        mTextUnderSlabInsulationR = findViewById(R.id.ekotrope_slabs_text_underslab_insulation_r);
        mTextFullyInsulated = findViewById(R.id.ekotrope_slabs_checkbox_fully_insulated);
        mTextUnderSlabInsulationWidth = findViewById(R.id.ekotrope_slabs_text_underslab_insulation_width);
        mTextPerimeterInsulationDepth = findViewById(R.id.ekotrope_slabs_text_perimeter_insulation_depth);
        mTextPerimeterInsulationR = findViewById(R.id.ekotrope_slabs_text_perimeter_insulation_r);
        mTextThermalBreak = findViewById(R.id.ekotrope_slabs_checkbox_thermal_break);
        mButtonSave = findViewById(R.id.ekotrope_slabs_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            double newUnderSlabInsulationR = Double.parseDouble(mTextUnderSlabInsulationR.getText().toString());
            double newUnderSlabInsulationWidth = Double.parseDouble(mTextUnderSlabInsulationWidth.getText().toString());
            double newPerimeterInsulationDepth = Double.parseDouble(mTextPerimeterInsulationDepth.getText().toString());
            double newPerimeterInsulationR = Double.parseDouble(mTextPerimeterInsulationR.getText().toString());
            boolean newFullyInsulated = mTextFullyInsulated.isChecked();
            boolean newThermalBreak = mTextThermalBreak.isChecked();

            Ekotrope_Slab_Table newSlab = new Ekotrope_Slab_Table(mPlanId, mSlabIndex,
                    mTextName.getText().toString(), mSlab.typeName, newUnderSlabInsulationR,
                    newFullyInsulated, newUnderSlabInsulationWidth, newPerimeterInsulationDepth,
                    newPerimeterInsulationR, newThermalBreak);

            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mSlabsViewModel.updateSlab(newSlab);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextName.setText(mSlab.name);
        mTextUnderSlabInsulationR.setText(Double.toString(mSlab.underslabInsulationR));
        mTextUnderSlabInsulationWidth.setText(Double.toString(mSlab.underslabInsulationWidth));
        mTextPerimeterInsulationDepth.setText(Double.toString(mSlab.perimeterInsulationDepth));
        mTextPerimeterInsulationR.setText(Double.toString(mSlab.perimeterInsulationR));
        mTextFullyInsulated.setChecked(mSlab.isFullyInsulated);
        mTextThermalBreak.setChecked(mSlab.thermalBreak);
    }
}