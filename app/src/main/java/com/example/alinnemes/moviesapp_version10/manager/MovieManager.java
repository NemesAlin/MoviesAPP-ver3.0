package com.example.alinnemes.moviesapp_version10.manager;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.listeners.ProcessListener;
import com.example.alinnemes.moviesapp_version10.listeners.RefreshListener;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.presenters.DetailPresenter;
import com.example.alinnemes.moviesapp_version10.presenters.MainPresenter;
import com.example.alinnemes.moviesapp_version10.tasks.DetailMovieTask;
import com.example.alinnemes.moviesapp_version10.tasks.FavoriteMovieTask;
import com.example.alinnemes.moviesapp_version10.tasks.FetchMoreMoviesTask;
import com.example.alinnemes.moviesapp_version10.tasks.FetchNowPlayingMoviesTask;
import com.example.alinnemes.moviesapp_version10.tasks.ListMovieFromDBTask;
import com.example.alinnemes.moviesapp_version10.tasks.ListReviewsMovieFromDBTask;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class MovieManager {

    public static final String LIST_FAVORITES = "favorites";
    public static final String LIST_POPULAR = "popular";
    public static final String LIST_TOP_RATED = "top_rated";
    public static final String LIST_NOW_PLAYING = "now_playing";
    public static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String apiKey_PARAM = "api_key";
    public static final String page = "page";

    private ProcessListener processListener;
    private RefreshListener refreshListener;
    private DetailPresenter detailPresenter;
    private MainPresenter mainPresenter;
    private String param;

    public void startListingMovies(String param, int page, boolean fetchFromNetwork) {

        this.param = param;

//        if (fetchFromNetwork) {
//            new FetchMovieTask(this).execute(LIST_POPULAR);
//            new FetchMovieTask(this).execute(LIST_TOP_RATED);
//        }

        switch (param) {
            case LIST_POPULAR:
                startListingMoreMovies(param, page);
                break;
            case LIST_TOP_RATED:
                startListingMoreMovies(param, page);
                    break;
            case LIST_FAVORITES:
                startListingFromDB();
                break;
            case LIST_NOW_PLAYING:
                startListingMoreMovies(param, page);
                break;
        }
    }

    public void startListingNowPlayingMovies(String param, int page) {
        new FetchNowPlayingMoviesTask(this).execute(param, String.valueOf(page));
    }

    public void startListingMoreMovies(String param, int page) {
        new FetchMoreMoviesTask(this).execute(param, String.valueOf(page));
    }

    public void onRequestDetailMovie(long id, String param, String fromDB) {
        new DetailMovieTask(this).execute(String.valueOf(id), param, fromDB);
    }

    public void onRequestMovieReviewsById(long id) {
        new ListReviewsMovieFromDBTask(this).execute(id);
    }

    public void onLoadedListOfMovies(ArrayList<Movie> movies) {
        mainPresenter.onListingMovies(movies);
    }

    public void onLoadedMoreMovies(ArrayList<Movie> moreMovies) {
        mainPresenter.onListingMoreMovies(moreMovies);
    }

    public void startListingFromDB() {
        switch (param) {
            case LIST_FAVORITES:
                new ListMovieFromDBTask(MoviesApp.getContext(), this).execute(LIST_FAVORITES);
                break;
            case LIST_POPULAR:
                new ListMovieFromDBTask(MoviesApp.getContext(), this).execute(LIST_POPULAR);
                break;
            case LIST_TOP_RATED:
                new ListMovieFromDBTask(MoviesApp.getContext(), this).execute(LIST_TOP_RATED);
                break;
        }
    }

    public void onLoadedDetails(Movie movie) {
        detailPresenter.onListingDetails(movie);
    }

    public void setProcessListener(ProcessListener processListener) {
        if (processListener != null)
            this.processListener = processListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        if (refreshListener != null)
            this.refreshListener = refreshListener;
    }

    public void setDetailPresenter(DetailPresenter detailPresenter) {
        if (detailPresenter != null)
            this.detailPresenter = detailPresenter;
    }

    public void setMainPresenter(MainPresenter mainPresenter) {
        if (mainPresenter != null)
            this.mainPresenter = mainPresenter;
    }

    public void updateFavoriteState(Movie movie) {
        new FavoriteMovieTask(this, MoviesApp.getContext(), movie).execute();
    }

    public void onLoadStarted(String msg) {
        if (processListener != null) {
            processListener.onLoadStarted(msg);
        }
    }

    public void onLoadEnded() {
        if (processListener != null)
            processListener.onLoadEnded();
    }

    public void onLoadProgress(Movie movie) {
        if (processListener != null)
            processListener.onLoadProgress(movie);
    }

    public void onMarkedFavoriteMovie(boolean isFavorite) {
        detailPresenter.onMarkedSuccesfull(isFavorite);
    }

    public void onResfreshEnded() {
        refreshListener.endRefresh();
    }

}
