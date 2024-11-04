package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.ekotrope_framedfloors.Ekotrope_FramedFloorsActivity.PLAN_ID;

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
import com.burgess.bridge.ekotrope_abovegradewallslist.Ekotrope_AboveGradeWallsListActivity;
import com.burgess.bridge.ekotrope_ceilingslist.Ekotrope_CeilingsListActivity;
import com.burgess.bridge.ekotrope_doorslist.Ekotrope_DoorsListActivity;
import com.burgess.bridge.ekotrope_framedfloorslist.Ekotrope_FramedFloorsListActivity;
import com.burgess.bridge.ekotrope_rimjoistslist.Ekotrope_RimJoistsListActivity;
import com.burgess.bridge.ekotrope_slabslist.Ekotrope_SlabsListActivity;
import com.burgess.bridge.ekotrope_windowslist.Ekotrope_WindowsListActivity;
import com.burgess.bridge.inspect.InspectActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import data.Tables.Inspection_Table;


public class EkotropeDataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mInspectorId;
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
    private Button mButtonFramedFloors;
    private Button mButtonAboveGradeWalls;
    private Button mButtonWindows;
    private Button mButtonDoors;
    private Button mButtonCeilings;
    private Button mButtonSlabs;
    private Button mButtonRimJoists;
    private Button mButtonEmail;
    private Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        apiQueue = BridgeAPIQueue.getInstance(this);

        mEkotropeDataViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EkotropeDataViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = formatter.format(OffsetDateTime.now());

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
        mButtonFramedFloors = findViewById(R.id.ekotrope_data_button_framed_floors);
        mButtonAboveGradeWalls = findViewById(R.id.ekotrope_data_button_above_grade_walls);
        mButtonWindows = findViewById(R.id.ekotrope_data_button_windows);
        mButtonDoors = findViewById(R.id.ekotrope_data_button_doors);
        mButtonCeilings = findViewById(R.id.ekotrope_data_button_ceilings);
        mButtonSlabs = findViewById(R.id.ekotrope_data_button_slabs);
        mButtonRimJoists = findViewById(R.id.ekotrope_data_button_rim_joists);
        mButtonEmail = findViewById(R.id.ekotrope_data_button_email_json);
        mButtonSubmit = findViewById(R.id.ekotrope_data_button_submit);
    }

    private void initializeDisplayContent() {
        mTextEkotropeProjectId.setText(mInspection.ekotrope_project_id);
        mTextEkotropePlanId.setText(mInspection.ekotrope_plan_id);
    }

    private void initializeButtonListeners() {
        mButtonFramedFloors.setOnClickListener(view -> {
            Intent framedFloorsListIntent = new Intent(this, Ekotrope_FramedFloorsListActivity.class);
            framedFloorsListIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(framedFloorsListIntent);
        });

        mButtonAboveGradeWalls.setOnClickListener(view -> {
            Intent aboveGradeWallsIntent = new Intent(this, Ekotrope_AboveGradeWallsListActivity.class);
            aboveGradeWallsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(aboveGradeWallsIntent);
        });

        mButtonWindows.setOnClickListener(view -> {
            Intent windowsIntent = new Intent(this, Ekotrope_WindowsListActivity.class);
            windowsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(windowsIntent);
        });

        mButtonDoors.setOnClickListener(view -> {
            Intent doorsIntent = new Intent(this, Ekotrope_DoorsListActivity.class);
            doorsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(doorsIntent);
        });

        mButtonCeilings.setOnClickListener(view -> {
            Intent ceilingsIntent = new Intent(this, Ekotrope_CeilingsListActivity.class);
            ceilingsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(ceilingsIntent);
        });

        mButtonSlabs.setOnClickListener(view -> {
            Intent slabsIntent = new Intent(this, Ekotrope_SlabsListActivity.class);
            slabsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(slabsIntent);
        });

        mButtonRimJoists.setOnClickListener(view -> {
            Intent rimJoistsIntent = new Intent(this, Ekotrope_RimJoistsListActivity.class);
            rimJoistsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(rimJoistsIntent);
        });

        mButtonEmail.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            Intent emailIntent = BridgeLogger.sendEkotropeJson(mEkotropeDataViewModel.getInspectionSyncJson(mInspection.ekotrope_plan_id, mInspection.ekotrope_project_id), mInspectionId);
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