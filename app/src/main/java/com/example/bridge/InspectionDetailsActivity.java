package com.example.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class InspectionDetailsActivity extends AppCompatActivity {

    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String BUILDER_ID = "com.example.bridge.BUILDER_ID";
    public static final int INSPECTION_ID_NOT_SET = -1;
    public static final int BUILDER_ID_NOT_SET = -1;
    private int mInspectionId;
    private int mBuilderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        setSupportActionBar((Toolbar) findViewById(R.id.inspection_details_toolbar));

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspection_details_multiline_inspection_address);
        TextView textBuilder = findViewById(R.id.inspection_details_text_builder);
        TextView textSuperintendent = findViewById(R.id.inspection_details_text_superintendent);
        TextView textNotes = findViewById(R.id.inspection_details_text_notes);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_SET);

        displayAddress(textAddress);
        displayInspectionSummary(textBuilder, textSuperintendent, textNotes);
    }

    private void displayAddress(TextView textAddress) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        textAddress.append(inspection.getCommunity() + "\n");
        textAddress.append(inspection.getAddress() + "\n");
        textAddress.append(inspection.getInspectionType());
    }

    private void displayInspectionSummary(TextView textBuilder, TextView textSuperintendent, TextView textNotes) {
        Inspection inspection = DataManager.getInstance().getInspection(mInspectionId);
        textBuilder.setText(inspection.getBuilder());
        textSuperintendent.setText(inspection.getSuperintendent());
        textNotes.setText(inspection.getNotes());
    }
}