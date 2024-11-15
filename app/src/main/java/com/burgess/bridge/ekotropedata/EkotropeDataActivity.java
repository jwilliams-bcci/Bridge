package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.fragment_ekotrope_inspectiontypes.Ekotrope_Final;
import com.burgess.bridge.fragment_ekotrope_inspectiontypes.Ekotrope_Rough;
import com.burgess.bridge.inspect.InspectActivity;
import com.google.android.material.snackbar.Snackbar;

import data.Tables.Inspection_Table;


public class EkotropeDataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private static final String TAG = "EKOTROPE_DATA";
    private long mLastClickTime = 0;

    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String EKOTROPE_SUBMITTED = "com.burgess.bridge.EKOTROPE_SUBMITTED";
    public static final boolean EKOTROPE_SUBMITTED_TRUE = true;
    public int mInspectionId;
    private EkotropeDataViewModel mEkotropeDataViewModel;

    private static BridgeAPIQueue apiQueue;

    private ConstraintLayout mConstraintLayout;
    private Inspection_Table mInspection;
    private TextView mTextEkotropeProjectId;
    private TextView mTextEkotropePlanId;
    private Button mButtonEmail;
    private Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        apiQueue = BridgeAPIQueue.getInstance(this);

        mEkotropeDataViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EkotropeDataViewModel.class);

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
    }

    private void initializeDisplayContent() {
        mTextEkotropeProjectId.setText(mInspection.ekotrope_project_id);
        mTextEkotropePlanId.setText(mInspection.ekotrope_plan_id);

        Bundle bundle = new Bundle();
        bundle.putInt(INSPECTION_ID, mInspectionId);
        if (mInspection.inspection_type.contains("Rough")) {
            Ekotrope_Rough ekotrope_rough = new Ekotrope_Rough();
            ekotrope_rough.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ekotrope_data_fragment_inspection_type, ekotrope_rough).commit();
        } else if (mInspection.inspection_type.contains("Final")) {
            Ekotrope_Final ekotrope_final = new Ekotrope_Final();
            ekotrope_final.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ekotrope_data_fragment_inspection_type, ekotrope_final).commit();
        }
    }

    private void initializeButtonListeners() {
        mButtonEmail.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            Intent emailIntent = BridgeLogger.sendEkotropeJson(mEkotropeDataViewModel.getInspectionSyncJson_Rough(mInspection.ekotrope_plan_id, mInspection.ekotrope_project_id), mInspectionId);
            startActivity(emailIntent);

            Snackbar.make(mConstraintLayout, "JSON sent via email", Snackbar.LENGTH_SHORT).show();
        });

        mButtonSubmit.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            apiQueue.updateEkotropePlanData(mEkotropeDataViewModel, mInspection.ekotrope_plan_id, mInspection.ekotrope_project_id, new ServerCallback() {
                @Override
                public void onSuccess(String message) {

                }

                @Override
                public void onFailure(String message) {
                    Snackbar.make(mConstraintLayout, message, Snackbar.LENGTH_SHORT).show();
                }
            });
            Intent inspectIntent = new Intent(this, InspectActivity.class);
            inspectIntent.putExtra(INSPECTION_ID, mInspectionId);
            inspectIntent.putExtra(EKOTROPE_SUBMITTED, EKOTROPE_SUBMITTED_TRUE);
            startActivity(inspectIntent);
        });
    }
}