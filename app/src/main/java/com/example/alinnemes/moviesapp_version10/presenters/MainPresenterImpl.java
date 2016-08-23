package com.example.alinnemes.moviesapp_version10.presenters;

import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.listeners.ProcessListener;
import com.example.alinnemes.moviesapp_version10.listeners.RefreshListener;
import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.views.MainView;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public class MainPresenterImpl implements MainPresenter {

    protected MovieManager movieManager;
    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.movieManager = new MovieManager();
        this.mainView = mainView;
        movieManager.setMainPresenter(this);
    }

    public MainPresenterImpl() {
        this.movieManager = new MovieManager();
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        movieManager.setRefreshListener(refreshListener);
    }

    public void setProcessListener(ProcessListener processListener) {
        movieManager.setProcessListener(processListener);
    }


    @Override
    public void onListingMovies(ArrayList<Movie> movies) {
        mainView.listMovies(movies);
    }

    public void onRequestingMoviesList(String param) {
        movieManager.startListingMovies(param, SplashActivity.fetchFromNetwork);
    }

    @Override
    public void onDestroy() {
        movieManager.setProcessListener(null);
        movieManager.setRefreshListener(null);
    }


}
