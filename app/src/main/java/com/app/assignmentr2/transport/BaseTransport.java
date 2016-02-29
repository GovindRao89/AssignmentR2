package com.app.assignmentr2.transport;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Govind on 28-02-2016.
 */
public class BaseTransport {

    private static final String TAG = "BaseTransport";
    protected Context mContext = null;

    public BaseTransport(Context context) {
        mContext = context;
    }
}
