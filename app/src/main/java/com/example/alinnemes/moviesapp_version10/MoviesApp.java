package com.example.alinnemes.moviesapp_version10;

import android.app.Application;
import android.content.Context;

import com.example.alinnemes.moviesapp_version10.domain.AnalyticsManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MoviesApp extends Application {

    private static Context context;
    @Inject
    AnalyticsManager analyticsManager;
    private ObjectGraph objectGraph;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);
        analyticsManager.registerAppEnter();
        context = this;
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this));
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }
}
