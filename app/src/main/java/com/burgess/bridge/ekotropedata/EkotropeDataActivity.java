package com.burgess.bridge.ekotropedata;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import android.os.SystemClock;
import android.widget.Button;

public class EkotropeDataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mInspectorId;
    private Button mButtonEmail;
    private static final String TAG = "EKOTROPE_DATA";
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

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

            BridgeLogger.log('I', TAG, "Sending Ekotrope Data...");
            try {
                Intent emailIntent = sendEkotropeEmail(mInspectorId);
                startActivity(Intent.createChooser(emailIntent, "Send Ekotrope Data..."));
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
            }
        });
    }

    public Intent sendEkotropeEmail(String inspectorId) {
        //Uri logFileUri = FileProvider.getUriForFile(ctx, "com.burgess.bridge", logFile);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String[] to = {"jwilliams@burgess-inc.com", "rsandlin@burgess-inc.com", "bwallace@burgess-inc.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(logFileUri)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ekotrope Data for Inspector: " + inspectorId + " Inspection: " + "IDText");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "JSON DATA\n Address: " + "AddressText" + "\nEkotrope Project ID: " + "IDText");
        return emailIntent;
    }
}