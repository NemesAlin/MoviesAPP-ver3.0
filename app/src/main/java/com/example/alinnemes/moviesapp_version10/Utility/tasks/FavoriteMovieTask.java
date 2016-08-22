package com.example.alinnemes.moviesapp_version10.Utility.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 22-Aug-16.
 */
public class FavoriteMovieTask extends AsyncTask<Void, Void, Integer> {

    private Context context;
    private MovieManager movieManager;
    private Movie movie;

    public FavoriteMovieTask(MovieManager movieManager, Context context, Movie movie) {
        this.movieManager = movieManager;
        this.context = context;
        this.movie = movie;

    }


    @Override
    protected Integer doInBackground(Void... voids) {

       Integer imageToReturnToView;
        MoviesDB moviesDB = new MoviesDB(context);
        moviesDB.open();
        Movie movieToUpdate = moviesDB.getMovie(movie.getTitle());
        if (movieToUpdate.isFavorite()) {
            moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), !movieToUpdate.isFavorite());
            imageToReturnToView = R.drawable.unfavorite_icon;
        } else {
            moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), !movieToUpdate.isFavorite());
            imageToReturnToView = R.drawable.favorite_icon;
        }
        moviesDB.close();

        return imageToReturnToView;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        movieManager.onMarkedFavoriteMovie(integer);
    }
}
