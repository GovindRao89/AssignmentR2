/**
 *
 */

package com.app.assignmentr2.controller;


public interface IHandler {

    void register();

    void unregister();

    void handleEvent(Object obj);
}
