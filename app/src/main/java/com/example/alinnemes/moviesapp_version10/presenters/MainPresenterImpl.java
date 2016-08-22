package com.example.alinnemes.moviesapp_version10.presenters;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public class MainPresenterImpl implements MainPresenter {

    protected MovieManager movieManager;

    @Override
    public void creatingNewManager() {
        movieManager = new MovieManager();
    }

    @Override
    public void settingListenerForManager(ProcessListener listener) {
        movieManager.setProcessListener(listener);
    }

    @Override
    public ArrayList<Movie> onLoadedMoviesListFinished() {
        return movieManager.getMoviesList();
    }

    @Override
    public void onRequestingMoviesList(Context context, String param) {
        movieManager.startListingMovies(context, param, SplashActivity.fetchFromNetwork);
    }
}
