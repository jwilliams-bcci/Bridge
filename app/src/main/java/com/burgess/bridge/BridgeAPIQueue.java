package com.burgess.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

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
import com.burgess.bridge.attachments.AttachmentsViewModel;
import com.burgess.bridge.ekotropedata.EkotropeDataViewModel;
import com.burgess.bridge.login.LoginViewModel;
import com.burgess.bridge.routesheet.RouteSheetViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import data.Tables.Attachment_Table;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.Fault_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;
import data.Tables.PastInspection_Table;
import data.Tables.Room_Table;

import static com.burgess.bridge.Constants.API_EKOTROPE_AUTH;
import static com.burgess.bridge.Constants.API_EKOTROPE_URL;
import static com.burgess.bridge.Constants.API_PROD_URL;
import static com.burgess.bridge.Constants.API_STAGE_URL;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;
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
    private static final String GET_DEFECT_ITEMS_V2_URL = "GetDefectItemsV2?inspectorid=%s&inspectiondate=%s";
    private static final String GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V2_URL = "GetDefectItem_InspectionType_XRefV2?inspectorid=%s&inspectiondate=%s";
    private static final String GET_BUILDERS_URL = "GetBuilders";
    private static final String GET_INSPECTORS_URL = "GetInspectors";
    private static final String GET_INSPECTORS_V2_URL = "GetInspectorsV2";
    private static final String GET_ROOMS_URL = "GetRooms";
    private static final String GET_DIRECTIONS_URL = "GetDirections";
    private static final String GET_FAULTS_URL = "GetFaults";
    private static final String GET_INSPECTIONS_V3_URL = "GetInspectionsV3?inspectorid=%s&inspectiondate=%s";
    private static final String GET_INSPECTIONS_V4_URL = "GetInspectionsV4?inspectorid=%s&inspectiondate=%s";
    private static final String GET_REMAINING_INSPECTIONS_URL = "GetRemainingInspections?inspectorid=%s&inspectiondate=%s";
    private static final String GET_EKOTROPE_DATA_URL = "GetInspectionsV3?inspectorid=%s&inspectiondate=%s"; //ToDo: URL needs to be changed
    private static final String GET_PAST_INSPECTIONS_URL = "GetPastInspections?locationid=%s";
    private static final String GET_ATTACHMENTS_URL = "GetAttachments?inspectionid=%s&locationid=%s";
    private static final String GET_INSPECTION_HISTORY_URL = "GetInspectionHistory?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private static final String GET_INSPECTION_HISTORY_V2_URL = "GetInspectionHistoryV2?inspectionorder=%s&inspectiontypeid=%s&locationid=%s";
    private static final String GET_MULTIFAMILY_HISTORY_URL = "GetMultifamilyHistory?locationid=%s&inspectionnumber=%s";
    private static final String GET_MULTIFAMILY_HISTORY_V2_URL = "GetMultifamilyHistoryV2?locationid=%s&inspectionnumber=%s";
    private static final String GET_CHECK_EXISTING_INSPECTION_V2_URL = "CheckExistingInspectionV2?inspectionid=%s&inspectorid=%s";
    private static final String GET_REPORT_DATA_URL = "GetReportData?inspectorId=%s&inspectionDate=%s";
    private static final String POST_TRANSFER_INSPECTION_URL = "TransferInspection?inspectionId=%s&inspectorId=%s";
    private static final String POST_MULTIFAMILY_DETAILS_URL = "InsertMultifamilyDetails";
    private static final String POST_INSPECTION_ATTACHMENT_URL = "InsertInspectionAttachment";
    private static final String POST_INSPECTION_DEFECT_V2_URL = "InsertInspectionDetailsV2";
    private static final String POST_INSPECTION_DEFECT_V3_URL = "InsertInspectionDetailsV3";
    private static final String POST_INSPECTION_STATUS_URL = "UpdateInspectionStatus?InspectionId=%s&StatusId=%s&UserId=%s&InspectionTotal=%s&SuperPresent=%s&StartTime=%s&EndTime=%s&Training=%s&TraineeId=%s";
    private static final String POST_INSPECTION_STATUS_V2_URL = "UpdateInspectionStatusV2";

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            mEditor.putString(PREF_AUTH_TOKEN, response.optString("AuthorizationToken"));
            mEditor.putString(PREF_SECURITY_USER_ID, response.optString("SecurityUserId"));
            mEditor.putString(PREF_INSPECTOR_ID, response.optString("InspectorId"));
            mEditor.putString(PREF_INSPECTOR_DIVISION_ID, response.optString("DivisionId"));
            mEditor.putString(PREF_LOGIN_NAME, userName);
            mEditor.putString(PREF_LOGIN_PASSWORD, password);
            mEditor.putInt(PREF_IND_INSPECTIONS_REMAINING, 0);
            mEditor.putInt(PREF_TEAM_INSPECTIONS_REMAINING, 0);
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
                    BridgeLogger.log('E', TAG, "ERROR in updateInspectors: " + e.getMessage());
                }
            }
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
    public JsonArrayRequest updateInspectorsV2(LoginViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += GET_INSPECTORS_V2_URL;

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
                    BridgeLogger.log('E', TAG, "ERROR in updateInspectors: " + e.getMessage());
                }
            }
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
    public JsonArrayRequest updateRouteSheet(RouteSheetViewModel vm, String inspectorId, String inspectionDate, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_INSPECTIONS_V4_URL, inspectorId, inspectionDate);

        ArrayList<JsonArrayRequest> inspectionHistoryRequests = new ArrayList<>();
        ArrayList<JsonArrayRequest> multifamilyHistoryRequests = new ArrayList<>();
        ArrayList<JsonArrayRequest> pastInspectionRequests = new ArrayList<>();
        ArrayList<JsonObjectRequest> ekotropePlansIdRequests = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            inspectionHistoryRequests.clear();
            multifamilyHistoryRequests.clear();
            pastInspectionRequests.clear();
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
                    inspection.job_number = obj.getString("JobNumber");
                    inspection.require_risk_assessment = obj.getBoolean("RequireRiskAssessment");
                    inspection.ekotrope_project_id = obj.getString("EkotropeProjectID");
                    inspection.ekotrope_plan_id = null;
                    inspection.start_time = null;
                    inspection.end_time = null;
                    inspection.is_complete = false;
                    inspection.is_uploaded = false;
                    inspection.is_failed = false;
                    inspection.route_sheet_order = obj.optInt("Order");
                    inspection.trainee_id = -1;

                    if (inspection.division_id == 20) {
                        multifamilyHistoryRequests.add(updateMultifamilyHistory(vm, inspection.id, inspection.location_id, inspection.inspection_order, inspection.inspection_type_id));
                    } else {
                        if (inspection.reinspect) {
                            inspectionHistoryRequests.add(updateInspectionHistory(vm, inspection.id, inspection.inspection_order, inspection.inspection_type_id, inspection.location_id));
                        }
                    }
                    if (inspection.ekotrope_project_id != "null") {
                        ekotropePlansIdRequests.add(getEkotropePlanId(vm, inspection.ekotrope_project_id, inspection.id, new ServerCallback() {
                            @Override
                            public void onSuccess(String message) {
                                BridgeLogger.log('I', TAG, "Added plan id for " + inspection.id);
                                callback.onSuccess("Added plan");
                            }
                            @Override
                            public void onFailure(String message) {
                                BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + message);
                                callback.onFailure("Error while updating route sheet! Please send activity log...");
                            }
                        }));
                    }
                    pastInspectionRequests.add(updatePastInspections(vm, inspection.location_id));
                    vm.insertInspection(inspection);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + e.getMessage());
                    callback.onFailure("Error while updating route sheet! Please send activity log...");
                }
            }

            for (int lcv = 0; lcv < inspectionHistoryRequests.size(); lcv++) {
                getRequestQueue().add(inspectionHistoryRequests.get(lcv));
            }
            for (int lcv = 0; lcv < multifamilyHistoryRequests.size(); lcv++) {
                getRequestQueue().add(multifamilyHistoryRequests.get(lcv));
            }
            for (int lcv = 0; lcv < pastInspectionRequests.size(); lcv++) {
                getRequestQueue().add(pastInspectionRequests.get(lcv));
            }
            for (int lcv = 0; lcv < ekotropePlansIdRequests.size(); lcv++) {
                getRequestQueue().add(ekotropePlansIdRequests.get(lcv));
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateRouteSheet.");
                callback.onFailure("Error while updating route sheet! Please send activity log...");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateRouteSheet.");
                callback.onFailure("Error while updating route sheet! Please send activity log...");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + errorMessage);
                callback.onFailure("Error while updating route sheet! Please send activity log...");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateDefectItemsV2(RouteSheetViewModel vm, String inspectorId, String inspectionDate) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_DEFECT_ITEMS_V2_URL, inspectorId, inspectionDate);

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
                    BridgeLogger.log('E', TAG, "ERROR in updateDefectItemsForInspector: " + e.getMessage());
                }
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateDefectItems.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateDefectItems.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateDefectItems: " + errorMessage);
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
    public JsonArrayRequest updateDefectItem_InspectionTypeXRefV2(RouteSheetViewModel vm, String inspectorId, String inspectionDate) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V2_URL, inspectorId, inspectionDate);

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
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateDIIT.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateDIIT.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateDIIT: " + errorMessage);
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
    public JsonArrayRequest updateInspectionHistory(RouteSheetViewModel vm, int inspectionId, int inspectionOrder, int inspectionTypeId, int locationId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        if (inspectionTypeId == 1154) {
            url += String.format(GET_MULTIFAMILY_HISTORY_V2_URL, inspectionOrder, locationId);
        } else {
            url += String.format(GET_INSPECTION_HISTORY_V2_URL, inspectionOrder, inspectionTypeId, locationId);
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    InspectionHistory_Table hist = new InspectionHistory_Table();
                    hist.id = obj.optInt("InspectionDetailID");
                    hist.first_inspection_detail_id = obj.optInt("FirstDefectInspectionDetailID");
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

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonArrayRequest updateMultifamilyHistory(RouteSheetViewModel vm, int inspectionId, int locationId, int inspectionNumber, int inspectionTypeId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_MULTIFAMILY_HISTORY_V2_URL, locationId, inspectionNumber);
        boolean isReobservation = (inspectionTypeId == 1154);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    if (!isReobservation) {
                        InspectionDefect_Table newDefect = new InspectionDefect_Table();
                        newDefect.inspection_id = inspectionId;
                        newDefect.defect_item_id = obj.optInt("DefectItemID");
                        newDefect.defect_status_id = obj.optInt("DefectStatusID");
                        newDefect.comment = obj.optString("Comment");
                        newDefect.prior_inspection_detail_id = obj.optInt("InspectionDetailID");
                        newDefect.first_inspection_detail_id = obj.optInt("FirstDefectInspectionDetailID");
                        newDefect.reinspection_required = false;
                        newDefect.picture_path = null;
                        newDefect.is_uploaded = false;
                        newDefect.is_editable = false;
                        if (vm.multifamilyDefectExists(newDefect.first_inspection_detail_id) > 0) {
                            int existingId = vm.multifamilyDefectExists(newDefect.first_inspection_detail_id);
                            vm.updateExistingMFCDefect(newDefect.defect_status_id, newDefect.comment, existingId);
                        } else {
                            vm.insertInspectionDefect(newDefect);
                        }
                    } else {
                        InspectionHistory_Table hist = new InspectionHistory_Table();
                        boolean needsReview = (obj.optInt("DefectStatusID") == 2);
                        if (needsReview) {
                            hist.id = obj.optInt("InspectionDetailID");
                            hist.inspection_id = inspectionId;
                            hist.first_inspection_detail_id = obj.optInt("FirstDefectInspectionDetailID");
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
                        } else {
                            InspectionDefect_Table newDefect = new InspectionDefect_Table();
                            newDefect.inspection_id = inspectionId;
                            newDefect.defect_item_id = obj.optInt("DefectItemID");
                            newDefect.defect_status_id = obj.optInt("DefectStatusID");
                            newDefect.comment = obj.optString("Comment");
                            newDefect.prior_inspection_detail_id = obj.optInt("InspectionDetailID");
                            newDefect.first_inspection_detail_id = obj.optInt("FirstDefectInspectionDetailID");
                            newDefect.reinspection_required = false;
                            newDefect.picture_path = null;
                            newDefect.is_uploaded = false;
                            if (vm.multifamilyDefectExists(newDefect.first_inspection_detail_id) > 0) {
                                int existingId = vm.multifamilyDefectExists(newDefect.first_inspection_detail_id);
                                vm.updateExistingMFCDefect(newDefect.defect_status_id, newDefect.comment, existingId);
                            } else {
                                vm.insertInspectionDefect(newDefect);
                            }
                        }
                    }
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateMultifamilyHistory: " + e.getMessage());
                }
            }
        }, error -> {
            String errorMessage = new String(error.networkResponse.data);
            BridgeLogger.log('E', TAG, "ERROR in updateMultifamilyHistory: " + errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonObjectRequest updateInspectionsRemaining(RouteSheetViewModel vm, String inspectorId, String inspectionDate, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_REMAINING_INSPECTIONS_URL, inspectorId, inspectionDate);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            int inspectionsRemaining = response.optInt("Remaining");
            mEditor.putInt(PREF_TEAM_INSPECTIONS_REMAINING, inspectionsRemaining);
            mEditor.apply();
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateInspectionsRemaining.");
                callback.onFailure("Error while updating inspection remaining! Please send activity log...");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateInspectionsRemaining.");
                callback.onFailure("Error while updating inspection remaining! Please send activity log...");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateInspectionsRemaining: " + errorMessage);
                callback.onFailure("Error while updating inspection remaining! Please send activity log...");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonObjectRequest checkExistingInspection(RouteSheetViewModel vm, int inspectionId, int inspectorId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(GET_CHECK_EXISTING_INSPECTION_V2_URL, inspectionId, inspectorId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            int futureDated = response.optInt("FutureDated");
            int reassignedInspection = response.optInt("ReassignedInspection");
            int notAssigned = response.optInt("NotAssigned");
            if (futureDated > 0) {
                vm.deleteInspection(inspectionId);
                BridgeLogger.log('I', TAG, "Found future dated for " + inspectionId + " - deleted...");
            } else if (reassignedInspection > 0) {
                vm.deleteInspection(inspectionId);
                BridgeLogger.log('I', TAG, "Found reassigned for " + inspectionId + " - deleted...");
            } else if (notAssigned > 0) {
                vm.deleteInspection(inspectionId);
                BridgeLogger.log('I', TAG, "Found not assigned for " + inspectionId + " - deleted...");
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

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public StringRequest getReportData(RouteSheetViewModel vm, int inspectorId, String inspectionDate, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_REPORT_DATA_URL, inspectorId, inspectionDate);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            vm.updateUrl(response.substring(1, response.length()-1));
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in getReportData.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in getReportData.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in getReportData: " + errorMessage);
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

    // Ekotrope API Calls
    public JsonObjectRequest getEkotropePlanId(RouteSheetViewModel vm, String projectId, int inspectionId, final ServerCallback callback) {
        String url = API_EKOTROPE_URL + projectId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            String planId = response.optString("masterPlanId");
            vm.updateEkotropePlanId(planId, inspectionId);

            getRequestQueue().add(getEkotropePlanData(vm, planId, new ServerCallback() {
                @Override
                public void onSuccess(String message) {
                    BridgeLogger.log('I', TAG, "Added plan data for " + planId);
                    callback.onSuccess("Added plan data");
                }
                @Override
                public void onFailure(String message) {
                    BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + message);
                    callback.onFailure("Error while updating route sheet! Please send activity log...");
                }
            }));
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in getEkotropePlanId.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in getEkotropePlanId.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in getEkotropePlanId: " + errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, "Basic " +  Base64.encodeToString(API_EKOTROPE_AUTH.getBytes(), Base64.DEFAULT));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonObjectRequest getEkotropePlanData(RouteSheetViewModel vm, String planId, final ServerCallback callback) {
        String url = API_EKOTROPE_URL + planId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONObject thermalEnvelope = response.optJSONObject("thermalEnvelope");
            JSONArray framedFloorArray = thermalEnvelope.optJSONArray("framedFloors");
            for (int lcv = 0; lcv < framedFloorArray.length(); lcv++) {
                JSONObject framedFloorObj = framedFloorArray.optJSONObject(lcv);
                JSONObject typeObj = framedFloorObj.optJSONObject("type");
                JSONObject assemblyDetailsObj = typeObj.optJSONObject("assemblyDetails");
                JSONArray cavityInsulationGradeObj = assemblyDetailsObj.optJSONArray("cavityInsulationGrade");
                JSONArray studSpacingObj = assemblyDetailsObj.optJSONArray("framingSpacing");
                JSONArray studWidthObj = assemblyDetailsObj.optJSONArray("framingWidth");
                JSONArray studDepthObj = assemblyDetailsObj.optJSONArray("framingDepth");
                JSONArray studMaterialObj = assemblyDetailsObj.optJSONArray("studType");

                Ekotrope_FramedFloor_Table framedFloor = new Ekotrope_FramedFloor_Table();
                framedFloor.plan_id = planId;
                framedFloor.index = lcv;
                framedFloor.name = framedFloorObj.optString("name");
                framedFloor.cavityInsulationGrade = cavityInsulationGradeObj.optString(0);
                framedFloor.cavityInsulationR = assemblyDetailsObj.optDouble("cavityR");
                framedFloor.continuousInsulationR = assemblyDetailsObj.optDouble("continuousR");
                framedFloor.studSpacing = studSpacingObj.optDouble(0);
                framedFloor.studWidth = studWidthObj.optDouble(0);
                framedFloor.studDepth = studDepthObj.optDouble(0);
                framedFloor.studMaterial = studMaterialObj.optString(0);

                vm.insertFramedFloor(framedFloor);
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in getEkotropePlanData.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in getEkotropePlanData.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in getEkotropePlanData: " + errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders () {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, "Basic " +  Base64.encodeToString(API_EKOTROPE_AUTH.getBytes(), Base64.DEFAULT));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Attachments
    public JsonArrayRequest updateAttachments(AttachmentsViewModel vm, int inspectionId, int locationId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_ATTACHMENTS_URL, inspectionId, locationId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Attachment_Table attachment = new Attachment_Table();
                    attachment.id = obj.optInt("AttachmentID");
                    attachment.inspection_id = obj.optInt("InspectionID");
                    attachment.location_id = obj.optInt("LocationID");
                    attachment.file_name = obj.optString("AttachmentFileName");
                    attachment.file_data = Base64.decode(obj.optString("ImageFileData"), Base64.DEFAULT);
                    attachment.attachment_type = obj.optString("AttachmentType");
                    attachment.file_path = null;
                    attachment.is_uploaded = true;

                    String fileName = attachment.inspection_id + "_" + attachment.file_name;
                    String path = ctx.getExternalFilesDir("Attachments").getAbsolutePath() + "/" + fileName;
                    File file = new File(path);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(attachment.file_data);
                        fos.close();
                    } catch (Exception e) {
                        BridgeLogger.log('E', TAG, "ERROR in updateAttachments: " + e.getMessage());
                    }

                    attachment.file_path = path;

                    vm.insertAttachment(attachment);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateAttachments: " + e.getMessage());
                }
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateAttachments.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateAttachments.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateAttachments: " + errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Past Inspections
    public JsonArrayRequest updatePastInspections(RouteSheetViewModel vm, int locationId) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(GET_PAST_INSPECTIONS_URL, locationId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    PastInspection_Table pastInspection = new PastInspection_Table();
                    pastInspection.id = obj.optInt("InspectionID");
                    pastInspection.inspection_submit_time = OffsetDateTime.parse(obj.optString("InspectionSubmitTime"));
                    pastInspection.location_id = obj.optInt("LocationID");
                    pastInspection.inspector = obj.getString("Inspector");
                    pastInspection.inspection_type = obj.getString("InspectionType");
                    pastInspection.inspection_status_id = obj.optInt("InspectionStatusID");
                    pastInspection.incomplete_reason = obj.getString("IncompleteReason");

                    vm.insertPastInspection(pastInspection);
                } catch (JSONException e) {
                    BridgeLogger.log('E', TAG, "ERROR in updatePastInspections: " + e.getMessage());
                }
            }
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updatePastInspections.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updatePastInspections.");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updatePastInspections: " + errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString("AuthorizationToken", "NULL"));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Transfer Inspection
    public StringRequest transferInspection(int inspectionId, int inspectorId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += String.format(POST_TRANSFER_INSPECTION_URL, inspectionId, inspectorId);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
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
    public StringRequest uploadInspectionAttachment(JSONObject inspectionAttachment, int inspectionId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_ATTACHMENT_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            BridgeLogger.log('I', TAG, "Uploaded attachment for " + inspectionId);
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof  NoConnectionError){
                BridgeLogger.log('E', TAG, "Lost connection in uploadInspectionAttachment.");
                callback.onFailure("No connection, please try again");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in uploadInspectionAttachment.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in uploadInspectionAttachment: " + errorMessage);
                callback.onFailure("Error! please contact support...");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
                return params;
            }

            @Override
            public byte[] getBody() { return inspectionAttachment.toString().getBytes(); }

            @Override
            public String getBodyContentType() { return "application/json"; }
        };

        request.setTag(Constants.CANCEL_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, 0));
        return request;
    }
    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect, int defectItemId, int inspectionId, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_DEFECT_V3_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
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

        request.setTag(Constants.CANCEL_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, 0));
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

        request.setTag(Constants.CANCEL_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public StringRequest updateInspectionStatusV2(JSONObject inspectionStatusRequest, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_STATUS_V2_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateInspectionStatusV2.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateInspectionStatusV2.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateInspectionStatusV2: " + errorMessage);
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
                return inspectionStatusRequest.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setTag(Constants.CANCEL_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, 0));
        return request;
    }
}
