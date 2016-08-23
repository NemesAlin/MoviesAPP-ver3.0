package com.example.alinnemes.moviesapp_version10.views;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 22-Aug-16.
 */
public interface DetailView {

    void setFavoriteMovieIcon(boolean isFavorite);

    void listDetails(Movie movie);
}
