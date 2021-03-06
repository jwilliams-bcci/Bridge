package com.example.bridge.defectitem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bridge.DirectionFragment;
import com.example.bridge.FaultFragment;
import com.example.bridge.LocationFragment;
import com.example.bridge.R;
import com.example.bridge.RoomFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import data.CannedComment;
import data.DataManager;
import data.DefectItem;
import data.Tables.DefectItem_Table;

public class DefectItemActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String DEFECT_ID = "com.example.bridge.DEFECT_ID";
    public static final int DEFECT_ID_NOT_FOUND = -1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public boolean pictureTaken = false;
    public final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

    private int mInspectionId;
    private int mDefectId;
    private DefectItemViewModel mDefectItemViewModel;
    private LiveData<DefectItem_Table> mDefectItem;
    private Spinner mSpinnerCannedComment;
    private TextView mDefectItemDetails;
    private TextView mDefectItemTextSpeech;
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
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        mDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);

        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mSpinnerCannedComment = findViewById(R.id.defect_item_spinner_canned_comment);
        mDefectItemTextSpeech = findViewById(R.id.defect_item_text_speech);
        mImageViewThumbnail = findViewById(R.id.defect_item_imageview_thumbnail);
        mDefectItemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DefectItemViewModel.class);
        mDefectItem = mDefectItemViewModel.getDefectItem(mDefectId);

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
                    mDefectItemTextSpeech.setText(matches.get(0));
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

        });

        ImageButton buttonCamera = findViewById(R.id.defect_item_button_camera);
        buttonCamera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                //error
            }
        });

        ImageButton buttonMicrophone = findViewById(R.id.defect_item_button_microphone);
        buttonMicrophone.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    mSpeechRecognizer.stopListening();
                    mDefectItemTextSpeech.setHint("");
                    break;
                case MotionEvent.ACTION_DOWN:
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    //mDefectItemTextSpeech.setText("");
                    mDefectItemTextSpeech.setHint("Listening...");
                    break;
                default:
                    break;
            }
            return false;
        });

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

    private void saveInspectionDefect() {
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageViewThumbnail.setImageBitmap(imageBitmap);
            pictureTaken = true;
        }
    }
}