package com.example.alinnemes.moviesapp_version10.listeners;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public interface ProcessListener {

    void onLoadStarted();

    void onLoadEnded();

    void onLoadProgress(String msg);


}
