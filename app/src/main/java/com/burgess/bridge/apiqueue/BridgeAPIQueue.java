package com.burgess.bridge.apiqueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

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
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.Constants;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.attachments.AttachmentsViewModel;
import com.burgess.bridge.ekotrope_data.Ekotrope_DataViewModel;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitViewModel;
import com.burgess.bridge.routesheet.RouteSheetViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import data.Repositories.SubmitRequestRepository;
import data.Tables.Attachment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_Ceiling_Table;
import data.Tables.Ekotrope_ClothesDryer_Table;
import data.Tables.Ekotrope_ClothesWasher_Table;
import data.Tables.Ekotrope_Dishwasher_Table;
import data.Tables.Ekotrope_DistributionSystem_Table;
import data.Tables.Ekotrope_Door_Table;
import data.Tables.Ekotrope_Duct_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.Ekotrope_Infiltration_Table;
import data.Tables.Ekotrope_Lighting_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;
import data.Tables.Ekotrope_RangeOven_Table;
import data.Tables.Ekotrope_Refrigerator_Table;
import data.Tables.Ekotrope_RimJoist_Table;
import data.Tables.Ekotrope_Slab_Table;
import data.Tables.Ekotrope_Window_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Tables.PastInspection_Table;
import data.Tables.SubmitRequest_Table;

import static com.burgess.bridge.Constants.API_EKOTROPE_AUTH_PROD;
import static com.burgess.bridge.Constants.API_EKOTROPE_INSPECTION_SYNC;
import static com.burgess.bridge.Constants.API_EKOTROPE_PLAN_URL;
import static com.burgess.bridge.Constants.API_EKOTROPE_PROJECT_URL;
import static com.burgess.bridge.Constants.API_PROD_URL;
import static com.burgess.bridge.Constants.API_STAGE_URL;
import static com.burgess.bridge.Constants.EKOTROPE_INSPECTION_TYPE_FINAL;
import static com.burgess.bridge.Constants.EKOTROPE_INSPECTION_TYPE_ROUGH;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_IS_ONLINE;
import static com.burgess.bridge.apiqueue.APIConstants.API_EKOTROPE_AUTH_TEST;

public class BridgeAPIQueue {
    private static BridgeAPIQueue instance;
    private RequestQueue queue;
    private static Context ctx;
    private static boolean isProd;
    private static boolean isEkotropeProd;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String TAG = "API";
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_BEARER = "Bearer ";
    private static final String GET_DEFECT_ITEMS_V2_URL = "GetDefectItemsV2?inspectorid=%s&inspectiondate=%s";
    private static final String GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V2_URL = "GetDefectItem_InspectionType_XRefV2?inspectorid=%s&inspectiondate=%s";
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

    private static SubmitRequestRepository submitRequestRepository;

