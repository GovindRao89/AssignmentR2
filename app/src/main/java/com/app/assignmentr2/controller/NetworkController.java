package com.app.assignmentr2.controller;

import com.app.assignmentr2.transport.TransportManager;

/**
 * Created by Govind on 26-02-2016.
 */
public class NetworkController {

    private TransportManager mTransportManager;

    public NetworkController(ControllerManager cm){
        mTransportManager = cm.getTransportManager();
    }


}
