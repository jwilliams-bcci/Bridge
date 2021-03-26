package com.example.bridge.routesheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.bridge.SimpleItemTouchHelperCallback;
import com.example.bridge.OnStartDragListener;
import com.example.bridge.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import data.Tables.Inspection_Table;

public class RouteSheetActivity extends AppCompatActivity implements OnStartDragListener {
    private RouteSheetViewModel mRouteSheetViewModel;
    private SharedPreferences mSharedPreferences;
    private ImageView mReorderHandle;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mReorderHandle = findViewById(R.id.item_inspection_list_imageview_reorder_handle);

        initializeDisplayContent();
        updateRouteSheet();

        Button buttonOrderRouteSheet = findViewById(R.id.route_sheet_button_order_route_sheet);
        buttonOrderRouteSheet.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Order route sheet", Toast.LENGTH_LONG).show();
        });

        Button buttonUpdateRouteSheet = findViewById(R.id.route_sheet_button_update_route_sheet);
        buttonUpdateRouteSheet.setOnClickListener(v -> {
            updateRouteSheet();
            Toast.makeText(getApplicationContext(), "Route sheet updated.", Toast.LENGTH_SHORT).show();
        });
    }



    private void initializeDisplayContent() {
        RecyclerView recyclerInspections = findViewById(R.id.route_sheet_list_inspections);
        final RouteSheetListAdapter adapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
        recyclerInspections.setAdapter(adapter);
        adapter.setDragListener(this);
        recyclerInspections.setLayoutManager(new LinearLayoutManager(this));

        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);
        mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mSharedPreferences.getString("InspectorId", "0"))).observe(this, inspections -> {
            adapter.submitList(inspections);
            adapter.setCurrentList(inspections);
            //adapter.notifyDataSetChanged();
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerInspections);
    }



    private void updateRouteSheet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        DateFormat format = new SimpleDateFormat("MM-dd-YYYY", Locale.ENGLISH);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://apistage.burgess-inc.com/api/Bridge/GetInspections?inspectorid=" + mSharedPreferences.getString("InspectorId", "NULL") + "&inspectiondate=" + formatter.format(LocalDateTime.now());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Inspection_Table inspection = new Inspection_Table();
                    inspection.id = obj.optInt("InspectionID");
                    inspection.inspection_date = format.parse(obj.getString("InspectionDate"));
                    inspection.location_id = obj.optInt("LocationID");
                    inspection.builder_name = obj.getString("BuilderName");
                    inspection.builder_id = obj.optInt("BuilderID");
                    inspection.super_name = obj.getString("SuperName");
                    inspection.inspector_id = obj.optInt("InspectorID");
                    inspection.inspector = obj.getString("Inspector");
                    inspection.community = obj.getString("Community");
                    inspection.community_id = obj.optInt("CommunityID");
                    inspection.city = obj.getString("City");
                    inspection.inspection_type_id = obj.optInt("InspectionTypeID");
                    inspection.inspection_type = obj.getString("InspectionType");
                    inspection.reinspect = obj.getBoolean("ReInspect");
                    inspection.address = obj.getString("Address1");
                    inspection.inspection_status_id = obj.optInt("InspectionStatusID");
                    inspection.inspection_status = obj.getString("InspectionStatus");
                    inspection.super_phone = obj.getString("SuperPhone");
                    inspection.super_email = obj.getString("SuperEmailAddress");
                    inspection.super_present = obj.optInt("SuperintendentPresent");
                    inspection.incomplete_reason = obj.getString("IncompleteReason");
                    inspection.incomplete_reason_id = obj.optInt("IncompleteReasonID");
                    inspection.notes = obj.getString("Comment");
                    inspection.is_complete = false;
                    inspection.is_uploaded = false;
                    inspection.route_sheet_order = obj.optInt("Order");

                    mRouteSheetViewModel.insert(inspection);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error with getting JSON " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Error in parsing date", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(getApplicationContext(), "Error in updating route sheet " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
        int pos = viewHolder.getAdapterPosition() + 1;
        Log.d("DRAG","Position of drag is... " + pos);
    }
}