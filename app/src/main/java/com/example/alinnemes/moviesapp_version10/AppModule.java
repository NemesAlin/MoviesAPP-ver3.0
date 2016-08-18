package com.example.alinnemes.moviesapp_version10;

import android.app.Application;

import com.example.alinnemes.moviesapp_version10.domain.DomainModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alin.nemes on 18-Aug-16.
 */

@Module(
        injects = {
                App.class
        },
        includes = {
                DomainModule.class
        }
)

public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public Application provideApp(){
        return app;
    }

}
