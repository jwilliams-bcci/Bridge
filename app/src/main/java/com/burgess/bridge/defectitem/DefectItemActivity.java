package com.burgess.bridge.defectitem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.inspect.InspectActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static final String INSPECTION_DEFECT_ID = "com.burgess.bridge.INSPECTION_DEFECT_ID";
    public static final int INSPECTION_DEFECT_ID_NOT_FOUND = -1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String FILTER_OPTION = "com.burgess.bridge.FILTER_OPTION";
    public boolean mPictureTaken = false;
    public final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    private String mCurrentPhotoPath;
    private long mLastClickTime = 0;

    private int mInspectionId;
    private int mDefectId;
    private int mInspectionDefectId;
    private int mInspectionHistoryId;
    private String mFilter;
    private DefectItemViewModel mDefectItemViewModel;
    private DefectItem_Table mDefectItem;
    private RadioGroup mRadioGroupDefectStatus;
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
    private Button mSaveInspectionDefect;
    private ImageButton mButtonMicrophone;
    private ImageButton mButtonCamera;
    private Inspection_Table mInspection;
    private ConstraintLayout mConstraintLayout;

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
        mDefectItemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DefectItemViewModel.class);
        checkPermission();

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mInspectionDefectId = intent.getIntExtra(INSPECTION_DEFECT_ID, INSPECTION_DEFECT_ID_NOT_FOUND);
        mInspectionHistoryId = intent.getIntExtra(INSPECTION_HISTORY_ID, INSPECTION_HISTORY_ID_NOT_FOUND);
        mFilter = intent.getStringExtra(FILTER_OPTION);
        mInspection = mDefectItemViewModel.getInspectionSync(mInspectionId);
        mDefectItem = mDefectItemViewModel.getDefectItemSync(mDefectId);
        mRoomList = mDefectItemViewModel.getRooms();
        mDirectionList = mDefectItemViewModel.getDirections();
        mFaultList = mDefectItemViewModel.getFaults();

        initializeViews();
        initializeButtonListeners();
        initializeTextViewListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.defect_item_constraint_layout);
        mDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);
        mRadioGroupDefectStatus = findViewById(R.id.defect_item_radio_group);
        mDefectItemTextLocation = findViewById(R.id.defect_item_text_location);
        mDefectItemTextRoom = findViewById(R.id.defect_item_text_room);
        mDefectItemTextDirection = findViewById(R.id.defect_item_text_direction);
        mDefectItemTextFault = findViewById(R.id.defect_item_text_fault);
        mDefectItemTextCannedComment = findViewById(R.id.defect_item_text_canned_comment);
        mDefectItemLabelPreviousComment = findViewById(R.id.defect_item_label_previous_comment);
        mDefectItemTextPreviousComment = findViewById(R.id.defect_item_text_previous_comment);
        mButtonMicrophone = findViewById(R.id.defect_item_button_microphone);
        mDefectItemTextComment = findViewById(R.id.defect_item_text_comment);
        mImageViewThumbnail = findViewById(R.id.defect_item_imageview_thumbnail);
        mButtonCamera = findViewById(R.id.defect_item_button_camera);
        mSaveInspectionDefect = findViewById(R.id.defect_item_button_save);
        mButtonCancel = findViewById(R.id.defect_item_button_cancel);
    }
    private void initializeButtonListeners() {
        setupVoiceToTextListener();
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
            long newId = 0;
            String comment = "";
            InspectionDefect_Table inspectionDefect;

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

            // If it's Multifamily and not in Observation categories, require a picture
            if (mInspection.division_id == 20 && !mDefectItem.defect_category_name.contains("Observation") && defectStatusId == 3 && !mPictureTaken) {
                Snackbar.make(mConstraintLayout, "MFC Inspections require a photo for items marked C in this category.", Snackbar.LENGTH_LONG).show();
                return;
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
            if (mPictureTaken) {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, mDefectItem.reinspection_required, mCurrentPhotoPath);
            } else {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, mDefectItem.reinspection_required, null);
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
                startActivity(inspectIntent);
            }
        });
        mButtonCancel.setOnClickListener(v -> {
            Intent inspectIntent = new Intent(DefectItemActivity.this, InspectActivity.class);
            inspectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            inspectIntent.putExtra(InspectActivity.FILTER_OPTION, mFilter);
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
        mDefectItemDetails.append(Integer.toString(mDefectItem.item_number));
        mDefectItemDetails.append(" - ");
        mDefectItemDetails.append(mDefectItem.item_description);

        // If mDefectId == 1, then it's a note, hide the radio buttons.
        if (mDefectId == 1) {
            mRadioGroupDefectStatus.setVisibility(View.INVISIBLE);
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
                mCurrentPhotoPath = currentItem.picture_path;
                displayThumbnail();
            }
        }

        // If mInspectionHistoryId > 0, then this is a reinspect and we need to show the previous comment label / text
        if (mInspectionHistoryId > 0) {
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
}