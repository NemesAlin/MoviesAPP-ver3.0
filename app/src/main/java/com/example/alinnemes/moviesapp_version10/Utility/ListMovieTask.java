package com.example.alinnemes.moviesapp_version10.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class ListMovieTask extends AsyncTask<String, Void, Void> {

    private Context mContext;
    ProgressDialog pdLoading = new ProgressDialog(mContext);


    public ListMovieTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();

        moviesDB.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pdLoading.dismiss();
    }
}
