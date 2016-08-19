package com.example.alinnemes.moviesapp_version10.model.movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface MoviePresenter {

    ArrayList<Movie> getPopularMovies();

    ArrayList<Movie> getTopRatedMovies();

    ArrayList<Movie> getFavoriteMovies();


}
