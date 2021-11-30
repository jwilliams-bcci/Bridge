package com.burgess.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;
import data.Tables.Room_Table;

import static com.burgess.bridge.Constants.API_PROD_URL;
import static com.burgess.bridge.Constants.API_STAGE_URL;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
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
    private static final String GET_BUILDERS_URL = "GetBuilders";
    private static final String GET_INSPECTORS_URL = "GetInspectors";
    private static final String GET_ROOMS_URL = "GetRooms";
    private static final String GET_DIRECTIONS_URL = "GetDirections";
    private static final String GET_FAULTS_URL = "GetFaults";
    private static final String GET_INSPECTIONS_URL = "GetInspections?inspectorid=%s&inspectiondate=%s";
    private static final String GET_INSPECTION_HISTORY_URL = "GetInspectionHistory?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private static final String GET_CHECK_EXISTING_INSPECTION_URL = "CheckExistingInspection?inspectionid=%s&inspectorid=%s";
    private static final String POST_TRANSFER_INSPECTION_URL = "TransferInspection?inspectionId=%s&inspectorId=%s";
    private static final String POST_MULTIFAMILY_DETAILS_URL = "InsertMultifamilyDetails";
    private static final String POST_INSPECTION_DEFECT_URL = "InsertInspectionDetails";
    private static final String POST_INSPECTION_STATUS_URL = "UpdateInspectionStatus?InspectionId=%s&StatusId=%s&UserId=%s&InspectionTotal=%s&SuperPresent=%s&StartTime=%s&EndTime=%s&Training=%s&TraineeId=%s";

    private BridgeAPIQueue(Context context) {
        ctx = context;
        queue = getRequestQueue();
        mSharedPreferences = context.getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        BridgeLogger.getInstance(ctx);

        // TODO: If true, all endpoints are pointing to BORE, otherwise BOREStage
        isProd = true;
    }

    public static synchronized BridgeAPIQueue getInstance(Context context) {
        if (instance == null) {
            instance = new BridgeAPIQueue(context);
        }
        return instance;
    }

    public static synchronized BridgeAPIQueue getInstance() {
        if (instance == null) {
            throw new IllegalStateException(BridgeAPIQueue.class.getSimpleName() + " is not initialized, call getInstance(Context context) first");
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

    public boolean isProd() {
        return isProd;
    }

    // Login & Database Update
    public JsonObjectRequest loginUser(String userName, String password, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(LOGIN_URL, userName, password);
        BridgeLogger.log('I', TAG, "Application is currently in: " + (isProd ? "PRODUCTION" : "STAGING"));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            mEditor.putString(PREF_AUTH_TOKEN, response.optString("AuthorizationToken"));
            mEditor.putString(PREF_SECURITY_USER_ID, response.optString("SecurityUserId"));
            mEditor.putString(PREF_INSPECTOR_ID, response.optString("InspectorId"));
            mEditor.putString(PREF_INSPECTOR_DIVISION_ID, response.optString("DivisionId"));
            mEditor.putString(PREF_LOGIN_NAME, userName);
            mEditor.putString(PREF_LOGIN_PASSWORD, password);
            mEditor.putLong(PREF_AUTH_TOKEN_AGE, System.currentTimeMillis());
            mEditor.apply();
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                mEditor.putBoolean(PREF_IS_ONLINE, false);
                mEditor.apply();
                BridgeLogger.log('E', TAG, "Lost connection in loginUser.");
                callback.onFailure("Lost connection during login!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in loginUser.");
                callback.onFailure("Request timed during login!");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, "Authentication error in loginUser.");
                callback.onFailure("Authentication error! Please try again.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in loginUser: " + errorMessage);
                callback.onFailure("Error during login!");
            }
        });
        return request;
    }
    public JsonArrayRequest updateCannedComments(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_CANNED_COMMENTS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            mEditor.putBoolean(PREF_IS_ONLINE, true);
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    CannedComment_Table cannedComment = new CannedComment_Table();
                    cannedComment.id = i + 1;
                    cannedComment.text = obj.optString("Comment");
                    cannedComment.isEnergy = obj.optInt("IsEnergy");
                    vm.insertCannedComment(cannedComment);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateCannedComments: " + e.getMessage());
                }
            }
            BridgeLogger.log('I', TAG, "Canned Comments downloaded");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateCannedComments.");
                callback.onFailure("Lost connection while getting canned comments!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateCannedComments.");
                callback.onFailure("Request timed out while getting canned comments!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateCannedComments: " + errorMessage);
                callback.onFailure("Error getting canned comments!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                    defectItem.reinspection_required = obj.optBoolean("ReInspectionRequired");

                    vm.insertDefectItem(defectItem);
                } catch (JSONException e) {
                    Log.e(TAG, "ERROR - updateDefectItems: " + e.getMessage());
                    BridgeLogger.log('E', TAG, "ERROR in updateDefectItems: " + e.getMessage());
                }
            }
            Log.i(TAG, "Defect Items downloaded");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateDefectItems.");
                callback.onFailure("Lost connection while getting defect items!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateDefectItems.");
                callback.onFailure("Request timed out while getting defect items!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateDefectItems: " + errorMessage);
                callback.onFailure("Error getting defect items!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                    BridgeLogger.log('E', TAG, "ERROR in updateDIIT: " + e.getMessage());
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateDIIT.");
                callback.onFailure("Lost connection while getting DIITs!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateDIIT.");
                callback.onFailure("Request timed out while getting DIITs!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateDIIT: " + errorMessage);
                callback.onFailure("Error getting DIITs!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateBuilders(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_BUILDERS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Builder_Table builder = new Builder_Table();
                    builder.id = obj.optInt("BuilderID");
                    builder.builder_name = obj.optString("BuilderName");
                    builder.reinspection_required = obj.optBoolean("ConditionalReInspects");

                    vm.insertBuilder(builder);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateBuilders: " + e.getMessage());
                }
            }
            Log.i(TAG, "Builders downloaded");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateBuilders.");
                callback.onFailure("Lost connection while getting builders!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateBuilders.");
                callback.onFailure("Request timed out while getting builders!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateBuilders: " + errorMessage);
                callback.onFailure("Error getting builders!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateInspectors(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_INSPECTORS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Inspector_Table inspector = new Inspector_Table();
                    inspector.id = obj.optInt("InspectorId");
                    inspector.inspector_name = obj.optString("InspectorName");
                    inspector.division_id = obj.optInt("DivisionID");

                    vm.insertInspector(inspector);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateBuilders: " + e.getMessage());
                }
            }
            Log.i(TAG, "Inspectors downloaded");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateInspectors.");
                callback.onFailure("Lost connection while getting inspectors!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateInspectors.");
                callback.onFailure("Request timed out while getting inspectors!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateInspectors: " + errorMessage);
                callback.onFailure("Error getting inspectors!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateRooms(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_ROOMS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Room_Table room = new Room_Table();
                    room.id = obj.optInt("RoomId");
                    room.room_name = obj.optString("RoomName");

                    vm.insertRoom(room);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateRooms: " + e.getMessage());
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateRooms.");
                callback.onFailure("Lost connection while getting rooms!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateRooms.");
                callback.onFailure("Request timed out while getting rooms!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateRooms: " + errorMessage);
                callback.onFailure("Error getting rooms!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateDirections(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_DIRECTIONS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Direction_Table direction = new Direction_Table();
                    direction.id = obj.optInt("DirectionID");
                    direction.direction_description = obj.optString("Direction");
                    direction.direction_order = obj.optInt("Order");

                    vm.insertDirection(direction);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateDirections: " + e.getMessage());
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateDirections.");
                callback.onFailure("Lost connection while getting directions!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateDirections.");
                callback.onFailure("Request timed out while getting directions!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateDirections: " + errorMessage);
                callback.onFailure("Error getting directions!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateFaults(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_FAULTS_URL;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Fault_Table fault = new Fault_Table();
                    fault.id = obj.optInt("FaultID");
                    fault.text = obj.optString("Fault");
                    fault.display_text = obj.optString("PlainText");

                    vm.insertFault(fault);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateFaults: " + e.getMessage());
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateFaults.");
                callback.onFailure("Lost connection while getting faults!");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateFaults.");
                callback.onFailure("Request timed out while getting faults!");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateFaults: " + errorMessage);
                callback.onFailure("Error getting faults!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Route Sheet
    public JsonArrayRequest updateRouteSheet(RouteSheetViewModel vm, String inspectorId, String inspectionDate) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_INSPECTIONS_URL, inspectorId, inspectionDate);

        ArrayList<JsonArrayRequest> inspectionHistoryRequests = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Inspection_Table inspection = new Inspection_Table();
                    inspection.id = obj.optInt("InspectionID");
                    inspection.inspection_date = OffsetDateTime.parse(obj.optString("InspectionDate"));
                    inspection.division_id = obj.optInt("DivisionID");
                    inspection.location_id = obj.optInt("LocationID");
                    inspection.builder_name = obj.getString("BuilderName");
                    inspection.builder_id = obj.optInt("BuilderID");
                    inspection.super_name = obj.getString("SuperName");
                    inspection.inspector_id = obj.optInt("InspectorID");
                    inspection.inspector = obj.getString("Inspector");
                    inspection.community = obj.getString("Community");
                    inspection.community_id = obj.optInt("CommunityID");
                    inspection.inspection_class = obj.optInt("InspectionClass");
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
                    inspection.start_time = null;
                    inspection.end_time = null;
                    inspection.is_complete = false;
                    inspection.is_uploaded = false;
                    inspection.route_sheet_order = obj.optInt("Order");
                    inspection.trainee_id = -1;

                    if (inspection.reinspect) {
                        inspectionHistoryRequests.add(updateInspectionHistory(vm, inspection.id, inspection.inspection_order, inspection.inspection_type_id, inspection.location_id));
                    }
                    vm.insertInspection(inspection);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + e.getMessage());
                }
            }
            BridgeLogger.log('I', TAG, "Inspections downloaded");
            for (int lcv = 0; lcv < inspectionHistoryRequests.size(); lcv++) {
                getRequestQueue().add(inspectionHistoryRequests.get(lcv));
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateRouteSheet.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateRouteSheet.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + errorMessage);
            }
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
                    hist.reviewed_status = null;
                    hist.inspection_defect_id = -1;
                    vm.insertInspectionHistory(hist);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateInspectionHistory: " + e.getMessage());
                }
            }
            BridgeLogger.log('I', TAG, "Inspection histories downloaded.");
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            BridgeLogger.log('E', TAG, "ERROR in updateInspectionHistory: " + errorMessage);
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
    public JsonObjectRequest checkExistingInspection(RouteSheetViewModel vm, int inspectionId, int inspectorId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(GET_CHECK_EXISTING_INSPECTION_URL, inspectionId, inspectorId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            int futureDated = response.optInt("FutureDated");
            int reassignedInspection = response.optInt("ReassignedInspection");
            if (futureDated > 0) {
                BridgeLogger.log('I', TAG, "Found future-dated inspection for " + inspectionId + ". Removing...");
                vm.deleteInspection(inspectionId);
            } else if (reassignedInspection > 0) {
                BridgeLogger.log('I', TAG, "Found reassigned inspection for " + inspectionId + ". Removing...");
                vm.deleteInspection(inspectionId);
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in checkExistingInspection.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in checkExistingInspection.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in checkExistingInspection: " + errorMessage);
            }
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

    // Transfer Inspection
    public StringRequest transferInspection(int inspectionId, int inspectorId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(POST_TRANSFER_INSPECTION_URL, inspectionId, inspectorId);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            BridgeLogger.log('I', TAG, "Transferred inspection " + inspectionId + " to " + inspectorId);
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in transferInspection.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in transferInspection.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in transferInspection: " + errorMessage);
                callback.onFailure("Error! Please contact support...");
            }
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

    // Review & Submit
    public StringRequest uploadMultifamilyDetails(JSONObject multifamilyDetails, int inspectionId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_MULTIFAMILY_DETAILS_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            BridgeLogger.log('I', TAG, "Uploaded Multifamily Details for InspectionID " + inspectionId + ".");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in uploadMultifamilyDetails.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in uploadMultifamilyDetails.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in uploadMultifamilyDetails: " + errorMessage);
                callback.onFailure("Error! Please contact support...");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }

            @Override
            public byte[] getBody() {
                return multifamilyDetails.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect, int defectItemId, int inspectionId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_DEFECT_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            BridgeLogger.log('I', TAG, "Uploaded DefectItemID " + defectItemId + " for InspectionID " + inspectionId + ".");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in uploadInspectionDefect.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in uploadInspectionDefect.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in uploadInspectionDefect: " + errorMessage);
                callback.onFailure("Error! Please contact support...");
            }
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

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public StringRequest updateInspectionStatus(int inspectionId, int inspectionStatusId, String userId, int inspectionTotal, int superPresent, String startTime, String endTime, int training, int traineeId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(POST_INSPECTION_STATUS_URL, inspectionId, inspectionStatusId, userId, inspectionTotal, superPresent, startTime, endTime, 0, traineeId);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            BridgeLogger.log('I', TAG, "Updated status for " + inspectionId + ".");
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                mEditor.putBoolean(PREF_IS_ONLINE, false);
                mEditor.apply();
                BridgeLogger.log('E', TAG, "Lost connection in updateInspectionStatus.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateInspectionStatus.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateInspectionStatus: " + errorMessage);
                callback.onFailure("Error! Please contact support...");
            }
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
