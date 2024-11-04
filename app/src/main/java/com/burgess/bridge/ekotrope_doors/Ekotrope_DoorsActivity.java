package com.burgess.bridge.ekotrope_doors;

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

import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorsActivity extends AppCompatActivity {
    public static final String PLAN_ID = "com.burgess.bridge.PLAN_ID";
    public static final String DOOR_INDEX = "com.burgess.bridge.DOOR_INDEX";
    public static final int DOOR_INDEX_NOT_FOUND = -1;

    private Ekotrope_DoorsViewModel mDoorsViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText mTextName;
    private EditText mTextWallIndex;
    private EditText mTextDoorArea;
    private EditText mTextUFactor;
    private CheckBox mCheckBoxRemove;
    private Button mButtonSave;

    private String mPlanId;
    private int mDoorIndex;
    private Ekotrope_Door_Table mDoor;
    private boolean valid = true;

    public static final String TAG = "DOORS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_doors);
        setSupportActionBar(findViewById(R.id.ekotrope_doors_toolbar));
        mDoorsViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DoorsViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);
        mDoorIndex = intent.getIntExtra(DOOR_INDEX, DOOR_INDEX_NOT_FOUND);
        mDoor = mDoorsViewModel.getDoor(mPlanId, mDoorIndex);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        initializeTextValidators();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_doors_constraint_layout);
        mTextName = findViewById(R.id.ekotrope_doors_text_name);
        mTextWallIndex = findViewById(R.id.ekotrope_doors_text_wall_index);
        mCheckBoxRemove = findViewById(R.id.ekotrope_doors_checkbox_remove);
        mTextDoorArea = findViewById(R.id.ekotrope_doors_text_door_area);
        mTextUFactor = findViewById(R.id.ekotrope_doors_text_u_factor);
        mButtonSave = findViewById(R.id.ekotrope_doors_button_save);
    }

    private void initializeButtonListeners() {
        mButtonSave.setOnClickListener(v -> {
            if (!valid) {
                Snackbar.make(mConstraintLayout, "Please fix errors", Snackbar.LENGTH_LONG).show();
                return;
            }
            boolean newRemove = mCheckBoxRemove.isChecked();
            double newDoorArea = Double.parseDouble(mTextDoorArea.getText().toString());
            double newUFactor = Double.parseDouble(mTextUFactor.getText().toString());

            Ekotrope_Door_Table newDoor = new Ekotrope_Door_Table(mPlanId, mDoorIndex,
                    mTextName.getText().toString(), mDoor.typeName, newRemove,
                    Integer.parseInt(mTextWallIndex.getText().toString()), -1,
                    newDoorArea, newUFactor, true);
            Snackbar.make(mConstraintLayout, "Saving...", Snackbar.LENGTH_LONG).show();
            mDoorsViewModel.updateDoor(newDoor);
            finish();
        });
    }

    private void initializeDisplayContent() {
        mTextName.setText(mDoor.name);
        mTextWallIndex.setText(Integer.toString(mDoor.installedWallIndex));
        mTextDoorArea.setText(Double.toString(mDoor.doorArea));
        mTextUFactor.setText(Double.toString(mDoor.uFactor));
    }

    private void initializeTextValidators() {
        mTextDoorArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Double.parseDouble(mTextDoorArea.getText().toString()) < 0) {
                        mTextDoorArea.setError("Must be greater than or equal to 0");
                        valid = false;
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    mTextDoorArea.setError("Must be a number");
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
                    double uFactor = Double.parseDouble(mTextUFactor.getText().toString());
                    if (uFactor < 0) {
                        mTextUFactor.setError("Must be greater than or equal to 0");
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
    }
}