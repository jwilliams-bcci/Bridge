package com.burgess.bridge.defectitem;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.Manifest;
import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.inspect.InspectActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;
import data.Tables.Room_Table;

public class DefectItemActivity extends AppCompatActivity {
    private static final String TAG = "DEFECT_ITEM";
    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String DEFECT_ID = "com.burgess.bridge.DEFECT_ID";
    public static final int DEFECT_ID_NOT_FOUND = -1;
    public static final String INSPECTION_HISTORY_ID = "com.burgess.bridge.INSPECTION_HISTORY_ID";
    public static final int INSPECTION_HISTORY_ID_NOT_FOUND = -1;
    public static final String FIRST_DETAIL_ID = "com.burgess.bridge.FIRST_DETAIL_ID";
    public static final int FIRST_DETAIL_ID_NOT_FOUND = -1;
    public static final String INSPECTION_DEFECT_ID = "com.burgess.bridge.INSPECTION_DEFECT_ID";
    public static final int INSPECTION_DEFECT_ID_NOT_FOUND = -1;
    public static final String SCROLL_POSITION = "com.burgess.bridge.SCROLL_POSITION";
    public static final int SCROLL_POSITION_NOT_FOUND = -1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String FILTER_OPTION = "com.burgess.bridge.FILTER_OPTION";
    public boolean mPictureTaken = false;
    public boolean mAttachmentIncluded = false;
    public final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    private String mCurrentPhotoPath;
    private String mCurrentAttachmentPath;
    private long mLastClickTime = 0;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private int mInspectionId;
    private int mDefectId;
    private int mInspectionDefectId;
    private int mInspectionHistoryId;
    private int mFirstDetailId;
    private int mScrollPosition;
    private String mFilter;
    private DefectItemViewModel mDefectItemViewModel;
    private DefectItem_Table mDefectItem;
    private RadioGroup mRadioGroupDefectStatus;
    private RadioButton mRadioButtonR;
    private RadioButton mRadioButtonNA;
    private TextView mDefectItemDetails;
    private TextView mDefectItemTextLocation;
    private TextView mDefectItemTextRoom;
    private TextView mDefectItemTextDirection;
    private TextView mDefectItemTextFault;
    private TextView mDefectItemTextCannedComment;
    private TextView mDefectItemTextComment;
    private TextView mDefectItemTextPreviousComment;
    private TextView mDefectItemLabelPreviousComment;
    private Button mButtonCancel;
    private ImageView mImageViewThumbnail;
    private TextView mTextAttachmentLabel;
    private Button mButtonAddAttachment;
    private TextView mTextAttachmentFilename;
    private Button mSaveInspectionDefect;
    private ImageButton mButtonMicrophone;
    private ImageButton mButtonCamera;
    private Inspection_Table mInspection;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mLabelLotNumber;
    private TextView mTextLotNumber;
    private TextView mLabelConstructionStage;
    private Spinner mSpinnerConstructionStage;
    private ArrayAdapter<String> mSpinnerConstructionStageAdapter;

    private InspectionDefect_Table inspectionDefect;
    private byte[] attachmentData;

    // Activity Results
    private ActivityResultLauncher<Intent> chooseFileLauncher;

    // Fragments
    private LocationFragment mLocationFragment;
    private RoomFragment mRoomFragment;
    private DirectionFragment mDirectionFragment;
    private FaultFragment mFaultFragment;
    private CannedCommentFragment mCannedCommentFragment;

