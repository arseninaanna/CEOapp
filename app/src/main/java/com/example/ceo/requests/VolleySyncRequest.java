package com.example.ceo.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class VolleySyncRequest {

    private String TAG = "VOLLEY";
    private String url;

    VolleySyncRequest(String url) {
        this.url = url;
    }

    JSONObject fetchModules(Context ctx, JSONObject data) {
        JSONObject response = null;
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, data, future, future);
        requestQueue.add(request);

        try {
            response = future.get(10, TimeUnit.SECONDS); // Blocks for at most 10 seconds.
            if (VolleyLog.DEBUG) {
                Log.i(TAG, response.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "Post request failed: " + e.toString(), e);
        }

        return response;
    }

}
