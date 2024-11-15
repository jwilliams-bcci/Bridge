package com.burgess.bridge.ekotrope_windows;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String WINDOW_INDEX = "com.burgess.bridge.WINDOW_INDEX";
    public static final int WINDOW_INDEX_NOT_FOUND = -1;

    private Ekotrope_WindowsViewModel mWindowsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Toolbar mToolbar;
    private TextView mTextName;
    private EditText mTextWindowArea;
    private EditText mTextOverhangDepth;
    private EditText mTextDistanceOverhangToTop;
    private EditText mTextDistanceOverhangToBottom;
    private EditText mTextSHGC;
    private EditText mTextUFactor;
    private EditText mTextSummerShading;
    private EditText mTextWinterShading;
    private CheckBox mCheckboxRemove;
    private Spinner mSpinnerOrientation;
    private Button mButtonSave;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mWindowIndex;
    private Ekotrope_Window_Table mWindow;
    private List<String> mOrientations;
    private boolean valid = true;

    public static final String TAG = "WINDOWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_windows);
        setSupportActionBar(findViewById(R.id.ekotrope_windows_toolbar));
        mWindowsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_WindowsViewModel.class);

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
        mWindowIndex = intent.getIntExtra(WINDOW_INDEX, WINDOW_INDEX_NOT_FOUND);
        mWindow = mWindowsViewModel.getWindow(mPlanId, mWindowIndex);

        // Set spinner lists...
        mOrientations = new ArrayList<>();
        mOrientations.add("North");
        mOrientations.add("Northeast");
        mOrientations.add("East");
        mOrientations.add("Southeast");
        mOrientations.add("South");
        mOrientations.add("Southwest");
        mOrientations.add("West");
        mOrientations.add("Northwest");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
        initializeChangeTracking();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_windows_constraint_layout);
        mToolbar = findViewById(R.id.ekotrope_windows_toolbar);
        mTextName = findViewById(R.id.ekotrope_windows_text_name);
        mTextWindowArea = findViewById(R.id.ekotrope_windows_text_window_area);
        mTextOverhangDepth = findViewById(R.id.ekotrope_windows_text_overhang_depth);
        mTextDistanceOverhangToTop = findViewById(R.id.ekotrope_windows_text_distance_overhang_to_top);
        mTextDistanceOverhangToBottom = findViewById(R.id.ekotrope_windows_text_distance_overhang_to_bottom);
        mTextSHGC = findViewById(R.id.ekotrope_windows_text_shgc);
        mTextUFactor = findViewById(R.id.ekotrope_windows_text_u_factor);
        mTextSummerShading = findViewById(R.id.ekotrope_windows_text_adjacent_summer_shading);
        mTextWinterShading = findViewById(R.id.ekotrope_windows_text_adjacent_winter_shading);
        mCheckboxRemove = findViewById(R.id.ekotrope_windows_checkbox_remove);
        mSpinnerOrientation = findViewById(R.id.ekotrope_windows_spinner_orientation);
        mButtonSave = findViewById(R.id.ekotrope_windows_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newRemove = mCheckboxRemove.isChecked();
            double newWindowArea = Double.parseDouble(mTextWindowArea.getText().toString());
            String newOrientation = mSpinnerOrientation.getSelectedItem().toString();
            double newOverhangDepth = Double.parseDouble(mTextOverhangDepth.getText().toString());
            double newDistanceOverhangToTop = Double.parseDouble(mTextDistanceOverhangToTop.getText().toString());
            double newDistanceOverhangToBottom = Double.parseDouble(mTextDistanceOverhangToBottom.getText().toString());
            double newSHGC = Double.parseDouble(mTextSHGC.getText().toString());
            double newUFactor = Double.parseDouble(mTextUFactor.getText().toString());
            double newAdjacentSummerShading = Double.parseDouble(mTextSummerShading.getText().toString());
            double newAdjacentWinterShading = Double.parseDouble(mTextWinterShading.getText().toString());

            mCheckboxRemove.clearFocus();
            mTextWindowArea.clearFocus();
            mSpinnerOrientation.clearFocus();
            mTextOverhangDepth.clearFocus();
            mTextDistanceOverhangToTop.clearFocus();
            mTextDistanceOverhangToBottom.clearFocus();
            mTextSHGC.clearFocus();
            mTextUFactor.clearFocus();
            mTextSummerShading.clearFocus();
            mTextWinterShading.clearFocus();

            if((boolean)mCheckboxRemove.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Remove",
                        Boolean.toString(mWindow.remove), Boolean.toString(newRemove));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextWindowArea.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Window Area",
                        Double.toString(mWindow.windowArea), Double.toString(newWindowArea));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mSpinnerOrientation.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Orientation",
                        mWindow.orientation, newOrientation);
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextOverhangDepth.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Overhang Depth",
                        Double.toString(mWindow.overhangDepth), Double.toString(newOverhangDepth));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextDistanceOverhangToTop.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Distance Overhang To Top",
                        Double.toString(mWindow.distanceOverhangToTop), Double.toString(newDistanceOverhangToTop));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextDistanceOverhangToBottom.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Distance Overhang To Bottom",
                        Double.toString(mWindow.distanceOverhangToBottom), Double.toString(newDistanceOverhangToBottom));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextSHGC.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "SHGC",
                        Double.toString(mWindow.SHGC), Double.toString(newSHGC));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextUFactor.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "U Factor",
                        Double.toString(mWindow.uFactor), Double.toString(newUFactor));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextSummerShading.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Adjacent Summer Shading",
                        Double.toString(mWindow.adjacentSummerShading), Double.toString(newAdjacentSummerShading));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }
            if((boolean)mTextWinterShading.getTag()) {
                Ekotrope_ChangeLog_Table changeLogEntry = new Ekotrope_ChangeLog_Table(mInspectionId,
                        mProjectId, mPlanId, "Window", mWindow.name, "Adjacent Winter Shading",
                        Double.toString(mWindow.adjacentWinterShading), Double.toString(newAdjacentWinterShading));
                mWindowsViewModel.insertChangeLog(changeLogEntry);
            }

            Ekotrope_Window_Table newWindow = new Ekotrope_Window_Table(mPlanId, mWindowIndex,
                    mTextName.getText().toString(), mWindow.typeName, newRemove, newWindowArea,
                    newOrientation, mWindow.installedWallIndex, mWindow.installedFoundationWallIndex,
                    newOverhangDepth, newDistanceOverhangToTop, newDistanceOverhangToBottom,
                    newSHGC, newUFactor, newAdjacentSummerShading, newAdjacentWinterShading, true);

            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mWindowsViewModel.updateWindow(newWindow);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format("Window - %s", mWindow.name));
        mTextName.setText(mWindow.typeName);
        mTextWindowArea.setText(String.format(Double.toString(mWindow.windowArea)));
        mTextOverhangDepth.setText(String.format(Double.toString(mWindow.overhangDepth)));
        mTextDistanceOverhangToTop.setText(String.format(Double.toString(mWindow.distanceOverhangToTop)));
        mTextDistanceOverhangToBottom.setText(String.format(Double.toString(mWindow.distanceOverhangToBottom)));
        mTextSHGC.setText(String.format(Double.toString(mWindow.SHGC)));
        mTextUFactor.setText(String.format(Double.toString(mWindow.uFactor)));
        mTextSummerShading.setText(String.format(Double.toString(mWindow.adjacentSummerShading)));
        mTextWinterShading.setText(String.format(Double.toString(mWindow.adjacentWinterShading)));

        ArrayAdapter<String> orientationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mOrientations);
        orientationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOrientation.setAdapter(orientationAdapter);
        mSpinnerOrientation.setSelection(mOrientations.indexOf(mWindow.orientation));
    }

    private void initializeTextValidators() {
        mTextWindowArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextWindowArea.getText().toString()) < 0) {
                        mTextWindowArea.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                        mTextWindowArea.setError("Must be a number");
                        valid = false;
                }
            }
        });
        mTextOverhangDepth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextOverhangDepth.getText().toString()) < 0) {
                        mTextOverhangDepth.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextOverhangDepth.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextDistanceOverhangToTop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextDistanceOverhangToTop.getText().toString()) < 0) {
                        mTextDistanceOverhangToTop.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextDistanceOverhangToTop.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextDistanceOverhangToBottom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextDistanceOverhangToTop.getText().toString()) < 0) {
                        mTextDistanceOverhangToTop.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextDistanceOverhangToTop.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextSHGC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double shgc = Double.parseDouble(mTextSHGC.getText().toString());
                    if (shgc < 0 || shgc > 1) {
                        mTextSHGC.setError("Must be between 0 and 1");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextSHGC.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextUFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextUFactor.getText().toString()) <= 0) {
                        mTextUFactor.setError("Must be greater than 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextUFactor.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextSummerShading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double summerShading = Double.parseDouble(mTextSummerShading.getText().toString());
                    if (summerShading < 0 || summerShading > 1) {
                        mTextSummerShading.setError("Must be between 0 and 1");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextSummerShading.setError("Must be a number");
                    valid = false;
                }
            }
        });
        mTextWinterShading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double winterShading = Double.parseDouble(mTextWinterShading.getText().toString());
                    if (winterShading < 0 || winterShading > 1) {
                        mTextWinterShading.setError("Must be between 0 and 1");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextWinterShading.setError("Must be a number");
                    valid = false;
                }
            }
        });
    }

    private void initializeChangeTracking() {
        BridgeHelper.setChangeTracker(mCheckboxRemove, mWindow.remove);
        BridgeHelper.setChangeTracker(mTextWindowArea, mWindow.windowArea);
        BridgeHelper.setChangeTracker(mSpinnerOrientation, mWindow.orientation);
        BridgeHelper.setChangeTracker(mTextOverhangDepth, mWindow.overhangDepth);
        BridgeHelper.setChangeTracker(mTextDistanceOverhangToTop, mWindow.distanceOverhangToTop);
        BridgeHelper.setChangeTracker(mTextDistanceOverhangToBottom, mWindow.distanceOverhangToBottom);
        BridgeHelper.setChangeTracker(mTextSHGC, mWindow.SHGC);
        BridgeHelper.setChangeTracker(mTextUFactor, mWindow.uFactor);
        BridgeHelper.setChangeTracker(mTextSummerShading, mWindow.adjacentSummerShading);
        BridgeHelper.setChangeTracker(mTextWinterShading, mWindow.adjacentWinterShading);
    }
}