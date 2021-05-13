package com.burgess.bridge.reviewandsubmit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Enums.IncompleteReason;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class ReviewAndSubmitActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final String LOCATION_ID = "com.example.bridge.LOCATION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private boolean mSupervisorPresent;
    private boolean mStatusCorrect;
    private ReviewAndSubmitViewModel mReviewAndSubmitViewModel;
    private SharedPreferences mSharedPreferences;
    private LiveData<Inspection_Table> mInspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar((Toolbar) findViewById(R.id.review_and_submit_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.review_and_submit_text_address);

        mReviewAndSubmitViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReviewAndSubmitViewModel.class);
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        //mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSupervisorPresent = false;
        mStatusCorrect = false;
        mInspection = mReviewAndSubmitViewModel.getInspection(mInspectionId);

        displayAddress(textAddress);

        RecyclerView recyclerInspectionDefects = findViewById(R.id.review_and_submit_recycler_inspection_defects);
        final ReviewAndSubmitListAdapter adapter = new ReviewAndSubmitListAdapter(new ReviewAndSubmitListAdapter.ReviewAndSubmitDiff());
        recyclerInspectionDefects.setAdapter(adapter);
        recyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));
        mReviewAndSubmitViewModel.getInspectionDefectsForReview(mInspectionId).observe(this, inspectionDefects ->
                adapter.submitList(inspectionDefects));

        Button buttonSubmit = findViewById(R.id.review_and_submit_button_submit);
        buttonSubmit.setOnClickListener(v -> {
            Log.d("SUBMIT", "Submit clicked, show dialog");
            new AlertDialog.Builder(this)
                    .setTitle("Is the following resolution accurate?")
                    .setMessage("FAILED")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mStatusCorrect = true;
                            try {
                                completeInspection();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            returnToRouteSheet();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showEditResolutionDialog();
                        }
                    })
                    .show();

            new AlertDialog.Builder(this)
                    .setTitle("Supervisor Present?")
                    .setMessage("Was the supervisor present?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSupervisorPresent = true;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void displayAddress(TextView textAddress) {
        textAddress.setText("");
        mInspection.observe(this, inspection -> {
            textAddress.append(inspection.community + "\n");
            textAddress.append(inspection.address + "\n");
            textAddress.append(inspection.inspection_type);
        });
    }

    private void completeInspection() throws JSONException {
        RequestQueue queue = BridgeAPIQueue.getInstance(this).getRequestQueue();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);

        JSONArray json = new JSONArray();
        JSONObject obj;
        InspectionDefect_Table insp;
        for(int lcv = 0; lcv < inspectionDefects.size(); lcv++) {
            insp = inspectionDefects.get(lcv);
            obj = new JSONObject();
            obj.put("InspectionId", insp.inspection_id);
            obj.put("DefectItemId", insp.defect_item_id);
            obj.put("DefectStatusId", insp.defect_status_id);
            obj.put("Comment", insp.comment);
            obj.put("ImageData", "");
            obj.put("ImageFileName", "Test File Name");
            json.put(obj);
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, "https://apistage.burgess-inc.com/api/Bridge/InsertInspectionDetails", json, response ->  {

        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };

        queue.add(request);
    }

    private void showEditResolutionDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_resolution, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Change Resolution");
        builder.create().show();

        Spinner spinnerResolutions = view.findViewById(R.id.dialog_edit_resolution_spinner_resolutions);
        spinnerResolutions.setAdapter(new ArrayAdapter<IncompleteReason>(this, android.R.layout.simple_spinner_item, IncompleteReason.values()));

        Button buttonSaveResolution = view.findViewById(R.id.dialog_edit_resolution_button_save);
        buttonSaveResolution.setOnClickListener(v -> {
            returnToRouteSheet();
        });
    }

    private void returnToRouteSheet() {
        Intent routeSheetIntent = new Intent(ReviewAndSubmitActivity.this, RouteSheetActivity.class);
        startActivity(routeSheetIntent);
    }
}