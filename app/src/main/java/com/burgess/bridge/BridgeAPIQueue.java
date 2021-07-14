package com.burgess.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;

public class BridgeAPIQueue {
    private static BridgeAPIQueue instance;
    private RequestQueue queue;
    private static Context ctx;
    private boolean isProd;
    private SharedPreferences mSharedPreferences;

    private static final String TAG = "API";
    private static final String UPLOAD_INSPECTION_DEFECT_URL = "InsertInspectionDetails/";

    private BridgeAPIQueue(Context context) {
        ctx = context;
        queue = getRequestQueue();
        isProd = false;
        mSharedPreferences = context.getSharedPreferences("Bridge_Preferences", Context.MODE_PRIVATE);
    }

    public static synchronized BridgeAPIQueue getInstance(Context context) {
        if (instance == null) {
            instance = new BridgeAPIQueue(context);
        }
        return instance;
    }

    public static synchronized BridgeAPIQueue getInstance() {
        if (instance == null) {
            throw new IllegalStateException(BridgeAPIQueue.class.getSimpleName() + " is not initialized, call getInstance(...) first");
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

    public StringRequest uploadInspectionDefect(JSONObject inspectionDefect) {
        String url = isProd ? Constants.API_PROD_URL : Constants.API_STAGE_URL;
        url += UPLOAD_INSPECTION_DEFECT_URL;

        Log.i(TAG, "here boy-o");

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.i(TAG, "Uploaded defect.");
        }, error -> {
            Log.e(TAG, "uploadInspectionDefect: " + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mSharedPreferences.getString(PREF_AUTH_TOKEN, "NULL"));
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
        return request;
    }
}
