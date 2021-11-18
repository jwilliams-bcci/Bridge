package com.burgess.bridge.reviewandsubmit;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.Constants;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import data.Enums.IncompleteReason;
import data.Tables.Builder_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;
import data.Tables.MultifamilyDetails_Table;
import data.Views.ReviewAndSubmit_View;

public class ReviewAndSubmitActivity extends AppCompatActivity {
    private ReviewAndSubmitViewModel mReviewAndSubmitViewModel;
    private Inspection_Table mInspection;

    public static final String TAG = "REVIEW_AND_SUBMIT";
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private int mInspectionStatusId;
    private boolean mSupervisorPresent;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerInspectionDefects;
    private TextView mTextAddress;
    private ConstraintLayout mConstraintLayout;
    private StringRequest mUploadMultifamilyDetailsRequest;
    private StringRequest mUploadInspectionDataRequest;
    private StringRequest mUpdateInspectionStatusRequest;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mSecurityUserId;
    private int mDivisionId;
    private ItemTouchHelper mItemTouchHelper;
    private List<ReviewAndSubmit_View> mInspectionDefectList;
    private List<InspectionDefect_Table> mReinspectionRequiredDefectList;
    private Button mButtonAttachFile;
    private Button mButtonSubmit;
    private boolean mIsOnline;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar(findViewById(R.id.review_and_submit_toolbar));
        mReviewAndSubmitViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReviewAndSubmitViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mReviewAndSubmitViewModel.getInspectionSync(mInspectionId);
        mSecurityUserId = mSharedPreferences.getString(Constants.PREF_SECURITY_USER_ID, "NULL");
        mDivisionId = mInspection.division_id;
        mIsOnline = mSharedPreferences.getBoolean(PREF_IS_ONLINE, false);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.review_and_submit_constraint_layout);
        mLockScreen = findViewById(R.id.review_and_submit_lock_screen);
        mProgressBar = findViewById(R.id.review_and_submit_progress_bar);
        mTextAddress = findViewById(R.id.review_and_submit_text_address);
        mButtonAttachFile = findViewById(R.id.review_and_submit_button_attach_file);
        mButtonSubmit = findViewById(R.id.review_and_submit_button_submit);
        mRecyclerInspectionDefects = findViewById(R.id.review_and_submit_recycler_inspection_defects);
    }
    private void initializeButtonListeners() {
        mButtonAttachFile.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "This feature is coming soon!", Snackbar.LENGTH_SHORT).show();
        });
        mButtonSubmit.setOnClickListener(v -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            mInspectionStatusId = getInspectionStatusId();
            String statusMessage;
            switch (mInspectionStatusId) {
                case 11:
                    statusMessage = "PASSED";
                    break;
                case 12:
                    statusMessage = "FAILED";
                    break;
                case 26:
                    statusMessage = "NO DEFICIENCIES OBSERVED";
                    break;
                case 25:
                    statusMessage = "DEFICIENCIES OBSERVED";
                    break;
                default:
                    statusMessage = "NA";
                    break;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Is the following resolution accurate?")
                    .setMessage(statusMessage)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        try {
                            completeInspection();
                        } catch (Exception e) {
                            BridgeLogger.log('E', TAG, "ERROR in completeInspection: " + e.getMessage());
                            hideProgressSpinner();
                            Snackbar.make(mConstraintLayout, "Error! Please return to route sheet and send activity log", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> showEditResolutionDialog())
                    .show();

            new AlertDialog.Builder(this)
                    .setTitle("Supervisor Present?")
                    .setMessage("Was the supervisor present?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> mSupervisorPresent = true)
                    .setNegativeButton("No", (dialogInterface, i) -> mSupervisorPresent = false)
                    .show();
        });
    }
    private void initializeDisplayContent() {
        // Display address
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type);

        // Populate the recycler view of inspection defects
        ReviewAndSubmitListAdapter reviewAndSubmitListAdapter = new ReviewAndSubmitListAdapter(new ReviewAndSubmitListAdapter.ReviewAndSubmitDiff());
        mRecyclerInspectionDefects.setAdapter(reviewAndSubmitListAdapter);
        mRecyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));
        mInspectionDefectList = mReviewAndSubmitViewModel.getInspectionDefectsForReviewSync(mInspectionId);
        reviewAndSubmitListAdapter.submitList(mInspectionDefectList);

        // If this is not a reinspection, set up the swipe functionality to remove an item
        if (!mInspection.reinspect) {
            ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                    ReviewAndSubmitViewHolder holder = (ReviewAndSubmitViewHolder) viewHolder;
                    mReviewAndSubmitViewModel.deleteInspectionDefect(holder.mInspectionDefectId);
                }
            };
            ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
            touchHelper.attachToRecyclerView(mRecyclerInspectionDefects);
        }
    }

    private int getInspectionStatusId() {
        int status;
        int builderId = mInspection.builder_id;
        Builder_Table builder = mReviewAndSubmitViewModel.getBuilder(builderId);
        boolean builderConditionalReinspect = builder.reinspection_required;

        if (mInspectionDefectList.isEmpty()) {
            status = 11;
        } else {
            List<ReviewAndSubmit_View> notAllCs = mInspectionDefectList.stream().filter(ReviewAndSubmit_View::notAllCs).collect(Collectors.toList());
            if (notAllCs.isEmpty()) {
                status = 11;
            } else {
                List<ReviewAndSubmit_View> reinspectionRequired = mInspectionDefectList.stream().filter(ReviewAndSubmit_View::reinspectionRequired).collect(Collectors.toList());
                if (builderConditionalReinspect && reinspectionRequired.isEmpty()) {
                    status = 11;
                } else {
                    status = 12;
                }
            }
        }

        if (mDivisionId == 20) {
            if (status == 11) {
                status = 26;
            } else if (status == 12) {
                status = 25;
            }
        }
        return status;
    }

    private void completeInspection() throws Exception {
        showProgressSpinner();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);
        String endTime = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            endTime = URLEncoder.encode(formatter.format(Calendar.getInstance().getTime()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            BridgeLogger.log('E', TAG, "ERROR in onCreate - " + e.getMessage());
            hideProgressSpinner();
            Snackbar.make(mConstraintLayout, "Error! Please return to route sheet and send Activity Log", Snackbar.LENGTH_LONG).show();
        }
        mReviewAndSubmitViewModel.completeInspection(mInspection.end_time, mInspectionId);


        if (mDivisionId == 20) {
            JSONObject jObj = new JSONObject();
            MultifamilyDetails_Table multifamilyDetails = mReviewAndSubmitViewModel.getMultifamilyDetails(mInspectionId);
            jObj.put("InspectionId", multifamilyDetails.inspection_id);
            jObj.put("BuilderPersonnel", multifamilyDetails.builder_personnel);
            jObj.put("BurgessPersonnel", multifamilyDetails.burgess_personnel);
            jObj.put("AreaObserved", multifamilyDetails.area_observed);
            jObj.put("Temperature", multifamilyDetails.temperature);
            jObj.put("WeatherConditions", multifamilyDetails.weather_conditions);
            mUploadMultifamilyDetailsRequest = BridgeAPIQueue.getInstance().uploadMultifamilyDetails(jObj, mInspectionId, new ServerCallback() {
                @Override
                public void onSuccess(String message) {
                }

                @Override
                public void onFailure(String message) {
                    BridgeLogger.log('E', TAG, "ERROR in uploadInspectionDataRequest");
                }
            });
        }

        JSONObject jObj;
        InspectionDefect_Table defect;
        BridgeLogger.getInstance().log('I', TAG, "Setting up update inspection status request... InspID:" + mInspectionId + " StartTime:" + mInspection.start_time + " EndTime:" + endTime + " Status:" + mInspectionStatusId + " Sup Present:" + mSupervisorPresent);
        mUpdateInspectionStatusRequest = BridgeAPIQueue.getInstance().updateInspectionStatus(mInspectionId, mInspectionStatusId, mSecurityUserId, inspectionDefects.size(), (mSupervisorPresent ? 1 : 0), mInspection.start_time.toString(), endTime, (mInspection.trainee_id > 0 ? 1 : 0), mInspection.trainee_id, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                BridgeLogger.getInstance().log('I', TAG, "mUpdateInspectionStatusRequest returned success.");
                if (mDivisionId == 20) {
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadMultifamilyDetailsRequest);
                }
                mReviewAndSubmitViewModel.uploadInspection(mInspectionId);
            }

            @Override
            public void onFailure(String message) {
                BridgeLogger.getInstance().log('E', TAG, "ERROR in completeInspection: " + message);
            }
        });

        // If there are no defects, go ahead and update the status
        if (inspectionDefects.isEmpty()) {
            BridgeLogger.log('I', TAG, "No defects");
            BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateInspectionStatusRequest);
        }
        // If all defects have been previously uploaded
        else if (mReviewAndSubmitViewModel.remainingToUpload(mInspectionId) == 0) {
            BridgeLogger.log('I', TAG, "All uploaded previously");
            BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateInspectionStatusRequest);
        } else {
            BridgeLogger.log('I', TAG, "Found defects, uploading...");
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
                    try{
                        jObj.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(defect.id)));
                        jObj.put("ImageFileName", defect.picture_path.substring(defect.picture_path.lastIndexOf("/")+1));
                    } catch (NullPointerException e) {
                        Snackbar.make(mConstraintLayout, "Photo is missing! Please check photo for #" + defect.defect_item_id, Snackbar.LENGTH_SHORT).show();
                        hideProgressSpinner();
                        return;
                    }
                } else {
                    jObj.put("ImageData", null);
                    jObj.put("ImageFileName", null);
                }
                jObj.put("PriorInspectionDetailId", defect.prior_inspection_detail_id);
                InspectionDefect_Table finalDefect = defect;
                if (!defect.is_uploaded) {
                    mUploadInspectionDataRequest = BridgeAPIQueue.getInstance().uploadInspectionDefect(jObj, defect.defect_item_id, defect.inspection_id, new ServerCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mReviewAndSubmitViewModel.markDefectUploaded(finalDefect.id);
                            if (mReviewAndSubmitViewModel.remainingToUpload(mInspectionId) == 0) {
                                BridgeLogger.log('I', TAG, "All defect items uploaded. Adding mUpdateInspectionStatusRequest");
                                mUpdateInspectionStatusRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateInspectionStatusRequest);
                            }
                        }
                        @Override
                        public void onFailure(String message) {
                            BridgeLogger.log('E', TAG, "ERROR in uploadInspectionDataRequest");
                        }
                    });
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadInspectionDataRequest);
                }
            }
        }
        hideProgressSpinner();
        returnToRouteSheet();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void returnToRouteSheet() {
        finish();
        Intent routeSheetIntent = new Intent(ReviewAndSubmitActivity.this, RouteSheetActivity.class);
        routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(routeSheetIntent);
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
}