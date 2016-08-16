package com.example.alinnemes.moviesapp_version10.Utility.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class ListMovieFromDBTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private Context mContext;
    private MovieManager movieManager;

    public ListMovieFromDBTask(Context mContext, MovieManager movieManager) {
        this.mContext = mContext;
        this.movieManager = movieManager;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();

        ArrayList<Movie> movies = new ArrayList<Movie>();
        if (params[0].equals(MovieManager.LIST_FAVORITES)) {
            movies = moviesDB.getFavoriteMovies();
        }
        if (params[0].equals(MovieManager.LIST_POPULAR)) {
            movies = moviesDB.getPopularMovies();
        }
        if (params[0].equals(MovieManager.LIST_TOP_RATED)) {
            movies = moviesDB.getTopRatedMovies();
        }
        moviesDB.close();
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        movieManager.setMoviesList(movies);
        movieManager.onLoadEnded();
    }
}
