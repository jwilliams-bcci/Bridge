package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;

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
    private Inspection_Table mInspection;
    private EkotropeDataViewModel mEkotropeDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        mEkotropeDataViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EkotropeDataViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mEkotropeDataViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mButtonEmail = findViewById(R.id.ekotrope_data_button_email);
    }

    private void initializeDisplayContent() {

    }

    private void initializeButtonListeners() {
        mButtonEmail.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            try {
                Intent emailIntent = sendEkotropeEmail();
                startActivity(Intent.createChooser(emailIntent, "Send Ekotrope Data..."));
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
            }
        });
    }

    public Intent sendEkotropeEmail() {
        //Uri logFileUri = FileProvider.getUriForFile(ctx, "com.burgess.bridge", logFile);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String[] to = {"jwilliams@burgess-inc.com", "rsandlin@burgess-inc.com", "bwallace@burgess-inc.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(logFileUri)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ekotrope Data for Inspector: " + mInspectorId + " Inspection: " + mInspectionId);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "JSON DATA\n Address: " + mInspection.address + "\nEkotrope Project ID: " + mInspection.ekotrope_project_id);
        return emailIntent;
    }
}