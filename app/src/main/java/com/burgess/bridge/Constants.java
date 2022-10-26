package com.burgess.bridge;

import java.sql.Time;

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

    public static final String API_STAGE_URL = "https://apistage.burgess-inc.com/api/Bridge/";
    public static final String API_PROD_URL = "https://api.burgess-inc.com/api/Bridge/";

    public static final String CANCEL_TAG = "CANCEL_ALL";
}
