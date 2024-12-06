package com.burgess.bridge;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_ROUTE_SHEET_LAST_UPDATED;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesRepository {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public String getInspectorId() {
        return sharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");
    }

    public String getRouteSheetLastUpdated() {
        return sharedPreferences.getString(PREF_ROUTE_SHEET_LAST_UPDATED, "NULL");
    }

    public void setRouteSheetLastUpdated(String lastUpdated) {
        sharedPreferences.edit().putString(PREF_ROUTE_SHEET_LAST_UPDATED, lastUpdated).apply();
    }
}
