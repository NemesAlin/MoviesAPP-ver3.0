package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.R;
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
import java.util.Locale;

/**
 * Created by alin.nemes on 02-Aug-16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonSTRING;

        try {
            final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String apiKey_PARAM = "api_key";

            //http://api.themoviedb.org/3/movie/popular?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/videos?api_key = {MY_API_KEY}
            Uri.Builder builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath(params[0]);
            if (isNumeric(params[0])) {
                if (params.length > 1 && !params[1].equals(DetailActivity.MOVIE_DETAIL_QUERTY)) {
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
            if (isNumeric(params[0])) {
                if (params.length > 1 && params[1].equals(DetailActivity.MOVIE_DETAIL_QUERTY)) {
                    getDataFromJsonToUpdateRuntimeForAMovie(moviesJsonSTRING, params[0]);
                } else
                    getDataFromJsonMovieTrailers(moviesJsonSTRING, params[0]);
            } else {
                getDataFromJson(moviesJsonSTRING, params[0]);
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

        return null;
    }

    private void getDataFromJson(String moviesJsonSTRING, String params) throws JSONException {
        MoviesDB moviesDB = new MoviesDB(mContext);

        //movie information
        final String OWN_ID = "id";
        final String OWN_TITLE = "title";
        final String OWN_OVERVIEW = "overview";
        final String OWN_RELEASEDATE = "release_date";
        final String OWN_POSTER_PATH = "poster_path";
        final String OWN_VOTEAVERAGE = "vote_average";
        final String OWN_POPULARITY = "popularity";


        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);
        JSONArray moviesResultsArray = moviesJson.getJSONArray("results");
        //refresh the lists
        moviesDB.open();
        if (params.equals(mContext.getString(R.string.pref_sorting_default))) {
            moviesDB.deletePopularList();
        } else {
            moviesDB.deleteTopRatedList();
        }
        moviesDB.close();

        for (int i = 0; i < moviesResultsArray.length(); i++) {

            long id;
            String title;
            String overview;
            String release_date;
            String poster_path;
            double vote_average;
            double popularity;


            JSONObject movieJSONObject = moviesResultsArray.getJSONObject(i);

            id = movieJSONObject.getLong(OWN_ID);
            title = movieJSONObject.getString(OWN_TITLE);
            overview = movieJSONObject.getString(OWN_OVERVIEW);
            release_date = movieJSONObject.getString(OWN_RELEASEDATE);
            poster_path = String.format("http://image.tmdb.org/t/p/w342%s", movieJSONObject.getString(OWN_POSTER_PATH));
            vote_average = movieJSONObject.getDouble(OWN_VOTEAVERAGE);
            popularity = movieJSONObject.getDouble(OWN_POPULARITY);

            new FetchMovieTask(mContext).execute(String.valueOf(id),DetailActivity.MOVIE_DETAIL_QUERTY);

            moviesDB.open();
            if (moviesDB.getMovie(title) == null) {
                moviesDB.createMovie(id, title, overview, release_date, poster_path, vote_average, 0, popularity, false);
            }
            if (params.equals(mContext.getString(R.string.pref_sorting_default))) {
                moviesDB.createPopularList(id, title, overview, release_date, poster_path, vote_average, 0, popularity, false);
            } else {
                moviesDB.createTopRatedList(id, title, overview, release_date, poster_path, vote_average, 0, popularity, false);
            }
            moviesDB.close();
        }
    }

    private void getDataFromJsonToUpdateRuntimeForAMovie(String moviesJsonSTRING, String params) throws JSONException {
        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        final String OWN_RUNTIME = "runtime";
        int runtime;

        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);

        runtime = moviesJson.getInt(OWN_RUNTIME);
        Movie movie = moviesDB.getMovie(Long.parseLong(params));
        long affected = moviesDB.updateMovie(movie.getId(), runtime, movie.isFavorite());
        Movie newmovie = moviesDB.getMovie(movie.getTitle());
//        runtimeTV.setText(String.format(Locale.US, "%dmin", runtime));
        moviesDB.close();
    }

    private void getDataFromJsonMovieTrailers(String moviesJsonSTRING, String params) throws JSONException {

    }

    public boolean isNumeric(String s) {
        return java.util.regex.Pattern.matches("\\d+", s);
    }

}
