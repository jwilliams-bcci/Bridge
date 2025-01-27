package com.burgess.bridge.apiqueue;

import static com.burgess.bridge.apiqueue.APIConstants.API_EKOTROPE_AUTH_PROD;
import static com.burgess.bridge.apiqueue.APIConstants.API_EKOTROPE_AUTH_TEST;
import static com.burgess.bridge.apiqueue.APIConstants.API_EKOTROPE_PLAN_URL;
import static com.burgess.bridge.apiqueue.APIConstants.API_EKOTROPE_PROJECT_URL;
import static com.burgess.bridge.apiqueue.APIConstants.API_STAGE_URL;
import static com.burgess.bridge.apiqueue.APIConstants.API_PROD_URL;
import static com.burgess.bridge.apiqueue.APIConstants.AUTH_BEARER;
import static com.burgess.bridge.apiqueue.APIConstants.AUTH_HEADER;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_CHECK_EXISTING_INSPECTION_V2_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_DEFECT_ITEMS_V3_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V3_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_INSPECTIONS_V5_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_INSPECTION_DEFECT_HISTORY_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_PAST_INSPECTIONS_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_REMAINING_INSPECTIONS_URL;
import static com.burgess.bridge.apiqueue.APIConstants.BRIDGE_GET_REPORT_DATA_URL;

import android.text.TextUtils;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.routesheet.RouteSheetViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

public class RouteSheetAPI {
    private final static String TAG = "RouteSheetAPI";

    public RouteSheetAPI() { }

    public static JsonArrayRequest updateInspections(RouteSheetViewModel vm, String authToken, String inspectorId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_INSPECTIONS_V5_URL, inspectorId);

        ArrayList<JsonArrayRequest> inspectionDefectHistoryRequests = new ArrayList<>();
        ArrayList<JsonArrayRequest> pastInspectionRequests = new ArrayList<>();
        ArrayList<JsonObjectRequest> ekotropePlansIdRequests = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            inspectionDefectHistoryRequests.clear();
            pastInspectionRequests.clear();
            ekotropePlansIdRequests.clear();
            vm.updateIndividualRemaining(response.length());
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    Gson gson = new Gson();
                    Inspection_Table inspection = gson.fromJson(obj.toString(), Inspection_Table.class);

