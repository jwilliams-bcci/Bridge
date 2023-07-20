package com.burgess.bridge.editresolution;

import static android.view.View.GONE;
import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import data.Enums.Resolution;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class EditResolutionActivity extends AppCompatActivity {
    private EditResolutionViewModel mEditResolutionViewModel;
    private SharedPreferences mSharedPreferences;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private TextView mTextAddress;
    private Spinner mSpinnerResolutions;
    private ImageButton mButtonCamera;
    private ImageView mImageView;
    private TextView mTextComment;
    private Button mButtonSubmit;
    private ProgressBar mProgressSpinner;

    public static final String INSPECTION_ID = "com.example.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    private int mInspectionId;
    private Inspection_Table mInspection;
    private StringRequest mEditResolutionRequest;
    private String mCurrentPhotoPath;
    private boolean mPictureTaken = false;
    private boolean mAttachmentIncluded = false;
    private long mLastClickTime = 0;

    private static final String TAG = "EDIT_RESOLUTION";
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resolution);
        setSupportActionBar(findViewById(R.id.edit_resolution_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditResolutionViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EditResolutionViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mEditResolutionViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.edit_resolution_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mTextAddress = findViewById(R.id.edit_resolution_text_inspection_address);
        mSpinnerResolutions = findViewById(R.id.edit_resolution_spinner_resolutions);
        mTextComment = findViewById(R.id.edit_resolution_text_note);
        mButtonCamera = findViewById(R.id.edit_resolution_button_camera);
        mImageView = findViewById(R.id.edit_resolution_imageview);
        mButtonSubmit = findViewById(R.id.edit_resolution_button_submit);
        mProgressSpinner = findViewById(R.id.edit_resolution_progress_spinner);
    }

    private void initializeButtonListeners() {
        mButtonCamera.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    BridgeLogger.log('E', TAG, "ERROR in launching camera activity: " + e.getMessage());
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.burgess.bridge", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        mButtonSubmit.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            mButtonSubmit.setEnabled(false);
            mProgressSpinner.setVisibility(View.VISIBLE);

            Resolution selectedItem = (Resolution) mSpinnerResolutions.getSelectedItem();
            OffsetDateTime inspectionTime = OffsetDateTime.now();
            if (selectedItem.code == 3 && mCurrentPhotoPath == null) {
                Snackbar.make(mConstraintLayout, "For \"Not Ready\", please submit a picture", Snackbar.LENGTH_SHORT).show();
            } else {
                try {
                    addNote();
                    mEditResolutionRequest = getUpdateInspectionStatusRequest(selectedItem.code, inspectionTime.toString());
                    BridgeAPIQueue.getInstance().getRequestQueue().add(mEditResolutionRequest);
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in EditResolution: " + e.getMessage());
                    Snackbar.make(mConstraintLayout, "Error trying to change status! Please send an activity log!", Snackbar.LENGTH_LONG);
                }
            }
        });
    }

    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type + "\n");
        mProgressSpinner.setVisibility(GONE);

        mSpinnerResolutions.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (List<Resolution>) Arrays.stream(Resolution.values()).filter(e -> e.editResolutionOnly).collect(Collectors.toList())));
        mSpinnerResolutions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resolution selectedItem = (Resolution) mSpinnerResolutions.getSelectedItem();
                if (selectedItem.code == 3) {
                    mButtonCamera.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                } else {
                    mButtonCamera.setVisibility(GONE);
                    mImageView.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addNote() {
        InspectionDefect_Table inspectionDefect;
        long newId = 0;

        if (mPictureTaken && !mAttachmentIncluded) {
            inspectionDefect = new InspectionDefect_Table(mInspectionId, 1, 7, mTextComment.getText().toString(), 0, null, null, false, mCurrentPhotoPath, null);
        } else {
            BridgeLogger.log('I', TAG, "problem here in editresolutionactivity 197");
            inspectionDefect = new InspectionDefect_Table(mInspectionId, 1, 7, mTextComment.getText().toString(), 0, null, null, false, null, null);
        }
        newId = mEditResolutionViewModel.addInspectionDefect(inspectionDefect);

        InspectionDefect_Table newInspectionDefect = mEditResolutionViewModel.getInspectionDefect((int) newId);
        BridgeAPIQueue.getInstance().getRequestQueue().add(getUploadInspectionDefectRequest(newInspectionDefect));
    }

    private StringRequest getUploadInspectionDefectRequest(InspectionDefect_Table noteDetails) {
        BridgeLogger.log('I', TAG, "Uploading note...");
        JSONObject jObj = new JSONObject();
        try{
            jObj.put("InspectionId", noteDetails.inspection_id);
            jObj.put("DefectItemId", noteDetails.defect_item_id);
            jObj.put("DefectStatusId", noteDetails.defect_status_id);
            if (noteDetails.comment != null) {
                jObj.put("Comment", noteDetails.comment);
            } else {
                jObj.put("Comment", "");
            }
            if (noteDetails.picture_path != null) {
                try {
                    jObj.put("ImageData", Base64.getEncoder().encodeToString(getPictureData(noteDetails.id)));
                    jObj.put("ImageFileName", noteDetails.picture_path.substring(noteDetails.picture_path.lastIndexOf("/")+1));
                } catch (NullPointerException e) {
                    Snackbar.make(mConstraintLayout, "Photo is missing!", Snackbar.LENGTH_SHORT).show();
                    return null;
                }
            } else {
                jObj.put("ImageData", null);
                jObj.put("ImageFileName", null);
            }
            jObj.put("PriorInspectionDetailId", noteDetails.prior_inspection_detail_id);
        } catch (JSONException e) {
            BridgeLogger.log('E', TAG, "ERROR in parsing JSON: " + e.getMessage());
        }

        StringRequest request = BridgeAPIQueue.getInstance().uploadInspectionDefect(jObj, noteDetails.defect_item_id, noteDetails.inspection_id, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                BridgeLogger.log('I', TAG, "Note uploaded for " + mInspectionId);
            }

            @Override
            public void onFailure(String message) {
                BridgeLogger.log('E', TAG, "ERROR in EditResolution: " + message);
            }
        });

        return request;
    }

    private StringRequest getUpdateInspectionStatusRequest(int resolutionCode, String inspectionTime) {
        StringRequest request = BridgeAPIQueue.getInstance().updateInspectionStatus(mInspectionId, resolutionCode, mSharedPreferences.getString(PREF_SECURITY_USER_ID, "NULL"), 0, 0, inspectionTime, inspectionTime, 0, 0, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                BridgeLogger.log('I', TAG, "Resolution edited for " + mInspectionId + ": changed to " + resolutionCode);
                mEditResolutionViewModel.deleteInspectionDefects(mInspectionId);
                mEditResolutionViewModel.deleteInspection(mInspectionId);
                BridgeLogger.log('I', TAG, "Deleted defects and inspection " + mInspectionId);

                finish();
                Intent routeSheetIntent = new Intent(EditResolutionActivity.this, RouteSheetActivity.class);
                routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(routeSheetIntent);
            }

            @Override
            public void onFailure(String message) {
                BridgeLogger.log('E', TAG, "ERROR: " + message);
                Snackbar.make(mConstraintLayout, "Error! Please send activity log.", Snackbar.LENGTH_SHORT).show();
                mButtonSubmit.setEnabled(true);
                mProgressSpinner.setVisibility(GONE);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Camera functions
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Bridge_" + mInspectionId + "_NOT_READY_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void displayThumbnail() {
        try {
            Bitmap imageThumbnailBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentPhotoPath), 128, 128);
            Bitmap outBmp;
            float degrees = 90;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            outBmp = Bitmap.createBitmap(imageThumbnailBitmap, 0, 0, imageThumbnailBitmap.getWidth(), imageThumbnailBitmap.getHeight(), matrix, true);
            mImageView.setImageBitmap(outBmp);
            rotateImageFile(mCurrentPhotoPath);
        } catch (IOException e) {
            BridgeLogger.log('E', TAG, "ERROR in displayThumbnail: " + e.getMessage());
        } catch (NullPointerException e) {
            mImageView.setImageResource(R.drawable.ic_no_picture);
        }
    }
    private void rotateImageFile(String filePath) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap image;
        try {
            image = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            image = null;
            BridgeLogger.log('E', TAG, "ERROR in rotateImageFile: " + e.getMessage());
        }
        Bitmap outBmp;
        float degrees = 90;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        assert image != null;
        outBmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        FileOutputStream stream = new FileOutputStream(filePath, false);
        outBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }
    private byte[] getPictureData(int inspectionDefectId) {
        InspectionDefect_Table defect = mEditResolutionViewModel.getInspectionDefect(inspectionDefectId);
        Bitmap image = BitmapFactory.decodeFile(defect.picture_path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            return stream.toByteArray();
        } else {
            return null;
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