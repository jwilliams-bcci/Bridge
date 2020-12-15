package com.example.bridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.annotation.SuppressLint;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import data.CannedComment;
import data.DataManager;
import data.DefectItem;

public class DefectItemActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String DEFECT_ID = "com.example.bridge.DEFECT_ID";
    public static final int DEFECT_ID_NOT_FOUND = -1;
    public final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

    private int mInspectionId;
    private int mDefectId;
    private Spinner mSpinnerCannedComment;
    private LocationFragment mLocationFragment;
    private RoomFragment mRoomFragment;
    private DirectionFragment mDirectionFragment;
    private FaultFragment mFaultFragment;
    private TextView mDefectItemTextSpeech;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_item);
        setSupportActionBar((Toolbar) findViewById(R.id.defect_item_toolbar));
        checkPermission();

        Intent intent = getIntent();
        TextView textDefectItemDetails = findViewById(R.id.defect_item_text_defect_item_details);

        mDefectId = intent.getIntExtra(DEFECT_ID, DEFECT_ID_NOT_FOUND);
        mSpinnerCannedComment = findViewById(R.id.defect_item_spinner_canned_comment);
        mDefectItemTextSpeech = findViewById(R.id.defect_item_text_speech);
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

        ImageButton buttonCamera = findViewById(R.id.defect_item_button_camera);
        buttonCamera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, 1);
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

        displayDefectDetails(textDefectItemDetails);
        fillSpinner(mSpinnerCannedComment);
    }

    private void displayDefectDetails(TextView textDefectItemDetails) {
        DefectItem defectItem = DataManager.getInstance().getDefectItem(mDefectId);
        textDefectItemDetails.append(Integer.toString(defectItem.getItemNumber()));
        textDefectItemDetails.append(" - ");
        textDefectItemDetails.append(defectItem.getItemDescription());
    }

    private void fillSpinner(Spinner cannedCommentSpinner) {
        List<CannedComment> cannedComments = DataManager.getInstance().getCannedComments();
        ArrayAdapter<CannedComment> adapterCannedComments = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cannedComments);
        adapterCannedComments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCannedComment.setAdapter(adapterCannedComments);
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
}