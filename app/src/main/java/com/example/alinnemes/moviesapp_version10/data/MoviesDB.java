package com.example.alinnemes.moviesapp_version10.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 02-Aug-16.
 */
public class MoviesDB {
    private static final String DATABASE_NAME = "moviesapp.db";
    private static final int DATABASE_VERSION = 5;

    private static final String MOVIE_TABLE = "movie";
    private static final String POPULAR_MOVIE_TABLE = "popular";
    private static final String TOPRATED_MOVIE_TABLE = "top_rated";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_FAVORITE = "favorite";
    private static final String CREATE_TABLE = "CREATE TABLE " + MOVIE_TABLE + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            COLUMN_VOTE_AVERAGE + " REAL, " +
            COLUMN_POPULARITY + " REAL, " +
            COLUMN_FAVORITE + " TEXT NOT NULL " +
            ");";
    private static final String CREATE_POPULAR_TABLE = "CREATE TABLE " + POPULAR_MOVIE_TABLE + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            COLUMN_VOTE_AVERAGE + " REAL, " +
            COLUMN_POPULARITY + " REAL, " +
            COLUMN_FAVORITE + " TEXT NOT NULL " +
            ");";
    private static final String CREATE_TOPRATED_TABLE = "CREATE TABLE " + TOPRATED_MOVIE_TABLE + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            COLUMN_VOTE_AVERAGE + " REAL, " +
            COLUMN_POPULARITY + " REAL, " +
            COLUMN_FAVORITE + " TEXT NOT NULL " +
            ");";
    private String[] allColumns = {COLUMN_ID, COLUMN_TITLE, COLUMN_OVERVIEW, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_POPULARITY, COLUMN_FAVORITE};
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

    public Movie createMovie(String title, String overview, String release_date, String poster_path, double vote_average, double popularity, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_OVERVIEW, overview);
        values.put(COLUMN_RELEASE_DATE, release_date);
        values.put(COLUMN_POSTER_PATH, poster_path);
        values.put(COLUMN_VOTE_AVERAGE, vote_average);
        values.put(COLUMN_POPULARITY, popularity);
        values.put(COLUMN_FAVORITE, String.valueOf(favorite));

        long insertId = sqLiteDatabase.insert(MOVIE_TABLE, null, values);

        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();
        return newMovie;
    }

    public Movie createPopularList(String title, String overview, String release_date, String poster_path, double vote_average, double popularity, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_OVERVIEW, overview);
        values.put(COLUMN_RELEASE_DATE, release_date);
        values.put(COLUMN_POSTER_PATH, poster_path);
        values.put(COLUMN_VOTE_AVERAGE, vote_average);
        values.put(COLUMN_POPULARITY, popularity);
        values.put(COLUMN_FAVORITE, favorite);

        long insertId = sqLiteDatabase.insert(POPULAR_MOVIE_TABLE, null, values);

        Cursor cursor = sqLiteDatabase.query(POPULAR_MOVIE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();
        return newMovie;
    }

    public Movie createTopRatedList(String title, String overview, String release_date, String poster_path, double vote_average, double popularity, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_OVERVIEW, overview);
        values.put(COLUMN_RELEASE_DATE, release_date);
        values.put(COLUMN_POSTER_PATH, poster_path);
        values.put(COLUMN_VOTE_AVERAGE, vote_average);
        values.put(COLUMN_POPULARITY, popularity);
        values.put(COLUMN_FAVORITE, favorite);

        long insertId = sqLiteDatabase.insert(TOPRATED_MOVIE_TABLE, null, values);

        Cursor cursor = sqLiteDatabase.query(TOPRATED_MOVIE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();
        return newMovie;
    }

    public ArrayList<Movie> getPopularMovies() {
        ArrayList<Movie> allPopularMoviews = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(POPULAR_MOVIE_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie note = cursorToMovie(cursor);
            allPopularMoviews.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return allPopularMoviews;

    }

    public ArrayList<Movie> getTopRatedMovies() {
        ArrayList<Movie> allTopRatedMoviews = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(TOPRATED_MOVIE_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie note = cursorToMovie(cursor);
            allTopRatedMoviews.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return allTopRatedMoviews;

    }

    public long updateMovie(long idToUpdate, String newTitle, String newOverview, String newReleaseDate, String newPosterPath, double newVoteAverage, double newPopularity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_OVERVIEW, newOverview);
        values.put(COLUMN_RELEASE_DATE, newReleaseDate);
        values.put(COLUMN_POSTER_PATH, newPosterPath);
        values.put(COLUMN_VOTE_AVERAGE, newVoteAverage);
        values.put(COLUMN_POPULARITY, newPopularity);

        return sqLiteDatabase.update(MOVIE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public long deleteMovie(long idToDelete) {
        return sqLiteDatabase.delete(MOVIE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public long deletePopularList() {
        return sqLiteDatabase.delete(POPULAR_MOVIE_TABLE, null, null);
    }

    public long deleteTopRatedList() {
        return sqLiteDatabase.delete(TOPRATED_MOVIE_TABLE, null, null);
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

    public ArrayList<Movie> getFavoriteMovies() {
        ArrayList<Movie> allMovies = new ArrayList<Movie>();

        //grall all the information from your dataBase
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

    private Movie cursorToMovie(Cursor cursor) {
        Movie newMovie;
        try {
            newMovie = new Movie(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6), Boolean.parseBoolean(cursor.getString(7)));
        } catch (Exception e) {
            newMovie = null;
        }
        return newMovie;
    }

    private static class MyMovieDbHelper extends SQLiteOpenHelper {

        MyMovieDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_POPULAR_TABLE);
            sqLiteDatabase.execSQL(CREATE_TOPRATED_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(MyMovieDbHelper.class.getName(), "Updating database from version: " + oldVersion + " to: " + newVersion + ",witch will destroy the old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + POPULAR_MOVIE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TOPRATED_MOVIE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

}
