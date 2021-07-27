package com.burgess.bridge.defectitem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.util.DBUtil;

import android.Manifest;
import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.burgess.bridge.R;
import com.burgess.bridge.inspect.InspectActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;

import static com.burgess.bridge.Constants.PREF;

public class DefectItemActivity extends AppCompatActivity {
    private static final String TAG = "DEFECT_ITEM";
    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String INSPECTION_TYPE_ID = "com.burgess.bridge.INSPECTION_TYPE_ID";
    public static final int INSPECTION_TYPE_ID_NOT_FOUND = -1;
    public static final String DEFECT_ID = "com.burgess.bridge.DEFECT_ID";
    public static final int DEFECT_ID_NOT_FOUND = -1;
    public static final String INSPECTION_HISTORY_ID = "com.burgess.bridge.INSPECTION_HISTORY_ID";
    public static final int INSPECTION_HISTORY_ID_NOT_FOUND = -1;
    public static final String INSPECTION_DEFECT_ID = "com.burgess.bridge.INSPECTION_DEFECT_ID";
    public static final int INSPECTION_DEFECT_ID_NOT_FOUND = -1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public boolean pictureTaken = false;
    public final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    private String currentPhotoPath;

    private int mInspectionId;
    private int mInspectionTypeId;
    private int mDefectId;
    private int mInspectionDefectId;
    private int mInspectionHistoryId;
    private DefectItemViewModel mDefectItemViewModel;
    private LiveData<DefectItem_Table> mDefectItem;
    private RadioGroup mRadioGroupDefectStatus;
    private Spinner mSpinnerCannedComment;
    private TextView mDefectItemDetails;
    private TextView mDefectItemTextLocation;
    private TextView mDefectItemTextRoom;
    private TextView mDefectItemTextDirection;
    private TextView mDefectItemTextFault;
    private TextView mDefectItemTextComment;
    private TextView mDefectItemTextPreviousComment;
    private TextView mDefectItemLabelPreviousComment;
    private Button mButtonCancel;
    private ImageView mImageViewThumbnail;
    private SharedPreferences mSharedPreferences;
    private Button mSaveInspectionDefect;

