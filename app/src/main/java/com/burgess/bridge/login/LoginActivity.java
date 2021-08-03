package com.burgess.bridge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;

import static com.burgess.bridge.Constants.*;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mLoginViewModel;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView mTextUserName;
    private TextView mTextPassword;
    private TextView mTextVersionName;
    private Button mButtonLogin;
    private CheckBox mCheckBoxKeepMeLoggedIn;
    private ConstraintLayout mConstraintLayout;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;

    private static final String TAG = "LOGIN";
    private JsonObjectRequest mLoginRequest;
    private JsonArrayRequest mUpdateCannedCommentsRequest;
    private JsonArrayRequest mUpdateDefectItemsRequest;
    private JsonArrayRequest mUpdateDIITReference;

    private String mVersionName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        initializeViews();

        // Prepare Shared Preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // Set the text boxes to respond to "Return" on keyboard...
        mTextUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
        mTextPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);

        // Set the version label to display the version number...
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersionName = pInfo.versionName;
        mTextVersionName.setText("Version " + mVersionName);

        // Not currently being used - logic to check if "Keep Me Logged In" is checked
        if (mSharedPreferences.getBoolean(PREF_STAY_LOGGED_IN, false)) {
            Log.i(TAG, "KeepMeLoggedIn is true");
            Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
            startActivity(routeSheetIntent);
        }

        mButtonLogin.setOnClickListener(view -> {
            mProgressBar.setVisibility(View.VISIBLE);
            mLockScreen.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            String userName = mTextUserName.getText().toString();
            String password = mTextPassword.getText().toString();

            RequestQueue queue = BridgeAPIQueue.getInstance(LoginActivity.this).getRequestQueue();

            mLoginRequest = BridgeAPIQueue.getInstance().loginUser(userName, password, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "loginRequest returned success");
                    if (mCheckBoxKeepMeLoggedIn.isChecked()) {
                        mEditor.putBoolean(PREF_STAY_LOGGED_IN, true);
                        mEditor.apply();
                    }
                    Log.i(TAG, "Sending request for CannedComments...");
                    queue.add(mUpdateCannedCommentsRequest);
                }

                @Override
                public void onFailure() {
                    Snackbar.make(mConstraintLayout, "Error in login! Email support", Snackbar.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            mUpdateCannedCommentsRequest = BridgeAPIQueue.getInstance().updateCannedComments(mLoginViewModel, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "updateCannedComments returned success");
                    Log.i(TAG, "Sending request for DefectItems...");
                    queue.add(mUpdateDefectItemsRequest);
                }

                @Override
                public void onFailure() {
                    Log.e(TAG, "updateCannedComments returned failure");
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            mUpdateDefectItemsRequest = BridgeAPIQueue.getInstance().updateDefectItems(mLoginViewModel, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "updateDefectItems returned success");
                    Log.i(TAG, "Sending request for DIIT...");
                    queue.add(mUpdateDIITReference);
                }

                @Override
                public void onFailure() {
                    Log.i(TAG, "updateDefectItems returned failure");
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            mUpdateDIITReference = BridgeAPIQueue.getInstance().updateDefectItem_InspectionTypeXRef(mLoginViewModel, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "updateDefectItem_InspectionTypeXRef returned success");
                    Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                    startActivity(routeSheetIntent);
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onFailure() {
                    Log.i(TAG, "updateDefectItem_InspectionTypeXRef returned failure");
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });

            queue.add(mLoginRequest);
        });
    }

    private void initializeViews() {
        mTextUserName = findViewById(R.id.login_text_username);
        mTextPassword = findViewById(R.id.login_text_password);
        mTextVersionName = findViewById(R.id.login_text_version_name);
        mButtonLogin = findViewById(R.id.login_button_login);
        mCheckBoxKeepMeLoggedIn = findViewById(R.id.login_checkbox_keep_me_logged_in);
        mConstraintLayout = findViewById(R.id.login_constraint_layout);
        mLockScreen = findViewById(R.id.login_lock_screen);
        mProgressBar = findViewById(R.id.login_progress_bar);
    }
}