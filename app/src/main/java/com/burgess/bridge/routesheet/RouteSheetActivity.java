package com.burgess.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetActivity extends AppCompatActivity {
    private RouteSheetViewModel routeSheetVM;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private TextView tRefreshStatus;
    private TextView tSearchCommunity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rInspections;
    private Toolbar toolbar;
    private TextView tToolbarIndividualRemaining;
    private TextView tToolbarTeamRemaining;

    private String mInspectorId;
    private boolean mIsOnline;

    private static final String TAG = "ROUTE_SHEET";
    //private static BridgeAPIQueue apiQueue;
    private RouteSheetListAdapter mRouteSheetListAdapter;
    private LiveData<List<RouteSheet_View>> mInspectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        routeSheetVM = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);

        // Prepare shared preferences...
        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Prepare Logger
        BridgeLogger.getInstance(this);

        mInspectorId = sharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
        mIsOnline = sharedPreferences.getBoolean(PREF_IS_ONLINE, true);

        initializeViews();
        initializeButtonListeners();
        initializeTextListeners();
        initializeSwipeRefresh();
        initializeDisplayContent();

        routeSheetVM.getSnackbarMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
        routeSheetVM.getStatusMessage().observe(this, message -> {
            if (message != null) {
                tRefreshStatus.setText(message);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.route_sheet_menu, menu);
        return true;
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.route_sheet_constraint_layout);
        tRefreshStatus = findViewById(R.id.route_sheet_text_refresh_status);
        tSearchCommunity = findViewById(R.id.route_sheet_text_search_community);
        swipeRefreshLayout = findViewById(R.id.route_sheet_swipe_refresh);
        rInspections = findViewById(R.id.route_sheet_list_inspections);
        toolbar = findViewById(R.id.route_sheet_toolbar);
        tToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        tToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
    }
    private void initializeButtonListeners() {
        toolbar.setOnMenuItemClickListener(v -> {
            if (v.getItemId() == R.id.route_sheet_menu_send_activity_log) {
                Snackbar.make(constraintLayout, "Sending activity log...", Snackbar.LENGTH_SHORT).show();
                try {
                    Intent emailIntent = BridgeLogger.sendLogFile(mInspectorId, getVersionName());
                    startActivity(Intent.createChooser(emailIntent, "Send activity log..."));
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
                }
            }
            else if (v.getItemId() == R.id.route_sheet_menu_print_route_sheet) {
                Intent showRouteSheetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.burgess-inc.com" + routeSheetVM.getReportUrl()));
                startActivity(showRouteSheetIntent);
            }
            else if (v.getItemId() == R.id.route_sheet_menu_logout) {
                sharedPreferencesEditor.remove(PREF_AUTH_TOKEN);
                sharedPreferencesEditor.remove(PREF_SECURITY_USER_ID);
                sharedPreferencesEditor.remove(PREF_INSPECTOR_ID);
                sharedPreferencesEditor.remove(PREF_INSPECTOR_DIVISION_ID);
                sharedPreferencesEditor.remove(PREF_LOGIN_NAME);
                sharedPreferencesEditor.remove(PREF_LOGIN_PASSWORD);
                sharedPreferencesEditor.remove(PREF_AUTH_TOKEN_AGE);
                sharedPreferencesEditor.apply();

                Intent loginIntent = new Intent(RouteSheetActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
            return false;
        });
    }
    private void initializeTextListeners() {
        tSearchCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRouteSheetListAdapter.getFilter().filter(tSearchCommunity.getText());
                mRouteSheetListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void initializeSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::updateRouteSheet);
    }
    private void initializeDisplayContent() {
        try {
            mInspectionList = routeSheetVM.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId));
            mRouteSheetListAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff(), routeSheetVM);
            mRouteSheetListAdapter.setAuthToken(sharedPreferences.getString(PREF_AUTH_TOKEN, ""));
            mRouteSheetListAdapter.setInspectorId(mInspectorId);
            rInspections.setAdapter(mRouteSheetListAdapter);
            rInspections.setLayoutManager(new LinearLayoutManager(this));
            Objects.requireNonNull(rInspections.getItemAnimator()).setChangeDuration(0);
            ItemTouchHelper.Callback callback = new RouteSheetTouchHelper(mRouteSheetListAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rInspections);
            routeSheetVM.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId)).observe(this, inspections -> {
                mRouteSheetListAdapter.setCurrentList(inspections);
                tToolbarIndividualRemaining.setText(Integer.toString(inspections.size()));
            });
        } catch (Exception e) {
            Snackbar.make(constraintLayout, "Error in loading route sheet, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
        updateRouteSheet();
    }

    private String getVersionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            BridgeLogger.log('E', TAG, "ERROR in getVersionName: " + e.getMessage());
        }
        return pInfo.versionName != null ? pInfo.versionName : "VERSION NOT FOUND";
    }

    private void updateRouteSheet() {
        routeSheetVM.updateInspections(sharedPreferences.getString(PREF_AUTH_TOKEN, ""), mInspectorId);
        routeSheetVM.updateRouteSheetIndexes(mRouteSheetListAdapter.getCurrentList());
        routeSheetVM.clearCompletedAndFutureDatedInspections(sharedPreferences.getString(PREF_AUTH_TOKEN, ""), Integer.parseInt(mInspectorId));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routeSheetVM.updateRouteSheetIndexes(mRouteSheetListAdapter.getCurrentList());
    }
    @Override
    protected void onPause() {
        super.onPause();
        routeSheetVM.updateRouteSheetIndexes(mRouteSheetListAdapter.getCurrentList());
    }
}