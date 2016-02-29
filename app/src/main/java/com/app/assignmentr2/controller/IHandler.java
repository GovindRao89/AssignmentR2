/**
 *
 */

package com.app.assignmentr2.controller;

/**
 * Created by Govind on 28-02-2016.
 */
public interface IHandler {

    void register();

    void unregister();

    void handleEvent(Object obj);
}