                    if (inspection.DivisionID == 20 || inspection.ReInspect) {
                        inspectionDefectHistoryRequests.add(updateInspectionDefectHistory(vm, authToken, inspection, callback));
                    }
                    if (!TextUtils.isEmpty(inspection.EkotropeProjectID)) {
                        ekotropePlansIdRequests.add(updateEkotropePlanId(vm, inspection.EkotropeProjectID, inspection.InspectionID, callback));
                    }
                    pastInspectionRequests.add(updatePastInspections(vm, authToken, inspection.LocationID, callback));
                    vm.insertInspection(inspection);
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateInspections: " + e.getMessage());
                    callback.onFailure("Error reading inspections, please contact support");
                }
            }

            for (int lcv = 0; lcv < inspectionDefectHistoryRequests.size(); lcv++) {
                BridgeAPIQueue.getInstance().getRequestQueue().add(inspectionDefectHistoryRequests.get(lcv));
            }
            for (int lcv = 0; lcv < pastInspectionRequests.size(); lcv++) {
                BridgeAPIQueue.getInstance().getRequestQueue().add(pastInspectionRequests.get(lcv));
            }
            for (int lcv = 0; lcv < ekotropePlansIdRequests.size(); lcv++) {
                BridgeAPIQueue.getInstance().getRequestQueue().add(ekotropePlansIdRequests.get(lcv));
            }
            callback.onSuccess("Inspections updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateInspections - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting inspections, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateInspections - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateInspections - %s",error.getMessage()));
                callback.onFailure("Security token expired, please log out and back in");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateInspections - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateInspections - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateInspections - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateInspections - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonArrayRequest updateInspectionDefectHistory(RouteSheetViewModel vm, String authToken, Inspection_Table inspection, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_INSPECTION_DEFECT_HISTORY_URL, inspection.InspectionID);
        boolean isReobservation = (inspection.InspectionTypeID == 1154);
        int inspectionId = inspection.InspectionID;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    Gson gson = new Gson();
                    InspectionHistory_Table inspectionHistory = gson.fromJson(obj.toString(), InspectionHistory_Table.class);
                    inspectionHistory.InspectionID = inspectionId;
                    inspectionHistory.InspectionDefectID = -1;

                    if (inspection.DivisionID != 20
                        || (isReobservation && inspectionHistory.DefectStatusID == 2)
                        || (inspectionHistory.DefectStatusID == 7)) {
                        vm.insertInspectionHistory(inspectionHistory);
                    } else {
                        InspectionDefect_Table newDefect = new InspectionDefect_Table();
                        newDefect.InspectionID = inspection.InspectionID;
                        newDefect.DefectItemID = inspectionHistory.DefectItemID;
                        newDefect.DefectStatusID = inspectionHistory.DefectStatusID;
                        newDefect.Comment = inspectionHistory.Comment;
                        newDefect.PriorInspectionDetailID = inspectionHistory.InspectionDetailID;
                        newDefect.FirstDefectInspectionDetailID = inspectionHistory.FirstDefectInspectionDetailID;
                        newDefect.ReinspectionRequired = false;
                        newDefect.PicturePath = null;
                        newDefect.IsUploaded = false;
                        newDefect.IsEditable = false;

                        int existingDefectId = vm.getExistingMFCDefectThread(inspectionHistory.FirstDefectInspectionDetailID, inspectionId);
                        if (existingDefectId > 0) {
                            vm.updateExistingMFCDefect(inspectionHistory.DefectStatusID, inspectionHistory.Comment, existingDefectId);
                        } else {
                            vm.insertInspectionDefect(newDefect);
                        }
                    }
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateInspectionDefectHistory: " + e.getMessage());
                    callback.onFailure("Error reading inspection defect history, please contact support");
                }
            }
            callback.onSuccess("Inspection Defect Histories Updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting inspection defect history, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Security token expired, please log out and back in");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateInspectionDefectHistory - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonObjectRequest updateEkotropePlanId(RouteSheetViewModel vm, String projectId, int inspectionId, final ServerCallback callback) {
        String url = API_EKOTROPE_PROJECT_URL + projectId;
        String auth = BridgeAPIQueue.isEkotropeProd() ? API_EKOTROPE_AUTH_PROD : API_EKOTROPE_AUTH_TEST;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            String planId = response.optString("masterPlanId");
            vm.updateEkotropePlanId(planId, inspectionId);
            BridgeAPIQueue.getInstance().getRequestQueue().add(getEkotropePlanData(vm, planId, callback));
            callback.onSuccess("Ekotrope Plan ID Updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting Ekotrope Plan ID, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateEkotropePlanId - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, "Basic " + Base64.encodeToString((auth).getBytes(), Base64.DEFAULT));
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonArrayRequest updatePastInspections(RouteSheetViewModel vm, String authToken, int locationId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_PAST_INSPECTIONS_URL, locationId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    Gson gson = new Gson();
                    PastInspection_Table pastInspection = gson.fromJson(obj.toString(), PastInspection_Table.class);
                    vm.insertPastInspection(pastInspection);
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in updatePastInspections: " + e.getMessage());
                    callback.onFailure("Error reading past inspections, please contact support");
                }
            }
            callback.onSuccess("Past Inspections Updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting past inspections, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updatePastInspections - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonArrayRequest updateDefectItems(RouteSheetViewModel vm, String authToken, String inspectorId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_DEFECT_ITEMS_V3_URL, inspectorId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    Gson gson = new Gson();
                    DefectItem_Table defectItem = gson.fromJson(obj.toString(), DefectItem_Table.class);
                    vm.insertDefectItem(defectItem);
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateDefectItems: " + e.getMessage());
                    callback.onFailure("Error reading defect items, please contact support");
                }
            }
            callback.onSuccess("Defect Items Updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting defect items, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateDefectItems - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonArrayRequest updateDefectItem_InspectionTypeXRef(RouteSheetViewModel vm, String authToken, String inspectorId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V3_URL, inspectorId);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int lcv = 0; lcv < response.length(); lcv++) {
                try {
                    JSONObject obj = response.getJSONObject(lcv);
                    Gson gson = new Gson();
                    DefectItem_InspectionType_XRef diit = gson.fromJson(obj.toString(), DefectItem_InspectionType_XRef.class);
                    vm.insertDIIT(diit);
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in updateDefectItem_InspectionTypeXRefV3: " + e.getMessage());
                    callback.onFailure("Error reading defect items, please contact support");
                }
            }
            callback.onSuccess("DIIT References Updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting DIIT references, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateDefectItem_InspectionTypeXRef - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonObjectRequest updateInspectionsRemaining(RouteSheetViewModel vm, String authToken, int inspectorId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_REMAINING_INSPECTIONS_URL, inspectorId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            int inspectionsRemaining = response.optInt("Remaining");
            vm.setTeamRemainingMessage(String.valueOf(inspectionsRemaining));
            callback.onSuccess(String.valueOf(inspectionsRemaining));
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting canned comments, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in updateInspectionsRemaining - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(10), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static StringRequest getReportData(RouteSheetViewModel vm, String authToken, int inspectorId, String inspectionDate, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_REPORT_DATA_URL, inspectorId, inspectionDate);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            callback.onSuccess(response.substring(1, response.length()-1));
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in getReportData - %s", error.getMessage()));
                callback.onFailure("Request timed out while getting route sheet report link, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in getReportData - %s", error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in getReportData - %s", error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in getReportData - %s", error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in getReportData - %s", error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in getReportData - %s", error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in getReportData - %s", error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(5), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonObjectRequest checkExistingInspection(RouteSheetViewModel vm, String authToken, int inspectionId, int inspectorId, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_CHECK_EXISTING_INSPECTION_V2_URL, inspectionId, inspectorId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            int futureDated = response.optInt("FutureDated");
            int reassignedInspection = response.optInt("ReassignedInspection");
            int notAssigned = response.optInt("NotAssigned");
            if (futureDated > 0) {
                vm.deleteInspectionDefects(inspectionId);
                vm.deleteInspection(inspectionId);
                vm.deleteInspectionHistories(inspectionId);
                BridgeLogger.log('I', TAG, "Found future dated for " + inspectionId + " - deleted...");
            } else if (reassignedInspection > 0) {
                vm.deleteInspectionDefects(inspectionId);
                vm.deleteInspection(inspectionId);
                vm.deleteInspectionHistories(inspectionId);
                BridgeLogger.log('I', TAG, "Found reassigned for " + inspectionId + " - deleted...");
            } else if (notAssigned > 0) {
                vm.deleteInspectionDefects(inspectionId);
                vm.deleteInspection(inspectionId);
                vm.deleteInspectionHistories(inspectionId);
                BridgeLogger.log('I', TAG, "Found not assigned for " + inspectionId + " - deleted...");
            }
            callback.onSuccess("Cleared completed and reassigned inspections");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Request timed out while checking existing inspections, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in checkExistingInspection - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, AUTH_BEARER + authToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    // Ekotrope Plan Data
    public static JsonObjectRequest getEkotropePlanData(RouteSheetViewModel vm, String planId, final ServerCallback callback) {
        String url = API_EKOTROPE_PLAN_URL + planId;
        String auth = BridgeAPIQueue.isEkotropeProd() ? API_EKOTROPE_AUTH_PROD : API_EKOTROPE_AUTH_TEST;

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

            callback.onSuccess("Ekotrope plan data updated");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting Ekotrope plan data, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error in getEkotropePlanData - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER, "Basic " + Base64.encodeToString((auth).getBytes(), Base64.DEFAULT));
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(15), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static void addFramedFloors(JSONArray framedFloorsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addAboveGradeWalls(JSONArray aboveGradeWallsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addWindows(JSONArray windowsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addDoors(JSONArray doorsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addCeilings(JSONArray ceilingsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addSlabs(JSONArray slabsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addRimJoists(JSONArray rimJoistsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addMechanicalEquipments(JSONArray mechanicalEquipmentsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addDistributionSystems(JSONArray distributionSystemsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addDucts(JSONArray ductsArray, String planId, int ds_id, RouteSheetViewModel vm) {
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
    public static void addMechanicalVentilations(JSONArray mechanicalVentilationsArray, String planId, RouteSheetViewModel vm) {
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
    public static void addLighting(JSONObject lightingObj, String planId, RouteSheetViewModel vm) {
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
    public static void addRefrigerator(JSONObject applianceObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_Refrigerator_Table refrigerator = new Ekotrope_Refrigerator_Table();
        refrigerator.plan_id = planId;
        refrigerator.refrigerator_consumption = applianceObj.optDouble("refrigeratorConsumption");
        refrigerator.is_changed = false;

        vm.insertRefrigerator(refrigerator);
    }
    public static void addDishwasher(JSONObject dishwasherObj, String planId, RouteSheetViewModel vm) {
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
    public static void addClothesDryer(JSONObject clothesDryerObj, String planId, RouteSheetViewModel vm) {
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
    public static void addClothesWasher(JSONObject clothesWasherObj, String planId, RouteSheetViewModel vm) {
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
    public static void addRangeOven(JSONObject rangeOvenObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_RangeOven_Table rangeOven = new Ekotrope_RangeOven_Table();
        rangeOven.plan_id = planId;
        rangeOven.fuel_type = rangeOvenObj.optString("fuel");
        rangeOven.is_induction_range = rangeOvenObj.optBoolean("isInductionStove");
        rangeOven.is_convection_oven = rangeOvenObj.optBoolean("isOvenConvection");
        rangeOven.is_changed = false;

        vm.insertRangeOven(rangeOven);
    }
    public static void addInfiltration(JSONObject infiltrationObj, String planId, RouteSheetViewModel vm) {
        Ekotrope_Infiltration_Table infiltration = new Ekotrope_Infiltration_Table();
        infiltration.plan_id = planId;
        infiltration.cfm_50 = infiltrationObj.optDouble("cfm50");
        infiltration.ach_50 = infiltrationObj.optDouble("ach50");
        infiltration.measurement_type = infiltrationObj.optString("fieldTestStatus");
        infiltration.is_changed = false;

        vm.insertInfiltration(infiltration);
    }
}
