package com.app.assignmentr2.controller;

import android.util.Log;

import com.app.assignmentr2.listener.NetworkListener;
import com.app.assignmentr2.request.NetworkRequest;
import com.app.assignmentr2.transport.TransportManager;
import com.app.assignmentr2.utility.HttpMethods;

/**
 * Created by Govind on 26-02-2016.
 */
public class NetworkController {

    private static final String TAG = "NetworkController";
    private TransportManager mTransportManager;
    private static final String BASE_URL = "http://appsculture.com/vocab";

    public NetworkController(ControllerManager cm) {
        mTransportManager = cm.getTransportManager();
    }

    public int fetchListOfWords(ControllerCallback listener) {
        String url = BASE_URL + "/words.json";
        ControllerManager cm = ControllerManager.getInstance();
        int requestId = cm.getNextRequestId();
        // Create new listener for response and error
        NetworkListener networkListener = new NetworkListener(requestId);
        NetworkRequest request = new NetworkRequest(HttpMethods.GET, url, null,
                networkListener, networkListener);
        Log.d(TAG, "Old sendBusSearch Url = " + url);
        mTransportManager.execute(request, requestId);
        cm.addListener(requestId, listener);
        return requestId;
    }


}
