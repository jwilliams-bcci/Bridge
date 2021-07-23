package com.burgess.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.Constants;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

public class RouteSheetActivity extends AppCompatActivity implements OnStartDragListener {
    private RouteSheetViewModel mRouteSheetViewModel;
    private SharedPreferences mSharedPreferences;
    private ImageView mReorderHandle;
    private ItemTouchHelper mItemTouchHelper;

    private static final String TAG = "ROUTE_SHEET";
    private JsonArrayRequest mUpdateRouteSheetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mReorderHandle = findViewById(R.id.item_inspection_list_imageview_reorder_handle);
        RequestQueue queue = BridgeAPIQueue.getInstance(RouteSheetActivity.this).getRequestQueue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        String inspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        initializeDisplayContent();
        mUpdateRouteSheetRequest = BridgeAPIQueue.getInstance().updateRouteSheet(mRouteSheetViewModel, inspectorId, formatter.format(LocalDateTime.now()));
        queue.add(mUpdateRouteSheetRequest);

        Button buttonOrderRouteSheet = findViewById(R.id.route_sheet_button_order_route_sheet);
        buttonOrderRouteSheet.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Order route sheet", Toast.LENGTH_LONG).show();
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
            //adapter.notifyDataSetChanged();
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerInspections);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
        int pos = viewHolder.getAdapterPosition() + 1;
        Log.d(TAG, "Position of drag is " + pos);
    }

    @Override
    public void onEndDrag(RecyclerView.ViewHolder viewHolder) {
        int pos = viewHolder.getAdapterPosition() + 1;
        Log.d(TAG, "Position of drag is " + pos);
    }
}