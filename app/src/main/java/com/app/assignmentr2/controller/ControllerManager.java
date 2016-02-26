
package com.app.assignmentr2.controller;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.app.assignmentr2.transport.TransportFactory;
import com.app.assignmentr2.transport.TransportType;
import com.app.assignmentr2.transport.TransportManager;

public class ControllerManager {

    private static final String TAG = ControllerManager.class.getSimpleName();
    private static int requestId = 0;
    private Context mContext;
    private TransportManager mTransportManager = null;
    private ConcurrentHashMap<Integer, ControllerCallback> mListenersMap = new ConcurrentHashMap<Integer, ControllerCallback>();
    private NetworkController mNetworkController = null;
    private MainThread mhThread = null;
    MainHandler mHandler = null;
    private static volatile ControllerManager controllerInstance;

    public static ControllerManager getInstance() {
        if (null == controllerInstance) {
            throw new ExceptionInInitializerError();
        }
        return controllerInstance;
    }

    private void initModules() {
        mTransportManager = (TransportManager) TransportFactory.createTransport(
                TransportType.TYPE_TRANSPORT_VOLLEY, this, mContext);
        mNetworkController = new NetworkController(this);
    }

    // Should be called only Once
    public static ControllerManager createInstance(Context context) {
        if (null != controllerInstance) {
            throw new ExceptionInInitializerError();
        }

        synchronized (ControllerManager.class) {
            if (null != controllerInstance) {
                throw new ExceptionInInitializerError();
            }
            controllerInstance = new ControllerManager(context);
            return controllerInstance;
        }
    }

    private ControllerManager(Context context) {
        mContext = context;
        initialize();
        initModules();
    }


    public synchronized int getNextRequestId() {
        return ++requestId;
    }

    /**
     * Adds a listener to the {@link #mListenersMap} so that callback can be
     * sent properly to the right listener.
     * @param requestId against which listener needs to be added into the
     *            {@link #mListenersMap}.
     * @param listener which needs to be added into the {@link #mListenersMap}.
     */
    public void addListener(int requestId, ControllerCallback listener) {
        if (listener == null) {
            throw new NullPointerException("Listener should not be NULL");
        }

        synchronized (mListenersMap) {
            if ((mListenersMap != null) && (mListenersMap.get(requestId) != null)) {
                mListenersMap.remove(requestId);
            }
            mListenersMap.put(requestId, listener);
        }
    }

    /**
     * Removes a listener to the {@link #mListenersMap} so that callback will
     * not be recieved by listener as now he don't wants to.
     * @param requestId against which listener needs to be removed from the
     *            {@link #mListenersMap}.
     */
    public void removeListener(int requestId) {
        synchronized (mListenersMap) {
            if (mListenersMap != null && mListenersMap.get(requestId) != null) {
                // Indirectly Asking GC to collect callbackListener as it is of
                // no use now
                ControllerCallback callback = mListenersMap.remove(requestId);
                callback = null;
            }
        }
    }

    public ControllerCallback getControllerCallback(int requestId) {
        ControllerCallback callback = null;
        synchronized (mListenersMap) {
            if (mListenersMap != null) {
                callback = mListenersMap.get(requestId);
            }
        }
        // LOG should be shown if Callback is Null
        if (null == callback)
            Log.e(TAG, "ControllerCallback is NULL");
        return callback;
    }

    public int cancelRequest(int requestId) {
        removeListener(requestId);
        mTransportManager.cancelPendingRequests(requestId);
        return 0;
    }

    public TransportManager getTransportManager() {
        return mTransportManager;
    }

    public NetworkController getNetworkController() {
        return mNetworkController;
    }

    /**
     * Initialize core manager.
     */
    private void initialize() {
        Log.d(TAG, "Initialization started ..........");
        mhThread = new MainThread("Parental Control Processor");
        mhThread.start();
        mHandler = new MainHandler(this, mhThread.getLooper());
        Log.d(TAG, "Initialization done.");
    }

    private class MainThread extends HandlerThread {
        public MainThread(String name) {
            super(name);
        }
    }

    static class MainHandler extends Handler {

        private final WeakReference<ControllerManager> activityInstance;

        public MainHandler(ControllerManager instance, Looper looper) {
            super(looper);
            activityInstance = new WeakReference<ControllerManager>(instance);
        }

        private SparseArray<IHandler> mHandlerList = new SparseArray<IHandler>();

        /**
         * Registers the unique eventId with handler, In case of duplicate, the
         * app would be crashed
         **/
        void register(int eventId, IHandler value) {
            Log.d(TAG, "Registering Handler as " + eventId);
            if (mHandlerList.indexOfKey(eventId) >= 0) {
                Log.e(TAG, "Event " + eventId
                        + " is already registered. Please use different eventid");
                throw new RuntimeException("Duplicate Entry Found in Handler List");
            }
            mHandlerList.put(eventId, value);
        }

        void unregister(int eventId) {
            Log.d(TAG, "Unregistering Handler " + eventId);
            mHandlerList.remove(eventId);
        }

        /*
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {

            /**
             * Main Event handler
             **/
            Log.d(TAG, "Message received with Event as " + msg.what);
            if (mHandlerList.indexOfKey(msg.what) >= 0) {
                Log.d(TAG, "Handler is registered for this event ->" + msg.what);
                mHandlerList.get(msg.what).handleEvent(msg);
            } else {
                Log.d(TAG, "Handler is NOT registered for this event ->" + msg.what);
                super.handleMessage(msg);

            }
        }
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public MainHandler getMainHandler() {
        return mHandler;
    }

    public void registerHandler(int eventId, IHandler value) {
        mHandler.register(eventId, value);
    }

    public void unregisterHandler(int eventId) {
        mHandler.unregister(eventId);
    }

}
