package com.app.assignmentr2.transport;

import android.content.Context;
import android.util.Log;
import com.app.assignmentr2.controller.ControllerManager;

/**
 * Created by Govind on 28-02-2016.
 */
public class TransportFactory {

    private static final String TAG = "TransportFactory";

    /**
     * Return transport of given type.
     * @param type Transport type.
     * @param context Context.
     * @return
     */
    public static BaseTransport createTransport(TransportType type, ControllerManager cm, Context context) {
        Log.d(TAG, "Create transport. Type : " + type.ordinal());

        switch (type) {
            case TYPE_TRANSPORT_VOLLEY:
                return new TransportManager(context, cm.getMainHandler());
            default:
                Log.e(TAG, "Invalid transport type");
                return null;
        }
    }
}
