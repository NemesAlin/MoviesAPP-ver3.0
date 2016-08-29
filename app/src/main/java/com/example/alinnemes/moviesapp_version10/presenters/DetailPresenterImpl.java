package com.example.alinnemes.moviesapp_version10.presenters;

import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.views.DetailView;

/**
 * Created by alin.nemes on 19-Aug-16.
 */

public class DetailPresenterImpl extends MainPresenterImpl implements DetailPresenter {

    private DetailView detailView;

    public DetailPresenterImpl() {
        super();
        movieManager.setDetailPresenter(this);
    }

    public void setView(DetailView view) {
        detailView = view;
    }

    public void requestDetailMovie(long idMovie, String param1, String param2) {
        movieManager.onRequestDetailMovie(idMovie, param1, param2);
    }

    @Override
    public void onListingDetails(Movie movie) {
        detailView.listDetails(movie);
    }

    public void onIconClickListener(Movie movie) {
        movieManager.updateFavoriteState(movie);
    }

    @Override
    public void onDestroy() {
        detailView = null;
        movieManager.setProcessListener(null);
    }

    @Override
    public void onMarkedSuccessful(boolean isFavorite) {
        detailView.setFavoriteMovieIcon(isFavorite);
    }

}
