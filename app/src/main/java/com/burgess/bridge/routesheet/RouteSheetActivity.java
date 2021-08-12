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
import android.util.Log;
import android.widget.Button;

import com.android.volley.toolbox.JsonArrayRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.base.BaseActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

public class RouteSheetActivity extends AppCompatActivity implements OnDragListener {
    private RouteSheetViewModel mRouteSheetViewModel;
    private SharedPreferences mSharedPreferences;
    private ItemTouchHelper mItemTouchHelper;

    private static final String TAG = "ROUTE_SHEET";
    private JsonArrayRequest mUpdateRouteSheetRequest;
    private String mInspectorId;
    private String mVersionName;
    private ConstraintLayout mConstraintLayout;
    private Button mButtonUpdateRouteSheet;
    private Button mButtonPrintRouteSheet;
    private Button mButtonSendActivityLog;
    private RecyclerView mRecyclerInspections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));

        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersionName = pInfo.versionName;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        mUpdateRouteSheetRequest = BridgeAPIQueue.getInstance().updateRouteSheet(mRouteSheetViewModel, mInspectorId, formatter.format(LocalDateTime.now()));
        BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateRouteSheetRequest);
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.route_sheet_constraint_layout);
        mButtonUpdateRouteSheet = findViewById(R.id.route_sheet_button_update_route_sheet);
        mButtonPrintRouteSheet = findViewById(R.id.route_sheet_button_print_route_sheet);
        mButtonSendActivityLog = findViewById(R.id.route_sheet_button_send_log);
        mRecyclerInspections = findViewById(R.id.route_sheet_list_inspections);
    }

    private void initializeButtonListeners() {
        mButtonUpdateRouteSheet.setOnClickListener(v -> {
            BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateRouteSheetRequest);
            Snackbar.make(mConstraintLayout, "Route sheet updated.", Snackbar.LENGTH_SHORT).show();
        });

        mButtonSendActivityLog.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Sending activity log...", Snackbar.LENGTH_SHORT).show();
            Intent emailIntent = BridgeLogger.sendLogFile(mInspectorId, mVersionName);
            startActivity(Intent.createChooser(emailIntent, "Send activity log..."));
        });

        mButtonPrintRouteSheet.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Printing route sheet not yet available.", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void initializeDisplayContent() {
        RouteSheetListAdapter routeSheetListAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
        mRecyclerInspections.setAdapter(routeSheetListAdapter);
        routeSheetListAdapter.setDragListener(this);
        mRecyclerInspections.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerInspections.getItemAnimator().setChangeDuration(0);

        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);
        mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL"))).observe(this, inspections -> {
            routeSheetListAdapter.submitList(inspections);
            routeSheetListAdapter.setCurrentList(inspections);
        });

        ItemTouchHelper.Callback callback = new RouteSheetRecyclerViewTouchHelperCallback(routeSheetListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerInspections);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}