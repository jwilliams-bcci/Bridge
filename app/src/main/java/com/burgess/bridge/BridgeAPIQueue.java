package com.burgess.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
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
import com.burgess.bridge.routesheet.RouteSheetViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

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
    private static final String GET_CANNED_COMMENTS_URL = "GetCannedComments";
    private static final String GET_DEFECT_ITEMS_URL = "GetDefectItems";
    private static final String GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_URL = "GetDefectItem_InspectionType_XRef";
    private static final String GET_INSPECTIONS_URL = "GetInspections?inspectorid=%s&inspectiondate=%s";
    private static final String GET_INSPECTION_HISTORY_URL = "GetInspectionHistory?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private static final String POST_INSPECTION_DEFECT_URL = "InsertInspectionDetails";
    private static final String POST_INSPECTION_STATUS_URL = "UpdateInspectionStatus2?InspectionId=%s&StatusId=%s";

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
        url += GET_CANNED_COMMENTS_URL;

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
        url += GET_DEFECT_ITEMS_URL;

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
        url += GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_URL;

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
    public JsonArrayRequest updateRouteSheet(RouteSheetViewModel vm, String inspectorId, String inspectionDate, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_INSPECTIONS_URL, inspectorId, inspectionDate);

        ArrayList<JsonArrayRequest> inspectionHistoryRequests = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("MM-dd-YYYY", Locale.ENGLISH);
        
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Inspection_Table inspection = new Inspection_Table();
                    inspection.id = obj.optInt("InspectionID");
                    inspection.inspection_date = format.parse(obj.getString("InspectionDate"));
                    inspection.location_id = obj.optInt("LocationID");
                    inspection.builder_name = obj.getString("BuilderName");
                    inspection.builder_id = obj.optInt("BuilderID");
                    inspection.super_name = obj.getString("SuperName");
                    inspection.inspector_id = obj.optInt("InspectorID");
                    inspection.inspector = obj.getString("Inspector");
                    inspection.community = obj.getString("Community");
                    inspection.community_id = obj.optInt("CommunityID");
                    inspection.city = obj.getString("City");
                    inspection.inspection_type_id = obj.optInt("InspectionTypeID");
                    inspection.inspection_type = obj.getString("InspectionType");
                    inspection.reinspect = obj.getBoolean("ReInspect");
                    inspection.inspection_order = obj.optInt("InspectionOrder");
                    inspection.address = obj.getString("Address1");
                    inspection.inspection_status_id = obj.optInt("InspectionStatusID");
                    inspection.inspection_status = obj.getString("InspectionStatus");
                    inspection.super_phone = obj.getString("SuperPhone");
                    inspection.super_email = obj.getString("SuperEmailAddress");
                    inspection.super_present = obj.optInt("SuperintendentPresent");
                    inspection.incomplete_reason = obj.getString("IncompleteReason");
                    inspection.incomplete_reason_id = obj.optInt("IncompleteReasonID");
                    inspection.notes = obj.getString("Comment");
                    inspection.is_complete = false;
                    inspection.is_uploaded = false;
                    inspection.route_sheet_order = obj.optInt("Order");

                    if (inspection.reinspect) {
                        inspectionHistoryRequests.add(updateInspectionHistory(vm, inspection.id, inspection.inspection_order, inspection.inspection_type_id, inspection.location_id));
                    }
                    vm.insertInspection(inspection);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateRouteSheet: " + e.getMessage());
                    callback.onFailure();
                } catch (ParseException e) {
                    Log.e(TAG, "ERROR - updateRouteSheet: " + e.getMessage());
                    callback.onFailure();
                }
            }
            for (int lcv = 0; lcv < inspectionHistoryRequests.size(); lcv++) {
                getRequestQueue().add(inspectionHistoryRequests.get(lcv));
            }
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateRouteSheet: " + errorMessage);
            callback.onFailure();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        return request;
    }
    public JsonArrayRequest updateInspectionHistory(RouteSheetViewModel vm, int inspectionId, int inspectionOrder, int inspectionTypeId, int locationId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_INSPECTION_HISTORY_URL, inspectionOrder, inspectionTypeId, locationId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    InspectionHistory_Table hist = new InspectionHistory_Table();
                    hist.id = obj.optInt("InspectionDetailID");
                    hist.inspection_id = inspectionId;
                    hist.previous_inspection_id = obj.optInt("InspectionID");
                    hist.defect_item_id = obj.optInt("DefectItemID");
                    hist.defect_item_number = obj.optInt("ItemNumber");
                    hist.defect_category_id = obj.optInt("DefectCategoryID");
                    hist.defect_category_name = obj.optString("CategoryName");
                    hist.defect_item_description = obj.optString("ItemDescription");
                    hist.comment = obj.optString("Comment");
                    hist.is_reviewed = false;
                    vm.insertInspectionHistory(hist);
                    Log.i(TAG, "Added id: " + hist.id);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateInspectionHistory: " + e.getMessage());
                }
            }
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateInspectionHistory: " + errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        return request;
    }

    // Review & Submit
    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect, int defectItemId, int inspectionId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_DEFECT_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Uploaded defect " + defectItemId + " for inspection " + inspectionId + ".");
            callback.onSuccess();
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - uploadInspectionDefect: " + errorMessage);
            callback.onFailure();
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
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(45), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public StringRequest updateInspectionStatus(int inspectionId, int inspectionStatusId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(POST_INSPECTION_STATUS_URL, inspectionId, inspectionStatusId);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Updated status for " + inspectionId + ".");
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            Log.e(TAG, "ERROR - updateInspectionStatus: " + errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(45), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
