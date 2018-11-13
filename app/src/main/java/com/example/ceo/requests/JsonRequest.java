package com.example.ceo.requests;


import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

class JsonRequest extends JsonObjectRequest {

    JsonRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    JsonRequest(String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String responseString;
        try {
            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

        try {
            return Response.success(new JSONObject(responseString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            if (VolleyLog.DEBUG) {
                Log.e(VolleyLog.TAG, "Invalid JSON response: " + responseString);
            }

            return Response.error(new ParseError(je));
        }
    }

}
