package com.app.assignmentr2.controller;

import java.lang.ref.WeakReference;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Govind on 28-02-2016.
 */
public abstract class ControllerCallback {

    private static final String TAG = "ControllerCallback";
    /**
     * Moving form Strong refs to Weak references Assuming WeakReference would
     * remove activity on destroying from memory
     */
    WeakReference<Activity> mActivityReference = null;
    WeakReference<Handler> mHandler = null;

    public ControllerCallback(Activity activity) {
        if (activity == null) {
            throw new NullPointerException();
        }
        mActivityReference = new WeakReference<Activity>(activity);
    }

    public ControllerCallback(Handler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        mHandler = new WeakReference<Handler>(handler);
    }

    protected abstract void onSuccessResponse(int requestId, int status, Object data);

    protected abstract void onErrorResponse(int requestId, int status, Object data);

    public void onSuccessResponseProxy(final int requestId, final int status, final Object data) {
        Log.d(TAG, "onSuccessResponseProxy Called");
        // Weak Reference NULL is Included to avoid Null Pointer Crash
        if (null != mActivityReference && null != mActivityReference.get()) {
            Activity activity = mActivityReference.get();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    onSuccessResponse(requestId, status, data);
                }
            });
            activity = null;
            // Weak Reference NULL is Included to avoid Null Pointer Crash
        } else if (null != mHandler && null != mHandler.get()) {
            Handler handler = mHandler.get();
            handler.post(new Runnable() {
                public void run() {
                    onSuccessResponse(requestId, status, data);
                }
            });
            handler = null;
        } else {
            Log.e(TAG,
                    "Listener Object Reference is NULL now in SuccessResponse (Assuming Activity is destroyed)");
        }
    }

    public void onErrorResponseProxy(final int requestId, final int status, final Object data) {
        Log.d(TAG, "onErrorResponseProxy Called");
        // Weak Reference NULL is Included to avoid Null Pointer Crash
        if (null != mActivityReference && null != mActivityReference.get()) {
            Activity activity = mActivityReference.get();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    onErrorResponse(requestId, status, data);
                }
            });
            activity = null;
            // Weak Reference NULL is Included to avoid Null Pointer Crash
        } else if (null != mHandler && null != mHandler.get()) {
            Handler handler = mHandler.get();
            handler.post(new Runnable() {
                public void run() {
                    onErrorResponse(requestId, status, data);
                }
            });
            handler = null;
        } else {
            Log.e(TAG,
                    "Listener Object Reference is NULL now in ErrorResponse (Assuming Activity is destroyed)");
        }
    }
}
