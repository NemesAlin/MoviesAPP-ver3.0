package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class MovieManager {

    private ArrayList<Movie> movies;
    public static final String LIST_FAVORITES = "favorites";
    public static final String LIST_POPULAR = "popular";
    public static final String LIST_TOP_RATED = "top_rated";
    private ProcessListener processListener;
    private String param;
    private Context context;

    private static MovieManager ourInstance = new MovieManager();

    public static MovieManager getInstance() {
        return ourInstance;
    }

    private MovieManager() {
    }

    public void getMovies(Context context, String param) {

        this.param = param;
        this.context = context;

        processListener.onProcessStarted();
        switch (param) {
            case LIST_FAVORITES:
                new ListMovieTask(context, this).execute(LIST_FAVORITES);
                break;
            case LIST_POPULAR:
                new ListMovieTask(context, this).execute(LIST_POPULAR);
                break;
            case LIST_TOP_RATED:
                new ListMovieTask(context, this).execute(LIST_TOP_RATED);
                break;
        }


//        new FetchMovieTask(mContext).execute(String.valueOf(id), DetailActivity.MOVIE_DETAIL_QUERTY);
//        new FetchMovieTask(mContext).execute(String.valueOf(id), DetailActivity.MOVIE_TRAILER_QUERY);

    }

    public void getTrailers(){

    }


    public ArrayList<Movie> getMoviesList() {
        return movies;
    }

    public void setMoviesList(ArrayList<Movie> movies) {
        this.movies = movies;

        if (movies.size() == 0) {
            switch (param) {
                case LIST_POPULAR:
                    new FetchMovieTask(context, this).execute(LIST_POPULAR);
                    break;
                case LIST_TOP_RATED:
                    new FetchMovieTask(context, this).execute(LIST_TOP_RATED);
                    break;
                case LIST_FAVORITES:
                    processListener.onProcessEnded();
                    break;
            }
        } else {
            processListener.onProcessEnded();
        }
    }


    public void setProcessListener(ProcessListener processListener) {
        this.processListener = processListener;
    }

    public void publishProgress() {
        processListener.onProcessUpdate();
    }

    public void publishProgressFromNetwork() {
        processListener.onProcessUpdateFromNetwork();
    }
}
