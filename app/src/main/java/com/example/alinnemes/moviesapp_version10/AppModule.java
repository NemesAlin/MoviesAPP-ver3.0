package com.example.alinnemes.moviesapp_version10;

import com.example.alinnemes.moviesapp_version10.domain.DomainModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alin.nemes on 18-Aug-16.
 */

@Module(
        injects = {
                MoviesApp.class
        },
        includes = {
                DomainModule.class
        }
)

public class AppModule {
    private MoviesApp app;

    public AppModule(MoviesApp app) {
        this.app = app;
    }

    @Provides
    public MoviesApp provideApp() {
        return app;
    }

}
