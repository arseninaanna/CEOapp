package com.example.ceo.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BackendAPI {

    private String TAG = VolleyLog.TAG;
    String baseUrl = "http://ec2-18-224-185-2.us-east-2.compute.amazonaws.com";
    Context ctx;

    public BackendAPI(Context ctx) {
        VolleyLog.DEBUG = true;

        this.ctx = ctx;
    }

    public void login(String email, String password, Response.Listener<Boolean> respCb, Response.ErrorListener errCb) {
        String url = baseUrl + "/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("login", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        makeRequest(url, jsonBody, response -> {
            try {
                String str = response.getString("status");
                System.out.println("json string: " + str);

                respCb.onResponse(str.equals("OK"));
            } catch (JSONException e) {
                e.printStackTrace();
                respCb.onResponse(false);
            }
        }, errCb);
    }

    public void get(String urlEnd, Response.Listener<JSONArray> respCb, Response.ErrorListener errCb) {
        String url = baseUrl + urlEnd;

        makeGetRequest(url, response -> {
            try {
                JSONArray map = response.getJSONArray("data");
                System.out.println(response);

                respCb.onResponse(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, errCb);
    }

    private JsonObjectRequest makeRequest(String url, JSONObject data, Response.Listener<JSONObject> respCb, Response.ErrorListener errCb) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonRequest(url, data, respCb, err -> {
            Log.e(TAG, "Post request failed: " + err.toString(), err);

            errCb.onErrorResponse(err);
        });
        requestQueue.add(request);

        return request;
    }

    private JsonObjectRequest makeGetRequest(String url, Response.Listener<JSONObject> respCb, Response.ErrorListener errCb) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), respCb, err -> {
            Log.e(TAG, "Get request failed: " + err.toString(), err);

            errCb.onErrorResponse(err);
        });
        requestQueue.add(request);

        return request;
    }

}