    private BridgeAPIQueue(Context context) {
        ctx = context;
        queue = getRequestQueue();
        mSharedPreferences = context.getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        BridgeLogger.getInstance(ctx);

        // TODO: If true, all endpoints are pointing to BORE, otherwise BOREStage
        isProd = true;
        isEkotropeProd = false;
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

    public static boolean isProd() {
        return isProd;
    }

    public static boolean isEkotropeProd() { return isEkotropeProd; }

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
                    inspection.InspectionID = obj.optInt("InspectionID");
                    //inspection.InspectionDate = OffsetDateTime.parse(obj.optString("InspectionDate"));
                    inspection.DivisionID = obj.optInt("DivisionID");
                    inspection.LocationID = obj.optInt("LocationID");
                    inspection.BuilderName = obj.getString("BuilderName");
                    inspection.BuilderID = obj.optInt("BuilderID");
                    inspection.SuperName = obj.getString("SuperName");
                    inspection.InspectorID = obj.optInt("InspectorID");
                    inspection.Inspector = obj.getString("Inspector");
                    inspection.Community = obj.getString("Community");
                    inspection.CommunityID = obj.optInt("CommunityID");
                    inspection.InspectionClass = obj.optInt("InspectionClass");
                    inspection.City = obj.getString("City");
                    inspection.InspectionTypeID = obj.optInt("InspectionTypeID");
                    inspection.InspectionType = obj.getString("InspectionType");
                    inspection.ReInspect = obj.getBoolean("ReInspect");
                    inspection.InspectionOrder = obj.optInt("InspectionOrder");
                    inspection.Address = obj.getString("Address1");
                    inspection.InspectionStatusID = obj.optInt("InspectionStatusID");
                    inspection.InspectionStatus = obj.getString("InspectionStatus");
                    inspection.SuperPhone = obj.getString("SuperPhone");
                    inspection.SuperEmailAddress = obj.getString("SuperEmailAddress");
                    inspection.SuperintendentPresent = obj.optInt("SuperintendentPresent");
                    inspection.IncompleteReason = obj.getString("IncompleteReason");
                    inspection.IncompleteReasonID = obj.optInt("IncompleteReasonID");
                    inspection.Notes = obj.getString("Comment");
                    inspection.JobNumber = obj.getString("JobNumber");
                    inspection.RequireRiskAssessment = obj.getBoolean("RequireRiskAssessment");
                    inspection.EkotropeProjectID = obj.getString("EkotropeProjectID");
                    inspection.EkotropePlanID = null;
                    inspection.StartTime = null;
                    inspection.EndTime = null;
                    inspection.IsComplete = false;
                    inspection.IsUploaded = false;
                    inspection.IsFailed = false;
                    inspection.RouteSheetOrder = obj.optInt("Order");
                    inspection.TraineeID = -1;
                    inspection.JotformLink = obj.getString("EnergyJotformLink");

                    if (inspection.DivisionID == 20) {
                        multifamilyHistoryRequests.add(updateMultifamilyHistory(vm, inspection.InspectionID, inspection.LocationID, inspection.InspectionOrder, inspection.InspectionTypeID));
                    } else {
                        if (inspection.ReInspect) {
                            inspectionHistoryRequests.add(updateInspectionHistory(vm, inspection.InspectionID, inspection.InspectionOrder, inspection.InspectionTypeID, inspection.LocationID));
                        }
                    }
                    if (inspection.EkotropeProjectID != "null") {
                        ekotropePlansIdRequests.add(getEkotropePlanId(vm, inspection.EkotropeProjectID, inspection.InspectionID, new ServerCallback() {
                            @Override
                            public void onSuccess(String message) {
                                BridgeLogger.log('I', TAG, "Added plan InspectionID for " + inspection.InspectionID);
                                callback.onSuccess("Added plan");
                            }
                            @Override
                            public void onFailure(String message) {
                                BridgeLogger.log('E', TAG, "ERROR in updateRouteSheet: " + message);
                                callback.onFailure("Error while updating route sheet! Please send activity log...");
                            }
                        }));
                    }
                    pastInspectionRequests.add(updatePastInspections(vm, inspection.LocationID));
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
                    defectItem.DefectItemID = obj.optInt("DefectItemID");
                    defectItem.DefectCategoryID = obj.optInt("DefectCategoryID");
                    defectItem.CategoryName = obj.optString("CategoryName");
                    defectItem.ItemNumber = obj.optInt("ItemNumber");
                    //defectItem.InspectionTypeID = obj.optInt("InspectionTypeID");
                    defectItem.ItemDescription = obj.optString("ItemDescription");
                    defectItem.SpanishItemDescription = obj.optString("SpanishItemDescription");
                    defectItem.ReInspectionRequired = obj.optBoolean("ReInspectionRequired");

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
                    relation.DefectItemID = obj.optInt("DefectItemID");
                    relation.InspectionTypeID = obj.optInt("InspectionTypeID");

                    vm.insertDIIT(relation);
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
                    hist.InspectionDetailID = obj.optInt("InspectionDetailID");
                    hist.FirstDefectInspectionDetailID = obj.optInt("FirstDefectInspectionDetailID");
                    hist.InspectionID = inspectionId;
                    hist.PreviousInspectionID = obj.optInt("InspectionID");
                    hist.DefectItemID = obj.optInt("DefectItemID");
                    hist.ItemNumber = obj.optInt("ItemNumber");
                    hist.DefectCategoryID = obj.optInt("DefectCategoryID");
                    hist.CategoryName = obj.optString("CategoryName");
                    hist.ItemDescription = obj.optString("ItemDescription");
                    hist.Comment = obj.optString("Comment");
                    hist.IsReviewed = false;
                    hist.ReviewedStatus = null;
                    hist.InspectionDefectID = -1;
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
                        newDefect.InspectionID = inspectionId;
                        newDefect.DefectItemID = obj.optInt("DefectItemID");
                        newDefect.DefectStatusID = obj.optInt("DefectStatusID");
                        newDefect.Comment = obj.optString("Comment");
                        newDefect.PriorInspectionDetailID = obj.optInt("InspectionDetailID");
                        newDefect.FirstDefectInspectionDetailID = obj.optInt("FirstDefectInspectionDetailID");
                        newDefect.ReinspectionRequired = false;
                        newDefect.PicturePath = null;
                        newDefect.IsUploaded = false;
                        newDefect.IsEditable = false;
                        if (vm.getExistingMFCDefect(newDefect.FirstDefectInspectionDetailID, inspectionId) > 0) {
                            int existingId = vm.getExistingMFCDefect(newDefect.FirstDefectInspectionDetailID, inspectionId);
                            vm.updateExistingMFCDefect(newDefect.DefectStatusID, newDefect.Comment, existingId);
                        } else {
                            vm.insertInspectionDefect(newDefect);
                        }
                    } else {
                        InspectionHistory_Table hist = new InspectionHistory_Table();
                        boolean needsReview = (obj.optInt("DefectStatusID") == 2);
                        if (needsReview) {
                            hist.InspectionDetailID = obj.optInt("InspectionDetailID");
                            hist.InspectionID = inspectionId;
                            hist.FirstDefectInspectionDetailID = obj.optInt("FirstDefectInspectionDetailID");
                            hist.PreviousInspectionID = obj.optInt("InspectionID");
                            hist.DefectItemID = obj.optInt("DefectItemID");
                            hist.ItemNumber = obj.optInt("ItemNumber");
                            hist.DefectCategoryID = obj.optInt("DefectCategoryID");
                            hist.CategoryName = obj.optString("CategoryName");
                            hist.ItemDescription = obj.optString("ItemDescription");
                            hist.Comment = obj.optString("Comment");
                            hist.IsReviewed = false;
                            hist.ReviewedStatus = null;
                            hist.InspectionDefectID = -1;
                            vm.insertInspectionHistory(hist);
                        } else {
                            InspectionDefect_Table newDefect = new InspectionDefect_Table();
                            newDefect.InspectionID = inspectionId;
                            newDefect.DefectItemID = obj.optInt("DefectItemID");
                            newDefect.DefectStatusID = obj.optInt("DefectStatusID");
                            newDefect.Comment = obj.optString("Comment");
                            newDefect.PriorInspectionDetailID = obj.optInt("InspectionDetailID");
                            newDefect.FirstDefectInspectionDetailID = obj.optInt("FirstDefectInspectionDetailID");
                            newDefect.ReinspectionRequired = false;
                            newDefect.PicturePath = null;
                            newDefect.IsUploaded = false;
                            if (vm.getExistingMFCDefect(newDefect.FirstDefectInspectionDetailID, inspectionId) > 0) {
                                int existingId = vm.getExistingMFCDefect(newDefect.FirstDefectInspectionDetailID, inspectionId);
                                vm.updateExistingMFCDefect(newDefect.DefectStatusID, newDefect.Comment, existingId);
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
            vm.setReportUrl(response.substring(1, response.length()-1));
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
        String url = API_EKOTROPE_PROJECT_URL + projectId;

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
                params.put(AUTH_HEADER, "Basic " +  Base64.encodeToString(API_EKOTROPE_AUTH_PROD.getBytes(), Base64.DEFAULT));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonObjectRequest getEkotropePlanData(RouteSheetViewModel vm, String planId, final ServerCallback callback) {
        String url = API_EKOTROPE_PLAN_URL + planId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            JSONObject appliances = response.optJSONObject("appliances");
            JSONObject mechanicals = response.optJSONObject("mechanicals");
            JSONObject thermalEnvelope = response.optJSONObject("thermalEnvelope");
            JSONObject infiltration = thermalEnvelope.optJSONObject("infiltration");
            JSONArray framedFloorArray = thermalEnvelope.optJSONArray("framedFloors");
            JSONArray aboveGradeWallArray = thermalEnvelope.optJSONArray("walls");
            JSONArray windowArray = thermalEnvelope.optJSONArray("windows");
            JSONArray doorArray = thermalEnvelope.optJSONArray("doors");
            JSONArray ceilingArray = thermalEnvelope.optJSONArray("ceilings");
            JSONArray slabArray = thermalEnvelope.optJSONArray("slabs");
            JSONArray rimJoistArray = thermalEnvelope.optJSONArray("rimJoists");
            JSONArray mechanicalEquipmentArray = mechanicals.optJSONArray("mechanicalEquipment");
            JSONArray distributionSystemArray = mechanicals.optJSONArray("distributionSystems");
            JSONArray mechanicalVentilationArray = mechanicals.optJSONArray("mechanicalVentilation");
            JSONObject lighting = response.optJSONObject("lighting");
            JSONObject dishwasher = appliances.optJSONObject("dishwasher");
            JSONObject clothesDryer = appliances.optJSONObject("clothesDryer");
            JSONObject clothesWasher = appliances.optJSONObject("clothesWasher");
            JSONObject rangeOven = appliances.optJSONObject("rangeOven");

            addFramedFloors(framedFloorArray, planId, vm);
            addAboveGradeWalls(aboveGradeWallArray, planId, vm);
            addWindows(windowArray, planId, vm);
            addDoors(doorArray, planId, vm);
            addCeilings(ceilingArray, planId, vm);
            addSlabs(slabArray, planId, vm);
            addRimJoists(rimJoistArray, planId, vm);
            addMechanicalEquipments(mechanicalEquipmentArray, planId, vm);
            addDistributionSystems(distributionSystemArray, planId, vm);
            addMechanicalVentilations(mechanicalVentilationArray, planId, vm);
            addLighting(lighting, planId, vm);
            addRefrigerator(appliances, planId, vm);
            addDishwasher(dishwasher, planId, vm);
            addClothesDryer(clothesDryer, planId, vm);
            addClothesWasher(clothesWasher, planId, vm);
            addRangeOven(rangeOven, planId, vm);
            addInfiltration(infiltration, planId, vm);
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
                params.put(AUTH_HEADER, "Basic " +  Base64.encodeToString(API_EKOTROPE_AUTH_PROD.getBytes(), Base64.DEFAULT));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(90), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public JsonObjectRequest updateEkotropePlanData(Ekotrope_DataViewModel vm, String planId, String projectId, String inspectionType, final ServerCallback callback) {
        String auth = isEkotropeProd ? API_EKOTROPE_AUTH_PROD : API_EKOTROPE_AUTH_TEST;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_EKOTROPE_INSPECTION_SYNC, null, response -> {
            BridgeLogger.log('I', TAG, String.format(Locale.ENGLISH, "API Response Code: %s", response.optString("Status")));
            callback.onSuccess("Success!");
        }, error -> {
            BridgeLogger.log('I', TAG, String.format(Locale.ENGLISH, "API Response Code: %s", error.networkResponse.statusCode));
            if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, "Lost connection in updateEkotropePlanData.");
                callback.onFailure("No connection, please try again.");
            } else if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, "Request timed out in updateEkotropePlanData.");
                callback.onFailure("Request timed out, please try again");
            } else {
                String errorMessage = new String(error.networkResponse.data);
                BridgeLogger.log('E', TAG, "ERROR in updateEkotropePlanData: " + errorMessage);
                callback.onFailure(errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, "Basic " +  Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                return params;
            }

            @Override
            public byte[] getBody() {
                if (inspectionType.equals(EKOTROPE_INSPECTION_TYPE_ROUGH)) {
                    return vm.getInspectionSyncJson_Rough(planId, projectId).toString().getBytes();
                } else if (inspectionType.equals(EKOTROPE_INSPECTION_TYPE_FINAL)) {
                    return vm.getInspectionSyncJson_Final(planId, projectId).toString().getBytes();
                } else {
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(30), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public void addFramedFloors(JSONArray framedFloorsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < framedFloorsArray.length(); lcv++) {
            JSONObject framedFloorObj = framedFloorsArray.optJSONObject(lcv);
            JSONObject typeObj = framedFloorObj.optJSONObject("type");
            JSONObject assemblyDetailsObj = Objects.requireNonNull(typeObj).optJSONObject("assemblyDetails");
            JSONArray cavityInsulationGradeObj = Objects.requireNonNull(assemblyDetailsObj).optJSONArray("cavityInsulationGrade");
            JSONArray studSpacingObj = assemblyDetailsObj.optJSONArray("framingSpacing");
            JSONArray studWidthObj = assemblyDetailsObj.optJSONArray("framingWidth");
            JSONArray studDepthObj = assemblyDetailsObj.optJSONArray("framingDepth");
            JSONArray studMaterialObj = assemblyDetailsObj.optJSONArray("studType");

            Ekotrope_FramedFloor_Table framedFloor = new Ekotrope_FramedFloor_Table();
            framedFloor.plan_id = planId;
            framedFloor.index = lcv;
            framedFloor.name = framedFloorObj.optString("name");
            framedFloor.typeName = typeObj.optString("name");
            framedFloor.cavityInsulationGrade = Objects.requireNonNull(cavityInsulationGradeObj).optString(0);
            framedFloor.cavityInsulationR = assemblyDetailsObj.optDouble("cavityR");
            framedFloor.continuousInsulationR = assemblyDetailsObj.optDouble("continuousR");
            framedFloor.studSpacing = Objects.requireNonNull(studSpacingObj).optDouble(0);
            framedFloor.studWidth = Objects.requireNonNull(studWidthObj).optDouble(0);
            framedFloor.studDepth = Objects.requireNonNull(studDepthObj).optDouble(0);
            framedFloor.studMaterial = Objects.requireNonNull(studMaterialObj).optString(0);
            framedFloor.isChanged = false;

            vm.insertFramedFloor(framedFloor);
        }
    }
    public void addAboveGradeWalls(JSONArray aboveGradeWallsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < aboveGradeWallsArray.length(); lcv++) {
            JSONObject aboveGradeWallObj = aboveGradeWallsArray.optJSONObject(lcv);
            JSONObject typeObj = aboveGradeWallObj.optJSONObject("type");
            JSONObject assemblyDetailsObj = typeObj.optJSONObject("assemblyDetails");
            JSONArray cavityInsulationGradeObj = assemblyDetailsObj.optJSONArray("cavityInsulationGrade");
            JSONArray studSpacingObj = assemblyDetailsObj.optJSONArray("framingSpacing");
            JSONArray studWidthObj = assemblyDetailsObj.optJSONArray("framingWidth");
            JSONArray studDepthObj = assemblyDetailsObj.optJSONArray("framingDepth");
            JSONArray studMaterialObj = assemblyDetailsObj.optJSONArray("studType");

            Ekotrope_AboveGradeWall_Table aboveGradeWall = new Ekotrope_AboveGradeWall_Table();
            aboveGradeWall.plan_id = planId;
            aboveGradeWall.index = lcv;
            aboveGradeWall.name = aboveGradeWallObj.optString("name");
            aboveGradeWall.typeName = typeObj.optString("name");
            aboveGradeWall.cavityInsulationGrade = cavityInsulationGradeObj.optString(0);
            aboveGradeWall.cavityInsulationR = assemblyDetailsObj.optDouble("cavityR");
            aboveGradeWall.continuousInsulationR = assemblyDetailsObj.optDouble("continuousR");
            aboveGradeWall.studSpacing = studSpacingObj.optDouble(0);
            aboveGradeWall.studWidth = studWidthObj.optDouble(0);
            aboveGradeWall.studDepth = studDepthObj.optDouble(0);
            aboveGradeWall.studMaterial = studMaterialObj.optString(0);
            aboveGradeWall.isChanged = false;

            vm.insertAboveGradeWall(aboveGradeWall);
        }
    }
    public void addWindows(JSONArray windowsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < windowsArray.length(); lcv++) {
            JSONObject windowObj = windowsArray.optJSONObject(lcv);
            JSONObject typeObj = windowObj.optJSONObject("type");
            JSONObject shadingFactorsObj = windowObj.optJSONObject("shadingFactors");
            JSONObject adjacentObj = shadingFactorsObj.optJSONObject("adjacent");

            Ekotrope_Window_Table window = new Ekotrope_Window_Table();
            window.plan_id = planId;
            window.index = lcv;
            window.name = windowObj.optString("name");
            window.typeName = typeObj.optString("name");
            window.remove = false;
            window.windowArea = windowObj.optDouble("surfaceArea");
            window.orientation = windowObj.optString("orientation");
            window.installedWallIndex = windowObj.optInt("installedWallIndex");
            window.installedFoundationWallIndex = -1;
            window.overhangDepth = windowObj.optDouble("overhangDepth");
            window.distanceOverhangToTop = windowObj.optDouble("distanceOverhangToTop");
            window.distanceOverhangToBottom = windowObj.optDouble("distanceOverhangToBottom");
            window.SHGC = typeObj.optDouble("SHGC");
            window.uFactor = typeObj.optDouble("uFactor");
            window.adjacentSummerShading = adjacentObj.optDouble("summer");
            window.adjacentWinterShading = adjacentObj.optDouble("winter");
            window.isChanged = false;

            vm.insertWindow(window);
        }
    }
    public void addDoors(JSONArray doorsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < doorsArray.length(); lcv++) {
            JSONObject doorObj = doorsArray.optJSONObject(lcv);
            JSONObject typeObj = doorObj.optJSONObject("type");

            Ekotrope_Door_Table door = new Ekotrope_Door_Table();
            door.plan_id = planId;
            door.index = lcv;
            door.name = doorObj.optString("name");
            door.typeName = typeObj.optString("name");
            door.remove = false;
            door.installedWallIndex = doorObj.optInt("installedWallIndex");
            door.installedFoundationWallIndex = -1;
            door.doorArea = doorObj.optDouble("surfaceArea");
            door.uFactor = typeObj.optDouble("uFactor");
            door.isChanged = false;

            vm.insertDoor(door);
        }
    }
    public void addCeilings(JSONArray ceilingsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < ceilingsArray.length(); lcv++) {
            JSONObject ceilingObj = ceilingsArray.optJSONObject(lcv);
            JSONObject typeObj = ceilingObj.optJSONObject("type");
            JSONObject assemblyDetailsObj = typeObj.optJSONObject("assemblyDetails");
            JSONArray cavityInsulationGradeObj = assemblyDetailsObj.optJSONArray("cavityInsulationGrade");
            JSONArray cavityInsulationRObj = assemblyDetailsObj.optJSONArray("cavityInsulationRValues");
            JSONArray studSpacingObj = assemblyDetailsObj.optJSONArray("framingSpacing");
            JSONArray studWidthObj = assemblyDetailsObj.optJSONArray("framingWidth");
            JSONArray studDepthObj = assemblyDetailsObj.optJSONArray("framingDepth");
            JSONArray studMaterialObj = assemblyDetailsObj.optJSONArray("studType");

            Ekotrope_Ceiling_Table ceiling = new Ekotrope_Ceiling_Table();
            ceiling.plan_id = planId;
            ceiling.index = lcv;
            ceiling.name = ceilingObj.optString("name");
            ceiling.typeName = typeObj.optString("name");
            ceiling.cavityInsulationGrade = cavityInsulationGradeObj.optString(0);
            ceiling.cavityInsulationR = cavityInsulationRObj.optDouble(0);
            ceiling.continuousInsulationR = assemblyDetailsObj.optDouble("continuousR");
            ceiling.studSpacing = studSpacingObj.optDouble(0);
            ceiling.studWidth = studWidthObj.optDouble(0);
            ceiling.studDepth = studDepthObj.optDouble(0);
            ceiling.studMaterial = studMaterialObj.optString(0);
            ceiling.hasRadiantBarrier = typeObj.optBoolean("radiantBarrier");
            ceiling.isChanged = false;

            vm.insertCeiling(ceiling);
        }
    }
    public void addSlabs(JSONArray slabsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < slabsArray.length(); lcv++) {
            JSONObject slabObj = slabsArray.optJSONObject(lcv);
            JSONObject typeObj = slabObj.optJSONObject("type");

            Ekotrope_Slab_Table slab = new Ekotrope_Slab_Table();
            slab.plan_id = planId;
            slab.index = lcv;
            slab.name = slabObj.optString("name");
            slab.typeName = typeObj.optString("name");
            slab.underslabInsulationR = typeObj.optDouble("underSlabRValue");
            slab.isFullyInsulated = typeObj.optBoolean("slabCompletelyInsulated");
            slab.underslabInsulationWidth = typeObj.optDouble("underslabInsulationWidth");
            slab.perimeterInsulationDepth = typeObj.optDouble("perimeterInsulationDepth");
            slab.perimeterInsulationR = typeObj.optDouble("perimeterRValue");
            slab.thermalBreak = false;
            slab.isChanged = false;

            vm.insertSlab(slab);
        }
    }
    public void addRimJoists(JSONArray rimJoistsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < rimJoistsArray.length(); lcv++) {
            JSONObject rimJoistObj = rimJoistsArray.optJSONObject(lcv);
            JSONObject typeObj = rimJoistObj.optJSONObject("type");

            Ekotrope_RimJoist_Table rimJoint = new Ekotrope_RimJoist_Table();
            rimJoint.plan_id = planId;
            rimJoint.index = lcv;
            rimJoint.name = rimJoistObj.optString("name");
            rimJoint.typeName = typeObj.optString("name");
            rimJoint.betweenInteriorAnd = rimJoistObj.optString("betweenInteriorAnd");
            rimJoint.surfaceArea = rimJoistObj.optDouble("surfaceArea");
            rimJoint.uFactor = typeObj.optDouble("uFactor");
            rimJoint.rFactor = typeObj.optDouble("rFactor");
            rimJoint.isChanged = false;

            vm.insertRimJoist(rimJoint);
        }
    }
    public void addMechanicalEquipments(JSONArray mechanicalEquipmentsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < mechanicalEquipmentsArray.length(); lcv++) {
            JSONObject mechanicalEquipmentObj = mechanicalEquipmentsArray.optJSONObject(lcv);
            JSONObject typeObj = mechanicalEquipmentObj.optJSONObject("type");

            Ekotrope_MechanicalEquipment_Table mechanicalEquipment = new Ekotrope_MechanicalEquipment_Table();
            mechanicalEquipment.plan_id = planId;
            mechanicalEquipment.index = lcv;
            mechanicalEquipment.name = mechanicalEquipmentObj.optString("name");
            mechanicalEquipment.equipment_type = typeObj.optString("equipmentType");
            mechanicalEquipment.model_number = typeObj.optString("modelNumber");
            mechanicalEquipment.location = mechanicalEquipmentObj.optString("location");
            mechanicalEquipment.percent_heating_load = mechanicalEquipmentObj.optDouble("heatingPercentLoad");
            mechanicalEquipment.percent_cooling_load = mechanicalEquipmentObj.optDouble("coolingPercentLoad");
            mechanicalEquipment.percent_hot_water_load = mechanicalEquipmentObj.optDouble("hotWaterPercentLoad");
            mechanicalEquipment.ahri_reference_number = typeObj.optString("ahriReferenceNumber");
            mechanicalEquipment.ahri_reference_fuel_type = typeObj.optString("fuel");
            mechanicalEquipment.rc_test_conducted = false;
            mechanicalEquipment.rc_test_method = "NON_INVASIVE";
            mechanicalEquipment.rc_metering_device = "PISTON_CAP_TUBE";
            mechanicalEquipment.rc_difference_dtd = 0.0;
            mechanicalEquipment.rc_difference_ctoa = 0.0;
            mechanicalEquipment.rc_weight_deviation = 0.0;
            mechanicalEquipment.is_changed = false;

            vm.insertMechanicalEquipment(mechanicalEquipment);
        }
    }
    public void addDistributionSystems(JSONArray distributionSystemsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < distributionSystemsArray.length(); lcv++) {
            JSONObject distributionSystemObj = distributionSystemsArray.optJSONObject(lcv);
            JSONObject testedDetailsObj = distributionSystemObj.optJSONObject("testedDetails");
            JSONArray ductsArray = testedDetailsObj.optJSONArray("ducts");

            Ekotrope_DistributionSystem_Table distributionSystem = new Ekotrope_DistributionSystem_Table();
            distributionSystem.plan_id = planId;
            distributionSystem.index = lcv;
            distributionSystem.system_type = distributionSystemObj.optString("systemType");
            distributionSystem.is_leakage_to_outside_tested = testedDetailsObj.optBoolean("isLeakageToOutsideTested");
            distributionSystem.leakage_to_outside = testedDetailsObj.optDouble("leakageToOutside");
            distributionSystem.total_leakage = testedDetailsObj.optDouble("totalLeakage");
            distributionSystem.total_duct_leakage_test_condition = testedDetailsObj.optString("totalLeakageTestCondition");
            distributionSystem.number_of_returns = testedDetailsObj.optInt("numberOfReturnGrilles");
            distributionSystem.sq_feet_served = testedDetailsObj.optDouble("sqFtServed");
            addDucts(ductsArray, planId, lcv, vm);
            distributionSystem.is_changed = false;

            vm.insertDistributionSystem(distributionSystem);
        }
    }
    public void addDucts(JSONArray ductsArray, String planId, int ds_id, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < ductsArray.length(); lcv++) {
            JSONObject ductObj = ductsArray.optJSONObject(lcv);

            Ekotrope_Duct_Table duct = new Ekotrope_Duct_Table();
            duct.plan_id = planId;
            duct.index = lcv;
            duct.ds_id = ds_id;
            duct.location = ductObj.optString("location");
            duct.percent_supply_area = ductObj.optDouble("percentSupplyArea");
            duct.percent_return_area = ductObj.optDouble("percentReturnArea");
            duct.is_changed = false;

            vm.insertDuct(duct);
        }
    }
    public void addMechanicalVentilations(JSONArray mechanicalVentilationsArray, String planId, RouteSheetViewModel vm) {
        for (int lcv = 0; lcv < mechanicalVentilationsArray.length(); lcv++) {
            JSONObject mechanicalVentilationObj = mechanicalVentilationsArray.optJSONObject(lcv);

            Ekotrope_MechanicalVentilation_Table mechanicalVentilation = new Ekotrope_MechanicalVentilation_Table();
            mechanicalVentilation.plan_id = planId;
            mechanicalVentilation.index = lcv;
            mechanicalVentilation.motor_type = mechanicalVentilationObj.optString("motorType");
            mechanicalVentilation.ventilation_type = mechanicalVentilationObj.optString("ventilationType");
            mechanicalVentilation.measured_flow_rate = mechanicalVentilationObj.optDouble("ventilationRate");
            mechanicalVentilation.fan_watts = mechanicalVentilationObj.optDouble("watts");
            mechanicalVentilation.operational_hours_per_day = mechanicalVentilationObj.optDouble("operationalHoursPerDay");
            mechanicalVentilation.is_changed = false;

            vm.insertMechanicalVentilation(mechanicalVentilation);
        }
    }
    public void addLighting(JSONObject lightingObj, String planId, RouteSheetViewModel vm) {
        JSONObject percentEfficientObj = lightingObj.optJSONObject("percentEfficient");
        JSONObject percentLEDObj = lightingObj.optJSONObject("percentLED");

        Ekotrope_Lighting_Table lighting = new Ekotrope_Lighting_Table();
        lighting.plan_id = planId;
        lighting.percent_interior_fluorescent = percentEfficientObj.optDouble("interior");
        lighting.percent_interior_led = percentLEDObj.optDouble("interior");
        lighting.percent_exterior_fluorescent = percentEfficientObj.optDouble("exterior");
        lighting.percent_exterior_led = percentLEDObj.optDouble("exterior");
        lighting.percent_garage_fluorescent = percentEfficientObj.optDouble("garage");
        lighting.percent_garage_led = percentLEDObj.optDouble("garage");
        lighting.is_changed = false;

        vm.insertLighting(lighting);
    }
    public void addRefrigerator(JSONObject applianceObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_Refrigerator_Table refrigerator = new Ekotrope_Refrigerator_Table();
        refrigerator.plan_id = planId;
        refrigerator.refrigerator_consumption = applianceObj.optDouble("refrigeratorConsumption");
        refrigerator.is_changed = false;

        vm.insertRefrigerator(refrigerator);
    }
    public void addDishwasher(JSONObject dishwasherObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_Dishwasher_Table dishwasher = new Ekotrope_Dishwasher_Table();
        dishwasher.plan_id = planId;
        dishwasher.dishwasher_available = dishwasherObj.optBoolean("isAvailable");
        dishwasher.dishwasher_defaults_type = dishwasherObj.optString("defaultsType");
        dishwasher.dishwasher_size = dishwasherObj.optString("size");
        dishwasher.dishwasher_efficiency_type = dishwasherObj.optString("efficiencyType");
        dishwasher.dishwasher_efficiency = dishwasherObj.optDouble("efficiency");
        dishwasher.dishwasher_annual_gas_cost = dishwasherObj.optDouble("annualGasCost");
        dishwasher.dishwasher_gas_rate = dishwasherObj.optDouble("gasRate");
        dishwasher.dishwasher_electric_rate = dishwasherObj.optDouble("electricRate");
        dishwasher.is_changed = false;

        vm.insertDishwasher(dishwasher);
    }
    public void addClothesDryer(JSONObject clothesDryerObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_ClothesDryer_Table clothesDryer = new Ekotrope_ClothesDryer_Table();
        clothesDryer.plan_id = planId;
        if (clothesDryerObj.length() == 0) {
            clothesDryer.available = false;
            clothesDryer.defaults_type = null;
            clothesDryer.combined_energy_factor = null;
            clothesDryer.utilization_factor = null;
        } else {
            clothesDryer.available = true;
            clothesDryer.defaults_type = clothesDryerObj.optString("defaultsType");
            clothesDryer.combined_energy_factor = clothesDryerObj.optDouble("combinedEfficiencyFactor");
            clothesDryer.utilization_factor = clothesDryerObj.optString("utilizationFactor");
        }
        clothesDryer.is_changed = false;

        vm.insertClothesDryer(clothesDryer);
    }
    public void addClothesWasher(JSONObject clothesWasherObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_ClothesWasher_Table clothesWasher = new Ekotrope_ClothesWasher_Table();
        clothesWasher.plan_id = planId;
        if (clothesWasherObj.length() == 0) {
            clothesWasher.available = false;
            clothesWasher.defaults_type = null;
            clothesWasher.load_type = null;
            clothesWasher.labeled_energy_rating = null;
            clothesWasher.integrated_modified_energy_factor = null;
        } else {
            clothesWasher.available = true;
            clothesWasher.defaults_type = clothesWasherObj.optString("defaultsType");
            clothesWasher.load_type = clothesWasherObj.optString("loadType");
            clothesWasher.labeled_energy_rating = clothesWasherObj.optDouble("labeledEnergyRating");
            clothesWasher.integrated_modified_energy_factor = clothesWasherObj.optDouble("integratedModifiedEnergyFactor");
        }
        clothesWasher.is_changed = false;

        vm.insertClothesWasher(clothesWasher);
    }
    public void addRangeOven(JSONObject rangeOvenObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_RangeOven_Table rangeOven = new Ekotrope_RangeOven_Table();
        rangeOven.plan_id = planId;
        rangeOven.fuel_type = rangeOvenObj.optString("fuel");
        rangeOven.is_induction_range = rangeOvenObj.optBoolean("isInductionStove");
        rangeOven.is_convection_oven = rangeOvenObj.optBoolean("isOvenConvection");
        rangeOven.is_changed = false;

        vm.insertRangeOven(rangeOven);
    }
    public void addInfiltration(JSONObject infiltrationObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_Infiltration_Table infiltration = new Ekotrope_Infiltration_Table();
        infiltration.plan_id = planId;
        infiltration.cfm_50 = infiltrationObj.optDouble("cfm50");
        infiltration.ach_50 = infiltrationObj.optDouble("ach50");
        infiltration.measurement_type = infiltrationObj.optString("fieldTestStatus");
        infiltration.is_changed = false;

        vm.insertInfiltration(infiltration);
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
                    attachment.AttachmentID = obj.optInt("AttachmentID");
                    attachment.InspectionID = obj.optInt("InspectionID");
                    attachment.LocationID = obj.optInt("LocationID");
                    attachment.FileName = obj.optString("AttachmentFileName");
                    attachment.FileData = Base64.decode(obj.optString("ImageFileData"), Base64.DEFAULT);
                    attachment.AttachmentType = obj.optString("AttachmentType");
                    attachment.FilePath = null;
                    attachment.IsUploaded = true;

                    String fileName = attachment.InspectionID + "_" + attachment.FileName;
                    String path = ctx.getExternalFilesDir("Attachments").getAbsolutePath() + "/" + fileName;
                    File file = new File(path);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(attachment.FileData);
                        fos.close();
                    } catch (Exception e) {
                        BridgeLogger.log('E', TAG, "ERROR in updateAttachments: " + e.getMessage());
                    }

                    attachment.FilePath = path;

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
                    pastInspection.InspectionID = obj.optInt("InspectionID");
                    //pastInspection.InspectionSubmitTime = OffsetDateTime.parse(obj.optString("InspectionSubmitTime"));
                    pastInspection.LocationID = obj.optInt("LocationID");
                    pastInspection.Inspector = obj.getString("Inspector");
                    pastInspection.InspectionType = obj.getString("InspectionType");
                    pastInspection.InspectionStatusID = obj.optInt("InspectionStatusID");
                    pastInspection.IncompleteReason = obj.getString("IncompleteReason");

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

        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, 0));
        return request;
    }
    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect, int defectItemId, int inspectionId, ReviewAndSubmitViewModel vm, final ServerCallback callback) {
        String url = isProd ? API_PROD_URL : API_STAGE_URL;
        url += POST_INSPECTION_DEFECT_V3_URL;

        if (vm != null) {
            SubmitRequest_Table submitRequest = new SubmitRequest_Table(inspectionId, defectItemId, false, false);
            vm.insertSubmitRequest(submitRequest);
        }

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
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, 0));
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
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, 0));
        return request;
    }
}
