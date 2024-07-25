package com.burgess.bridge.reviewandsubmit;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.Constants;
import com.burgess.bridge.R;
import com.burgess.bridge.ResolutionHelper;
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

import data.Enums.Resolution;
import data.Tables.Attachment_Table;
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
    public int mInspectionStatusId;
    private boolean mSupervisorPresent;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerInspectionDefects;
    private TextView mTextAddress;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mTextChangeReason;
    private StringRequest mUploadMultifamilyDetailsRequest;
    private StringRequest mUploadInspectionAttachmentRequest;
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
    private Button mButtonSortDescription;
    private Button mButtonSortItemNumber;
    private boolean mIsOnline;
    private boolean mChangedStatus;
    private long mLastClickTime = 0;
    private ChangeResolutionFragment mChangeResolutionFragment;
    private ActivityResultLauncher<Intent> chooseFileLauncher;
    private ReviewAndSubmitListAdapter mReviewAndSubmitListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_submit);
        setSupportActionBar(findViewById(R.id.review_and_submit_toolbar));
        mReviewAndSubmitViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ReviewAndSubmitViewModel.class);

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mReviewAndSubmitViewModel.getInspectionSync(mInspectionId);
        mSecurityUserId = mSharedPreferences.getString(Constants.PREF_SECURITY_USER_ID, "NULL");
        mDivisionId = mInspection.division_id;
        mIsOnline = mSharedPreferences.getBoolean(PREF_IS_ONLINE, false);
        mChangedStatus = false;

        initializeViews();
        initializeActivityResults();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.review_and_submit_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mLockScreen = findViewById(R.id.review_and_submit_lock_screen);
        mProgressBar = findViewById(R.id.review_and_submit_progress_bar);
        mTextAddress = findViewById(R.id.review_and_submit_text_address);
        mButtonAttachFile = findViewById(R.id.review_and_submit_button_attach_file);
        mButtonSubmit = findViewById(R.id.review_and_submit_button_submit);
        mButtonSortDescription = findViewById(R.id.review_and_submit_button_sort_description);
        mButtonSortItemNumber = findViewById(R.id.review_and_submit_button_sort_item_number);
        mRecyclerInspectionDefects = findViewById(R.id.review_and_submit_recycler_inspection_defects);
    }
    private void initializeButtonListeners() {
        mButtonAttachFile.setOnClickListener(v -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("*/*");
            chooseFileLauncher.launch(fileIntent);
        });
        mButtonSubmit.setOnClickListener(v -> {
            try {
                // Prevent double clicking
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                mInspectionStatusId = getInspectionStatusId();

                showSupervisorDialog();
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in mButtonSubmit.click(): " + e.getMessage());
            }
        });
        mButtonSortDescription.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Sorting by description...", Snackbar.LENGTH_SHORT).show();
            mReviewAndSubmitViewModel.getInspectionDefectsForReviewDescriptionSort(mInspectionId).observe(this, defectItems ->
                    mReviewAndSubmitListAdapter.setCurrentList(defectItems));
        });
        mButtonSortItemNumber.setOnClickListener(v -> {
            Snackbar.make(mConstraintLayout, "Sorting by item number...", Snackbar.LENGTH_SHORT).show();
            mReviewAndSubmitViewModel.getInspectionDefectsForReviewItemNumberSort(mInspectionId).observe(this, defectItems ->
                    mReviewAndSubmitListAdapter.setCurrentList(defectItems));
        });
    }
    private void initializeActivityResults() {
        chooseFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Attachment_Table attachment = new Attachment_Table();
                    attachment.inspection_id = mInspectionId;
                    attachment.location_id = mInspection.location_id;
                    attachment.file_name = null;
                    attachment.file_data = null;
                    attachment.file_path = data.getData().toString();
                    attachment.is_uploaded = false;

                    mReviewAndSubmitViewModel.insertAttachment(attachment);
                }
            }
        });
    }
    private void initializeDisplayContent() {
        try{
            mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
            mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

            // Display address
            mTextAddress.setText("");
            mTextAddress.append(mInspection.community + "\n");
            mTextAddress.append(mInspection.address + "\n");
            mTextAddress.append(mInspection.inspection_type);

            // Populate the recycler view of inspection defects
            mReviewAndSubmitListAdapter = new ReviewAndSubmitListAdapter(new ReviewAndSubmitListAdapter.ReviewAndSubmitDiff());
            mRecyclerInspectionDefects.setAdapter(mReviewAndSubmitListAdapter);
            mRecyclerInspectionDefects.setLayoutManager(new LinearLayoutManager(this));
            mReviewAndSubmitViewModel.getInspectionDefectsForReviewDescriptionSort(mInspectionId).observe(this, defectItems -> {
                mReviewAndSubmitListAdapter.submitList(defectItems);
            });

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
                            mReviewAndSubmitListAdapter.notifyDataSetChanged();
                        } else {
                            mReviewAndSubmitViewModel.deleteInspectionDefect(holder.mInspectionDefectId);
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
            mInspectionDefectList = mReviewAndSubmitViewModel.getInspectionDefectsForReviewSync(mInspectionId);

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
                } else {
                    status = 25;
                }
            }

            if (mInspection.inspection_type_id == 785 || mInspection.inspection_type_id == 1160 || mInspection.inspection_type_id == 1148) {
                status = 27;
            }

            if (mInspection.inspection_type_id == 1828 && mInspection.builder_id == 2597) {
                status = 32;
            }

            if (mInspection.inspection_type_id == 1329 && (status == 11 || status == 26)) {
                status = 27;
            }

            return status;
        } catch (Exception e) {
            BridgeLogger.log('E', TAG, "ERROR in getInspectionStatusId: " + e.getMessage());
            return 0;
        }
    }

    public void updateInspectionStatus(int statusId) {
        mInspectionStatusId = statusId;
        BridgeLogger.log('I', TAG, "Changed Resolution to " + statusId);
        mChangedStatus = true;
        try {
            showResolutionCheckDialog(statusId);
        } catch (Exception e) {
            BridgeLogger.log('E', TAG, "ERROR in completeInspection: " + e.getMessage());
        }
    }

    private void completeInspection() throws Exception {
        showProgressSpinner();
        List<InspectionDefect_Table> inspectionDefects = mReviewAndSubmitViewModel.getAllInspectionDefectsSync(mInspectionId);
        List<Attachment_Table> inspectionAttachments = mReviewAndSubmitViewModel.getAttachmentsToUpload(mInspectionId);
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
                    BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(mInspectionId);
                    BridgeLogger.log('E', TAG, "ERROR in completeInspection. Cancelled requests for Inspection ID: " + mInspectionId);
                }
            });
        }

        JSONObject defectJSON;
        JSONObject statusJSON = new JSONObject();
        InspectionDefect_Table inspectionDefect;
        DefectItem_Table defectItem;
        BridgeLogger.log('I', TAG, "Setting up update inspection status request... InspID:" + mInspectionId + " StartTime:" + mInspection.start_time + " EndTime:" + endTime + " Status:" + mInspectionStatusId + " Sup Present:" + mSupervisorPresent);
        statusJSON.put("InspectionId", mInspectionId);
        statusJSON.put("StatusId", mInspectionStatusId);
        statusJSON.put("UserId", mSecurityUserId);
        statusJSON.put("InspectionTotal", inspectionDefects.size());
        statusJSON.put("SuperPresent", (mSupervisorPresent ? 1 : 0));
        statusJSON.put("StartTime", mInspection.start_time.toString());
        statusJSON.put("EndTime", endTime.toString());
        statusJSON.put("Training", (mInspection.trainee_id > 0 ? 1 : 0));
        statusJSON.put("TraineeId", mInspection.trainee_id);
        if (mChangedStatus) {
            statusJSON.put("ChangeReason","Changed the resolution.");
        } else {
            statusJSON.put("ChangeReason", null);
        }
        mUpdateInspectionStatusRequest = BridgeAPIQueue.getInstance().updateInspectionStatusV2(statusJSON, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                BridgeLogger.log('I', TAG, "mUpdateInspectionStatusRequest returned success.");
                if (mDivisionId == 20) {
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadMultifamilyDetailsRequest);
                }
                mReviewAndSubmitViewModel.uploadInspection(mInspectionId);
            }

            @Override
            public void onFailure(String message) {
                BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(mInspectionId);
                mReviewAndSubmitViewModel.markInspectionFailed(mInspectionId);
                BridgeLogger.log('E', TAG, "ERROR in completeInspection. Cancelled requests for Inspection ID: " + mInspectionId);
            }
        });

        if (inspectionAttachments.isEmpty()) {
            BridgeLogger.log('I', TAG, "No inspection-level attachments to upload");
        } else {
            Attachment_Table attachment;
            for (int lcv = 0; lcv < inspectionAttachments.size(); lcv++) {
                attachment = inspectionAttachments.get(lcv);
                Attachment_Table finalAttachment = attachment;
                JSONObject jsonAttachment = new JSONObject();
                jsonAttachment.put("InspectionId", attachment.inspection_id);
                jsonAttachment.put("UserId", 2806);
                jsonAttachment.put("ImageData", attachment.file_data);
                jsonAttachment.put("ImageFileName", attachment.file_name);
                mUploadInspectionAttachmentRequest = BridgeAPIQueue.getInstance().uploadInspectionAttachment(jsonAttachment, mInspectionId, new ServerCallback() {
                    @Override
                    public void onSuccess(String message) {
                        BridgeLogger.log('I', TAG, "mUploadInspectionAttachmentRequest returned success.");
                        mReviewAndSubmitViewModel.updateIsUploaded(finalAttachment.id);
                    }

                    @Override
                    public void onFailure(String message) {
                        BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(mInspectionId);
                        mReviewAndSubmitViewModel.markInspectionFailed(mInspectionId);
                        BridgeLogger.log('E', TAG, "ERROR in completeInspection when uploading attachments. Cancelled requests for Inspection ID: " + mInspectionId);
                    }
                });
                BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadInspectionAttachmentRequest);
            }
        }

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

                defectJSON = new JSONObject();
                defectJSON.put("InspectionId", inspectionDefect.inspection_id);
                defectJSON.put("DefectItemId", inspectionDefect.defect_item_id);
                defectJSON.put("DefectStatusId", inspectionDefect.defect_status_id);

                if (inspectionDefect.comment != null) {
                    defectJSON.put("Comment", inspectionDefect.comment);
                } else {
                    defectJSON.put("Comment", "");
                }
                if (inspectionDefect.picture_path != null) {
                    try {
                        defectJSON.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(inspectionDefect.id, false)));
                        defectJSON.put("ImageFileName", inspectionDefect.picture_path.substring(inspectionDefect.picture_path.lastIndexOf("/")+1));
                    } catch (NullPointerException e) {
                        Snackbar.make(mConstraintLayout, "Photo is missing! Please check photo for #" + defectItem.item_number, Snackbar.LENGTH_SHORT).show();
                        hideProgressSpinner();
                        return;
                    }
                } else {
                    defectJSON.put("ImageData", null);
                    defectJSON.put("ImageFileName", null);
                }
                if (inspectionDefect.attachment_data != null) {
                    try {
                        defectJSON.put("AttachmentData", Base64.getEncoder().encodeToString(inspectionDefect.attachment_data));
                        defectJSON.put("AttachmentFileName", mInspectionId + "_" + defectItem.id + "_Attachment.pdf");
                    } catch (NullPointerException e) {
                        Snackbar.make(mConstraintLayout, "Attachment is missing! Please check attachment for #" + defectItem.item_number, Snackbar.LENGTH_SHORT).show();

                        hideProgressSpinner();
                        return;
                    }
                } else {
                    defectJSON.put("AttachmentData", null);
                    defectJSON.put("AttachmentFileName", null);
                }
                defectJSON.put("PriorInspectionDetailId", inspectionDefect.prior_inspection_detail_id);
                defectJSON.put("FirstInspectionDetailId", inspectionDefect.first_inspection_detail_id);
                if (mInspection.require_risk_assessment) {
                    defectJSON.put("LotNumber", inspectionDefect.lot_number);
                    defectJSON.put("StageOfConstruction", inspectionDefect.stage_of_construction);
                } else {
                    defectJSON.put("LotNumber", null);
                    defectJSON.put("StageOfConstruction", null);
                }
                InspectionDefect_Table finalDefect = inspectionDefect;
                if (!inspectionDefect.is_uploaded) {
                    mUploadInspectionDataRequest = BridgeAPIQueue.getInstance().uploadInspectionDefect(defectJSON, inspectionDefect.defect_item_id, inspectionDefect.inspection_id, new ServerCallback() {
                        @Override
                        public void onSuccess(String message) {
                            mReviewAndSubmitViewModel.markDefectUploaded(finalDefect.id);
                            BridgeLogger.log('I', TAG, "Defect item uploaded... " + finalDefect);
                            if (mReviewAndSubmitViewModel.multifamilyDefectExists(finalDefect.first_inspection_detail_id) > 0) {
                                int existingId = mReviewAndSubmitViewModel.multifamilyDefectExists(finalDefect.first_inspection_detail_id);
                                mReviewAndSubmitViewModel.updateExistingMFCDefect(finalDefect.defect_status_id, finalDefect.comment, existingId);
                                BridgeLogger.log('I', TAG, "Updated existing defect");
                            }
                            if (mReviewAndSubmitViewModel.remainingToUpload(mInspectionId) == 0) {
                                BridgeLogger.log('I', TAG, "All defect items uploaded. Adding mUpdateInspectionStatusRequest");
                                mUpdateInspectionStatusRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                BridgeAPIQueue.getInstance().getRequestQueue().add(mUpdateInspectionStatusRequest);
                            }
                        }
                        @Override
                        public void onFailure(String message) {
                            BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(Constants.CANCEL_TAG);
                            mReviewAndSubmitViewModel.markInspectionFailed(mInspectionId);
                            BridgeLogger.log('E', TAG, "ERROR in uploadInspectionDataRequest. Cancelled requests for Inspection ID: " + mInspectionId);
                        }
                    });
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mUploadInspectionDataRequest);
                }
            }
        }
        hideProgressSpinner();
        returnToRouteSheet();
    }

    private byte[] getPictureData(int inspectionDefectId, boolean isAttachment) {
        InspectionDefect_Table defect = mReviewAndSubmitViewModel.getInspectionDefect(inspectionDefectId);
        Bitmap image;
        if (isAttachment) {
            return defect.attachment_data;
        } else {
            image = BitmapFactory.decodeFile(defect.picture_path);
        }
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

    private void showSupervisorDialog() {
        AlertDialog supervisorDialog = new AlertDialog.Builder(this)
                .setTitle("Supervisor Present?")
                .setMessage("Was the supervisor present?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mSupervisorPresent = true;
                    showResolutionCheckDialog(getInspectionStatusId());
                })
                .setNegativeButton("No", (dialog, which) -> {
                    mSupervisorPresent = false;
                    showResolutionCheckDialog(getInspectionStatusId());
                })
                .create();

        supervisorDialog.show();
    }

    private void showResolutionCheckDialog(int statusId) {
        String statusMessage = Resolution.findByCode(statusId).toString();

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
                .setNegativeButton("No", (dialog, which) -> {
                    showEditResolutionDialog();
                })
                .create();

        resolutionDialog.show();

        final Button button = resolutionDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);

        handler.postDelayed(() -> {
            button.setEnabled(true);
        }, 5000);
    }

    private void showEditResolutionDialog() {
        mChangeResolutionFragment = ChangeResolutionFragment.newInstance();
        ResolutionHelper resolutionHelper = new ResolutionHelper(mInspection.division_id, mInspection.inspection_class, mInspection.inspection_type, mInspection.builder_id, mInspection.inspection_type_id);
        mChangeResolutionFragment.setResolutionList(resolutionHelper.buildList());
        mChangeResolutionFragment.show(getSupportFragmentManager(), "CHANGE_RESOLUTION");
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