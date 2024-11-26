package com.burgess.bridge.apiqueue;

import static com.burgess.bridge.Constants.*;
import static com.burgess.bridge.apiqueue.APIConstants.*;
import static com.burgess.bridge.apiqueue.APIConstants.API_PROD_URL;
import static com.burgess.bridge.apiqueue.APIConstants.API_STAGE_URL;

import android.content.Context;
import android.content.SharedPreferences;

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
import com.burgess.bridge.login.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.Inspector_Table;
import data.Tables.Room_Table;

public class LoginAPI {
    private final static String TAG = "LoginAPI";

    public LoginAPI() { }

    public static JsonObjectRequest loginUser(JSONObject loginObject, LoginViewModel vm, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(BRIDGE_LOGIN_V2_URL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, loginObject, response -> {
            vm.setSharedPreferences(response, loginObject.optString("username"), loginObject.optString("password"));
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out during login, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Authentication error, verify username and password and try again");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
                callback.onFailure("Unknown error, please contact support");
            }
        }) {
            @Override
            public String getBodyContentType() { return "application/json"; }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(10), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
    public static JsonArrayRequest getCannedComments(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(BRIDGE_GET_CANNED_COMMENTS_URL);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
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
                    callback.onFailure("Error reading canned comments, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting canned comments, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
    public static JsonArrayRequest getBuilders(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(BRIDGE_GET_BUILDERS_URL);

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
                    callback.onFailure("Error reading builders, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting builders, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
    public static JsonArrayRequest getInspectorsV2(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url +=  String.format(BRIDGE_GET_INSPECTORS_V2_URL);

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
                    callback.onFailure("Error reading inspectors, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting inspectors, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
    public static JsonArrayRequest getRooms(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_ROOMS_URL);

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
                    callback.onFailure("Error reading rooms, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting rooms, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
    public static JsonArrayRequest getDirections(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_DIRECTIONS_URL);

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
                    callback.onFailure("Error reading directions, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting directions, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
    public static JsonArrayRequest getFaults(LoginViewModel vm, String authToken, final ServerCallback callback) {
        String url = BridgeAPIQueue.isProd() ? API_PROD_URL : API_STAGE_URL;
        url += String.format(BRIDGE_GET_FAULTS_URL);

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
                    callback.onFailure("Error reading faults, please contact support");
                }
            }
            callback.onSuccess("Success");
        }, error -> {
            if (error instanceof TimeoutError) {
                BridgeLogger.log('E', TAG, String.format("TimeoutError - %s",error.getMessage()));
                callback.onFailure("Request timed out while getting faults, check connection and try again");
            } else if (error instanceof NoConnectionError) {
                BridgeLogger.log('E', TAG, String.format("NoConnectionError - %s",error.getMessage()));
                callback.onFailure("No internet connection, check connection and try again");
            } else if (error instanceof AuthFailureError) {
                BridgeLogger.log('E', TAG, String.format("AuthFailureError - %s",error.getMessage()));
                callback.onFailure("Token error, please contact support");
            } else if (error instanceof ServerError) {
                BridgeLogger.log('E', TAG, String.format("ServerError - %s",error.getMessage()));
                callback.onFailure("Server error, please contact support");
            } else if (error instanceof NetworkError) {
                BridgeLogger.log('E', TAG, String.format("NetworkError - %s",error.getMessage()));
                callback.onFailure("Network error, please contact support");
            } else if (error instanceof ParseError) {
                BridgeLogger.log('E', TAG, String.format("ParseError - %s",error.getMessage()));
                callback.onFailure("Parse error, please contact support");
            } else {
                BridgeLogger.log('E', TAG, String.format("Unknown error - %s",error.getMessage()));
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
}
