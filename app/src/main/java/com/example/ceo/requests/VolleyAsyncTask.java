package com.example.ceo.requests;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Response;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class VolleyAsyncTask extends AsyncTask<String, String, JSONObject> {

    private Context ctx;
    private String url;
    private JSONObject data;

    public VolleyAsyncTask(Context hostContext, String requestUrl, JSONObject requestData, Response.Listener respCb, Response.ErrorListener errCb) {
        ctx = hostContext;
        url = requestUrl;
        data = requestData;


    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        VolleySyncRequest req = new VolleySyncRequest(url);

        return req.fetchModules(ctx, data);
    }

}
