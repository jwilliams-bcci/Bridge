package com.burgess.bridge.apiqueue;

public class APIConstants {
    public static final String API_STAGE_URL = "https://apistage.burgess-inc.com/api/Bridge/";
    public static final String API_PROD_URL = "https://api.burgess-inc.com/api/Bridge/";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_BEARER = "Bearer ";

    public static final String BRIDGE_LOGIN_URL = "Login?userName=%s&password=%s";
    public static final String BRIDGE_LOGIN_V2_URL = "Login_V2";
    public static final String BRIDGE_GET_CANNED_COMMENTS_URL = "GetCannedComments";
    public static final String BRIDGE_GET_BUILDERS_URL = "GetBuilders";
    public static final String BRIDGE_GET_INSPECTORS_V2_URL = "GetInspectorsV2";
    public static final String BRIDGE_GET_ROOMS_URL = "GetRooms";
    public static final String BRIDGE_GET_DIRECTIONS_URL = "GetDirections";
    public static final String BRIDGE_GET_FAULTS_URL = "GetFaults";
}
