package com.burgess.bridge.ekotrope_lighting;

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

import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Lighting_Table;

public class Ekotrope_LightingActivity extends AppCompatActivity {
    private Ekotrope_LightingViewModel mLightingViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private EditText mTextInteriorF;
    private EditText mTextInteriorL;
    private EditText mTextExteriorF;
    private EditText mTextExteriorL;
    private EditText mTextGarageF;
    private EditText mTextGarageL;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_Lighting_Table mLighting;
    private boolean valid = true;

    public static final String TAG = "LIGHTING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_lighting);
        setSupportActionBar(findViewById(R.id.lighting_toolbar));
        mLightingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_LightingViewModel.class);

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
        mLighting = mLightingViewModel.getLighting(mPlanId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.lighting_constraint_layout);
        mToolbar = findViewById(R.id.lighting_toolbar);
        mTextInteriorF = findViewById(R.id.lighting_text_interior_f);
        mTextInteriorL = findViewById(R.id.lighting_text_interior_l);
        mTextExteriorF = findViewById(R.id.lighting_text_exterior_f);
        mTextExteriorL = findViewById(R.id.lighting_text_exterior_l);
        mTextGarageF = findViewById(R.id.lighting_text_garage_f);
        mTextGarageL = findViewById(R.id.lighting_text_garage_l);
        mButtonSave = findViewById(R.id.lighting_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            Double newInteriorF = Double.parseDouble(mTextInteriorF.getText().toString());
            Double newInteriorL = Double.parseDouble(mTextInteriorL.getText().toString());
            Double newExteriorF = Double.parseDouble(mTextExteriorF.getText().toString());
            Double newExteriorL = Double.parseDouble(mTextExteriorL.getText().toString());
            Double newGarageF = Double.parseDouble(mTextGarageF.getText().toString());
            Double newGarageL = Double.parseDouble(mTextGarageL.getText().toString());

            mTextInteriorF.clearFocus();
            mTextInteriorL.clearFocus();
            mTextExteriorF.clearFocus();
            mTextExteriorL.clearFocus();
            mTextGarageF.clearFocus();
            mTextGarageL.clearFocus();

            if ((boolean)mTextInteriorF.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A",
                        "Interior Fluorescent", Double.toString(mLighting.percent_interior_fluorescent),
                        Double.toString(newInteriorF));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextInteriorL.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A",
                        "Interior LED", Double.toString(mLighting.percent_interior_led),
                        Double.toString(newInteriorL));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextExteriorF.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A",
                        "Exterior Fluorescent", Double.toString(mLighting.percent_exterior_fluorescent),
                        Double.toString(newExteriorF));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextExteriorL.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A", "Exterior LED",
                        Double.toString(mLighting.percent_exterior_led),
                        Double.toString(newExteriorL));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextGarageF.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A",
                        "Garage Fluorescent", Double.toString(mLighting.percent_garage_fluorescent),
                        Double.toString(newGarageF));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }
            if ((boolean)mTextGarageL.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Lighting", "N/A", "Garage LED",
                        Double.toString(mLighting.percent_garage_led), Double.toString(newGarageL));
                mLightingViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Lighting_Table newLighting = new Ekotrope_Lighting_Table(mPlanId, newInteriorF, newInteriorL,
                    newExteriorF, newExteriorL, newGarageF, newGarageL, true);
            mLightingViewModel.updateLighting(newLighting);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextInteriorF.setText(String.format(mLighting.percent_interior_fluorescent.toString()));
        mTextInteriorL.setText(String.format(mLighting.percent_interior_led.toString()));
        mTextExteriorF.setText(String.format(mLighting.percent_exterior_fluorescent.toString()));
        mTextExteriorL.setText(String.format(mLighting.percent_exterior_led.toString()));
        mTextGarageF.setText(String.format(mLighting.percent_garage_fluorescent.toString()));
        mTextGarageL.setText(String.format(mLighting.percent_garage_led.toString()));
    }

    private void initializeTextValidators() {
        mTextInteriorF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextInteriorF.setError("Value must be between 0 and 100");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextInteriorF.setError("Please enter a number");
                    valid = false;
                }
            }
        });
        mTextInteriorL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextInteriorL.setError("Value must be between 0 and 100");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextInteriorL.setError("Please enter a number");
                }
            }
        });
        mTextExteriorF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextExteriorF.setError("Value must be between 0 and 100");
                        valid = false;
                        } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextExteriorF.setError("Please enter a number");
                }
            }
        });
        mTextExteriorL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextExteriorL.setError("Value must be between 0 and 100");
                        valid = false;
                        } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextExteriorL.setError("Please enter a number");
                }
            }
        });
        mTextGarageF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextGarageF.setError("Value must be between 0 and 100");
                        valid = false;
                        } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextGarageF.setError("Please enter a number");
                }
            }
        });
        mTextGarageL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    Double value = Double.parseDouble(editable.toString());
                    if (value < 0 || value > 100) {
                        mTextGarageL.setError("Value must be between 0 and 100");
                        valid = false;
                        } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextGarageL.setError("Please enter a number");
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mTextInteriorF, mLighting.percent_interior_fluorescent);
        BridgeHelper.setChangeTracker(mTextInteriorL, mLighting.percent_interior_led);
        BridgeHelper.setChangeTracker(mTextExteriorF, mLighting.percent_exterior_fluorescent);
        BridgeHelper.setChangeTracker(mTextExteriorL, mLighting.percent_exterior_led);
        BridgeHelper.setChangeTracker(mTextGarageF, mLighting.percent_garage_fluorescent);
        BridgeHelper.setChangeTracker(mTextGarageL, mLighting.percent_garage_led);
    }
}