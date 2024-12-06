package com.burgess.bridge.ekotrope_data;

import static com.burgess.bridge.Constants.EKOTROPE_INSPECTION_TYPE_FINAL;
import static com.burgess.bridge.Constants.EKOTROPE_INSPECTION_TYPE_ROUGH;
import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.ekotrope_fragment_inspectiontypes.Ekotrope_Final;
import com.burgess.bridge.ekotrope_fragment_inspectiontypes.Ekotrope_Rough;
import com.burgess.bridge.inspect.InspectActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import data.Tables.Inspection_Table;


public class Ekotrope_DataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private static final String TAG = "EKOTROPE_DATA";
    private long mLastClickTime = 0;

    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String EKOTROPE_SUBMITTED = "com.burgess.bridge.EKOTROPE_SUBMITTED";
    public static final boolean EKOTROPE_SUBMITTED_TRUE = true;
    public int mInspectionId;
    private Ekotrope_DataViewModel mEkotropeDataViewModel;

    private static BridgeAPIQueue apiQueue;

    private ConstraintLayout mConstraintLayout;
    private Inspection_Table mInspection;
    private TextView mTextEkotropeProjectId;
    private TextView mTextEkotropePlanId;
    private Button mButtonEmail;
    private Button mButtonSubmit;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;

    private String mInspectionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        apiQueue = BridgeAPIQueue.getInstance(this);

        mEkotropeDataViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DataViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        // Get intent data...
        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mEkotropeDataViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.ekotrope_data_constraint_layout);
        mTextEkotropeProjectId = findViewById(R.id.ekotrope_text_project_id);
        mTextEkotropePlanId = findViewById(R.id.ekotrope_text_plan_id);
        mButtonEmail = findViewById(R.id.ekotrope_data_button_email_json);
        mButtonSubmit = findViewById(R.id.ekotrope_data_button_submit);
        mLockScreen = findViewById(R.id.ekotrope_data_lock_screen);
        mProgressBar = findViewById(R.id.ekotrope_data_progress_bar);
    }

    private void initializeDisplayContent() {
        mTextEkotropeProjectId.setText(mInspection.EkotropeProjectID);
        mTextEkotropePlanId.setText(mInspection.EkotropePlanID);

        Bundle bundle = new Bundle();
        bundle.putInt(INSPECTION_ID, mInspectionId);
        if (mInspection.InspectionType.contains("Rough")) {
            mInspectionType = EKOTROPE_INSPECTION_TYPE_ROUGH;
            Ekotrope_Rough ekotrope_rough = new Ekotrope_Rough();
            ekotrope_rough.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ekotrope_data_fragment_inspection_type, ekotrope_rough).commit();
        } else if (mInspection.InspectionType.contains("Final")) {
            mInspectionType = EKOTROPE_INSPECTION_TYPE_FINAL;
            Ekotrope_Final ekotrope_final = new Ekotrope_Final();
            ekotrope_final.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ekotrope_data_fragment_inspection_type, ekotrope_final).commit();
        }
    }

    private void initializeButtonListeners() {
        mButtonEmail.setOnClickListener(view -> {
            if (mInspectionType.equals(EKOTROPE_INSPECTION_TYPE_FINAL)) {
                Intent emailIntent = BridgeLogger.sendEkotropeJson(mEkotropeDataViewModel.getInspectionSyncJson_Final(mInspection.EkotropePlanID, mInspection.EkotropeProjectID), mInspectionId);
                startActivity(emailIntent);
                Snackbar.make(mConstraintLayout, "JSON-Final sent via email", Snackbar.LENGTH_SHORT).show();
            } else if (mInspectionType.equals(EKOTROPE_INSPECTION_TYPE_ROUGH)) {
                Intent emailIntent = BridgeLogger.sendEkotropeJson(mEkotropeDataViewModel.getInspectionSyncJson_Rough(mInspection.EkotropePlanID, mInspection.EkotropeProjectID), mInspectionId);
                startActivity(emailIntent);
                Snackbar.make(mConstraintLayout, "JSON-Final sent via email", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mConstraintLayout, "Invalid inspection type", Snackbar.LENGTH_SHORT).show();
            }
        });

        mButtonSubmit.setOnClickListener(view -> {
            BridgeLogger.log('I', TAG, String.format(Locale.ENGLISH,"Submitting Ekotrope data for Inspection ID: %d, Plan ID: %s, Project ID: %s", mInspectionId, mInspection.EkotropePlanID, mInspection.EkotropeProjectID));
            BridgeHelper.showSpinner(mLockScreen, mProgressBar, getWindow());
            JsonObjectRequest request = BridgeAPIQueue.getInstance(this).updateEkotropePlanData(mEkotropeDataViewModel, mInspection.EkotropePlanID, mInspection.EkotropeProjectID, mInspectionType, new ServerCallback() {
                @Override
                public void onSuccess(String message) {
                    BridgeHelper.hideSpinner(mLockScreen, mProgressBar, getWindow());
                    Intent inspectIntent = new Intent(getApplicationContext(), InspectActivity.class);
                    inspectIntent.putExtra(INSPECTION_ID, mInspectionId);
                    inspectIntent.putExtra(EKOTROPE_SUBMITTED, EKOTROPE_SUBMITTED_TRUE);
                    startActivity(inspectIntent);
                }

                @Override
                public void onFailure(String message) {
                    BridgeHelper.hideSpinner(mLockScreen, mProgressBar, getWindow());
                    BridgeLogger.log('E', TAG, String.format(Locale.ENGLISH,"Failed to " +
                            "update Ekotrope data for Inspection ID: %d, Plan ID: %s, " +
                            "Project ID: %s, Message: %s", mInspectionId, mInspection.EkotropePlanID,
                            mInspection.EkotropeProjectID, message));
                    Snackbar.make(mConstraintLayout, message, Snackbar.LENGTH_SHORT).show();
                }
            });
            BridgeAPIQueue.getInstance(this).getRequestQueue().add(request);
        });
    }
}