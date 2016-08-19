package com.example.alinnemes.moviesapp_version10.domain;

import com.example.alinnemes.moviesapp_version10.MoviesApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,
        library = true
)

public class DomainModule {

    @Provides
    @Singleton
    public AnalyticsManager provideAnalyticsManager(MoviesApp app) {
        return new AnalyticsManager(app);
    }
}
