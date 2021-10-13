package com.burgess.bridge.transferinspection;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.Constants;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import data.DataManager;
import data.Inspection;
import data.Inspector;
import data.Location;
import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;

public class TransferInspectionActivity extends AppCompatActivity {
    private TransferInspectionViewModel mTransferInspectionViewModel;
    private SharedPreferences mSharedPreferences;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextAddress;
    private AutoCompleteTextView mTextNewInspector;
    private TextView mTextTransferNote;
    private Button mButtonTransfer;

    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private List<Inspector_Table> mInspectors;
    private ArrayAdapter<Inspector_Table> mAdapter;
    private int mNewInspectorId;

    private static final String TAG = "TRANSFER_INSPECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_inspection);
        setSupportActionBar((Toolbar) findViewById(R.id.transfer_inspection_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mTransferInspectionViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TransferInspectionViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mTransferInspectionViewModel.getInspectionSync(mInspectionId);
        mInspectors = mTransferInspectionViewModel.getInspectorsByDivision(Integer.parseInt(mSharedPreferences.getString(PREF_INSPECTOR_DIVISION_ID, "0")));
        mNewInspectorId = -1;

        initializeViews();
        initializeTextViewListeners();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.transfer_inspection_constraint_layout);
        mTextAddress = findViewById(R.id.transfer_inspection_text_inspection_address);
        mTextNewInspector = findViewById(R.id.transfer_inspection_text_new_inspector);
        mTextTransferNote = findViewById(R.id.transfer_inspection_text_note);
        mButtonTransfer = findViewById(R.id.transfer_inspection_button_transfer);
    }
    private void initializeTextViewListeners() {
        mTextNewInspector.setOnItemClickListener((parent, view, position, id) -> {
            Inspector_Table selectedItem = (Inspector_Table) parent.getItemAtPosition(position);
            mNewInspectorId = selectedItem.id;
        });
    }
    private void initializeButtonListeners() {
        mButtonTransfer.setOnClickListener(view -> {
            if (mNewInspectorId < 0) {
                Snackbar.make(mConstraintLayout, "Please choose a new Inspector from above.", Snackbar.LENGTH_SHORT).show();
            } else {
                StringRequest transferInspectionRequest = BridgeAPIQueue.getInstance().transferInspection(mInspectionId, mNewInspectorId, new ServerCallback() {
                    @Override
                    public void onSuccess(String message) {
                        BridgeLogger.getInstance().log('I', TAG, "Transferred Inspection " + mInspectionId + " to " + mNewInspectorId);
                    }

                    @Override
                    public void onFailure(String message) {
                        BridgeLogger.getInstance().log('E', TAG, "ERROR in TransferInspection: " + message);
                    }
                });
                BridgeAPIQueue.getInstance().getRequestQueue().add(transferInspectionRequest);

                finish();
                Intent routeSheetIntent = new Intent(TransferInspectionActivity.this, RouteSheetActivity.class);
                routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(routeSheetIntent);
            }
        });
    }
    private void initializeDisplayContent() {
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mInspectors);
        mTextNewInspector.setAdapter(mAdapter);
    }
}