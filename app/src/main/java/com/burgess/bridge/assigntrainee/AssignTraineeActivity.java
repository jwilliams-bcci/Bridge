package com.burgess.bridge.assigntrainee;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;

public class AssignTraineeActivity extends AppCompatActivity {
    private AssignTraineeViewModel mAssignTraineeViewModel;
    private SharedPreferences mSharedPreferences;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextAddress;
    private AutoCompleteTextView mTextInspector;
    private Button mButtonAssign;

    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private List<Inspector_Table> mInspectors;
    private ArrayAdapter<Inspector_Table> mAdapter;
    private int mTraineeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_trainee);
        setSupportActionBar(findViewById(R.id.assign_trainee_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mAssignTraineeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AssignTraineeViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mAssignTraineeViewModel.getInspectionSync(mInspectionId);
        mInspectors = mAssignTraineeViewModel.getInspectorsByDivision(Integer.parseInt(mSharedPreferences.getString(PREF_INSPECTOR_DIVISION_ID, "0")));
        mTraineeId = -1;

        initializeViews();
        initializeTextViewListeners();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.assign_trainee_constraint_layout);
        mTextAddress = findViewById(R.id.assign_trainee_text_inspection_address);
        mTextInspector = findViewById(R.id.assign_trainee_text_inspector);
        mButtonAssign = findViewById(R.id.assign_trainee_button_assign);
    }
    private void initializeTextViewListeners() {
        mTextInspector.setOnItemClickListener((parent, view, position, id) -> {
            Inspector_Table selectedItem = (Inspector_Table) parent.getItemAtPosition(position);
            mTraineeId = selectedItem.id;
        });
    }
    private void initializeButtonListeners() {
        mButtonAssign.setOnClickListener(view -> {
            if (mTraineeId < 0) {
                Snackbar.make(mConstraintLayout, "Please choose a trainee from above.", Snackbar.LENGTH_LONG).show();
            } else {
                mAssignTraineeViewModel.assignTrainee(mTraineeId, mInspectionId);

                finish();
            }
        });
    }
    private void initializeDisplayContent() {
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mInspectors);
        mTextInspector.setAdapter(mAdapter);
    }
}