package com.burgess.bridge.ekotrope_windows;

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

import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String WINDOW_INDEX = "com.burgess.bridge.WINDOW_INDEX";
    public static final int WINDOW_INDEX_NOT_FOUND = -1;

    private Ekotrope_WindowsViewModel mWindowsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextName;
    private EditText mTextWindowArea;
    private EditText mTextInstalledWallIndex;
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

    private String mPlanId;
    private int mWindowIndex;
    private Ekotrope_Window_Table mWindow;
    private ArrayAdapter<String> mOrientationAdapter;
    private List<String> mOrientations;

    public static final String TAG = "WINDOWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_windows);
        setSupportActionBar(findViewById(R.id.ekotrope_windows_toolbar));
        mWindowsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_WindowsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);
        mWindowIndex = intent.getIntExtra(WINDOW_INDEX, WINDOW_INDEX_NOT_FOUND);
        mWindow = mWindowsViewModel.getWindow(mPlanId, mWindowIndex);

        // Set spinner lists...
        mOrientations = new ArrayList<String>();
        mOrientations.add("North");
        mOrientations.add("Northeast");
        mOrientations.add("East");
        mOrientations.add("Southeast");
        mOrientations.add("South");
        mOrientations.add("Southwest");
        mOrientations.add("West");
        mOrientations.add("Northwest");

        intializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void intializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_windows_constraint_layout);
        mTextName = findViewById(R.id.ekotrope_windows_text_name);
        mTextWindowArea = findViewById(R.id.ekotrope_windows_text_window_area);
        mTextInstalledWallIndex = findViewById(R.id.ekotrope_windows_text_installed_wall_index);
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
            boolean newRemove = mCheckboxRemove.isChecked();
            double newWindowArea = Double.parseDouble(mTextWindowArea.getText().toString());
            String newOrientation = mSpinnerOrientation.getSelectedItem().toString();
            int newInstalledWallIndex = Integer.parseInt(mTextInstalledWallIndex.getText().toString());
            double newOverhangDepth = Double.parseDouble(mTextOverhangDepth.getText().toString());
            double newDistanceOverhangToTop = Double.parseDouble(mTextDistanceOverhangToTop.getText().toString());
            double newDistanceOverhangToBottom = Double.parseDouble(mTextDistanceOverhangToBottom.getText().toString());
            double newSHGC = Double.parseDouble(mTextSHGC.getText().toString());
            double newUFactor = Double.parseDouble(mTextUFactor.getText().toString());
            double newAdjacentSummerShading = Double.parseDouble(mTextSummerShading.getText().toString());
            double newAdjacentWinterShading = Double.parseDouble(mTextWinterShading.getText().toString());

            Ekotrope_Window_Table newWindow = new Ekotrope_Window_Table(mPlanId, mWindowIndex,
                    mTextName.getText().toString(), mWindow.typeName, newRemove, newWindowArea,
                    newOrientation, newInstalledWallIndex, -1,
                    newOverhangDepth, newDistanceOverhangToTop, newDistanceOverhangToBottom,
                    newSHGC, newUFactor, newAdjacentSummerShading, newAdjacentWinterShading);

            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mWindowsViewModel.updateWindow(newWindow);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextName.setText(mWindow.name);
        mTextWindowArea.setText(Double.toString(mWindow.windowArea));
        mTextInstalledWallIndex.setText(Integer.toString(mWindow.installedWallIndex));
        mTextOverhangDepth.setText(Double.toString(mWindow.overhangDepth));
        mTextDistanceOverhangToTop.setText(Double.toString(mWindow.distanceOverhangToTop));
        mTextDistanceOverhangToBottom.setText(Double.toString(mWindow.distanceOverhangToBottom));
        mTextSHGC.setText(Double.toString(mWindow.SHGC));
        mTextUFactor.setText(Double.toString(mWindow.uFactor));
        mTextSummerShading.setText(Double.toString(mWindow.adjacentSummerShading));
        mTextWinterShading.setText(Double.toString(mWindow.adjacentWinterShading));

        mOrientationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mOrientations);
        mOrientationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOrientation.setAdapter(mOrientationAdapter);
    }
}