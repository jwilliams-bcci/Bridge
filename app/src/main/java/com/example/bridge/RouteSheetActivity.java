package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import data.RouteSheet_View;
import data.Tables.Inspection_Table;

public class RouteSheetActivity extends AppCompatActivity {
    private RouteSheetViewModel mRouteSheetViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        sharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        initializeDisplayContent();

        Button buttonOrderRouteSheet = findViewById(R.id.route_sheet_button_order_route_sheet);
        buttonOrderRouteSheet.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Order route sheet", Toast.LENGTH_LONG).show();
        });

        Button buttonUpdateRouteSheet = findViewById(R.id.route_sheet_button_update_route_sheet);
        buttonUpdateRouteSheet.setOnClickListener(v -> {
            //Toast.makeText(getApplicationContext(), "Updating route sheet for InspectorID " + sharedPreferences.getString("InspectorId", "NULL"), Toast.LENGTH_LONG).show();
            updateRouteSheet();
            Toast.makeText(getApplicationContext(), "Route sheet updated.", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeDisplayContent() {
        RecyclerView recyclerInspections = findViewById(R.id.route_sheet_list_inspections);
        final RouteSheetListAdapter adapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff());
        recyclerInspections.setAdapter(adapter);
        recyclerInspections.setLayoutManager(new LinearLayoutManager(this));

        mRouteSheetViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);
        mRouteSheetViewModel.getAllInspectionsForRouteSheet().observe(this, inspections ->
                adapter.submitList(inspections));
    }

    private void updateRouteSheet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY");
        Inspection_Table inspection = new Inspection_Table();
        DateFormat format = new SimpleDateFormat("MM-dd-YYYY", Locale.ENGLISH);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://apistage.burgess-inc.com/api/Bridge/GetInspections?inspectorid="
                + sharedPreferences.getString("InspectorId", "NULL")
                + "&inspectiondate=" + formatter.format(LocalDateTime.now());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        inspection.id = obj.optInt("InspectionID");
                        inspection.inspection_date = format.parse(obj.getString("InspectionDate"));
                        inspection.location_id = obj.optInt("LocationID");
                        inspection.builder_name = obj.getString("BuilderName");
                        inspection.builder_id = obj.optInt("BuilderID");
                        inspection.super_name = obj.getString("SuperName");
                        inspection.inspector = obj.getString("Inspector");
                        inspection.community = obj.getString("Community");
                        inspection.community_id = obj.optInt("CommunityID");
                        inspection.city = obj.getString("City");
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

                        mRouteSheetViewModel.insert(inspection);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error with getting JSON " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Error in parsing date", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Shit's fucked, yo " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        queue.add(request);
    }
}