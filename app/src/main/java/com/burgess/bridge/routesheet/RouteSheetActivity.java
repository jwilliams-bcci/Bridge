package com.burgess.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

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

    private JsonArrayRequest mUpdateRouteSheetRequest;
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
        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Prepare Logger and API Queue
        BridgeLogger.getInstance(this);
        apiQueue = BridgeAPIQueue.getInstance(this);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
        mIsOnline = mSharedPreferences.getBoolean(PREF_IS_ONLINE, true);
        mUpdateRouteSheetRequest = apiQueue.updateRouteSheet(mRouteSheetViewModel, mInspectorId, formatter.format(OffsetDateTime.now()));

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
                Snackbar.make(mConstraintLayout, "Printing route sheet not yet available.", Snackbar.LENGTH_SHORT).show();
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
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            updateRouteSheet();
        });
    }
    private void initializeDisplayContent() {
        try {
            mInspectionList = mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId));
            mRouteSheetListAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
            mRecyclerInspections.setAdapter(mRouteSheetListAdapter);
            mRecyclerInspections.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerInspections.getItemAnimator().setChangeDuration(0);
            ItemTouchHelper.Callback callback = new RouteSheetTouchHelper(mRouteSheetListAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerInspections);
            mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mInspectorId)).observe(this, inspections -> {
                mRouteSheetListAdapter.setCurrentList(inspections);
                Log.i("SEARCH", "Size of inspections:" + inspections.size());
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
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<RouteSheet_View> routeSheetList = mRouteSheetListAdapter.getCurrentList();
        for (int lcv = 0; lcv < routeSheetList.size(); lcv++) {
            mRouteSheetViewModel.updateRouteSheetIndex(routeSheetList.get(lcv).id, lcv);
        }
    }
}