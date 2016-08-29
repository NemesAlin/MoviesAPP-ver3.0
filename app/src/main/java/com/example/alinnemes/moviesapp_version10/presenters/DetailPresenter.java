package com.example.alinnemes.moviesapp_version10.presenters;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface DetailPresenter {

    void onListingDetails(Movie movie);

    void onMarkedSuccessful(boolean isFavorite);


}
