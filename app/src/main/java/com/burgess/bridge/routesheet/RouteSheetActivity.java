package com.burgess.bridge.routesheet;

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
import com.burgess.bridge.BridgeAPIQueue;
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
    private ArrayList<Integer> mReinspectionIds;
    private ArrayList<JsonArrayRequest> mInspectionHistoryRequests;

    private static final String TAG = "ROUTE_SHEET";
    private static final String ROUTE_SHEET_URL = "https://api.burgess-inc.com/api/Bridge/GetInspections?inspectorid=%s&inspectiondate=%s";
    private static final String ROUTE_SHEET_URL_STAGE = "https://apistage.burgess-inc.com/api/Bridge/GetInspections?inspectorid=%s&inspectiondate=%s";
    private static final String INSPECTION_HISTORY_URL = "https://api.burgess-inc.com/api/Bridge/GetInspectionHistory?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private static final String INSPECTION_HISTORY_URL_STAGE = "https://api.burgess-inc.com/api/Bridge/GetInspectionHistory?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private JsonArrayRequest mUpdateRouteSheetRequest;
    private JsonArrayRequest mUpdateInspectionHistoryRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mReorderHandle = findViewById(R.id.item_inspection_list_imageview_reorder_handle);
        mReinspectionIds = new ArrayList<>();
        mInspectionHistoryRequests = new ArrayList<>();

        RequestQueue queue = BridgeAPIQueue.getInstance(RouteSheetActivity.this).getRequestQueue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        String inspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        initializeDisplayContent();
        mUpdateRouteSheetRequest = updateRouteSheet(String.format(ROUTE_SHEET_URL, inspectorId, formatter.format(LocalDateTime.now())), new ServerCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "updateRouteSheet returned success");
                if (mInspectionHistoryRequests.size() > 0) {
                    Log.i(TAG, "Reinspects included, fetching history");
                    for (int lcv = 0; lcv < mInspectionHistoryRequests.size(); lcv++) {
                        queue.add(mInspectionHistoryRequests.get(lcv));
                    }
                }
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "updateRouteSheet returned failure");
            }
        });
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

        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);
        mRouteSheetViewModel.getAllInspectionsForRouteSheet(Integer.parseInt(mSharedPreferences.getString(PREF_INSPECTOR_ID, "0"))).observe(this, inspections -> {
            adapter.submitList(inspections);
            adapter.setCurrentList(inspections);
            adapter.notifyDataSetChanged();
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerInspections);
    }

    private JsonArrayRequest updateRouteSheet(String url, final ServerCallback callBack) {
        DateFormat format = new SimpleDateFormat("MM-dd-YYYY", Locale.ENGLISH);
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
                    inspection.inspection_order = obj.optInt("InspectionOrder");
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

                    if (inspection.reinspect) {
                        mInspectionHistoryRequests.add(updateInspectionHistory(String.format(INSPECTION_HISTORY_URL, inspection.inspection_order, inspection.inspection_type_id, inspection.location_id), inspection.id, new ServerCallback() {
                            @Override
                            public void onSuccess() {
                                Log.i(TAG, "Inspection History added");
                            }

                            @Override
                            public void onFailure() {
                                Log.i(TAG, "Inspection History error");
                            }
                        }));
                    }

                    mRouteSheetViewModel.insertInspection(inspection);
                } catch (JSONException e) {
                    Log.i(TAG, "Error in parsing JSON: " + e.getMessage());
                    callBack.onFailure();
                } catch (ParseException e) {
                    Log.i(TAG, "Error in parsing date: " + e.getMessage());
                    callBack.onFailure();
                }
            }
            callBack.onSuccess();
        }, error -> {
            Log.i(TAG, "Error in updating route sheet: " + error.getMessage());
            callBack.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        return request;
    }

    private JsonArrayRequest updateInspectionHistory(String url, int inspectionId, final ServerCallback callBack) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    InspectionHistory_Table hist = new InspectionHistory_Table();
                    hist.id = obj.optInt("InspectionDetailID");
                    hist.inspection_id = inspectionId;
                    hist.previous_inspection_id = obj.optInt("InspectionID");
                    hist.defect_item_id = obj.optInt("DefectItemID");
                    hist.defect_item_number = obj.optInt("ItemNumber");
                    hist.defect_category_id = obj.optInt("DefectCategoryID");
                    hist.defect_category_name = obj.optString("CategoryName");
                    hist.defect_item_description = obj.optString("ItemDescription");
                    hist.comment = obj.optString("Comment");
                    mRouteSheetViewModel.insertInspectionHistory(hist);
                } catch (JSONException e) {
                    Log.i(TAG, "Error in parsing JSON: " + e.getMessage());
                    callBack.onFailure();
                }
            }
            callBack.onSuccess();
        }, error -> {
            Log.i(TAG, "Error in updateInspectionHistory: " + error.getMessage());
            callBack.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
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