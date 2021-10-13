package com.burgess.bridge.editresolution;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import data.DataManager;
import data.Enums.IncompleteReason;
import data.Inspection;
import data.InspectionResolution;
import data.Location;
import data.Tables.Inspection_Table;

public class EditResolutionActivity extends AppCompatActivity {
    private EditResolutionViewModel mEditResolutionViewModel;
    private SharedPreferences mSharedPreferences;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextAddress;
    private Spinner mSpinnerResolutions;
    private TextView mTextComment;
    private Button mButtonSubmit;

    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private StringRequest mEditResolutionRequest;

    private static final String TAG = "EDIT_RESOLUTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resolution);
        setSupportActionBar((Toolbar) findViewById(R.id.edit_resolution_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditResolutionViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EditResolutionViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mEditResolutionViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
        initializeAPIRequests();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.edit_resolution_constraint_layout);
        mTextAddress = findViewById(R.id.edit_resolution_text_inspection_address);
        mSpinnerResolutions = findViewById(R.id.edit_resolution_spinner_resolutions);
        mTextComment = findViewById(R.id.edit_resolution_text_note);
        mButtonSubmit = findViewById(R.id.edit_resolution_button_submit);
    }
    private void initializeButtonListeners() {
        mButtonSubmit.setOnClickListener(view -> {
            String inspectionTime = "";
            IncompleteReason selectedItem = (IncompleteReason) mSpinnerResolutions.getSelectedItem();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                inspectionTime = URLEncoder.encode(formatter.format(Calendar.getInstance().getTime()), "utf-8");
            } catch (UnsupportedEncodingException e) {
                BridgeLogger.getInstance().log('E', TAG, "ERROR in initializeAPIRequests: " + e.getMessage());
                Snackbar.make(mConstraintLayout, "Error! Please return to Route Sheet and send Activity Log", Snackbar.LENGTH_LONG).show();
            }
            mEditResolutionRequest = BridgeAPIQueue.getInstance().updateInspectionStatus(mInspectionId, selectedItem.code, mSharedPreferences.getString(PREF_SECURITY_USER_ID, "NULL"), 0, 0, inspectionTime, inspectionTime, new ServerCallback() {
                @Override
                public void onSuccess(String message) {
                    BridgeLogger.getInstance().log('I', TAG, "Resolution edited");
                }

                @Override
                public void onFailure(String message) {
                    BridgeLogger.getInstance().log('E', TAG, "ERROR: " + message);
                }
            });

            BridgeAPIQueue.getInstance().getRequestQueue().add(mEditResolutionRequest);
            finish();
            Intent routeSheetIntent = new Intent(EditResolutionActivity.this, RouteSheetActivity.class);
            routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(routeSheetIntent);
        });
    }
    private void initializeAPIRequests() {
    }
    private void initializeDisplayContent() {
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");

        mSpinnerResolutions.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, IncompleteReason.values()));
    }
}