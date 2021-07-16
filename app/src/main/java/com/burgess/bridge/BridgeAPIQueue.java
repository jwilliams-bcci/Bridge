package com.burgess.bridge;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.login.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import data.Repositories.CannedCommentRepository;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;

import static com.burgess.bridge.Constants.API_PROD_URL;
import static com.burgess.bridge.Constants.API_STAGE_URL;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

public class BridgeAPIQueue {
    private static BridgeAPIQueue instance;
    private RequestQueue queue;
    private static Context ctx;
    private boolean isProd;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String TAG = "API";
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_BEARER = "Bearer ";
    private static final String LOGIN_URL = "Login?userName=%s&password=%s";
    private static final String UPDATE_CANNED_COMMENTS_URL = "GetCannedComments";
    private static final String UPDATE_DEFECT_ITEMS_URL = "GetDefectItems";
    private static final String UPDATE_DEFECT_ITEM_INSPECTION_TYPE_XREF_URL = "GetDefectItem_InspectionType_XRef";
    private static final String UPLOAD_INSPECTION_DEFECT_URL = "InsertInspectionDetails";
    private static final String UPDATE_INSPECTION_STATUS_URL = "UpdateInspectionStatus?InspectionId=%s&StatusId=%s";

    private BridgeAPIQueue(Context context) {
        ctx = context;
        queue = getRequestQueue();
        isProd = false;
        mSharedPreferences = context.getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized BridgeAPIQueue getInstance(Context context) {
        if (instance == null) {
            instance = new BridgeAPIQueue(context);
        }
        return instance;
    }

    public static synchronized BridgeAPIQueue getInstance() {
        if (instance == null) {
            throw new IllegalStateException(BridgeAPIQueue.class.getSimpleName() + " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            Cache cache = new DiskBasedCache(ctx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            queue = new RequestQueue(cache, network);
            queue.start();
        }
        return queue;
    }

    // Login
    public JsonObjectRequest loginUser(String userName, String password, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(LOGIN_URL, userName, password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            mEditor.putString(PREF_AUTH_TOKEN, response.optString("AuthorizationToken"));
            mEditor.putString(PREF_SECURITY_USER_ID, response.optString("SecurityUserId"));
            mEditor.putString(PREF_INSPECTOR_ID, response.optString("InspectorId"));
            mEditor.apply();
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - loginUser: " + errorMessage);
            callback.onFailure();
        });
        return request;
    }
    public JsonArrayRequest updateCannedComments(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += UPDATE_CANNED_COMMENTS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    CannedComment_Table cannedComment = new CannedComment_Table();
                    cannedComment.id = obj.optInt("CommentKey");
                    cannedComment.text = obj.optString("Comment");
                    vm.insertCannedComment(cannedComment);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateCannedComments: " + e.getMessage());
                }
            }
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateCannedComments: " + errorMessage);
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }
    public JsonArrayRequest updateDefectItems(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += UPDATE_DEFECT_ITEMS_URL;

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

                    vm.insertDefectItem(defectItem);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateDefectItems: " + e.getMessage());
                }
            }
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateDefectItems: " + errorMessage);
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }
    public JsonArrayRequest updateDefectItem_InspectionTypeXRef(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += UPDATE_DEFECT_ITEM_INSPECTION_TYPE_XREF_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    DefectItem_InspectionType_XRef relation = new DefectItem_InspectionType_XRef();
                    relation.defect_item_id = obj.optInt("DefectItemID");
                    relation.inspection_type_id = obj.optInt("InspectionTypeID");

                    vm.insertReference(relation);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateDIIT: " + e.getMessage());
                }
            }
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateDIIT: " + errorMessage);
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }

    // Route Sheet

    // Review & Submit
    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect, int defectItemId, int inspectionId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += UPLOAD_INSPECTION_DEFECT_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Uploaded defect " + defectItemId + " for inspection " + inspectionId + ".");
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - uploadInspectionDefect: " + errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }

            @Override
            public byte[] getBody() {
                return inspectionDefect.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        return request;
    }
    public StringRequest updateInspectionStatus(int inspectionId, int inspectionStatusId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(UPDATE_INSPECTION_STATUS_URL, inspectionId, inspectionStatusId);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Updated status for " + inspectionId + ".");
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateInspectionStatus: " + errorMessage);
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        return request;
    }
}
