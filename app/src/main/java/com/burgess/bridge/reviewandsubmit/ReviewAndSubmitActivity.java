package com.burgess.bridge.reviewandsubmit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Enums.IncompleteReason;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;

public class ReviewAndSubmitActivity extends AppCompatActivity {
    public static final String UPLOAD_URL = "https://apistage.burgess-inc.com/api/Bridge/InsertInspectionDetails/";
    private ReviewAndSubmitViewModel mReviewAndSubmitViewModel;
    private SharedPreferences mSharedPreferences;
    private LiveData<Inspection_Table> mInspection;

    public static final String TAG = "REVIEW_AND_SUBMIT";
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mLocationId;
    private boolean mSupervisorPresent;
    private boolean mStatusCorrect;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerInspectionDefects;
    private TextView mTextAddress;
    private StringRequest mUploadInspectionDataRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar(findViewById(R.id.review_and_submit_toolbar));
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mReviewAndSubmitViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReviewAndSubmitViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mReviewAndSubmitViewModel.getInspection(mInspectionId);
        mTextAddress = findViewById(R.id.review_and_submit_text_address);
        mSupervisorPresent = false;
        mStatusCorrect = false;

        mLockScreen = findViewById(R.id.review_and_submit_lock_screen);
        mProgressBar = findViewById(R.id.review_and_submit_progress_bar);
        mRecyclerInspectionDefects = findViewById(R.id.review_and_submit_recycler_inspection_defects);
        final ReviewAndSubmitListAdapter adapter = new ReviewAndSubmitListAdapter(new ReviewAndSubmitListAdapter.ReviewAndSubmitDiff());
        mRecyclerInspectionDefects.setAdapter(adapter);
        mRecyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));
        List<InspectionDefect_Table> list = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);
        adapter.submitList(mReviewAndSubmitViewModel.getInspectionDefectsForReviewSync(mInspectionId));

        displayAddress(mTextAddress);

        Button buttonSubmit = findViewById(R.id.review_and_submit_button_submit);
        buttonSubmit.setOnClickListener(v -> {
            Log.d("SUBMIT", "Submit clicked, show dialog");
            new AlertDialog.Builder(this)
                    .setTitle("Is the following resolution accurate?")
                    .setMessage("FAILED")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        mStatusCorrect = true;
                        try {
                            completeInspection();
                        } catch (JSONException e) {
                            Log.i(TAG, "Yo fuck this... " + e.getMessage());
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> showEditResolutionDialog())
                    .show();

            new AlertDialog.Builder(this)
                    .setTitle("Supervisor Present?")
                    .setMessage("Was the supervisor present?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> mSupervisorPresent = true)
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
        mProgressBar.setVisibility(View.VISIBLE);
        mLockScreen.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestQueue queue = BridgeAPIQueue.getInstance(this).getRequestQueue();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);

        JSONArray jArray = new JSONArray();
        JSONObject jObj;
        InspectionDefect_Table inspection;
        for(int lcv = 0; lcv < inspectionDefects.size(); lcv++) {
            inspection = inspectionDefects.get(lcv);
            jObj = new JSONObject();
            jObj.put("InspectionId", inspection.inspection_id);
            jObj.put("DefectItemId", inspection.defect_item_id);
            jObj.put("DefectStatusId", inspection.defect_status_id);
            if (inspection.comment != null) {
                jObj.put("Comment", inspection.comment);
            } else {
                jObj.put("Comment", "");
            }
            jObj.put("Comment", inspection.comment);
            if (inspection.picture_path != null) {
                jObj.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(inspection.id)));
                jObj.put("ImageFileName", inspection.picture_path.substring(inspection.picture_path.lastIndexOf("/")+1));
            } else {
                jObj.put("ImageData", null);
                jObj.put("ImageFileName", null);
            }
            jArray.put(jObj);
        }

        mUploadInspectionDataRequest = uploadInspectionData(UPLOAD_URL, jArray, new ServerCallback() {
            @Override
            public void onSuccess() {
                mReviewAndSubmitViewModel.completeInspection(mInspectionId);
                returnToRouteSheet();
                mProgressBar.setVisibility(View.GONE);
                mLockScreen.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "Error in uploadInspectionData");
                mProgressBar.setVisibility(View.GONE);
                mLockScreen.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        mUploadInspectionDataRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30 * 1000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(mUploadInspectionDataRequest);
    }

    private StringRequest uploadInspectionData(String url, JSONArray jArray, final ServerCallback callBack) {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Uploaded " + response + " defects.");
            callBack.onSuccess();
        }, error -> {
            Log.i(TAG, error.getMessage());
            callBack.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }

            @Override
            public byte[] getBody() {
                return jArray.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        return request;
    }

    private byte[] getPictureData(int inspectionDefectId) {
        InspectionDefect_Table defect = mReviewAndSubmitViewModel.getInspectionDefect(inspectionDefectId);
        Bitmap image = BitmapFactory.decodeFile(defect.picture_path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    private void showEditResolutionDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_resolution, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Change Resolution");
        builder.create().show();

        Spinner spinnerResolutions = view.findViewById(R.id.dialog_edit_resolution_spinner_resolutions);
        spinnerResolutions.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, IncompleteReason.values()));

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