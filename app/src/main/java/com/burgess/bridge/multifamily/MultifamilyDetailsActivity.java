package com.burgess.bridge.multifamily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.inspect.InspectActivity;

import data.Tables.MultifamilyDetails_Table;

public class MultifamilyDetailsActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    private int mInspectionId;
    private MultifamilyDetailsViewModel mMultifamilyDetailsViewModel;

    private TextView mTextBuilderPersonnel;
    private TextView mTextBurgessPersonnel;
    private TextView mTextAreaObserved;
    private TextView mTextTemperature;
    private TextView mTextWeatherConditions;
    private Button mButtonSaveMultifamilyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multifamily_details);
        setSupportActionBar(findViewById(R.id.multifamily_details_toolbar));
        mMultifamilyDetailsViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MultifamilyDetailsViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);

        initializeViews();
        initializeButtonListeners();
    }

    private void initializeViews() {
        mTextBuilderPersonnel = findViewById(R.id.multifamily_details_text_builder_personnel);
        mTextBurgessPersonnel = findViewById(R.id.multifamily_details_text_burgess_personnel);
        mTextAreaObserved = findViewById(R.id.multifamily_details_text_area_observed);
        mTextTemperature = findViewById(R.id.multifamily_details_text_temperature);
        mTextWeatherConditions = findViewById(R.id.multifamily_details_text_weather_conditions);
        mButtonSaveMultifamilyDetails = findViewById(R.id.multifamily_details_button_save);
    }
    private void initializeButtonListeners() {

        mButtonSaveMultifamilyDetails.setOnClickListener(v -> {
            MultifamilyDetails_Table newDetails = new MultifamilyDetails_Table();
            newDetails.inspection_id = mInspectionId;
            newDetails.builder_personnel = mTextBuilderPersonnel.getText().toString();
            newDetails.burgess_personnel = mTextBurgessPersonnel.getText().toString();
            newDetails.area_observed = mTextAreaObserved.getText().toString();
            newDetails.temperature = mTextTemperature.getText().toString();
            newDetails.weather_conditions = mTextWeatherConditions.getText().toString();
            mMultifamilyDetailsViewModel.insertMultifamilyDetails(newDetails);

            Intent inspectIntent = new Intent(MultifamilyDetailsActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            startActivity(inspectIntent);
        });
    }
}