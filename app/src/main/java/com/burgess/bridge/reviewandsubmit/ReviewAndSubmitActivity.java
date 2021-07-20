package com.burgess.bridge.reviewandsubmit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.google.android.material.snackbar.Snackbar;

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
    private ReviewAndSubmitViewModel mReviewAndSubmitViewModel;
    private LiveData<Inspection_Table> mInspection;

    public static final String TAG = "REVIEW_AND_SUBMIT";
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mInspectionStatusId;
    private boolean mSupervisorPresent;
    private boolean mStatusCorrect;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerInspectionDefects;
    private TextView mTextAddress;
    private ConstraintLayout mConstraintLayout;
    private StringRequest mUploadInspectionDataRequest;
    private StringRequest mUpdateInspectionStatusRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar(findViewById(R.id.review_and_submit_toolbar));
        mReviewAndSubmitViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReviewAndSubmitViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspectionStatusId = 12;
        mInspection = mReviewAndSubmitViewModel.getInspection(mInspectionId);
        mTextAddress = findViewById(R.id.review_and_submit_text_address);
        mSupervisorPresent = false;
        mStatusCorrect = false;

        mConstraintLayout = findViewById(R.id.review_and_submit_constraint_layout);
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
        showProgressSpinner();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);

        JSONObject jObj;
        InspectionDefect_Table defect;
        for(int lcv = 0; lcv < inspectionDefects.size(); lcv++) {
            defect = inspectionDefects.get(lcv);
            jObj = new JSONObject();
            jObj.put("InspectionId", defect.inspection_id);
            jObj.put("DefectItemId", defect.defect_item_id);
            jObj.put("DefectStatusId", defect.defect_status_id);
            if (defect.comment != null) {
                jObj.put("Comment", defect.comment);
            } else {
                jObj.put("Comment", "");
            }
            jObj.put("Comment", defect.comment);
            if (defect.picture_path != null) {
                jObj.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(defect.id)));
                jObj.put("ImageFileName", defect.picture_path.substring(defect.picture_path.lastIndexOf("/")+1));
            } else {
                jObj.put("ImageData", null);
                jObj.put("ImageFileName", null);
            }
            InspectionDefect_Table finalDefect = defect;
            mUploadInspectionDataRequest = BridgeAPIQueue.getInstance().uploadInspectionDefect(jObj, defect.defect_item_id, defect.inspection_id, new ServerCallback() {
                @Override
                public void onSuccess() {
                    mReviewAndSubmitViewModel.markDefectUploaded(finalDefect.id);
                }

                @Override
                public void onFailure() {
                }
            });
            BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadInspectionDataRequest);
        }

        mUpdateInspectionStatusRequest = BridgeAPIQueue.getInstance().updateInspectionStatus(mInspectionId, mInspectionStatusId, new ServerCallback() {
            @Override
            public void onSuccess() {
                mReviewAndSubmitViewModel.completeInspection(mInspectionId);
                returnToRouteSheet();
                hideProgressSpinner();
            }

            @Override
            public void onFailure() {
                Snackbar.make(mConstraintLayout, "Error in submission! Email support", Snackbar.LENGTH_LONG).show();
                returnToRouteSheet();
                hideProgressSpinner();
            }
        });

        BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateInspectionStatusRequest);
    }

    private byte[] getPictureData(int inspectionDefectId) {
        InspectionDefect_Table defect = mReviewAndSubmitViewModel.getInspectionDefect(inspectionDefectId);
        Bitmap image = BitmapFactory.decodeFile(defect.picture_path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
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
            IncompleteReason selectedItem = (IncompleteReason) spinnerResolutions.getSelectedItem();
            mInspectionStatusId = selectedItem.code;
            try {
                completeInspection();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void showProgressSpinner() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLockScreen.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void hideProgressSpinner() {
        mProgressBar.setVisibility(View.GONE);
        mLockScreen.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void returnToRouteSheet() {
        Intent routeSheetIntent = new Intent(ReviewAndSubmitActivity.this, RouteSheetActivity.class);
        startActivity(routeSheetIntent);
    }
}