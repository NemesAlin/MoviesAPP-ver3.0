package com.example.alinnemes.moviesapp_version10.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.model.Movie;
import com.example.alinnemes.moviesapp_version10.model.Trailer;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 02-Aug-16.
 */
public class MoviesDB {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_FAVORITE = "favorite";

    public static final String COLUMN_TRAILER_ID = "_id";
    public static final String COLUMN_TRAILER_MOVIEID = "id_movie";
    public static final String COLUMN_TRAILER_SITE = "site";
    public static final String COLUMN_TRAILER_KEY = "key";
    public static final String COLUMN_TRAILER_NAME = "name";

    private static final String DATABASE_NAME = "moviesapp.db";
    private static final int DATABASE_VERSION = 11;

    private static final String MOVIE_TABLE = "movie";
    private static final String TRAILERS_TABLE = "trailers";

    private static final String CREATE_TRAILERS_TABLE = "CREATE TABLE " + TRAILERS_TABLE + " ( " +
            COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
            COLUMN_TRAILER_MOVIEID + " INTEGER, " +
            COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
            COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
            COLUMN_TRAILER_SITE + " TEXT NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_TRAILER_MOVIEID + ") REFERENCES " + MOVIE_TABLE + "(" + COLUMN_ID + ")" +
            ");";

    private static final String CREATE_TABLE = "CREATE TABLE " + MOVIE_TABLE + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            COLUMN_VOTE_AVERAGE + " REAL, " +
            COLUMN_RUNTIME + " INTEGER, " +
            COLUMN_POPULARITY + " REAL, " +
            COLUMN_FAVORITE + " TEXT NOT NULL " +
            ");";

    private String[] allColumns = {COLUMN_ID, COLUMN_TITLE, COLUMN_OVERVIEW, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH, COLUMN_BACKDROP_PATH, COLUMN_VOTE_AVERAGE, COLUMN_RUNTIME, COLUMN_POPULARITY, COLUMN_FAVORITE};
    private String[] allColumns_Trailer = {COLUMN_TRAILER_ID, COLUMN_TRAILER_MOVIEID, COLUMN_TRAILER_NAME, COLUMN_TRAILER_KEY, COLUMN_TRAILER_SITE};
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    private MyMovieDbHelper myMovieDbHelper;

    public MoviesDB(Context cxt) {
        this.context = cxt;
    }

    public MoviesDB open() throws android.database.SQLException {
        myMovieDbHelper = new MyMovieDbHelper(context);
        sqLiteDatabase = myMovieDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myMovieDbHelper.close();
    }

    public Movie createMovie(long _id, String title, String overview, String release_date, String poster_path, String backdrop_path, double vote_average, int runtime, double popularity, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, _id);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_OVERVIEW, overview);
        values.put(COLUMN_RELEASE_DATE, release_date);
        values.put(COLUMN_POSTER_PATH, poster_path);
        values.put(COLUMN_BACKDROP_PATH,backdrop_path);
        values.put(COLUMN_VOTE_AVERAGE, vote_average);
        values.put(COLUMN_RUNTIME, runtime);
        values.put(COLUMN_POPULARITY, popularity);
        values.put(COLUMN_FAVORITE, String.valueOf(favorite));

        long insertId = sqLiteDatabase.insert(MOVIE_TABLE, null, values);

        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();
        return newMovie;
    }

    public void createTrailer(String id, long idMovie, String name, String key, String site) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRAILER_ID, id);
        values.put(COLUMN_TRAILER_MOVIEID, idMovie);
        values.put(COLUMN_TRAILER_NAME, name);
        values.put(COLUMN_TRAILER_KEY, key);
        values.put(COLUMN_TRAILER_SITE, site);

        sqLiteDatabase.insert(TRAILERS_TABLE, null, values);
    }

    public ArrayList<Movie> getPopularMovies() {
        ArrayList<Movie> allPopularMovies = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, null, null, null, null, COLUMN_POPULARITY + " DESC ");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            allPopularMovies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return allPopularMovies;

    }

    public ArrayList<Movie> getTopRatedMovies() {
        ArrayList<Movie> allTopRatedMovies = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, null, null, null, null, COLUMN_VOTE_AVERAGE + " DESC ");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            allTopRatedMovies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return allTopRatedMovies;

    }

    public long updateMovie(long idToUpdate, String newTitle, String newOverview, String newReleaseDate, String newPosterPath, String newBackdropPath, double newVoteAverage, int newRunTime, double newPopularity, boolean newFavorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_OVERVIEW, newOverview);
        values.put(COLUMN_RELEASE_DATE, newReleaseDate);
        values.put(COLUMN_POSTER_PATH, newPosterPath);
        values.put(COLUMN_BACKDROP_PATH,newBackdropPath);
        values.put(COLUMN_VOTE_AVERAGE, newVoteAverage);
        values.put(COLUMN_RUNTIME, newRunTime);
        values.put(COLUMN_POPULARITY, newPopularity);
        values.put(COLUMN_FAVORITE, String.valueOf(newFavorite));

        return sqLiteDatabase.update(MOVIE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public long updateTrailer(String idToUpdate, String name, String key, String site) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRAILER_NAME, name);
        values.put(COLUMN_TRAILER_KEY, key);
        values.put(COLUMN_TRAILER_SITE, site);

        return sqLiteDatabase.update(TRAILERS_TABLE, values, COLUMN_TRAILER_ID + " = " + idToUpdate, null);
    }

    public long deleteMovie(long idToDelete) {
        return sqLiteDatabase.delete(MOVIE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> allMovies = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            allMovies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return allMovies;
    }

    public ArrayList<Trailer> getTrailers(long idMovie) {
        ArrayList<Trailer> allTrailers = new ArrayList<Trailer>();

        Cursor cursor = sqLiteDatabase.query(TRAILERS_TABLE, allColumns_Trailer, COLUMN_TRAILER_MOVIEID + " = " + idMovie, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Trailer trailer = cursorToTrailer(cursor);
            allTrailers.add(trailer);
            cursor.moveToNext();
        }

        cursor.close();
        return allTrailers;
    }

    public ArrayList<Movie> getFavoriteMovies() {
        ArrayList<Movie> allMovies = new ArrayList<Movie>();

        //grab all the favorite movies from dataBase
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_FAVORITE + " = " + "\"" + "true" + "\"", null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            allMovies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return allMovies;
    }

    public Movie getMovie(String title) {
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_TITLE + " = " + "\"" + title + "\"", null, null, null, null);
        cursor.moveToFirst();
        Movie movie = cursorToMovie(cursor);
        cursor.close();
        return movie;
    }

    public Movie getMovie(long idToSearch) {
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_ID + " = " + idToSearch, null, null, null, null);
        cursor.moveToFirst();
        Movie movie = cursorToMovie(cursor);
        cursor.close();
        return movie;
    }

    public Trailer getTrailer(String id) {
        Cursor cursor = sqLiteDatabase.query(TRAILERS_TABLE, allColumns_Trailer, COLUMN_TRAILER_ID + " = " + "\"" + id + "\"", null, null, null, null);
        cursor.moveToFirst();
        Trailer trailer = cursorToTrailer(cursor);
        cursor.close();
        return trailer;
    }

    private Movie cursorToMovie(Cursor cursor) {
        Movie newMovie;
        try {
            newMovie = new Movie(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5), cursor.getDouble(6), cursor.getInt(7), cursor.getDouble(8), Boolean.parseBoolean(cursor.getString(9)), getTrailers(cursor.getLong(0)));
        } catch (Exception e) {
            newMovie = null;
        }
        return newMovie;
    }

    private Trailer cursorToTrailer(Cursor cursor) {
        Trailer trailer;
        try {
            trailer = new Trailer(cursor.getString(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        } catch (Exception e) {
            trailer = null;
        }
        return trailer;
    }

    private static class MyMovieDbHelper extends SQLiteOpenHelper {

        MyMovieDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TRAILERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(MyMovieDbHelper.class.getName(), "Updating database from version: " + oldVersion + " to: " + newVersion + ",witch will destroy the old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRAILERS_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

}
