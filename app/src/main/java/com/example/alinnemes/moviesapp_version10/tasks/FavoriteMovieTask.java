package com.example.alinnemes.moviesapp_version10.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

/**
 * Created by alin.nemes on 22-Aug-16.
 */
public class FavoriteMovieTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private MovieManager movieManager;
    private Movie movie;

    public FavoriteMovieTask(MovieManager movieManager, Context context, Movie movie) {
        this.movieManager = movieManager;
        this.context = context;
        this.movie = movie;

    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean isFavorite;
        MoviesDB moviesDB = new MoviesDB(context);
        moviesDB.open();
        Movie movieToUpdate = moviesDB.getMovie(movie.getId());
        if (movieToUpdate.isFavorite()) {
            moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), !movieToUpdate.isFavorite());
            isFavorite = false;
        } else {
            moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), !movieToUpdate.isFavorite());
            isFavorite = true;
        }
        moviesDB.close();

        return isFavorite;
    }

    @Override
    protected void onPostExecute(Boolean isFavorite) {
        super.onPostExecute(isFavorite);
        movieManager.onMarkedFavoriteMovie(isFavorite);
    }
}
