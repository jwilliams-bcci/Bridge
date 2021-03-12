package com.example.bridge;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class BridgeAPIQueue {
    private static BridgeAPIQueue instance;
    private RequestQueue queue;
    private static Context ctx;

    private BridgeAPIQueue(Context context) {
        ctx = context;
        queue = getRequestQueue();
    }

    public static synchronized BridgeAPIQueue getInstance(Context context) {
        if (instance == null) {
            instance = new BridgeAPIQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return queue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }
}
