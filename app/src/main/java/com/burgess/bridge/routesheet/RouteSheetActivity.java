package com.burgess.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import data.Tables.InspectionDefect_Table;

import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

public class RouteSheetActivity extends AppCompatActivity implements OnStartDragListener {
    private RouteSheetViewModel mRouteSheetViewModel;
    private SharedPreferences mSharedPreferences;
    private ImageView mReorderHandle;
    private ImageView mReupload;
    private ItemTouchHelper mItemTouchHelper;

    private static final String TAG = "ROUTE_SHEET";
    private JsonArrayRequest mUpdateRouteSheetRequest;
    private String mInspectorId;
    private String mVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mReorderHandle = findViewById(R.id.item_inspection_list_imageview_reorder_handle);
        mReupload = findViewById(R.id.item_inspection_list_imageview_reupload);
        RequestQueue queue = BridgeAPIQueue.getInstance(RouteSheetActivity.this).getRequestQueue();

        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersionName = pInfo.versionName;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        initializeDisplayContent();
        mUpdateRouteSheetRequest = BridgeAPIQueue.getInstance().updateRouteSheet(mRouteSheetViewModel, mInspectorId, formatter.format(LocalDateTime.now()));
        queue.add(mUpdateRouteSheetRequest);

        Button buttonSendActivityLog = findViewById(R.id.route_sheet_button_send_log);
        buttonSendActivityLog.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Sending activity log", Toast.LENGTH_LONG).show();
            Intent emailIntent = BridgeLogger.sendLogFile(mInspectorId, mVersionName);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        Button buttonPrintRouteSheet = findViewById(R.id.route_sheet_button_print_route_sheet);
        buttonPrintRouteSheet.setOnClickListener(v -> {
        });

        Button buttonUpdateRouteSheet = findViewById(R.id.route_sheet_button_update_route_sheet);
        buttonUpdateRouteSheet.setOnClickListener(v -> {
            queue.add(mUpdateRouteSheetRequest);
            Toast.makeText(getApplicationContext(), "Route sheet updated.", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeDisplayContent() {
        RecyclerView recyclerInspections = findViewById(R.id.route_sheet_list_inspections);
        final RouteSheetListAdapter adapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
        recyclerInspections.setAdapter(adapter);
        adapter.setDragListener(this);
        recyclerInspections.setLayoutManager(new LinearLayoutManager(this));
        recyclerInspections.getItemAnimator().setChangeDuration(0);

        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);
        mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL"))).observe(this, inspections -> {
            adapter.submitList(inspections);
            adapter.setCurrentList(inspections);
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerInspections);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEndDrag(RecyclerView.ViewHolder viewHolder) {
    }
}