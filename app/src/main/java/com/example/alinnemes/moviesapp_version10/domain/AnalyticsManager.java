package com.example.alinnemes.moviesapp_version10.domain;

import android.app.Application;
import android.widget.Toast;

/**
 * Created by alin.nemes on 18-Aug-16.
 */
public class AnalyticsManager {

    private Application app;

    public AnalyticsManager(Application app) {
        this.app = app;
    }

    public void registerAppEnter() {
        Toast.makeText(app, "Welcome!", Toast.LENGTH_LONG).show();
    }
}
