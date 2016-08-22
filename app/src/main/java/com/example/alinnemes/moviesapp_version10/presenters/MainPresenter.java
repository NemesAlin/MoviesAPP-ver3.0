package com.example.alinnemes.moviesapp_version10.presenters;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface MainPresenter {
    void creatingNewManager();

    void settingListenerForManager(ProcessListener listener);

    ArrayList<Movie> onLoadedMoviesListFinished();

    void onRequestingMoviesList(Context context, String param);

}
