package com.burgess.bridge.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.BridgeHelper;
import com.burgess.bridge.SharedPreferencesRepository;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import static com.burgess.bridge.Constants.*;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginVM;
    private ConstraintLayout constraintLayout;
    //private SharedPreferences sharedPreferences;
    //private SharedPreferences.Editor sharedPreferencesEditor;
    private TextView tUserName;
    private TextView tPassword;
    private ImageView ivShowPassword;
    private TextView tVersionName;
    private TextView tStaging;
    private Button bLogin;
    private CheckBox cbRememberCredentials;
    private LinearLayout lockScreen;
    private TextView tStatus;
    private ProgressBar progressBar;

    private SharedPreferencesRepository sharedPreferences;
    private String userName;
    private String password;

    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginVM = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        // Prepare Shared Preferences...
        sharedPreferences = new SharedPreferencesRepository(this);
        //sharedPreferencesEditor = sharedPreferences.edit();

        // Prepare Logger and API...
        BridgeLogger.getInstance(this);
        BridgeAPIQueue.getInstance(this);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
        checkPermissions();

        loginVM.getSnackbarMessage().observe(this, message -> {
            if (message != null) {
                BridgeHelper.hideSpinner(lockScreen, progressBar, tStatus, getWindow());
                showWorkOfflineSnackbar(message);
            }
        });
        loginVM.getStatus().observe(this, message -> {
            if (message != null) {
                tStatus.setText(message);
            }
        });
        loginVM.getCompleteLogin().observe(this, completeLogin -> {
            if (completeLogin) {
                BridgeHelper.hideSpinner(lockScreen, progressBar, tStatus, getWindow());
                Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                startActivity(routeSheetIntent);
            }
        });
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.login_constraint_layout);
        tUserName = findViewById(R.id.login_text_username);
        tPassword = findViewById(R.id.login_text_password);
        ivShowPassword = findViewById(R.id.login_imageview_show_password);
        tVersionName = findViewById(R.id.login_text_version_name);
        tStaging = findViewById(R.id.login_text_staging);
        bLogin = findViewById(R.id.login_button_login);
        cbRememberCredentials = findViewById(R.id.login_checkbox_remember_credentials);
        lockScreen = findViewById(R.id.ekotrope_data_lock_screen);
        tStatus = findViewById(R.id.login_text_status);
        progressBar = findViewById(R.id.login_progress_bar);
    }
    private void initializeButtonListeners() {
        bLogin.setOnClickListener(view -> {
            userName = tUserName.getText().toString();
            password = tPassword.getText().toString();

            sharedPreferences.setRememberCredentials(cbRememberCredentials.isChecked());

            if (!isNetworkAvailable()) {
                showWorkOfflineSnackbar("No network available!");
            } else {
                workOnline();
            }
        });
        ivShowPassword.setOnClickListener(view -> {
            if (tPassword.getTransformationMethod() == null) {
                tPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            } else {
                tPassword.setTransformationMethod(null);
            }
        });
    }
    private void initializeDisplayContent() {
        if (sharedPreferences.getRememberCredentials()) {
            cbRememberCredentials.setChecked(true);
            tUserName.setText(sharedPreferences.getLoginName());
            tPassword.setText(sharedPreferences.getLoginPassword());
        }

        // Set the version label to display the version number...
        PackageInfo pInfo;
        String versionName = "NOT FOUND";
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
        tVersionName.setText(String.format("Version %s", versionName));

        if (!BridgeAPIQueue.isProd()) {
            tStaging.setVisibility(View.VISIBLE);
        } else {
            tStaging.setVisibility(View.GONE);
        }

        // Set the text boxes to respond to "Return" on keyboard...
        tUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
        tPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
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
        BridgeHelper.showSpinner(lockScreen, progressBar, tStatus, getWindow());

        long tokenAge = sharedPreferences.getAuthTokenAge();
        if (tokenAge > System.currentTimeMillis() - (60 * 60 * 12 * 1000)) {
            if (checkSavedLogin()) {
                loginVM.updateCannedComments(sharedPreferences.getAuthToken());
            } else {
                loginVM.requestLogin(userName, password);
            }
        } else {
            loginVM.requestLogin(userName, password);
        }
    }
    private void workOffline() {
        if (checkSavedLogin()) {
            try {
                Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                startActivity(routeSheetIntent);
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in workOffline: " + e.getMessage());
                Snackbar.make(constraintLayout, "Error! Please contact support.", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(constraintLayout, "Credentials do not match, please try again when network is available", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkSavedLogin() {
        String savedUsername = sharedPreferences.getLoginName();
        String savedPassword = sharedPreferences.getLoginPassword();
        return userName != null && password != null && userName.equalsIgnoreCase(savedUsername) && password.equals(savedPassword);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void showWorkOfflineSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, message + " Continue offline?", Snackbar.LENGTH_INDEFINITE)
                .setAction("YES", v-> {
                    workOffline();
                });
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);

        snackbar.show();
    }
}