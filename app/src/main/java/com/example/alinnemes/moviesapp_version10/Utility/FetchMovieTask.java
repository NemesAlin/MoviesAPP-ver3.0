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
import java.util.ArrayList;

/**
 * Created by alin.nemes on 02-Aug-16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final Context mContext;
    private final MovieManager movieManager;

    public FetchMovieTask(Context context, MovieManager movieManager) {
        mContext = context;
        this.movieManager = movieManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        movieManager.onLoadStarted();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

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
            movies = getDataFromJson(moviesJsonSTRING, params[0]);
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

        return movies;
    }

    private ArrayList<Movie> getDataFromJson(String moviesJsonSTRING, String param) throws JSONException {
        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        ArrayList<Movie> movies = new ArrayList<>();

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


            if (moviesDB.getMovie(title) == null) {
                moviesDB.createMovie(id, title, overview, release_date, poster_path, vote_average, 0, popularity, false);
            }

        }

        switch (param) {
            case MovieManager.LIST_POPULAR:
                movies = moviesDB.getPopularMovies();
                break;
            case MovieManager.LIST_TOP_RATED:
                movies = moviesDB.getTopRatedMovies();
                break;
        }
        moviesDB.close();
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        movieManager.onLoadStarted();
        movieManager.startListingFromDB();
    }
}
