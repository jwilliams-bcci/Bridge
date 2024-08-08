package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.ekotropedata.EkotropeDataActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

import com.burgess.bridge.inspect.InspectListAdapter;
import com.burgess.bridge.inspect.InspectViewModel;
import com.burgess.bridge.inspect.ReinspectListAdapter;

import android.os.SystemClock;
import android.widget.Button;

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
    private InspectViewModel mInspectViewModel;

    public static final String SCROLL_POSITION = "com.burgess.bridge.SCROLL_POSITION";
    public static final int SCROLL_POSITION_NOT_FOUND = -1;
    public static final String FILTER_OPTION = "com.burgess.bridge.FILTER_OPTION";

    public int mScrollPosition;
    public int mInspectionTypeId;
    public boolean mReinspection;
    public String mFilter;
    private SharedPreferences.Editor mEditor;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private Spinner mSpinnerDefectCategories;
    private Button mButtonSaveAndExit;
    private Button mButtonSortItemNumber;
    private Button mButtonSortDescription;
    private Button mButtonAddNote;
    private Button mButtonViewEkotropeData;
    private RecyclerView mRecyclerDefectItems;
    private InspectListAdapter mInspectListAdapter;
    private ReinspectListAdapter mReinspectListAdapter;
    private TextView mTextAddress;
    private TextView mTextTotalDefectCountLabel;
    private TextView mTextTotalDefectCount;
    private Button mButtonReviewAndSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        mInspectViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mScrollPosition = intent.getIntExtra(SCROLL_POSITION, SCROLL_POSITION_NOT_FOUND);
        mFilter = intent.getStringExtra(FILTER_OPTION) != null ? intent.getStringExtra(FILTER_OPTION) : "ALL";
        mInspection = mInspectViewModel.getInspectionSync(mInspectionId);
        mReinspection = mInspectViewModel.getReinspect(mInspectionId);
        mInspectionTypeId = mInspection.inspection_type_id;

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
        emailIntent.putExtra(Intent.EXTRA_TEXT, "JSON DATA\n Address: " + mInspection.address + "\nEkotrope Project ID: " + "IDText");
        return emailIntent;
    }
}