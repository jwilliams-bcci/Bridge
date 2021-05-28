package com.burgess.bridge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button mButtonLogin;
    private CheckBox mCheckBoxKeepMeLoggedIn;
    private ConstraintLayout mConstraintLayout;
    private LinearLayout mLockScreen;
    private ProgressBar mProgressBar;

    private static final String TAG = "LOGIN";
    private static final String LOGIN_URL = "https://apistage.burgess-inc.com/api/Bridge/Login?userName=%s&password=%s";
    private static final String CANNED_COMMENTS_URL = "https://apistage.burgess-inc.com/api/Bridge/GetCannedComments";
    private static final String DEFECT_ITEMS_URL = "https://apistage.burgess-inc.com/api/Bridge/GetDefectItems";
    private static final String DEFECT_ITEM_INSPECTION_TYPE_XREF_URL = "https://apistage.burgess-inc.com/api/Bridge/GetDefectItem_InspectionType_XRef";
    private JsonObjectRequest mLoginRequest;
    private JsonArrayRequest mUpdateCannedCommentsRequest;
    private JsonArrayRequest mUpdateDefectItemsRequest;
    private JsonArrayRequest mUpdateDIITReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        mTextUserName = findViewById(R.id.login_text_username);
        mTextPassword = findViewById(R.id.login_text_password);
        mButtonLogin = findViewById(R.id.login_button_login);
        mCheckBoxKeepMeLoggedIn = findViewById(R.id.login_checkbox_keep_me_logged_in);
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mConstraintLayout = findViewById(R.id.login_constraint_layout);
        mLockScreen = findViewById(R.id.login_lock_screen);
        mProgressBar = findViewById(R.id.login_progress_bar);

        // Set the text boxes to respond to "Return" on keyboard
        mTextUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
        mTextPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);

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

            mLoginRequest = loginUser(String.format(LOGIN_URL, userName, password), new ServerCallback() {
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
            mUpdateCannedCommentsRequest = updateCannedComments(CANNED_COMMENTS_URL, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "updateCannedComments returned success");
                    queue.add(mUpdateDefectItemsRequest);
                }

                @Override
                public void onFailure() {
                    Log.i(TAG, "updateCannedComments returned failure");
                    mProgressBar.setVisibility(View.GONE);
                    mLockScreen.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            mUpdateDefectItemsRequest = updateDefectItems(DEFECT_ITEMS_URL, new ServerCallback() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "updateDefectItems returned success");
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
            mUpdateDIITReference = updateDefectItem_InspectionTypeXRef(DEFECT_ITEM_INSPECTION_TYPE_XREF_URL, new ServerCallback() {
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

    private JsonObjectRequest loginUser(String url, final ServerCallback callBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("AuthorizationToken", response.optString("AuthorizationToken"));
            editor.putString("SecurityUserId", response.optString("SecurityUserId"));
            editor.putString("InspectorId", response.optString("InspectorId"));
            editor.apply();

            Log.i(TAG, "AuthorizationToken set to " + response.optString("AuthorizationToken"));
            Log.i(TAG, "SecurityUserId set to " + response.optString("SecurityUserId"));
            Log.i(TAG, "InspectorId set to " + response.optString("InspectorId"));

            callBack.onSuccess();
        }, error -> {
            NetworkResponse response = error.networkResponse;
            if (error instanceof AuthFailureError && response != null) {
                Snackbar.make(mConstraintLayout, "Incorrect Username / Password", Snackbar.LENGTH_LONG).show();
            }
            Log.i(TAG, "Error in loginUser: " + error.getMessage());
            callBack.onFailure();
        });
        return request;
    }

    private JsonArrayRequest updateCannedComments(String url, final ServerCallback callBack) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    CannedComment_Table cannedComment = new CannedComment_Table();
                    cannedComment.id = obj.optInt("CommentKey");
                    cannedComment.text = obj.optString("Comment");

                    mLoginViewModel.insertCannedComment(cannedComment);
                } catch (JSONException e) {
                    Log.i(TAG, "Error in parsing JSON in updateCannedComments - " + e.getMessage());
                    callBack.onFailure();
                }
            }
            callBack.onSuccess();
        }, error -> {
            Log.i(TAG, "Error in updateCannedComments - " + error.getMessage());
            callBack.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }

    private JsonArrayRequest updateDefectItems(String url, final ServerCallback callBack) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    DefectItem_Table defectItem = new DefectItem_Table();
                    defectItem.id = obj.optInt("DefectItemID");
                    defectItem.defect_category_id = obj.optInt("DefectCategoryID");
                    defectItem.defect_category_name = obj.optString("CategoryName");
                    defectItem.item_number = obj.optInt("ItemNumber");
                    defectItem.inspection_type_id = obj.optInt("InspectionTypeID");
                    defectItem.item_description = obj.optString("ItemDescription");
                    defectItem.spanish_item_description = obj.optString("SpanishItemDescription");

                    mLoginViewModel.insertDefectItem(defectItem);
                } catch (JSONException e) {
                    Log.i(TAG, "Error in parsing JSON in updateDefectItems - " + e.getMessage());
                    callBack.onFailure();
                }
            }
            callBack.onSuccess();
        }, error -> {
            Log.i(TAG, "Error in updateDefectItems - " + error.getMessage());
            callBack.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }

    private JsonArrayRequest updateDefectItem_InspectionTypeXRef(String url, final ServerCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    DefectItem_InspectionType_XRef relation = new DefectItem_InspectionType_XRef();
                    relation.defect_item_id = obj.optInt("DefectItemID");
                    relation.inspection_type_id = obj.optInt("InspectionTypeID");

                    mLoginViewModel.insertReference(relation);
                } catch (JSONException e) {
                    Log.i(TAG, "Error in parsing updateDefectItem_InspectionTypeXRef - " + e.getMessage());
                    callback.onFailure();
                }
            }
            callback.onSuccess();
        }, error -> {
            Log.i(TAG, "Error in updateDefectItem_InspectionTypeXRef - " + error.getMessage());
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }
}