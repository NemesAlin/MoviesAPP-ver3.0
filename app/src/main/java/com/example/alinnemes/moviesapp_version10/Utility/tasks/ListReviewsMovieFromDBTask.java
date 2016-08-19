package com.example.alinnemes.moviesapp_version10.Utility.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.review.Review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public class ListReviewsMovieFromDBTask extends AsyncTask<Long, Void, ArrayList<Review>> {

    private Context mContext;
    private MovieManager movieManager;

    public ListReviewsMovieFromDBTask(Context mContext, MovieManager movieManager) {
        this.mContext = mContext;
        this.movieManager = movieManager;
    }

    @Override
    protected ArrayList<Review> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        ArrayList<Review> reviews;
        reviews = moviesDB.getReviews(params[0]);
        moviesDB.close();
        return reviews;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        movieManager.setReviews(reviews);
        movieManager.onLoadEnded();
    }
}