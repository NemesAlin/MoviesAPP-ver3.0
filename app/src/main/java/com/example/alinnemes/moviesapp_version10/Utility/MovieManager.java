package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class MovieManager {

    public static final String LIST_FAVORITES = "favorites";
    public static final String LIST_POPULAR = "popular";
    public static final String LIST_TOP_RATED = "top_rated";
    private static MovieManager ourInstance = new MovieManager();
    private ArrayList<Movie> movies;
    private Movie detailedMovie;
    private ProcessListener processListener;
    private String param;
    private Context context;

    private MovieManager() {
    }

    public static MovieManager getInstance() {
        return ourInstance;
    }

    public void startListingMovies(Context context, String param, boolean fetchFromNetwork) {

        this.param = param;
        this.context = context;

        switch (param) {
            case LIST_POPULAR:
                if (fetchFromNetwork)
                    new FetchMovieTask(context, this).execute(LIST_POPULAR);
                else startListingFromDB();
                break;
            case LIST_TOP_RATED:
                if (fetchFromNetwork)
                    new FetchMovieTask(context, this).execute(LIST_TOP_RATED);
                else startListingFromDB();
                break;
            case LIST_FAVORITES:
                startListingFromDB();
                break;
        }
    }


    public void startDetailedMovieTask(Context mContext, long id) {
        new DetailMovieTask(mContext, this).execute(String.valueOf(id), DetailActivity.MOVIE_DETAIL_QUERTY);
    }

    public void startTrailersMovieTask(Context mContext, long id) {
        new DetailMovieTask(mContext, this).execute(String.valueOf(id), DetailActivity.MOVIE_TRAILER_QUERY);
    }


    public ArrayList<Movie> getMoviesList() {
        return movies;
    }

    public void setMoviesList(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void startListingFromDB() {
        switch (param) {
            case LIST_FAVORITES:
                new ListMovieFromDBTask(context, this).execute(LIST_FAVORITES);
                break;
            case LIST_POPULAR:
                new ListMovieFromDBTask(context, this).execute(LIST_POPULAR);
                break;
            case LIST_TOP_RATED:
                new ListMovieFromDBTask(context, this).execute(LIST_TOP_RATED);
                break;
        }
    }

    public Movie getDetailedMovie() {
        return detailedMovie;
    }

    public void setDetailedMovie(Movie movie) {
        this.detailedMovie = movie;
    }

    public void setProcessListener(ProcessListener processListener) {
        if (processListener != null)
            this.processListener = processListener;
    }

    public void onLoadStarted() {
        if (processListener != null) {
            processListener.onLoadStarted();
        }
    }

    public void onLoadEnded() {
        if (processListener != null)
            processListener.onLoadEnded();
    }

    public void onLoadProgress(String msg) {
        if (processListener != null)
            processListener.onLoadProgress(msg);
    }


}
