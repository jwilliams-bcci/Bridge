package com.burgess.bridge;

public final class Constants {
    private Constants(){}

    public static final String PREF = "Bridge_Preferences";
    public static final String REMEMBER_CREDENTIALS = "RememberCredentials";
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

    public static final String API_STAGE_URL = "https://apistage.burgess-inc.com/api/Bridge/";
    public static final String API_PROD_URL = "https://api.burgess-inc.com/api/Bridge/";

    public static final String API_EKOTROPE_PROJECT_URL = "https://api.ekotrope.com/api/v1/projects/";
    public static final String API_EKOTROPE_PLAN_URL = "https://api.ekotrope.com/api/v1/houseplans/";
    public static final String API_EKOTROPE_INSPECTION_SYNC = "https://api.ekotrope.com/api/v1/inspectionSync/";
    public static final String API_EKOTROPE_AUTH_TEST = "burgesstestapi:gM5WcwHE";
    public static final String API_EKOTROPE_AUTH_PROD = "burgessapi:bh&K%gcR";

    public static final String CANCEL_TAG = "CANCEL_ALL";
}