    // Fragment data
    private List<Room_Table> mRoomList;
    private List<Direction_Table> mDirectionList;
    private List<Fault_Table> mFaultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_item);
        setSupportActionBar(findViewById(R.id.defect_item_toolbar));
        mDefectItemViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DefectItemViewModel.class);
        checkPermission();

        // Prepare shared preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mInspectionDefectId = intent.getIntExtra(INSPECTION_DEFECT_ID, INSPECTION_DEFECT_ID_NOT_FOUND);
        mInspectionHistoryId = intent.getIntExtra(INSPECTION_HISTORY_ID, INSPECTION_HISTORY_ID_NOT_FOUND);
        mFirstDetailId = intent.getIntExtra(FIRST_DETAIL_ID, FIRST_DETAIL_ID_NOT_FOUND);
        mScrollPosition = intent.getIntExtra(SCROLL_POSITION, SCROLL_POSITION_NOT_FOUND);
        mFilter = intent.getStringExtra(FILTER_OPTION);
        mInspection = mDefectItemViewModel.getInspectionSync(mInspectionId);
        mDefectItem = mDefectItemViewModel.getDefectItemSync(mDefectId);
        mRoomList = mDefectItemViewModel.getRooms();
        mDirectionList = mDefectItemViewModel.getDirections();
        mFaultList = mDefectItemViewModel.getFaults();

        initializeViews();
        initializeActivityResults();
        initializeButtonListeners();
        initializeTextViewListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.defect_item_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);
        mRadioGroupDefectStatus = findViewById(R.id.defect_item_radio_group);
        mRadioButtonR = findViewById(R.id.defect_item_radio_r);
        mRadioButtonNA = findViewById(R.id.defect_item_radio_na);
        mDefectItemTextLocation = findViewById(R.id.defect_item_text_location);
        mDefectItemTextRoom = findViewById(R.id.defect_item_text_room);
        mDefectItemTextDirection = findViewById(R.id.defect_item_text_direction);
        mDefectItemTextFault = findViewById(R.id.defect_item_text_fault);
        mDefectItemTextCannedComment = findViewById(R.id.defect_item_text_canned_comment);
        mDefectItemLabelPreviousComment = findViewById(R.id.defect_item_label_previous_comment);
        mDefectItemTextPreviousComment = findViewById(R.id.defect_item_text_previous_comment);
        mButtonMicrophone = findViewById(R.id.defect_item_button_microphone);
        mDefectItemTextComment = findViewById(R.id.defect_item_text_comment);
        mLabelLotNumber = findViewById(R.id.defect_item_label_lot_number);
        mTextLotNumber = findViewById(R.id.defect_item_text_lot_number);
        mLabelConstructionStage = findViewById(R.id.defect_item_label_construction_stage);
        mSpinnerConstructionStage = findViewById(R.id.defect_item_spinner_construction_stage);
        mImageViewThumbnail = findViewById(R.id.defect_item_imageview_thumbnail);
        mTextAttachmentLabel = findViewById(R.id.defect_item_text_attachment_label);
        mButtonAddAttachment = findViewById(R.id.defect_item_button_add_attachment);
        mTextAttachmentFilename = findViewById(R.id.defect_item_text_attachment_filename);
        mButtonCamera = findViewById(R.id.defect_item_button_camera);
        mSaveInspectionDefect = findViewById(R.id.defect_item_button_save);
        mButtonCancel = findViewById(R.id.defect_item_button_cancel);
    }
    private void initializeButtonListeners() {
        setupVoiceToTextListener();
        mButtonAddAttachment.setOnClickListener(v -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("*/*");
            chooseFileLauncher.launch(fileIntent);
        });
        mButtonCamera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.burgess.bridge", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        mSaveInspectionDefect.setOnClickListener(v -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            // Initialize local variables for new defect item.
            int defectStatusId = 0;
            int priorInspectionDetailId = mInspectionHistoryId == -1 ? 0 : mInspectionHistoryId;
            int firstInspectionDetailId = mFirstDetailId == -1 ? 0 : mFirstDetailId;
            long newId = 0;
            String comment = "";
            String lotNumber = "";
            String constructionStage = "";

            // If DefectId == 1, it's a note, set appropriate status id. Otherwise, get radio button
            if (mDefectId == 1) {
                defectStatusId = 7;
            } else {
                int selectedRadioId = mRadioGroupDefectStatus.getCheckedRadioButtonId();
                if (selectedRadioId == R.id.defect_item_radio_nc) {
                    defectStatusId = 2;
                } else if (selectedRadioId == R.id.defect_item_radio_c) {
                    defectStatusId = 3;
                } else if (selectedRadioId == R.id.defect_item_radio_r) {
                    defectStatusId = 6;
                } else if (selectedRadioId == R.id.defect_item_radio_na) {
                    defectStatusId = 4;
                }
            }

            // Comment length
            if (mDefectItemTextComment.length() > 250) {
                Snackbar.make(mConstraintLayout, "Comment is over 250 characters.", Snackbar.LENGTH_LONG).show();
                return;
            }

            // If it's Multifamily and not in Observation categories, require a picture
            if (mInspection.division_id == 20 && !mDefectItem.defect_category_name.contains("Observation") && defectStatusId == 3 && !mPictureTaken) {
                Snackbar.make(mConstraintLayout, "MFC Inspections require a photo for items marked C in this category.", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (mInspection.division_id != 20 && mDefectItem.defect_category_name.contains("Observation") && defectStatusId == 3 && !mPictureTaken) {
                Snackbar.make(mConstraintLayout, "Defect items marked C in this category require a picture.", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Custom rules for AHTVX
            int[] builderIds_AHTVX = { 3083, 3084, 3082, 3085 };
            for (int builderId : builderIds_AHTVX) {
                if (builderId == mInspection.builder_id) {
                    if (!mInspection.reinspect && defectStatusId == 2 && !mPictureTaken) {
                        Snackbar.make(mConstraintLayout, "A picture is required for this builder on NC items during 1st time inspections.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (mInspection.reinspect && defectStatusId == 3 && !mPictureTaken) {
                        Snackbar.make(mConstraintLayout, "A picture is required for this builder on C items during reinspections.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            // Custom rules for Coventry / Dreamfinders
            int[] builderIds_Coventry_Dreamfinders = { 396, 397, 2254, 2255 };
            for (int builderId : builderIds_Coventry_Dreamfinders) {
                if (builderId == mInspection.builder_id && (mInspection.inspection_class == 1 || mInspection.inspection_class == 2) ) {
                    if (!mInspection.reinspect && defectStatusId == 2 && !mPictureTaken) {
                        Snackbar.make(mConstraintLayout, "A picture is required for this builder on NC items during 1st time inspections.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            if (mInspection.require_risk_assessment && (mTextLotNumber.getText().toString() == null || mSpinnerConstructionStage.getSelectedItem().toString().equals("--Choose an option--") || !mPictureTaken)) {
                Snackbar.make(mConstraintLayout, "Must have a lot number, construction stage, and picture for this inspection type.", Snackbar.LENGTH_LONG).show();
                return;
            } else if (mInspection.require_risk_assessment) {
                lotNumber = mTextLotNumber.getText().toString();
                constructionStage = mSpinnerConstructionStage.getSelectedItem().toString();
            }

            // Append all text fields together
            comment += mDefectItemTextLocation.getText().toString();
            comment += mDefectItemTextRoom.getText().toString();
            comment += mDefectItemTextDirection.getText().toString();
            comment += mDefectItemTextFault.getText().toString();
            comment += mDefectItemTextCannedComment.getText().toString();
            comment += mDefectItemTextComment.getText();

            // If there is a previous comment and current comment, prepend previous comment and date
            if (!mDefectItemTextPreviousComment.getText().equals("") && !comment.equals("")) {
                LocalDate currentDate = LocalDate.now();
                comment = mDefectItemTextPreviousComment.getText() + "\n" + currentDate.getMonth().getValue() + "/" + currentDate.getDayOfMonth() + " - " + comment;
            } else if (!mDefectItemTextPreviousComment.getText().equals("") && comment.equals("")) {
                comment += mDefectItemTextPreviousComment.getText();
            }

            // If a picture was taken, create a new InspectionDefect_Table with the picture path
            if (mPictureTaken && !mAttachmentIncluded) {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, firstInspectionDetailId, lotNumber, constructionStage, mDefectItem.reinspection_required, mCurrentPhotoPath, null);
            } else if (mAttachmentIncluded && !mPictureTaken) {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, firstInspectionDetailId, lotNumber, constructionStage, mDefectItem.reinspection_required, null, attachmentData);
            } else if (mAttachmentIncluded && mPictureTaken) {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, firstInspectionDetailId, lotNumber, constructionStage, mDefectItem.reinspection_required, mCurrentPhotoPath, attachmentData);
            } else {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, firstInspectionDetailId, lotNumber, constructionStage, mDefectItem.reinspection_required, null, null);
            }

            // If mInspectionDefectId > 0, that means this is a previously created item, update the item. Otherwise, create a new one.
            if (mInspectionDefectId > 0) {
                InspectionDefect_Table currentItem = mDefectItemViewModel.getInspectionDefect(mInspectionDefectId);
                currentItem.comment = comment;
                currentItem.defect_status_id = defectStatusId;
                if (mPictureTaken) {
                    currentItem.picture_path = mCurrentPhotoPath;
                }
                mDefectItemViewModel.updateInspectionDefect(currentItem);
                mDefectItemViewModel.updateReviewedStatus(defectStatusId, mInspectionHistoryId);
                mDefectItemViewModel.updateIsReviewed(mInspectionHistoryId);
                mDefectItemViewModel.updateInspectionDefectId(mInspectionDefectId, mInspectionHistoryId);
                finish();
            } else {
                newId = mDefectItemViewModel.insertInspectionDefect(inspectionDefect);
                // If this is a reinspect, mInspectionHistoryId will be > 0, update the reviewed flag and the status id.
                if (mInspectionHistoryId > 0) {
                    mDefectItemViewModel.updateIsReviewed(mInspectionHistoryId);
                    mDefectItemViewModel.updateReviewedStatus(defectStatusId, mInspectionHistoryId);
                    mDefectItemViewModel.updateInspectionDefectId((int) newId, mInspectionHistoryId);
                }
                Intent inspectIntent = new Intent(DefectItemActivity.this, InspectActivity.class);
                inspectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
                inspectIntent.putExtra(InspectActivity.FILTER_OPTION, mFilter);
                inspectIntent.putExtra(InspectActivity.SCROLL_POSITION, mScrollPosition);
                startActivity(inspectIntent);
            }
        });
        mButtonCancel.setOnClickListener(v -> {
            Intent inspectIntent = new Intent(DefectItemActivity.this, InspectActivity.class);
            inspectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            inspectIntent.putExtra(InspectActivity.FILTER_OPTION, mFilter);
            inspectIntent.putExtra(InspectActivity.SCROLL_POSITION, mScrollPosition);
            startActivity(inspectIntent);
        });
    }
    private void initializeTextViewListeners() {
        mDefectItemTextLocation.setOnClickListener(v -> {
            mLocationFragment = LocationFragment.newInstance();
            mLocationFragment.show(getSupportFragmentManager(), "LOCATION");
        });
        mDefectItemTextRoom.setOnClickListener(view -> {
            mRoomFragment = RoomFragment.newInstance();
            mRoomFragment.setRoomList(mRoomList);
            mRoomFragment.show(getSupportFragmentManager(), "ROOM");
        });
        mDefectItemTextDirection.setOnClickListener(view -> {
            mDirectionFragment = DirectionFragment.newInstance();
            mDirectionFragment.setDirectionList(mDirectionList);
            mDirectionFragment.show(getSupportFragmentManager(), "DIRECTION");
        });
        mDefectItemTextFault.setOnClickListener(view -> {
            mFaultFragment = FaultFragment.newInstance();
            mFaultFragment.setFaultList(mFaultList);
            mFaultFragment.show(getSupportFragmentManager(), "FAULT");
        });
        mDefectItemTextCannedComment.setOnClickListener(view -> {
            if (mInspection.inspection_class == 7) {
                mCannedCommentFragment = CannedCommentFragment.newInstance(mDefectItemViewModel.getEnergyCannedCommentsSync());
            } else {
                mCannedCommentFragment = CannedCommentFragment.newInstance(mDefectItemViewModel.getCannedCommentsSync());
            }
            mCannedCommentFragment.show(getSupportFragmentManager(), "CANNED_COMMENT");
        });
    }
    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        mDefectItemDetails.append(Integer.toString(mDefectItem.item_number));
        mDefectItemDetails.append(" - ");
        mDefectItemDetails.append(mDefectItem.item_description);

        // Set up spinner
        String[] spinnerItems = new String[]{"--Choose an option--", "Preliminary", "Prime", "Critical"};
        mSpinnerConstructionStageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        mSpinnerConstructionStage.setAdapter(mSpinnerConstructionStageAdapter);

        if (!mInspection.require_risk_assessment) {
            mLabelLotNumber.setVisibility(View.GONE);
            mLabelConstructionStage.setVisibility(View.GONE);
            mTextLotNumber.setVisibility(View.GONE);
            mSpinnerConstructionStage.setVisibility(View.GONE);
        }

        // If mDefectId == 1, then it's a note, hide the radio buttons and show the Add Attachment button.
        if (mDefectId == 1) {
            mRadioGroupDefectStatus.setVisibility(View.INVISIBLE);
            mTextAttachmentLabel.setVisibility(View.VISIBLE);
            mButtonAddAttachment.setVisibility(View.VISIBLE);
            mTextAttachmentFilename.setVisibility(View.VISIBLE);
        }

        // If the category is 128, 131, or 132, default to C
        if (mDefectItem.defect_category_id == 128 || mDefectItem.defect_category_id == 131 || mDefectItem.defect_category_id == 132) {
            mRadioGroupDefectStatus.check(R.id.defect_item_radio_c);
        }

        // If MFC inspection, remove R and NC status
        if (mInspection.division_id == 20) {
            mRadioButtonR.setVisibility(View.GONE);
            mRadioButtonNA.setVisibility(View.GONE);
        }

        // Engineering Inspections where Defect Items default to Complete
        if (mInspection.inspection_type_id == 1706 || mInspection.inspection_type_id == 1708 || mInspection.inspection_type_id == 1709 ||
                mInspection.inspection_type_id == 1710 || mInspection.inspection_type_id == 1711 || mInspection.inspection_type_id == 1851 ||
                mInspection.inspection_type_id == 1852 || mInspection.inspection_type_id == 1853 || mInspection.inspection_type_id == 1854 ||
                mInspection.inspection_type_id == 1855) {
            mRadioGroupDefectStatus.check(R.id.defect_item_radio_c);
        }

        // If mInspectionDefectId > 0, then we're editing an existing item (coming from Review & Submit screen)
        // So populate the comment box and current status
        if (mInspectionDefectId > 0) {
            InspectionDefect_Table currentItem = mDefectItemViewModel.getInspectionDefect(mInspectionDefectId);
            switch (currentItem.defect_status_id) {
                case 2:
                    mRadioGroupDefectStatus.check(R.id.defect_item_radio_nc);
                    break;
                case 3:
                    mRadioGroupDefectStatus.check(R.id.defect_item_radio_c);
                    break;
                case 6:
                    mRadioGroupDefectStatus.check(R.id.defect_item_radio_r);
                    break;
                case 4:
                    mRadioGroupDefectStatus.check(R.id.defect_item_radio_na);
                    break;
            }
            mDefectItemTextComment.setText(currentItem.comment);

            if (currentItem.picture_path != null) {
                mPictureTaken = true;
                mCurrentPhotoPath = currentItem.picture_path;
                displayThumbnail();
            }
        }

        // If mInspectionHistoryId > 0, then this is a reinspect and we need to show the previous comment label / text
        if (mInspectionHistoryId > 0) {
            // Change constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mConstraintLayout);
            constraintSet.connect(R.id.defect_item_button_microphone, ConstraintSet.TOP, R.id.defect_item_text_previous_comment, ConstraintSet.BOTTOM);
            constraintSet.applyTo(mConstraintLayout);

            mDefectItemLabelPreviousComment.setVisibility(View.VISIBLE);
            mDefectItemTextPreviousComment.setVisibility(View.VISIBLE);
            mDefectItemTextPreviousComment.setText(mDefectItemViewModel.getInspectionHistoryComment(mInspectionHistoryId));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupVoiceToTextListener() {
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("SPEECH", "onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("SPEECH", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.d("SPEECH", "onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d("SPEECH", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("SPEECH", "onEndOfSpeech");
            }

            @Override
            public void onError(int i) {
                Log.d("SPEECH", "onError");
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d("SPEECH", "onResults");
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null) {
                    mDefectItemTextComment.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.d("SPEECH", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d("SPEECH", "onEvent");
            }
        });
        mButtonMicrophone.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    mSpeechRecognizer.stopListening();
                    mDefectItemTextComment.setHint("");
                    break;
                case MotionEvent.ACTION_DOWN:
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    mDefectItemTextComment.setHint("Listening...");
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    // Camera functions
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Bridge_" + mInspectionId + "_" + mDefectId + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        try {
            image = File.createTempFile(imageFileName, ".png", storageDir);
            mCurrentPhotoPath = image.getAbsolutePath();
            BridgeLogger.log('I', TAG, "Created image for InspectionId: " + mInspectionId + ", DefectId: " + mDefectId);
            return image;
        } catch (IOException e) {
            BridgeLogger.log('E', TAG, "ERROR in createImageFile: " + e.getMessage());
            return null;
        }
    }
    private void displayThumbnail() {
        try {
            Bitmap imageThumbnailBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentPhotoPath), 128, 128);
            Bitmap outBmp;
            float degrees = 90;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            outBmp = Bitmap.createBitmap(imageThumbnailBitmap, 0, 0, imageThumbnailBitmap.getWidth(), imageThumbnailBitmap.getHeight(), matrix, true);
            mImageViewThumbnail.setImageBitmap(outBmp);
        } catch (NullPointerException e) {
            mImageViewThumbnail.setImageResource(R.drawable.ic_no_picture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            displayThumbnail();
            mPictureTaken = true;
        }
    }

    private void initializeActivityResults() {
        chooseFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri data = result.getData().getData();
                BridgeLogger.log('I', TAG, "Attached file for Defect on Inspection: " + mInspectionId);
                mAttachmentIncluded = true;
                mTextAttachmentFilename.setText("testpdf");
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    int nextByte = inputStream.read();
                    while (nextByte != -1) {
                        os.write(nextByte);
                        nextByte = inputStream.read();
                    }
                    attachmentData = os.toByteArray();
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, e.getMessage());
                }
            }
        });
    }
}