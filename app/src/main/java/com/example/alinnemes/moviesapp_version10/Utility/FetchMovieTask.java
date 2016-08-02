package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

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

        String moviesJsonSTRING = null;

        try {
            final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String apiKey_PARAM = "api_key";

            //http://api.themoviedb.org/3/movie/popular?api_key=08d0ea437dc686b11c92b0fefc432d09
            Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(apiKey_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
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
            getDataFromJson(moviesJsonSTRING);
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

    private void getDataFromJson(String moviesJsonSTRING) throws JSONException {


        //movie information
        final String OWN_TITLE = "title";
        final String OWN_OVERVIEW = "overview";
        final String OWN_RELEASEDATE = "release_date";
        final String OWN_POSTER_PATH = "poster_path";
        final String OWN_VOTEAVERAGE = "vote_average";
        final String OWN_POPULARITY = "popularity";


        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);
        JSONArray moviesResultsArray = moviesJson.getJSONArray("results");

//        Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesResultsArray.length());

        for (int i = 0; i < moviesResultsArray.length(); i++) {

            String title;
            String overview;
            String release_date;
            String poster_path;
            double vote_average;
            double popularity;

            JSONObject movieJSONObject = moviesResultsArray.getJSONObject(i);

            title = movieJSONObject.getString(OWN_TITLE);
            overview = movieJSONObject.getString(OWN_OVERVIEW);
            release_date = movieJSONObject.getString(OWN_RELEASEDATE);
            poster_path = String.format("http://image.tmdb.org/t/p/w342%s", movieJSONObject.getString(OWN_POSTER_PATH));
            vote_average = movieJSONObject.getDouble(OWN_VOTEAVERAGE);
            popularity = movieJSONObject.getDouble(OWN_POPULARITY);

//            ContentValues movieValues = new ContentValues();
//
//            movieValues.put(MoviesDB.COLUMN_TITLE, title);
//            movieValues.put(MoviesDB.COLUMN_OVERVIEW, overview);
//            movieValues.put(MoviesDB.COLUMN_RELEASE_DATE, release_date);
//            movieValues.put(MoviesDB.COLUMN_POSTER_PATH, poster_path);
//            movieValues.put(MoviesDB.COLUMN_VOTE_AVERAGE, vote_average);
//            movieValues.put(MoviesDB.COLUMN_POPULARITY, popularity);
//            movieValues.put(MoviesDB.COLUMN_FAVORITE, false);
//
//            cVVector.add(movieValues);
//

            MoviesDB moviesDB = new MoviesDB(mContext);
            moviesDB.open();
            if (moviesDB.getMovie(title) == null) {
                moviesDB.createMovie(title, overview, release_date, poster_path, vote_average, popularity, false);
            }
            moviesDB.close();
        }
    }
}