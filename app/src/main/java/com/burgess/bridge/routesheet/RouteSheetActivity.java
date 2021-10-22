package com.burgess.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;

public class RouteSheetActivity extends AppCompatActivity implements OnDragListener {
    private RouteSheetViewModel mRouteSheetViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ItemTouchHelper mItemTouchHelper;
    private TextView mTextSearchCommunity;
    private Switch mSwitchSearchReinspects;
    private Button mButtonUpdateRouteSheet;
    private Button mButtonPrintRouteSheet;
    private Button mButtonSendActivityLog;
    private RecyclerView mRecyclerInspections;

    private JsonArrayRequest mUpdateRouteSheetRequest;
    private JsonObjectRequest mCheckInspectionDatesRequest;
    private String mInspectorId;
    private boolean mIsOnline;

    private static final String TAG = "ROUTE_SHEET";
    private static BridgeAPIQueue apiQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger and API Queue
        BridgeLogger.getInstance(this);
        apiQueue = BridgeAPIQueue.getInstance(this);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
        mIsOnline = mSharedPreferences.getBoolean(PREF_IS_ONLINE, true);
        mUpdateRouteSheetRequest = apiQueue.updateRouteSheet(mRouteSheetViewModel, mInspectorId, formatter.format(LocalDateTime.now()));

        initializeViews();
        initializeButtonListeners();
        initializeTextListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.route_sheet_constraint_layout);
        mButtonUpdateRouteSheet = findViewById(R.id.route_sheet_button_update_route_sheet);
        mButtonPrintRouteSheet = findViewById(R.id.route_sheet_button_print_route_sheet);
        mButtonSendActivityLog = findViewById(R.id.route_sheet_button_send_log);
        mTextSearchCommunity = findViewById(R.id.route_sheet_text_search_community);
        mSwitchSearchReinspects = findViewById(R.id.route_sheet_switch_search_reinspects);
        mRecyclerInspections = findViewById(R.id.route_sheet_list_inspections);
    }
    private void initializeButtonListeners() {
        mButtonUpdateRouteSheet.setOnClickListener(v -> {
            updateRouteSheet();
        });

        mButtonSendActivityLog.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Sending activity log...", Snackbar.LENGTH_SHORT).show();
            try {
                Intent emailIntent = BridgeLogger.sendLogFile(mInspectorId, getVersionName());
                startActivity(Intent.createChooser(emailIntent, "Send activity log..."));
            } catch (Exception e) {
                BridgeLogger.getInstance().log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
            }
        });

        mButtonPrintRouteSheet.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Printing route sheet not yet available.", Snackbar.LENGTH_SHORT).show();
        });
    }
    private void initializeTextListeners() {
        mTextSearchCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("SEARCH", "beforeTextChange: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("SEARCH", "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("SEARCH", "afterTextChanged: " + s.toString());
            }
        });
    }
    private void initializeDisplayContent() {
        try {
            RouteSheetListAdapter routeSheetListAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
            mRecyclerInspections.setAdapter(routeSheetListAdapter);
            routeSheetListAdapter.setDragListener(this);
            mRecyclerInspections.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerInspections.getItemAnimator().setChangeDuration(0);
            mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId)).observe(this, inspections -> {
                routeSheetListAdapter.getFilter().filter("AAA");
                routeSheetListAdapter.submitList(inspections);
                routeSheetListAdapter.setCurrentList(inspections);
            });

            ItemTouchHelper.Callback callback = new RouteSheetRecyclerViewTouchHelperCallback(routeSheetListAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerInspections);
        } catch (Exception e) {
            Snackbar.make(mConstraintLayout, "Error in loading route sheet, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }

        updateRouteSheet();
    }

    private String getVersionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            BridgeLogger.getInstance(this).log('E', TAG, "ERROR in getVersionName: " + e.getMessage());
        }
        return pInfo.versionName != null ? pInfo.versionName : "VERSION NOT FOUND";
    }

    private void updateRouteSheet() {
        if (mIsOnline) {
            Snackbar.make(mConstraintLayout, "Route sheet updating...", Snackbar.LENGTH_LONG).show();
            BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateRouteSheetRequest);
            List<Integer> allInspectionIds = mRouteSheetViewModel.getAllInspectionIds(Integer.parseInt(mInspectorId));
            ArrayList<JsonObjectRequest> checkDateRequests = new ArrayList<>();
            for (int lcv = 0; lcv < allInspectionIds.size(); lcv++) {
                checkDateRequests.add(apiQueue.checkExistingInspection(mRouteSheetViewModel, allInspectionIds.get(lcv), Integer.parseInt(mInspectorId)));
            }
            for (int lcv = 0; lcv < checkDateRequests.size(); lcv++) {
                apiQueue.getRequestQueue().add(checkDateRequests.get(lcv));
            }
        } else {
            Snackbar.make(mConstraintLayout, "Cannot update route sheet! Currently offline", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}