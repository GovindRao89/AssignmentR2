package com.app.assignmentr2.listener;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.app.assignmentr2.controller.ControllerCallback;
import com.app.assignmentr2.controller.ControllerManager;
import com.app.assignmentr2.model.NetworkResponse;
import com.app.assignmentr2.model.Words;
import com.app.assignmentr2.utility.AxisWalletConstants;
import com.app.assignmentr2.utility.VolleyErrorCode;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Govind on 28-02-2016.
 */
public class NetworkListener implements ErrorListener, Listener<JSONObject> {

    private static final String TAG = "NetworkListener";
    public int mRequestId;

    public NetworkListener(int requestId) {
        mRequestId = requestId;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse Data = " + error.getMessage());
        ControllerManager controllerManager = ControllerManager.getInstance();
        ControllerCallback controllerCallback = controllerManager.getControllerCallback(mRequestId);
        NetworkResponse resp = new NetworkResponse();
        resp.setmErrorCode(Integer.toString(VolleyErrorCode.getErrorCode(error)));
        resp.setmErrorDescription(error.getMessage());

        // Send callback to all the listeners.
        if (controllerCallback != null) {
            controllerCallback.onErrorResponseProxy(mRequestId, 0, resp);
            controllerManager.removeListener(mRequestId);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "OnResponse Data = " + response);
        ControllerManager controllerManager = ControllerManager.getInstance();
        ControllerCallback controllerCallback = controllerManager.getControllerCallback(mRequestId);
        NetworkResponse networkResponse = new NetworkResponse();
        List<Words> wordsArrayList = new ArrayList<Words>();

        try {
            Gson gson = new Gson();
            JSONObject jsonObject = response;
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Log.d(TAG, "Json Data Keys " + key);
                if (key.equalsIgnoreCase("words")) {
                    JSONArray jsonReturnFlightArray =
                            jsonObject.getJSONArray("words");
                    if (jsonReturnFlightArray != null) {
                        Log.d(TAG,
                                "JsonReturnWordsArray length "
                                        + jsonReturnFlightArray.length());
                        for (int i = 0; i < jsonReturnFlightArray.length(); i++) {
                            JSONObject jsonObject1 = jsonReturnFlightArray.getJSONObject(i);
                            Words wordObject =
                                    gson.fromJson(jsonObject1.toString(), Words.class);
                            wordsArrayList.add(wordObject);
                        }
                        networkResponse.setWordsList(wordsArrayList);
                    } else {
                        Log.d(TAG, "jsonReturnWordsArray is null");
                    }
                } else if (key.equalsIgnoreCase("version")) {
                    String versionNumber = jsonObject.getString("version");
                    if (!TextUtils.isEmpty(versionNumber)) {
                        networkResponse.setVersion(jsonObject.getString("version"));
                    } else {
                        Log.d(TAG, "jsonReturnVersion is null");
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception in gson parsing = " + e.getMessage());
            e.printStackTrace();
            networkResponse.setmErrorCode(AxisWalletConstants.JSON_PARSE_ERROR);
            networkResponse.setmErrorDescription(e.getMessage().toString());
        }
        Log.d(TAG, "Words array length = " + networkResponse.getWordsList().size());

        // Send callback to all the listeners with same url.
        if (controllerCallback != null) {
            if (networkResponse.getmErrorCode().equalsIgnoreCase("")) {
                controllerCallback.onSuccessResponseProxy(mRequestId, 0, networkResponse);
            } else {
                controllerCallback.onErrorResponseProxy(mRequestId, 0, networkResponse);
            }
            controllerManager.removeListener(mRequestId);
        }
    }
}
