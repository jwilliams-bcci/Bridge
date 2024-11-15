package com.burgess.bridge.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import static com.burgess.bridge.Constants.*;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mLoginViewModel;
    private ConstraintLayout mConstraintLayout;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView mTextUserName;
    private TextView mTextPassword;
    private ImageView mImageViewShowPassword;
    private TextView mTextVersionName;
    private TextView mTextStaging;
    private Button mButtonLogin;
    private CheckBox mCheckBoxRememberCredentials;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;

    private JsonArrayRequest mUpdateCannedCommentsRequest;
    private JsonArrayRequest mUpdateBuildersRequest;
    private JsonArrayRequest mUpdateInspectorsRequest;
    private JsonArrayRequest mUpdateRoomsRequest;
    private JsonArrayRequest mUpdateDirectionsRequest;
    private JsonArrayRequest mUpdateFaultsRequest;
    private String mUserName;
    private String mPassword;

    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        // Prepare Shared Preferences...
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(PREF_IS_ONLINE, true);
        mEditor.apply();

        // Prepare Logger and API...
        BridgeLogger.getInstance(this);
        BridgeAPIQueue.getInstance(this);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        checkPermissions();
    }

    private void initializeViews() {
        mTextUserName = findViewById(R.id.login_text_username);
        mTextPassword = findViewById(R.id.login_text_password);
        mImageViewShowPassword = findViewById(R.id.login_imageview_show_password);
        mTextVersionName = findViewById(R.id.login_text_version_name);
        mTextStaging = findViewById(R.id.login_text_staging);
        mButtonLogin = findViewById(R.id.login_button_login);
        mCheckBoxRememberCredentials = findViewById(R.id.login_checkbox_remember_credentials);
        mConstraintLayout = findViewById(R.id.login_constraint_layout);
        mLockScreen = findViewById(R.id.login_lock_screen);
        mProgressBar = findViewById(R.id.login_progress_bar);
    }
    private void initializeButtonListeners() {
        mButtonLogin.setOnClickListener(view -> {
            mUserName = mTextUserName.getText().toString();
            mPassword = mTextPassword.getText().toString();

            if (mCheckBoxRememberCredentials.isChecked()) {
                mEditor.putBoolean(REMEMBER_CREDENTIALS, true);
            } else {
                mEditor.putBoolean(REMEMBER_CREDENTIALS, false);
            }
            mEditor.apply();

            if (!isNetworkAvailable()) {
                showWorkOfflineSnackbar("No network available!");
            } else {
                mEditor.putBoolean(PREF_IS_ONLINE, true);
                mEditor.apply();
                workOnline();
            }
        });
        mImageViewShowPassword.setOnClickListener(view -> {
            if (mTextPassword.getTransformationMethod() == null) {
                mTextPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            } else {
                mTextPassword.setTransformationMethod(null);
            }
        });
    }
    private void initializeDisplayContent() {
        if (mSharedPreferences.getBoolean(REMEMBER_CREDENTIALS, false)) {
            mCheckBoxRememberCredentials.setChecked(true);
            mTextUserName.setText(mSharedPreferences.getString(PREF_LOGIN_NAME, ""));
            mTextPassword.setText(mSharedPreferences.getString(PREF_LOGIN_PASSWORD, ""));
        }

        // Set the version label to display the version number...
        PackageInfo pInfo = null;
        String versionName = "NOT FOUND";
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
        mTextVersionName.setText(String.format("Version %s", versionName));

        if (!BridgeAPIQueue.getInstance().isProd()) {
            mTextStaging.setVisibility(View.VISIBLE);
        } else {
            mTextStaging.setVisibility(View.GONE);
        }

        // Set the text boxes to respond to "Return" on keyboard...
        mTextUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
        mTextPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
    private void checkPermissions() {
        ArrayList<String> permissionRequests = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionRequests.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionRequests.add(Manifest.permission.CAMERA);
        }
        if (!permissionRequests.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionRequests.toArray(new String[0]), 100);
        }
    }

    private void workOnline() {
        showSpinner();

        RequestQueue queue = BridgeAPIQueue.getInstance(LoginActivity.this).getRequestQueue();
        long tokenAge = mSharedPreferences.getLong(PREF_AUTH_TOKEN_AGE, 0);

        JsonObjectRequest loginRequest = BridgeAPIQueue.getInstance().loginUser(mUserName, mPassword, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                if (mCheckBoxRememberCredentials.isChecked()) {
                    mEditor.putBoolean(REMEMBER_CREDENTIALS, true);
                    mEditor.apply();
                }
                queue.add(mUpdateCannedCommentsRequest);
            }

            @Override
            public void onFailure(String message) {
                if (message.equals("Authentication error! Please try again.")) {
                    Snackbar.make(mConstraintLayout, message, Snackbar.LENGTH_SHORT).show();
                } else {
                    showWorkOfflineSnackbar(message);
                }
                hideSpinner();
            }
        });
        mUpdateCannedCommentsRequest = BridgeAPIQueue.getInstance().updateCannedComments(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                queue.add(mUpdateBuildersRequest);
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });
        mUpdateBuildersRequest = BridgeAPIQueue.getInstance().updateBuilders(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                queue.add(mUpdateInspectorsRequest);
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });
        mUpdateInspectorsRequest = BridgeAPIQueue.getInstance().updateInspectorsV2(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                    queue.add(mUpdateRoomsRequest);
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });
        mUpdateRoomsRequest = BridgeAPIQueue.getInstance().updateRooms(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                queue.add(mUpdateDirectionsRequest);
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });
        mUpdateDirectionsRequest = BridgeAPIQueue.getInstance().updateDirections(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                queue.add(mUpdateFaultsRequest);
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });
        mUpdateFaultsRequest = BridgeAPIQueue.getInstance().updateFaults(mLoginViewModel, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                startActivity(routeSheetIntent);
                hideSpinner();
            }

            @Override
            public void onFailure(String message) {
                showWorkOfflineSnackbar(message);
                hideSpinner();
            }
        });

        if (tokenAge > System.currentTimeMillis() - (60 * 60 * 12 * 1000)) {
            BridgeLogger.log('I', TAG, "Token is younger than 12 hours");
            if (checkSavedLogin()) {
                BridgeLogger.log('I', TAG, "The same user is logging in, bypassing loginRequest.");
                queue.add(mUpdateCannedCommentsRequest);
            } else {
                BridgeLogger.log('I', TAG, "Different user is logging in, getting new token...");
                queue.add(loginRequest);
            }
        } else {
            BridgeLogger.log('I', TAG, "Token is older than 12 hours, getting new one...");
            queue.add(loginRequest);
        }
    }
    private void workOffline() {
        if (checkSavedLogin()) {
            BridgeLogger.log('I', TAG, "Working offline, credentials match");
            try {
                Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                startActivity(routeSheetIntent);
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in workOffline: " + e.getMessage());
                Snackbar.make(mConstraintLayout, "Error! Please contact support.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            BridgeLogger.log('I', TAG, "Working offline, credentials do not match");
            Snackbar.make(mConstraintLayout, "Credentials do not match, please try again when network is available", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkSavedLogin() {
        String savedUsername = mSharedPreferences.getString(PREF_LOGIN_NAME, null);
        String savedPassword = mSharedPreferences.getString(PREF_LOGIN_PASSWORD, null);
        return mUserName != null && mPassword != null && mUserName.equalsIgnoreCase(savedUsername) && mPassword.equals(savedPassword);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void showWorkOfflineSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(mConstraintLayout, message + " Continue offline?", Snackbar.LENGTH_INDEFINITE)
                .setAction("YES", v-> {
                    mEditor.putBoolean(PREF_IS_ONLINE, false);
                    mEditor.apply();
                    workOffline();
                });
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);

        snackbar.show();
    }

    private void showSpinner() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLockScreen.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void hideSpinner() {
        mProgressBar.setVisibility(View.GONE);
        mLockScreen.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}