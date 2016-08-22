package com.example.alinnemes.moviesapp_version10.presenters;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.views.DetailView;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface DetailPresenter {

    void setView(DetailView view);

    void onRequestDetailMovie(Context context, long idMovie, String param1, String param2);

    Movie onLoadedDetailedMovie();

    void onRequestReviewsDetailerMovie(Context context, long idMovie);

    void onPressedFavoriteIcon(Context context, Movie movie);

    void onDestroy();
}
