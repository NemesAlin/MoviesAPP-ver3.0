package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alinnemes.moviesapp_version10.model.Movie;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class DetailMovieTask extends AsyncTask<String,Void,Movie> {

    private Context mContext;
    private MovieManager movieManager;

    public DetailMovieTask(Context mContext, MovieManager movieManager) {
        this.mContext = mContext;
        this.movieManager = movieManager;

    }

    @Override
    protected Movie doInBackground(String... params) {
        if (params.length == 0){
            return null;
        }




        return null;
    }
}
