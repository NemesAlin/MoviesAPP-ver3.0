package com.example.alinnemes.moviesapp_version10.Utility.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

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
            //http://api.themoviedb.org/3/movie/popular?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/top_rated?api_key = {MY_API_KEY}
            Uri.Builder builtUri = Uri.parse(MovieManager.API_BASE_URL).buildUpon()
                    .appendPath(params[0]);
            builtUri.appendQueryParameter(MovieManager.apiKey_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
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
        Movie movie;

        //movie information
        final String OWN_ID = "id";
        final String OWN_TITLE = "title";
        final String OWN_OVERVIEW = "overview";
        final String OWN_RELEASEDATE = "release_date";
        final String OWN_POSTER_PATH = "poster_path";
        final String OWN_BACKDROP_PATH = "backdrop_path";
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
            String backdrop_path;
            double vote_average;
            double popularity;


            JSONObject movieJSONObject = moviesResultsArray.getJSONObject(i);

            id = movieJSONObject.getLong(OWN_ID);
            title = movieJSONObject.getString(OWN_TITLE);
            overview = movieJSONObject.getString(OWN_OVERVIEW);
            release_date = movieJSONObject.getString(OWN_RELEASEDATE);
            poster_path = String.format("http://image.tmdb.org/t/p/w342%s", movieJSONObject.getString(OWN_POSTER_PATH));
            backdrop_path = String.format("http://image.tmdb.org/t/p/w500%s", movieJSONObject.getString(OWN_BACKDROP_PATH));
            vote_average = movieJSONObject.getDouble(OWN_VOTEAVERAGE);
            popularity = movieJSONObject.getDouble(OWN_POPULARITY);

            movie = moviesDB.getMovie(title);
            if (movie == null) {//movie do not exist in the personal DB, add it
                moviesDB.createMovie(id, title, overview, release_date, poster_path, backdrop_path, vote_average, -1, popularity, false);
            } else if (!title.equals(movie.getTitle()) || !overview.equals(movie.getOverview()) || !release_date.equals(movie.getRelease_date()) || !poster_path.equals(movie.getPoster_path()) || !backdrop_path.equals(movie.getBackdrop_path()) || vote_average != movie.getVote_average() || popularity != movie.getPopularity()) {
                //if movie exist, but is not similar with the movie getted from the api, update it :)
                moviesDB.updateMovie(id, title, overview, release_date, poster_path, backdrop_path, vote_average, movie.getRuntime(), popularity, movie.isFavorite());
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
