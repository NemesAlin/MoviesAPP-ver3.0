package com.example.alinnemes.moviesapp_version10.Utility;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public interface ProcessListener {

    void onProcessStarted();
    void onProcessEnded();
    void onProcessUpdate();
    void onProcessUpdateFromNetwork();
}
