package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.JsonArrayRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.R;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import data.Tables.Inspection_Table;


public class EkotropeDataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mInspectorId;
    private Button mButtonEmail;
    private static final String TAG = "EKOTROPE_DATA";
    private long mLastClickTime = 0;

    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public int mInspectionId;
    private EkotropeDataViewModel mEkotropeDataViewModel;

    private static BridgeAPIQueue apiQueue;

    private Inspection_Table mInspection;
    private TextView mTextEkotropeProjectId;
    private TextView mTextEkotropePlanId;

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

        mButtonEmail = findViewById(R.id.ekotrope_data_button_email);
        mTextEkotropeProjectId = findViewById(R.id.ekotrope_text_project_id);
        mTextEkotropePlanId = findViewById(R.id.ekotrope_text_plan_id);
    }

    private void initializeDisplayContent() {
        mTextEkotropeProjectId.setText(mInspection.ekotrope_project_id);
        mTextEkotropePlanId.setText(mInspection.ekotrope_plan_id);
    }

    private void initializeButtonListeners() {
        mButtonEmail.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
        });
    }
}