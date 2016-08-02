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
    private static final int DATABASE_VERSION = 4;

    private static final String MOVIE_TABLE = "movie";

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
        values.put(COLUMN_FAVORITE, favorite);

        long insertId = sqLiteDatabase.insert(MOVIE_TABLE, null, values);

        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Movie newMovie = cursorToMovie(cursor);
        cursor.close();
        return newMovie;
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

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> allMoviews = new ArrayList<Movie>();

        //grall all the information from your dataBase
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie note = cursorToMovie(cursor);
            allMoviews.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return allMoviews;
    }

    public Movie getMovie(String title) {
        Cursor cursor = sqLiteDatabase.query(MOVIE_TABLE, allColumns, COLUMN_TITLE + " = " + "\"" + title + "\"", null, null, null, null);
        cursor.moveToFirst();
        Movie movie = cursorToMovie(cursor);
        cursor.close();
        return movie;
    }

    private Movie cursorToMovie(Cursor cursor) {
        Movie newMovie = null;
        try {
            newMovie = new Movie(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6), Boolean.parseBoolean(cursor.getString(7)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newMovie;
    }

    private static class MyMovieDbHelper extends SQLiteOpenHelper {

        MyMovieDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //create note table
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(MyMovieDbHelper.class.getName(), "Updating database from version: " + oldVersion + " to: " + newVersion + ",witch will destroy the old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

}
