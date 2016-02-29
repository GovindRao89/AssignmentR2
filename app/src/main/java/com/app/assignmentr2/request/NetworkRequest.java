package com.app.assignmentr2.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by Govind on 28-02-2016.
 */
public class NetworkRequest extends JsonObjectRequest {

    private static final String TAG = "NetworkRequest";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

    public NetworkRequest(int method, String url, JSONObject jsonRequest,
                          Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HEADER_ACCEPT, "application/json");
        headers.put(HEADER_ACCEPT_LANGUAGE, "en-US,en;q=0.5");
        return headers;
    }
}
