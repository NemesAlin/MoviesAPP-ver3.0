package com.example.alinnemes.moviesapp_version10.presenters;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface MainPresenter {

    void onListingMovies(ArrayList<Movie> movies);

    void onListingMoreMovies(ArrayList<Movie> moreMovies);

    void onDestroy();

}
