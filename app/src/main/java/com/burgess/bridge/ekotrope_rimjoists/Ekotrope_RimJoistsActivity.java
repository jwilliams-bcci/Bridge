package com.burgess.bridge.ekotrope_rimjoists;

import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import data.Tables.Ekotrope_RimJoist_Table;

public class Ekotrope_RimJoistsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String RIM_JOIST_INDEX = "com.burgess.bridge.RIM_JOIST_INDEX";
    public static final int RIM_JOIST_INDEX_NOT_FOUND = -1;

    private Ekotrope_RimJoistsViewModel mRimJoistsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextName;
    private EditText mTextBetweenInteriorAnd;
    private EditText mTextSurfaceArea;
    private EditText mTextUFactor;
    private EditText mTextRFactor;

    private String mPlanId;
    private int mRimJoistIndex;
    private Ekotrope_RimJoist_Table mRimJoist;

    public static final String TAG = "RIM_JOISTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_rim_joists);
        setSupportActionBar(findViewById(R.id.ekotrope_rim_joists_toolbar));
        mRimJoistsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_RimJoistsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);
        mRimJoistIndex = intent.getIntExtra(RIM_JOIST_INDEX, RIM_JOIST_INDEX_NOT_FOUND);
        mRimJoist = mRimJoistsViewModel.getRimJoist(mPlanId, mRimJoistIndex);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_rim_joists_constraint_layout);
        mTextName = findViewById(R.id.ekotrope_rim_joists_text_name);
        mTextBetweenInteriorAnd = findViewById(R.id.ekotrope_rim_joists_text_between_interior_and);
        mTextSurfaceArea = findViewById(R.id.ekotrope_rim_joists_text_surface_area);
        mTextUFactor = findViewById(R.id.ekotrope_rim_joists_text_u_factor);
        mTextRFactor = findViewById(R.id.ekotrope_rim_joists_text_r_factor);
    }

    private void initializeDisplayContent() {
        mTextName.setText(mRimJoist.name);
        mTextBetweenInteriorAnd.setText(mRimJoist.betweenInteriorAnd);
        mTextSurfaceArea.setText(Double.toString(mRimJoist.surfaceArea));
        mTextUFactor.setText(Double.toString(mRimJoist.uFactor));
        mTextRFactor.setText(Double.toString(mRimJoist.rFactor));
    }
}