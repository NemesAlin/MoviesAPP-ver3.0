package com.example.alinnemes.moviesapp_version10.views;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface MainView {

    void listMovies(ArrayList<Movie> movies);

    void loadMoreMovies(ArrayList<Movie> moreMovies);

    void showInformationToUser(String msg, int img);

    void onErrorOccurred();

}
