package com.burgess.bridge.multifamily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.inspect.InspectActivity;

import java.util.List;

import data.Tables.Inspection_Table;
import data.Tables.MultifamilyDetails_Table;

public class MultifamilyDetailsActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private MultifamilyDetails_Table mMultifamilyDetails;
    private MultifamilyDetailsViewModel mMultifamilyDetailsViewModel;

    private TextView mTextAreaObserved;
    private Button mButtonSaveMultifamilyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multifamily_details);
        setSupportActionBar(findViewById(R.id.multifamily_details_toolbar));
        mMultifamilyDetailsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MultifamilyDetailsViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);
        mInspection = mMultifamilyDetailsViewModel.getInspection(mInspectionId);
        mMultifamilyDetails = mMultifamilyDetailsViewModel.getMultifamilyDetails(mInspectionId);

        initializeViews();
        initializeDisplayContent();
        initializeButtonListeners();
    }

    private void initializeViews() {
        mTextAreaObserved = findViewById(R.id.multifamily_details_text_area_observed);
        mButtonSaveMultifamilyDetails = findViewById(R.id.multifamily_details_button_save);
    }
    private void initializeDisplayContent() {
        if (mMultifamilyDetails != null) {
            mTextAreaObserved.setText(mMultifamilyDetails.area_observed);
        }
    }
    private void initializeButtonListeners() {
        mButtonSaveMultifamilyDetails.setOnClickListener(v -> {
            MultifamilyDetails_Table newDetails = new MultifamilyDetails_Table();
            newDetails.inspection_id = mInspectionId;
            newDetails.area_observed = mTextAreaObserved.getText().toString();
            if (mMultifamilyDetails != null) {
                mMultifamilyDetailsViewModel.updateMultifamilyDetails(mMultifamilyDetails.id, "", "",
                        newDetails.area_observed, "", "");
            } else {
                mMultifamilyDetailsViewModel.insertMultifamilyDetails(newDetails);
            }

            Intent inspectIntent = new Intent(MultifamilyDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            startActivity(inspectIntent);
        });
    }
}