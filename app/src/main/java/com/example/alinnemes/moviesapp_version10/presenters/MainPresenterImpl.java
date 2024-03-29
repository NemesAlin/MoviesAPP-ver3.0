package com.example.alinnemes.moviesapp_version10.presenters;

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
        if (movies != null) {
            if (movies.size() != 0) {
                mainView.listMovies(movies);
            } else
                mainView.onErrorOccurred();
        } else
            mainView.onErrorOccurred();
    }

    @Override
    public void onListingMoreMovies(ArrayList<Movie> moreMovies) {
        mainView.loadMoreMovies(moreMovies);
    }


    public void onRequestingMoviesList(String param, int page) {
        movieManager.startListingMovies(param, page);
    }

    @Override
    public void onDestroy() {
        movieManager.setProcessListener(null);
        movieManager.setRefreshListener(null);
    }


}
