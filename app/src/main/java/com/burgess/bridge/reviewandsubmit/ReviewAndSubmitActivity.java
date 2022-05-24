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
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.Constants;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import data.Enums.IncompleteReason;
import data.Tables.Builder_Table;
import data.Tables.DefectItem_Table;
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
            try {
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
                    case 0:
                        Snackbar.make(mConstraintLayout, "Error in getting status, please send Activity log", Snackbar.LENGTH_SHORT).show();
                        return;
                    default:
                        statusMessage = "NA";
                        break;
                }

                Handler handler = new Handler();
                AlertDialog resolutionDialog = new AlertDialog.Builder(this)
                        .setTitle("Is the following resolution accurate? YES button will be active in 5 seconds...")
                        .setMessage(statusMessage)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                completeInspection();
                            } catch (Exception e) {
                                BridgeLogger.log('E', TAG, "ERROR in completeInspection: " + e.getMessage());
                                hideProgressSpinner();
                                Snackbar.make(mConstraintLayout, "Error! Please return to route sheet and send activity log", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> showEditResolutionDialog())
                        .create();

                AlertDialog supervisorDialog = new AlertDialog.Builder(this)
                        .setTitle("Supervisor Present?")
                        .setMessage("Was the supervisor present?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> mSupervisorPresent = true)
                        .setNegativeButton("No", (dialogInterface, i) -> mSupervisorPresent = false)
                        .create();

                resolutionDialog.show();
                supervisorDialog.show();

                final Button button = resolutionDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setEnabled(false);

                handler.postDelayed(() -> {
                    button.setEnabled(true);
                }, 5000);
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in mButtonSubmit.click(): " + e.getMessage());
            }
        });
    }
    private void initializeDisplayContent() {
        try{
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
            if (!mInspection.reinspect || (mInspection.division_id == 20 && mInspection.inspection_type_id != 1154)) {
                ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                        ReviewAndSubmitViewHolder holder = (ReviewAndSubmitViewHolder) viewHolder;
                        if (!holder.mIsEditable) {
                            Snackbar.make(mConstraintLayout, "Cannot delete previous items from Multifamily inspections!", Snackbar.LENGTH_SHORT).show();
                            reviewAndSubmitListAdapter.notifyDataSetChanged();
                        } else {
                            mReviewAndSubmitViewModel.deleteInspectionDefect(holder.mInspectionDefectId);
                            mInspectionDefectList.remove(viewHolder.getAdapterPosition());
                            reviewAndSubmitListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                    }
                };
                ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
                touchHelper.attachToRecyclerView(mRecyclerInspectionDefects);
            }
        } catch (Exception e) {
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
    }

    private int getInspectionStatusId() {
        try {
            int status;
            int builderId = mInspection.builder_id;
            Builder_Table builder = mReviewAndSubmitViewModel.getBuilder(builderId);
            boolean builderConditionalReinspect = builder.reinspection_required;

            if (mInspectionDefectList.isEmpty()) {
                status = 11;
            } else {
                List<ReviewAndSubmit_View> ncItems = mInspectionDefectList.stream().filter(ReviewAndSubmit_View::isNC).collect(Collectors.toList());
                if (ncItems.isEmpty()) {
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
        } catch (Exception e) {
            BridgeLogger.log('E', TAG, "ERROR in getInspectionStatusId: " + e.getMessage());
            return 0;
        }
    }

    private void completeInspection() throws Exception {
        showProgressSpinner();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);
        OffsetDateTime endTime = OffsetDateTime.now();
        if (mInspection.end_time == null) {
            mReviewAndSubmitViewModel.completeInspection(endTime, mInspectionId);
        } else {
            endTime = mInspection.end_time;
        }

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
                    BridgeLogger.log('I', TAG, "Uploaded multifamily details for " + mInspectionId);
                }

                @Override
                public void onFailure(String message) {
                    BridgeLogger.log('E', TAG, "ERROR in uploadInspectionDataRequest");
                }
            });
        }

        JSONObject jObj;
        InspectionDefect_Table inspectionDefect;
        DefectItem_Table defectItem;
        BridgeLogger.log('I', TAG, "Setting up update inspection status request... InspID:" + mInspectionId + " StartTime:" + mInspection.start_time + " EndTime:" + endTime + " Status:" + mInspectionStatusId + " Sup Present:" + mSupervisorPresent);
        mUpdateInspectionStatusRequest = BridgeAPIQueue.getInstance().updateInspectionStatus(mInspectionId, mInspectionStatusId, mSecurityUserId, inspectionDefects.size(), (mSupervisorPresent ? 1 : 0), mInspection.start_time.toString(), endTime.toString(), (mInspection.trainee_id > 0 ? 1 : 0), mInspection.trainee_id, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                BridgeLogger.log('I', TAG, "mUpdateInspectionStatusRequest returned success.");
                if (mDivisionId == 20) {
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadMultifamilyDetailsRequest);
                }
                mReviewAndSubmitViewModel.uploadInspection(mInspectionId);
                mReviewAndSubmitViewModel.deleteInspectionDefects(mInspectionId);
                mReviewAndSubmitViewModel.deleteInspectionHistories(mInspectionId);
                mReviewAndSubmitViewModel.deleteInspection(mInspectionId);
                BridgeLogger.log('I', TAG, "Deleted defects and inspection " + mInspectionId);
            }

            @Override
            public void onFailure(String message) {
                BridgeLogger.log('E', TAG, "ERROR in completeInspection: " + message);
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
        }
        else {
            BridgeLogger.log('I', TAG, "Found defects, uploading...");
            for(int lcv = 0; lcv < inspectionDefects.size(); lcv++) {
                inspectionDefect = inspectionDefects.get(lcv);
                defectItem = mReviewAndSubmitViewModel.getDefectItem(inspectionDefect.defect_item_id);

                jObj = new JSONObject();
                jObj.put("InspectionId", inspectionDefect.inspection_id);
                jObj.put("DefectItemId", inspectionDefect.defect_item_id);
                jObj.put("DefectStatusId", inspectionDefect.defect_status_id);

                if (inspectionDefect.comment != null) {
                    jObj.put("Comment", inspectionDefect.comment);
                } else {
                    jObj.put("Comment", "");
                }
                if (inspectionDefect.picture_path != null) {
                    try{
                        jObj.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(inspectionDefect.id)));
                        jObj.put("ImageFileName", inspectionDefect.picture_path.substring(inspectionDefect.picture_path.lastIndexOf("/")+1));
                    } catch (NullPointerException e) {
                        Snackbar.make(mConstraintLayout, "Photo is missing! Please check photo for #" + inspectionDefect.defect_item_id, Snackbar.LENGTH_SHORT).show();
                        hideProgressSpinner();
                        return;
                    }
                } else {
                    jObj.put("ImageData", null);
                    jObj.put("ImageFileName", null);
                }
                jObj.put("PriorInspectionDetailId", inspectionDefect.prior_inspection_detail_id);
                InspectionDefect_Table finalDefect = inspectionDefect;
                if (!inspectionDefect.is_uploaded) {
                    mUploadInspectionDataRequest = BridgeAPIQueue.getInstance().uploadInspectionDefect(jObj, inspectionDefect.defect_item_id, inspectionDefect.inspection_id, new ServerCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mReviewAndSubmitViewModel.markDefectUploaded(finalDefect.id);
                            BridgeLogger.log('I', TAG, "Defect item uploaded... ID: " + finalDefect.defect_item_id + ", Status: " + finalDefect.defect_status_id + ", Comment: " + finalDefect.comment);
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
        Bitmap outBmp;
        float degrees = 90;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        outBmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (outBmp != null) {
            outBmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
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