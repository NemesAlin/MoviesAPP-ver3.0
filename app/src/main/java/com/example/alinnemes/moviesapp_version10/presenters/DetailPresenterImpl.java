package com.example.alinnemes.moviesapp_version10.presenters;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.views.DetailView;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 19-Aug-16.
 */

public class DetailPresenterImpl extends MainPresenterImpl implements DetailPresenter {

    private DetailView detailView;

    private static DetailPresenterImpl instance;
    public static DetailPresenterImpl getInstance(){
        if (instance == null)
        {
            instance = new DetailPresenterImpl();
        }
        return instance;
    }

    private DetailPresenterImpl() {
    }

    @Override
    public void setView(DetailView view) {
        detailView = view;
    }

    @Override
    public void onRequestDetailMovie(Context context, long idMovie, String param1, String param2) {
        movieManager.startDetailedMovieTask(context, idMovie, param1, param2);
    }

    @Override
    public Movie onLoadedDetailedMovie() {
        return movieManager.getDetailedMovie();
    }

    @Override
    public void onRequestReviewsDetailerMovie(Context context, long idMovie) {
        movieManager.startListingMovieReviewsById(context,idMovie);
    }

    @Override
    public void onPressedFavoriteIcon(Context context, Movie movie) {
        movieManager.favoriteMovie(context,movie);
    }

    public void setFavoriteMovieIV(int imageResFavoriteMovieIV){
        detailView.setFavoriteMovieIV(imageResFavoriteMovieIV);
    }

    @Override
    public void onDestroy() {
        detailView = null;
    }
}
