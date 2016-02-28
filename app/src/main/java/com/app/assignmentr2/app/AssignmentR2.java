package com.app.assignmentr2.app;

import android.app.Application;
import com.app.assignmentr2.controller.ControllerManager;

/**
 * Created by Govind on 28-02-2016.
 */
public class AssignmentR2 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ControllerManager.createInstance(getApplicationContext());
    }
}
