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
import com.burgess.bridge.BridgeAPIQueue;
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
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetActivity extends AppCompatActivity {
    private RouteSheetViewModel mRouteSheetViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView mTextSearchCommunity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerInspections;
    private Toolbar mToolbar;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;

    private JsonArrayRequest mUpdateRouteSheetRequest;
    private JsonArrayRequest mUpdateDefectItemsRequest;
    private JsonArrayRequest mUpdateDIITRequest;
    private JsonArrayRequest mUpdatePastInspections;
    private JsonObjectRequest mUpdateInspectionsRemainingRequest;
    private JsonObjectRequest mUpdateEkotropePlansId;
    private StringRequest mUpdateReportDataRequest;
    private String mInspectorId;
    private boolean mIsOnline;

    private static final String TAG = "ROUTE_SHEET";
    private static BridgeAPIQueue apiQueue;
    private RouteSheetListAdapter mRouteSheetListAdapter;
    private LiveData<List<RouteSheet_View>> mInspectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mRouteSheetViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger and API Queue
        BridgeLogger.getInstance(this);
        apiQueue = BridgeAPIQueue.getInstance(this);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = formatter.format(OffsetDateTime.now());
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
        mIsOnline = mSharedPreferences.getBoolean(PREF_IS_ONLINE, true);
        mUpdateRouteSheetRequest = apiQueue.updateRouteSheet(mRouteSheetViewModel, mInspectorId, currentDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
        mUpdateDefectItemsRequest = apiQueue.updateDefectItemsV2(mRouteSheetViewModel, mInspectorId, currentDate);
        mUpdateDIITRequest = apiQueue.updateDefectItem_InspectionTypeXRefV2(mRouteSheetViewModel, mInspectorId, currentDate);
        mUpdateInspectionsRemainingRequest = apiQueue.updateInspectionsRemaining(mRouteSheetViewModel, mInspectorId, currentDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                mTextToolbarTeamRemaining.setText(Integer.toString(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));
            }

            @Override
            public void onFailure(String message) {

            }
        });
        mUpdateReportDataRequest = apiQueue.getReportData(mRouteSheetViewModel, Integer.parseInt(mInspectorId), currentDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                Snackbar.make(mConstraintLayout, "Route sheet available for printing", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(mConstraintLayout, "Error downloading route sheet for printing, please refresh", Snackbar.LENGTH_SHORT).show();
            }
        });

        initializeViews();
        initializeButtonListeners();
        initializeTextListeners();
        initializeSwipeRefresh();
        initializeDisplayContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.route_sheet_menu, menu);
        return true;
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.route_sheet_constraint_layout);
        mTextSearchCommunity = findViewById(R.id.route_sheet_text_search_community);
        mSwipeRefreshLayout = findViewById(R.id.route_sheet_swipe_refresh);
        mRecyclerInspections = findViewById(R.id.route_sheet_list_inspections);
        mToolbar = findViewById(R.id.route_sheet_toolbar);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
    }
    private void initializeButtonListeners() {
        mToolbar.setOnMenuItemClickListener(v -> {
            if (v.getItemId() == R.id.route_sheet_menu_send_activity_log) {
                Snackbar.make(mConstraintLayout, "Sending activity log...", Snackbar.LENGTH_SHORT).show();
                try {
                    Intent emailIntent = BridgeLogger.sendLogFile(mInspectorId, getVersionName());
                    startActivity(Intent.createChooser(emailIntent, "Send activity log..."));
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
                }
            }
            else if (v.getItemId() == R.id.route_sheet_menu_print_route_sheet) {
                Intent showRouteSheetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.burgess-inc.com" + mRouteSheetViewModel.getReportUrl()));
                startActivity(showRouteSheetIntent);
            }
            else if (v.getItemId() == R.id.route_sheet_menu_logout) {
                mEditor.remove(PREF_AUTH_TOKEN);
                mEditor.remove(PREF_SECURITY_USER_ID);
                mEditor.remove(PREF_INSPECTOR_ID);
                mEditor.remove(PREF_INSPECTOR_DIVISION_ID);
                mEditor.remove(PREF_LOGIN_NAME);
                mEditor.remove(PREF_LOGIN_PASSWORD);
                mEditor.remove(PREF_AUTH_TOKEN_AGE);
                mEditor.apply();

                Intent loginIntent = new Intent(RouteSheetActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
            return false;
        });
    }
    private void initializeTextListeners() {
        mTextSearchCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRouteSheetListAdapter.getFilter().filter(mTextSearchCommunity.getText());
                mRouteSheetListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void initializeSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this::updateRouteSheet);
    }
    private void initializeDisplayContent() {
        try {
            mInspectionList = mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId));
            mRouteSheetListAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
            mRecyclerInspections.setAdapter(mRouteSheetListAdapter);
            mRecyclerInspections.setLayoutManager(new LinearLayoutManager(this));
            Objects.requireNonNull(mRecyclerInspections.getItemAnimator()).setChangeDuration(0);
            ItemTouchHelper.Callback callback = new RouteSheetTouchHelper(mRouteSheetListAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerInspections);
            mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId)).observe(this, inspections -> {
                mRouteSheetListAdapter.setCurrentList(inspections);
                mTextToolbarIndividualRemaining.setText(Integer.toString(inspections.size()));
            });
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
        try {
            if (mIsOnline) {
                List<RouteSheet_View> routeSheetList = mRouteSheetListAdapter.getCurrentList();
                if (routeSheetList != null) {
                    for (int lcv = 0; lcv < routeSheetList.size(); lcv++) {
                        mRouteSheetViewModel.updateRouteSheetIndex(routeSheetList.get(lcv).id, lcv);
                    }
                    mEditor.putInt(PREF_IND_INSPECTIONS_REMAINING, routeSheetList.size());
                    mEditor.apply();
                }

                Snackbar.make(mConstraintLayout, "Route sheet updating...", Snackbar.LENGTH_SHORT).show();
                apiQueue.getRequestQueue().add(mUpdateRouteSheetRequest);
                apiQueue.getRequestQueue().add(mUpdateDefectItemsRequest);
                apiQueue.getRequestQueue().add(mUpdateDIITRequest);
                apiQueue.getRequestQueue().add(mUpdateInspectionsRemainingRequest);
                apiQueue.getRequestQueue().add(mUpdateReportDataRequest);
                List<Integer> allInspectionIds = mRouteSheetViewModel.getAllInspectionIds(Integer.parseInt(mInspectorId));
                ArrayList<JsonObjectRequest> checkDateRequests = new ArrayList<>();
                for (int lcv = 0; lcv < allInspectionIds.size(); lcv++) {
                    Inspection_Table inspection = mRouteSheetViewModel.getInspection(allInspectionIds.get(lcv));
                    if (inspection.is_uploaded) {
                        mRouteSheetViewModel.deleteInspectionDefects(inspection.id);
                        mRouteSheetViewModel.deleteInspectionHistories(inspection.id);
                        mRouteSheetViewModel.deleteInspection(inspection.id);
                    } else {
                        checkDateRequests.add(apiQueue.checkExistingInspection(mRouteSheetViewModel, allInspectionIds.get(lcv), Integer.parseInt(mInspectorId)));
                    }
                }
                for (int lcv = 0; lcv < checkDateRequests.size(); lcv++) {
                    apiQueue.getRequestQueue().add(checkDateRequests.get(lcv));
                }
                mRouteSheetListAdapter.notifyDataSetChanged();
            } else {
                Snackbar.make(mConstraintLayout, "Cannot update route sheet! Currently offline", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(mConstraintLayout, "Error updating route sheet, please try again and send an activity log", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + e.getMessage());
        } finally {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<RouteSheet_View> routeSheetList = mRouteSheetListAdapter.getCurrentList();
        for (int lcv = 0; lcv < routeSheetList.size(); lcv++) {
            mRouteSheetViewModel.updateRouteSheetIndex(routeSheetList.get(lcv).id, lcv);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        List<RouteSheet_View> routeSheetList = mRouteSheetListAdapter.getCurrentList();
        for (int lcv = 0; lcv < routeSheetList.size(); lcv++) {
            mRouteSheetViewModel.updateRouteSheetIndex(routeSheetList.get(lcv).id, lcv);
        }
    }
}