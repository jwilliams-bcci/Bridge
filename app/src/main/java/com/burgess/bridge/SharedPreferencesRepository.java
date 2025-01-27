package com.burgess.bridge;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
import static com.burgess.bridge.Constants.PREF_REMEMBER_CREDENTIALS;
import static com.burgess.bridge.Constants.PREF_ROUTE_SHEET_LAST_UPDATED;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesRepository {
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void removePref(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(PREF_AUTH_TOKEN, "NULL");
    }
    public void setAuthToken(String token) {
        sharedPreferences.edit().putString(PREF_AUTH_TOKEN, token).apply();
    }
    public long getAuthTokenAge() {
        return sharedPreferences.getLong(PREF_AUTH_TOKEN_AGE, 0);
    }
    public void setAuthTokenAge(long age) {
        sharedPreferences.edit().putLong(PREF_AUTH_TOKEN_AGE, age).apply();
    }

    public boolean getRememberCredentials() {
        return sharedPreferences.getBoolean(PREF_REMEMBER_CREDENTIALS, false);
    }
    public void setRememberCredentials(boolean remember) {
        sharedPreferences.edit().putBoolean(PREF_REMEMBER_CREDENTIALS, remember).apply();
    }
    public String getLoginName() {
        return sharedPreferences.getString(PREF_LOGIN_NAME, "NULL");
    }
    public void setLoginName(String name) {
        sharedPreferences.edit().putString(PREF_LOGIN_NAME, name).apply();
    }
    public String getLoginPassword() {
        return sharedPreferences.getString(PREF_LOGIN_PASSWORD, "NULL");
    }
    public void setLoginPassword(String password) {
        sharedPreferences.edit().putString(PREF_LOGIN_PASSWORD, password).apply();
    }

    public String getSecurityUserId() {
        return sharedPreferences.getString(PREF_SECURITY_USER_ID, "NULL");
    }
    public void setSecurityUserId(String securityUserId) {
        sharedPreferences.edit().putString(PREF_SECURITY_USER_ID, securityUserId).apply();
    }
    public String getInspectorId() {
        return sharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
    }
    public void setInspectorId(String inspectorId) {
        sharedPreferences.edit().putString(PREF_INSPECTOR_ID, inspectorId).apply();
    }
    public String getDivisionId() {
        return sharedPreferences.getString(PREF_INSPECTOR_DIVISION_ID, "NULL");
    }
    public void setDivisionId(String divisionId) {
        sharedPreferences.edit().putString(PREF_INSPECTOR_DIVISION_ID, divisionId).apply();
    }

    public String getRouteSheetLastUpdated() {
        return sharedPreferences.getString(PREF_ROUTE_SHEET_LAST_UPDATED, "NULL");
    }
    public void setRouteSheetLastUpdated(String lastUpdated) {
        sharedPreferences.edit().putString(PREF_ROUTE_SHEET_LAST_UPDATED, lastUpdated).apply();
    }
    public int getIndividualRemaining() {
        return sharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, 0);
    }
    public void setIndividualRemaining(int numInspections) {
        sharedPreferences.edit().putInt(PREF_IND_INSPECTIONS_REMAINING, numInspections).apply();
    }
    public int getTeamRemaining() {
        return sharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, 0);
    }
    public void setTeamRemaining(int numInspections) {
        sharedPreferences.edit().putInt(PREF_TEAM_INSPECTIONS_REMAINING, numInspections).apply();
    }
}
