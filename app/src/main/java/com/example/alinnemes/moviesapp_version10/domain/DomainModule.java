package com.example.alinnemes.moviesapp_version10.domain;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete =false,
        library = true
)

public class DomainModule {

    @Provides @Singleton
    public AnalyticsManager provideAnalyticsManager(Application app){
        return new AnalyticsManager(app);
    }
}
