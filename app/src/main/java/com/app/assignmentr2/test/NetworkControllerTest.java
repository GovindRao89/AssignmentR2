package com.app.assignmentr2.test;

import android.app.Activity;
import android.util.Log;
import com.app.assignmentr2.controller.ControllerCallback;
import com.app.assignmentr2.controller.ControllerManager;
import com.app.assignmentr2.controller.NetworkController;
import com.app.assignmentr2.model.NetworkResponse;
import com.app.assignmentr2.model.Words;

import java.util.List;

/**
 * Created by Govind Rao on 28-02-2016.
 */
public class NetworkControllerTest {

    private static final String TAG = "NetworkControllerTest";
    Activity mActivityRef = null;

    public NetworkControllerTest(Activity activityRef) {
        mActivityRef = activityRef;
    }

    public void networkCall(){
        final ControllerManager controllerManager = ControllerManager.getInstance();
        final NetworkController networkController = controllerManager.getNetworkController();

        ControllerCallback controllerCallback = new ControllerCallback(mActivityRef) {

            @Override
            protected void onSuccessResponse(int requestId, int status, Object data) {
                Log.d(TAG, "In OnSuccess Network Raw data = " + data.toString());
                NetworkResponse arr = (NetworkResponse) data;
                List<Words> wordsList = arr.getWordsList();
                String version = arr.getVersion();

                for (int i = 0; i < wordsList.size(); i++)
                    Log.d(TAG, "WORDS SEARCH data list = " + wordsList.get(i).toString());

                Log.d(TAG, "VERSION number = " + version.toString());
            }

            @Override
            protected void onErrorResponse(int requestId, int status, Object data) {
                NetworkResponse arr = (NetworkResponse) data;
                if (arr != null) {
                    Log.d(TAG, "In OnError network response code = " + arr.getmErrorCode()
                            + " message = " + arr.getmErrorDescription());
                }
            }
        };
        Log.d(TAG, "Sending a Bus Search request");
        networkController.fetchListOfWords(controllerCallback);
    }
}
