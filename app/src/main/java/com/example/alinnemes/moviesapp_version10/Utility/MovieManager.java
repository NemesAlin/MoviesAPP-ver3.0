package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class MovieManager {

    private ArrayList<Movie> movies;
    private Movie detailedMovie;
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
        if (processListener != null) {
            processListener.onProcessStarted();
        }
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

        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        if(hours*3600 + minutes*60 + seconds < 1800){
            switch (param) {
                case LIST_POPULAR:
                    new FetchMovieTask(context, this).execute(LIST_POPULAR);
                    break;
                case LIST_TOP_RATED:
                    new FetchMovieTask(context, this).execute(LIST_TOP_RATED);
                    break;
                case LIST_FAVORITES:
                    if (processListener != null)
                        processListener.onProcessEnded();
                    break;
            }
        } else {
            if (processListener != null)
                processListener.onProcessEnded();
        }
    }

    public void setDetailedMovie(Movie movie) {
        this.detailedMovie = movie;
        processListener.onProcessEnded();
    }

    public Movie startDetailedMovieTask() {
        return detailedMovie;
    }


    public void publishProgress() {
        if (processListener != null)
            processListener.onProcessUpdate();
    }

    public void publishProgressFromNetwork() {
        if (processListener != null)
            processListener.onProcessUpdateFromNetwork();
    }

    public void setProcessListener(ProcessListener processListener) {
        if (processListener != null)
            this.processListener = processListener;
    }
}
