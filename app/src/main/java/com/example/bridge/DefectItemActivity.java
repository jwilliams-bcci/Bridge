package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import data.CannedComment;
import data.DataManager;
import data.DefectItem;

public class DefectItemActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String DEFECT_ID = "com.example.bridge.DEFECT_ID";
    public static final int DEFECT_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mDefectId;
    private Spinner mSpinnerCannedComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_item);
        setSupportActionBar((Toolbar) findViewById(R.id.defect_item_toolbar));

        Intent intent = getIntent();
        TextView textDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);

        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mSpinnerCannedComment = findViewById(R.id.defect_item_spinner_canned_comment);

        TextView locationTextView = findViewById(R.id.defect_item_text_location);
        locationTextView.setOnClickListener(view -> {
            LocationFragment.newInstance().show(getSupportFragmentManager(), "TAG");
        });

        displayDefectDetails(textDefectItemDetails);
        fillSpinner(mSpinnerCannedComment);
    }

    private void displayDefectDetails(TextView textDefectItemDetails) {
        DefectItem defectItem = DataManager.getInstance().getDefectItem(mDefectId);
        textDefectItemDetails.append(Integer.toString(defectItem.getItemNumber()));
        textDefectItemDetails.append(" - ");
        textDefectItemDetails.append(defectItem.getItemDescription());
    }

    private void fillSpinner(Spinner cannedCommentSpinner) {
        List<CannedComment> cannedComments = DataManager.getInstance().getCannedComments();
        ArrayAdapter<CannedComment> adapterCannedComments = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cannedComments);
        adapterCannedComments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCannedComment.setAdapter(adapterCannedComments);
    }
}