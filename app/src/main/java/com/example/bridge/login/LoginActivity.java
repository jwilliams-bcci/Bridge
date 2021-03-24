package com.example.bridge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.bridge.BridgeAPIQueue;
import com.example.bridge.R;
import com.example.bridge.ServerCallback;
import com.example.bridge.routesheet.RouteSheetActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import data.Tables.CannedComment_Table;
import data.Tables.DefectCategory_InspectionType_XRef;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mLoginViewModel;
    private SharedPreferences mSharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textUserName = findViewById(R.id.login_text_username);
        TextView textPassword = findViewById(R.id.login_text_password);
        Button buttonLogin = findViewById(R.id.login_button_login);
        mSharedPreferences = getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);

        mLoginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        buttonLogin.setOnClickListener(view -> {
            textPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            textUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
            String userName = textUserName.getText().toString();
            String password = textPassword.getText().toString();

            RequestQueue queue = BridgeAPIQueue.getInstance(this).getRequestQueue();
            JsonObjectRequest loginRequest = loginUser("https://apistage.burgess-inc.com/api/Bridge/Login?userName=" + userName + "&password=" + password, new ServerCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure() {
                }
            });
            JsonArrayRequest updateCannedCommentsRequest = updateCannedComments("https://apistage.burgess-inc.com/api/Bridge/GetCannedComments", new ServerCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure() {

                }
            });
            JsonArrayRequest updateDefectItemsRequest = updateDefectItems("https://apistage.burgess-inc.com/api/Bridge/GetDefectItems", new ServerCallback() {
                @Override
                public void onSuccess() {
                    Intent routeSheetIntent = new Intent(LoginActivity.this, RouteSheetActivity.class);
                    startActivity(routeSheetIntent);
                }
                @Override
                public void onFailure() {

                }
            });
            JsonArrayRequest updateDIITReference = updateDefectItem_InspectionTypeXRef("https://apistage.burgess-inc.com/api/Bridge/GetDefectItem_InspectionType_XRef", new ServerCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure() {

                }
            });

            queue.add(updateCannedCommentsRequest);
            queue.add(updateDefectItemsRequest);
            queue.add(updateDIITReference);
            queue.add(loginRequest);
        });
    }

    private JsonObjectRequest loginUser(String url, final ServerCallback callBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("AuthorizationToken", response.optString("AuthorizationToken"));
            editor.putString("SecurityUserId", response.optString("SecurityUserId"));
            editor.putString("InspectorId", response.optString("InspectorId"));
            editor.apply();

            callBack.onSuccess();
        }, error -> {
            NetworkResponse response = error.networkResponse;
            if (error instanceof AuthFailureError && response != null) {
                Toast.makeText(getApplicationContext(), "Incorrect Username / Password", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
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
                    defectItem.item_number = obj.optInt("ItemNumber");
                    defectItem.item_description = obj.optString("ItemDescription");
                    defectItem.defect_category_id = obj.optInt("DefectCategoryID");
                    defectItem.category_name = obj.optString("CategoryName");

                    mLoginViewModel.insertDefectItem(defectItem);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error in getting defect JSON", Toast.LENGTH_SHORT).show();
                }
            }
            callBack.onSuccess();
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error in updating defect items, Authorization token is " + mSharedPreferences.getString("AuthorizationToken","NULL"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
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
                    Toast.makeText(getApplicationContext(), "Error in getting Canned Comment JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error in updating Canned Comments, Authorization token is " + mSharedPreferences.getString("AuthorizationToken", "NULL"), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
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
                    Toast.makeText(getApplicationContext(), "Error in getting DI/IT Xref", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        return request;
    }
}