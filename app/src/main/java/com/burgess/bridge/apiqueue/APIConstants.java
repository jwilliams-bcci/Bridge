package com.burgess.bridge.apiqueue;

public class APIConstants {
    public static final String API_STAGE_URL = "https://apistage.burgess-inc.com/api/Bridge/";
    public static final String API_PROD_URL = "https://api.burgess-inc.com/api/Bridge/";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_BEARER = "Bearer ";

    public static final String BRIDGE_LOGIN_V2_URL = "Login_V2";
    public static final String BRIDGE_GET_CANNED_COMMENTS_URL = "GetCannedComments";
    public static final String BRIDGE_GET_BUILDERS_URL = "GetBuilders";
    public static final String BRIDGE_GET_INSPECTORS_V2_URL = "GetInspectorsV2";
    public static final String BRIDGE_GET_ROOMS_URL = "GetRooms";
    public static final String BRIDGE_GET_DIRECTIONS_URL = "GetDirections";
    public static final String BRIDGE_GET_FAULTS_URL = "GetFaults";
    public static final String BRIDGE_GET_INSPECTIONS_V5_URL = "GetInspectionsV5?inspectorid=%s";
    public static final String BRIDGE_GET_INSPECTION_DEFECT_HISTORY_URL = "GetInspectionDefectHistory?inspectionid=%s";
    public static final String BRIDGE_GET_PAST_INSPECTIONS_URL = "GetPastInspections?locationid=%s";
    public static final String BRIDGE_GET_DEFECT_ITEMS_V3_URL = "GetDefectItemsV3?inspectorid=%s";
    public static final String BRIDGE_GET_DEFECT_ITEM_INSPECTION_TYPE_XREF_V3_URL = "GetDefectItem_InspectionType_XRefV3?inspectorid=%s";
    public static final String BRIDGE_GET_REPORT_DATA_URL = "GetReportData?inspectorId=%s&inspectionDate=%s";
    public static final String BRIDGE_GET_CHECK_EXISTING_INSPECTION_V2_URL = "CheckExistingInspectionV2?inspectionid=%s&inspectorid=%s";

    public static final String API_EKOTROPE_PROJECT_URL = "https://api.ekotrope.com/api/v1/projects/";
    public static final String API_EKOTROPE_PLAN_URL = "https://api.ekotrope.com/api/v1/houseplans/";
    public static final String API_EKOTROPE_INSPECTION_SYNC = "https://api.ekotrope.com/api/v1/projects/inspectionSync/";
    public static final String API_EKOTROPE_AUTH_TEST = "burgesstestapi:gM5WcwHE";
    public static final String API_EKOTROPE_AUTH_PROD = "burgessapi:bh&K%gcR";
}
