package com.example.alinnemes.moviesapp_version10.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.model.review.Review;
import com.example.alinnemes.moviesapp_version10.utilities.DataUtilityClass;

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
 * Created by alin.nemes on 19-Aug-16.
 */
public class ListReviewsMovieFromDBTask extends AsyncTask<Long, Void, Movie> {

    private Context mContext;
    private MovieManager movieManager;

    public ListReviewsMovieFromDBTask(MovieManager movieManager) {
        this.mContext = MoviesApp.getContext();
        this.movieManager = movieManager;
    }

    @Override
    protected Movie doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        Movie movie = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonSTRING;

        try {
            //http://api.themoviedb.org/3/movie/id?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/videos?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/reviews?api_key = {MY_API_KEY}
            Uri.Builder builtUri = Uri.parse(MovieManager.API_BASE_URL).buildUpon()
                    .appendPath(params[0].toString());
            if (DataUtilityClass.isNumeric(params[0].toString())) {
                builtUri.appendPath("reviews");
            }
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
            if (DataUtilityClass.isNumeric(params[0].toString())) {
                movie = getReviewsDataFromJsonMovie(moviesJsonSTRING, params[0]);
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

    private Movie getReviewsDataFromJsonMovie(String moviesJsonSTRING, Long params) throws JSONException {
        final String OWN_ID = "id";
        final String OWN_AUTHOR = "author";
        final String OWN_CONTENT = "content";

        MoviesDB moviesDB = new MoviesDB(mContext);
        moviesDB.open();
        Review review;

        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);
        JSONArray moviesResultsArray = moviesJson.getJSONArray("results");

        for (int i = 0; i < moviesResultsArray.length(); i++) {

            final String id;
            final String author;
            final String content;

            JSONObject movieJSONObject = moviesResultsArray.getJSONObject(i);

            id = movieJSONObject.getString(OWN_ID);
            author = movieJSONObject.getString(OWN_AUTHOR);
            content = movieJSONObject.getString(OWN_CONTENT);

            review = moviesDB.getReview(id);

            if (review == null) {//review doesn't exist, create it!
                moviesDB.createReview(id, params, author, content);
            } else if (!author.equals(review.getAuthor()) || !content.equals(review.getContent())) {
                //review exist,update it if is different from the api key;
                moviesDB.updateReview(id, author, content);
            }
        }

        Movie movieToReturn = moviesDB.getMovie(params);
        moviesDB.close();
        if (movieToReturn.getReviews().size() == 0) {
            movieToReturn.setReviews(null);
        }
        return movieToReturn;

    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        movieManager.onLoadedDetails(movie);
        movieManager.onLoadEnded();
    }
}