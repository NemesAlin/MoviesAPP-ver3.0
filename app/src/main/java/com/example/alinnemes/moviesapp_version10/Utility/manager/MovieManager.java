package com.example.alinnemes.moviesapp_version10.Utility.manager;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.tasks.DetailMovieTask;
import com.example.alinnemes.moviesapp_version10.Utility.tasks.FetchMovieTask;
import com.example.alinnemes.moviesapp_version10.Utility.tasks.ListMovieFromDBTask;
import com.example.alinnemes.moviesapp_version10.Utility.tasks.ListReviewsMovieFromDBTask;
import com.example.alinnemes.moviesapp_version10.Utility.tasks.NowPlayingMoviesTask;
import com.example.alinnemes.moviesapp_version10.fragments.DetailFragment;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.model.review.Review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class MovieManager {

    public static final String LIST_FAVORITES = "favorites";
    public static final String LIST_POPULAR = "popular";
    public static final String LIST_TOP_RATED = "top_rated";
    public static final String LIST_NOW_PLAYING = "now_playing";
    private static MovieManager ourInstance = new MovieManager();
    private ArrayList<Movie> movies;
    private Movie detailedMovie;
    private ProcessListener processListener;
    private String param;
    private Context context;

    public MovieManager() {
    }

    public static MovieManager getInstance() {
        return ourInstance;
    }

    public void startListingMovies(Context context, String param, boolean fetchFromNetwork) {

        this.param = param;
        this.context = context;

        if (fetchFromNetwork) {
            new FetchMovieTask(context, this).execute(LIST_POPULAR);
            new FetchMovieTask(context, this).execute(LIST_TOP_RATED);
        }

        switch (param) {
            case LIST_POPULAR:
                if (!fetchFromNetwork)
                    startListingFromDB();
                break;
            case LIST_TOP_RATED:
                if (!fetchFromNetwork)
                    startListingFromDB();
                break;
            case LIST_FAVORITES:
                startListingFromDB();
                break;
            case LIST_NOW_PLAYING:
                startListingNowPlayingMovies(context, param);
                break;
        }
    }

    public void startListingNowPlayingMovies(Context context, String param) {
        new NowPlayingMoviesTask(context, this).execute(param);
    }

    public void startDetailedMovieTask(Context mContext, long id, String param, String fromDB) {
        new DetailMovieTask(mContext, this).execute(String.valueOf(id), param, fromDB);
    }

    public void startListingMovieReviewsById(Context context, long id) {
        new ListReviewsMovieFromDBTask(context, this).execute(id);
    }

    public void setReviews(ArrayList<Review> reviews) {
        if (processListener instanceof DetailFragment) {
            ((DetailFragment) processListener).setReviews(reviews);
        }
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
