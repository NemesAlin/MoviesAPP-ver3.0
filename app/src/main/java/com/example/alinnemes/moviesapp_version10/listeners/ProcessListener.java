package com.example.alinnemes.moviesapp_version10.listeners;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public interface ProcessListener {

    void onLoadStarted(String msg);

    void onLoadEnded();

     void onLoadProgress(Movie movie);


}
