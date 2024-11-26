package com.burgess.bridge;

public final class Constants {
    private Constants(){}

    public static final String PREF = "Bridge_Preferences";
    public static final String PREF_AUTH_TOKEN = "AuthorizationToken";
    public static final String PREF_AUTH_TOKEN_AGE = "TokenAge";
    public static final String PREF_INSPECTOR_ID = "InspectorId";
    public static final String PREF_INSPECTOR_DIVISION_ID = "DivisionId";
    public static final String PREF_SECURITY_USER_ID = "SecurityUserId";
    public static final String PREF_LOGIN_NAME = "LoginName";
    public static final String PREF_LOGIN_PASSWORD = "LoginPassword";
    public static final String PREF_IS_ONLINE = "IsOnline";
    public static final String PREF_IND_INSPECTIONS_REMAINING = "IndividualInspectionsRemaining";
    public static final String PREF_TEAM_INSPECTIONS_REMAINING = "InspectionsRemaining";

    public static final String REMEMBER_CREDENTIALS = "RememberCredentials";
    public static final String CANCEL_TAG = "CANCEL_ALL";

    public static final String API_STAGE_URL = "https://apistage.burgess-inc.com/api/Bridge/";
    public static final String API_PROD_URL = "https://api.burgess-inc.com/api/Bridge/";

    public static final String API_EKOTROPE_PROJECT_URL = "https://api.ekotrope.com/api/v1/projects/";
    public static final String API_EKOTROPE_PLAN_URL = "https://api.ekotrope.com/api/v1/houseplans/";
    public static final String API_EKOTROPE_INSPECTION_SYNC = "https://api.ekotrope.com/api/v1/projects/inspectionSync/";
    public static final String API_EKOTROPE_AUTH_TEST = "burgesstestapi:gM5WcwHE";
    public static final String API_EKOTROPE_AUTH_PROD = "burgessapi:bh&K%gcR";

    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String EKOTROPE_PROJECT_ID = "com.burgess.bridge.EKOTROPE_PROJECT_ID";
    public static final String EKOTROPE_PLAN_ID = "com.burgess.bridge.EKOTROPE_PLAN_ID";
    public static final String COMPONENT_INDEX = "com.burgess.bridge.COMPONENT_INDEX";
    public static final String COMPONENT_TYPE = "com.burgess.bridge.COMPONENT_TYPE";

    public static final String COMPONENT_MECHANICAL_EQUIPMENT = "Mechanical Equipment";
    public static final String COMPONENT_DISTRIBUTION_SYSTEM = "Distribution System";
    public static final String COMPONENT_DUCT = "Duct";
    public static final String COMPONENT_MECHANICAL_VENTILATION = "Mechanical Ventilation";

    public static final String MECHANICAL_EQUIPMENT_INDEX = "com.burgess.bridge.MECHANICAL_EQUIPMENT_INDEX";
    public static final int MECHANICAL_EQUIPMENT_INDEX_NOT_FOUND = -1;

    public static final String DISTRIBUTION_SYSTEM_INDEX = "com.burgess.bridge.DISTRIBUTION_SYSTEM_INDEX";
    public static final int DISTRIBUTION_SYSTEM_INDEX_NOT_FOUND = -1;

    public static final String DUCT_INDEX = "com.burgess.bridge.DUCT_INDEX";
    public static final int DUCT_INDEX_NOT_FOUND = -1;

    public static final String MECHANICAL_VENTILATION_INDEX = "com.burgess.bridge.MECHANICAL_VENTILATION_INDEX";
    public static final int MECHANICAL_VENTILATION_INDEX_NOT_FOUND = -1;

    public static final String EKOTROPE_INSPECTION_TYPE_FINAL = "Final";
    public static final String EKOTROPE_INSPECTION_TYPE_ROUGH = "Rough";
}