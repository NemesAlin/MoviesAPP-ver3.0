package com.example.alinnemes.moviesapp_version10.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.BuildConfig;
import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.model.review.Review;
import com.example.alinnemes.moviesapp_version10.model.trailer.Trailer;
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
 * Created by alin.nemes on 10-Aug-16.
 */
public class DetailMovieTask extends AsyncTask<String, Movie, Movie> {

    private MovieManager movieManager;
    private MoviesDB moviesDB;

    public DetailMovieTask(MovieManager movieManager) {
        this.moviesDB = new MoviesDB(MoviesApp.getContext());
        this.movieManager = movieManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        movieManager.onLoadStarted(MoviesApp.getContext().getString(R.string.getting_movie_data_msg));
    }

    @Override
    protected Movie doInBackground(String... params) {
        moviesDB.open();

        Movie movie = null;
        if (params.length == 0) {
            return null;
        }
        if (params.length > 1) {
            if (params[2] != null && params[2].equals(DetailActivity.MOVIE_FROM_DB)) {
                movie = getMovieFromDB(Long.parseLong(params[0]));
                publishProgress(movie);
            }
        }

        if (movie != null) {
            if (movie.getRuntime() == -1 || movie.getRuntime() == 0) {
                movie = apiCall(params[0], DetailActivity.MOVIE_DETAIL_QUERTY);
            }
            if (movie.getRuntime() != -1 && movie.getTrailers() != null && movie.getTrailers().size() == 0) {
                movie = apiCall(params[0], DetailActivity.MOVIE_TRAILER_QUERY);
            }
            if (movie.getRuntime() != -1 && movie.getReviews() != null && movie.getReviews().size() == 0) {
                movie = apiCall(params[0], DetailActivity.MOVIE_REVIEW_QUERY);
            }
        }

        moviesDB.close();
        return movie;
    }

    public Movie apiCall(String... params) {
        Movie movie = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonSTRING;

        try {
            //http://api.themoviedb.org/3/movie/id?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/videos?api_key = {MY_API_KEY}
            //http://api.themoviedb.org/3/movie/id/reviews?api_key = {MY_API_KEY}
            Uri.Builder builtUri = Uri.parse(MovieManager.API_BASE_URL).buildUpon()
                    .appendPath(params[0]);
            if (DataUtilityClass.isNumeric(params[0])) {
                if (params.length > 1 && params[1].equals(DetailActivity.MOVIE_TRAILER_QUERY)) {
                    builtUri.appendPath("videos");
                }
                if (params.length > 1 && params[1].equals(DetailActivity.MOVIE_REVIEW_QUERY)) {
                    builtUri.appendPath("reviews");
                }
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
            if (DataUtilityClass.isNumeric(params[0])) {
                if (params.length > 1) {
                    switch (params[1]) {
                        case DetailActivity.MOVIE_DETAIL_QUERTY:
                            movie = getDataFromJsonToUpdateRuntimeForAMovie(moviesJsonSTRING, params[0]);
                            break;
                        case DetailActivity.MOVIE_TRAILER_QUERY:
                            movie = getTrailersDataFromJsonMovie(moviesJsonSTRING, params[0]);
                            break;
                        case DetailActivity.MOVIE_REVIEW_QUERY:
                            movie = getReviewsDataFromJsonMovie(moviesJsonSTRING, Long.parseLong(params[0]));
                            break;
                    }
                }
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

    private Movie getDataFromJsonToUpdateRuntimeForAMovie(String moviesJsonSTRING, String params) throws JSONException {

        final String OWN_RUNTIME = "runtime";
        int runtime;

        JSONObject moviesJson = new JSONObject(moviesJsonSTRING);

        try {
            runtime = moviesJson.getInt(OWN_RUNTIME);
        } catch (JSONException e) {
            runtime = -1;
        }
        Movie movie = moviesDB.getMovie(Long.parseLong(params));
        moviesDB.updateMovie(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getRelease_date(), movie.getPoster_path(), movie.getBackdrop_path(), movie.getVote_average(), runtime, movie.getPopularity(), movie.isFavorite());
        return moviesDB.getMovie(Long.parseLong(params));
    }

    private Movie getTrailersDataFromJsonMovie(String moviesJsonSTRING, String params) throws JSONException {
        final String OWN_id = "id";
        final String OWN_site = "site";
        final String OWN_key = "key";
        final String OWN_name = "name";

        Movie movieToReturn;
        Trailer trailer;

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

            trailer = moviesDB.getTrailer(id);

            if (trailer == null) {//trailer doesn't exist, create it!
                moviesDB.createTrailer(id, Long.parseLong(params), name, key, site);
            } else if (!site.equals(trailer.getSite()) || !name.equals(trailer.getName()) || !key.equals(trailer.getKey())) {
                //trailer exist,update it if is different from the api key;
                moviesDB.updateTrailer(id, name, key, site);
            }
        }


        movieToReturn = moviesDB.getMovie(Long.parseLong(params));
        if (movieToReturn.getTrailers().size() == 0) {
            movieToReturn.setTrailers(null);
        }
        return movieToReturn;
    }

    private Movie getReviewsDataFromJsonMovie(String moviesJsonSTRING, Long params) throws JSONException {
        final String OWN_ID = "id";
        final String OWN_AUTHOR = "author";
        final String OWN_CONTENT = "content";

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
        if (movieToReturn.getReviews().size() == 0) {
            movieToReturn.setReviews(null);
        }
        return movieToReturn;

    }


    public Movie getMovieFromDB(long movieId) {
        return moviesDB.getMovie(movieId);
    }

    @Override
    protected void onProgressUpdate(Movie... movies) {
        movieManager.onLoadProgress(movies[0]);
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        movieManager.onLoadedDetails(movie);
        movieManager.onLoadEnded();
    }

}
