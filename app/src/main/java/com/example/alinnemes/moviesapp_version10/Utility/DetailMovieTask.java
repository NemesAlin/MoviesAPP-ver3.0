package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alin.nemes on 10-Aug-16.
 */
public class DetailMovieTask extends AsyncTask<String, Void, Movie> {

    private Context mContext;
    private MovieManager movieManager;

    public DetailMovieTask(Context mContext, MovieManager movieManager) {
        this.mContext = mContext;
        this.movieManager = movieManager;

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        movieManager.publishProgress();
    }

    @Override
    protected Movie doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        publishProgress();
        Movie movie = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonSTRING;

        try {
            final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String apiKey_PARAM = "api_key";

            //http://api.themoviedb.org/3/movie/id?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/videos?api_key = {MY_API_KEY}
            Uri.Builder builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath(params[0]);
            if (DataUtilityClass.isNumeric(params[0])) {
                if (params.length > 1 && params[1].equals(DetailActivity.MOVIE_TRAILER_QUERY)) {
                    builtUri.appendPath("videos");
                }
            }
            builtUri.appendQueryParameter(apiKey_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonSTRING = buffer.toString();
            if (DataUtilityClass.isNumeric(params[0])) {
                if (params.length > 1 && params[1].equals(DetailActivity.MOVIE_DETAIL_QUERTY)) {
                    movie = getDataFromJsonToUpdateRuntimeForAMovie(moviesJsonSTRING, params[0]);
                } else
                   movie = getDataFromJsonMovieTrailers(moviesJsonSTRING, params[0]);
            }
        } catch (IOException io) {
            Log.e("IOExpection", "Error ", io);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("IOExpection", "Error closing stream", e);
                }
            }
        }
        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        movieManager.setDetailedMovie(movie);
    }

    private Movie getDataFromJsonToUpdateRuntimeForAMovie(String moviesJsonSTRING, String params) throws JSONException {
        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        final String OWN_RUNTIME = "runtime";
        int runtime;

        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);

        runtime = moviesJson.getInt(OWN_RUNTIME);
        Movie movie = moviesDB.getMovie(Long.parseLong(params));
        moviesDB.updateMovie(movie.getId(), runtime, movie.isFavorite());
        Movie movieToReturn = moviesDB.getMovie(Long.parseLong(params));
        moviesDB.close();
        return movieToReturn;
    }

    private Movie getDataFromJsonMovieTrailers(String moviesJsonSTRING, String params) throws JSONException {
        final String OWN_id = "id";
        final String OWN_site = "site";
        final String OWN_key = "key";
        final String OWN_name = "name";

        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        Movie movieToReturn;

        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);
        JSONArray moviesResultsArray = moviesJson.getJSONArray("results");

        for (int i = 0; i < moviesResultsArray.length(); i++) {

            final String id;
            final String site;
            final String key;
            final String name;

            JSONObject movieJSONObject = moviesResultsArray.getJSONObject(i);

            id = movieJSONObject.getString(OWN_id);
            site = movieJSONObject.getString(OWN_site);
            key = movieJSONObject.getString(OWN_key);
            name = movieJSONObject.getString(OWN_name);

            if (moviesDB.getTrailer(id) == null) {
                moviesDB.createTrailer(id, Long.parseLong(params), name, key, site);
            }
        }


        movieToReturn = moviesDB.getMovie(Long.parseLong(params));
        moviesDB.close();
        return movieToReturn;
    }

}