    // Fragments
    private LocationFragment mLocationFragment;
    private RoomFragment mRoomFragment;
    private DirectionFragment mDirectionFragment;
    private FaultFragment mFaultFragment;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_item);
        setSupportActionBar(findViewById(R.id.defect_item_toolbar));
        checkPermission();
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        mDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspectionTypeId = intent.getIntExtra(INSPECTION_TYPE_ID, INSPECTION_TYPE_ID_NOT_FOUND);
        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mInspectionDefectId = intent.getIntExtra(INSPECTION_DEFECT_ID, INSPECTION_DEFECT_ID_NOT_FOUND);
        mInspectionHistoryId = intent.getIntExtra(INSPECTION_HISTORY_ID, INSPECTION_HISTORY_ID_NOT_FOUND);
        mSpinnerCannedComment = findViewById(R.id.defect_item_spinner_canned_comment);
        mDefectItemLabelPreviousComment = findViewById(R.id.defect_item_label_previous_comment);
        mDefectItemTextPreviousComment = findViewById(R.id.defect_item_text_previous_comment);
        mDefectItemTextComment = findViewById(R.id.defect_item_text_comment);
        mImageViewThumbnail = findViewById(R.id.defect_item_imageview_thumbnail);
        mRadioGroupDefectStatus = findViewById(R.id.defect_item_radio_group);
        mDefectItemTextLocation = findViewById(R.id.defect_item_text_location);
        mDefectItemTextRoom = findViewById(R.id.defect_item_text_room);
        mDefectItemTextDirection = findViewById(R.id.defect_item_text_direction);
        mDefectItemTextFault = findViewById(R.id.defect_item_text_fault);
        mSaveInspectionDefect = findViewById(R.id.defect_item_button_save);
        mButtonCancel = findViewById(R.id.defect_item_button_cancel);

        mDefectItemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DefectItemViewModel.class);
        mDefectItem = mDefectItemViewModel.getDefectItem(mDefectId);

        Log.i(TAG, "InspectionHistoryId = " + mInspectionHistoryId);

        // Voice to text
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

        TextView locationTextView = findViewById(R.id.defect_item_text_location);
        locationTextView.setOnClickListener(view -> {
            Log.d("LOCATION", "Going into Location fragment...");
            mLocationFragment = LocationFragment.newInstance();
            mLocationFragment.show(getSupportFragmentManager(), "TAG");
        });

        TextView roomTextView = findViewById(R.id.defect_item_text_room);
        roomTextView.setOnClickListener(view -> {
            Log.d("ROOM", "Going into Room fragment...");
            mRoomFragment = RoomFragment.newInstance();
            mRoomFragment.show(getSupportFragmentManager(), "TAG");
        });

        TextView directionTextView = findViewById(R.id.defect_item_text_direction);
        directionTextView.setOnClickListener(view -> {
            Log.d("DIRECTION", "Going into Direction fragment...");
            mDirectionFragment = DirectionFragment.newInstance();
            mDirectionFragment.show(getSupportFragmentManager(), "TAG");
        });

        TextView faultTextView = findViewById(R.id.defect_item_text_fault);
        faultTextView.setOnClickListener(view -> {
            Log.d("FAULT", "Going into Fault fragment...");
            mFaultFragment = FaultFragment.newInstance();
            mFaultFragment.show(getSupportFragmentManager(), "TAG");
        });

        mSaveInspectionDefect.setOnClickListener(v -> {
            int selectedRadioId = mRadioGroupDefectStatus.getCheckedRadioButtonId();
            int defectStatusId = 0;
            boolean buttonSelected = true;
            String comment = "";
            switch (selectedRadioId) {
                case R.id.defect_item_radio_nc:
                    defectStatusId = 2;
                    break;
                case R.id.defect_item_radio_c:
                    defectStatusId = 3;
                    break;
                case R.id.defect_item_radio_r:
                    defectStatusId = 6;
                    break;
                case R.id.defect_item_radio_na:
                    defectStatusId = 4;
                    break;
                default:
                    buttonSelected = false;
            }

            comment += mDefectItemTextLocation.getText().toString();
            comment += mDefectItemTextRoom.getText().toString();
            comment += mDefectItemTextDirection.getText().toString();
            comment += mDefectItemTextFault.getText().toString();
            comment += mSpinnerCannedComment.getSelectedItem().toString();
            comment += mDefectItemTextComment.getText();
            if (!mDefectItemTextPreviousComment.getText().equals("") && !comment.equals("")) {
                LocalDate currentDate = LocalDate.now();
                comment = mDefectItemTextPreviousComment.getText() + "\n" + currentDate.getMonth().getValue() + "/" + currentDate.getDayOfMonth() + " - " + comment;
            } else if (!mDefectItemTextPreviousComment.getText().equals("") && comment.equals("")) {
                comment += mDefectItemTextPreviousComment.getText();
            }

            InspectionDefect_Table inspectionDefect;
            int priorInspectionDetailId = mInspectionHistoryId == -1 ? 0 : mInspectionHistoryId;

            if (pictureTaken) {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, currentPhotoPath);
            } else {
                inspectionDefect = new InspectionDefect_Table(mInspectionId, mDefectId, defectStatusId, comment, priorInspectionDetailId, null);
            }

            if (buttonSelected) {
                if (mInspectionDefectId > 0) {
                    InspectionDefect_Table currentItem = mDefectItemViewModel.getInspectionDefect(mInspectionDefectId);
                    currentItem.comment = comment;
                    currentItem.defect_status_id = defectStatusId;
                    mDefectItemViewModel.updateInspectionDefect(currentItem);
                    finish();
                } else {
                    mDefectItemViewModel.insertInspectionDefect(inspectionDefect);
                    if (mInspectionHistoryId > 0) {
                        mDefectItemViewModel.updateIsReviewed(mInspectionHistoryId);
                    }
                    Intent inspectIntent = new Intent(DefectItemActivity.this, InspectActivity.class);
                    inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
                    inspectIntent.putExtra(InspectActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
                    startActivity(inspectIntent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please select a status", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton buttonCamera = findViewById(R.id.defect_item_button_camera);
        buttonCamera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.burgess.bridge", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        ImageButton buttonMicrophone = findViewById(R.id.defect_item_button_microphone);
        buttonMicrophone.setOnTouchListener((view, motionEvent) -> {
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

        mButtonCancel.setOnClickListener(v -> {
            Intent inspectIntent = new Intent(DefectItemActivity.this, InspectActivity.class);
            inspectIntent.putExtra(InspectActivity.INSPECTION_ID, mInspectionId);
            inspectIntent.putExtra(InspectActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
            startActivity(inspectIntent);
        });

        if (mInspectionDefectId > 0) {
            InspectionDefect_Table currentItem = mDefectItemViewModel.getInspectionDefect(mInspectionDefectId);
            mDefectItemTextComment.setText(currentItem.comment);
        }

        if (mInspectionHistoryId > 0) {
            mDefectItemLabelPreviousComment.setVisibility(View.VISIBLE);
            mDefectItemTextPreviousComment.setVisibility(View.VISIBLE);
            mDefectItemTextPreviousComment.setText(mDefectItemViewModel.getInspectionHistoryComment(mInspectionHistoryId));
        }
        displayDefectDetails(mDefectItemDetails);
        fillSpinner(mSpinnerCannedComment);
    }

    private void displayDefectDetails(TextView textDefectItemDetails) {
        mDefectItem.observe(this, defectItem -> {
            textDefectItemDetails.append(Integer.toString(defectItem.item_number));
            textDefectItemDetails.append(" - ");
            textDefectItemDetails.append(defectItem.item_description);
        });
    }

    private void fillSpinner(Spinner spinnerCannedComments) {
        mDefectItemViewModel.getCannedComments().observe(this, cannedComments -> {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cannedComments);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCannedComments.setAdapter(adapter);
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Bridge_" + mInspectionId + "_" + mDefectId + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void rotateImageFile(String filePath) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap image;
        try {
            image = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            image = BitmapFactory.decodeFile(filePath, options);
            Log.i(TAG, "Out of memory error when rotating image");
        }
        Bitmap outBmp;
        float degrees = 90;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        outBmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        FileOutputStream stream = new FileOutputStream(filePath, false);
        outBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageThumbnailBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentPhotoPath), 128, 128);
            Bitmap outBmp;
            float degrees = 90;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            outBmp = Bitmap.createBitmap(imageThumbnailBitmap, 0, 0, imageThumbnailBitmap.getWidth(), imageThumbnailBitmap.getHeight(), matrix, true);
            mImageViewThumbnail.setImageBitmap(outBmp);
            try {
                rotateImageFile(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pictureTaken = true;
        }
    }
}