package com.example.alinnemes.moviesapp_version10.Utility;

import android.content.Context;

import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class MovieManager {

    private Context context;
    private ArrayList<Movie> movies;
    public static final String LIST_FAVORITES = "favorites";
    public static final String LIST_POPULAR = "popular";
    public static final String LIST_TOP_RATED = "top_rated";


    public MovieManager(Context context) {
        this.context = context;
    }

    public void listFavoriteMovies(){
        new ListMovieTask(context).execute(LIST_FAVORITES);
    }

    public void listPopularMovies(){
        new ListMovieTask(context).execute(LIST_POPULAR);
    }
    public void listTopRatedMovies(){
        new ListMovieTask(context).execute(LIST_TOP_RATED);
    }

    public ArrayList<Movie> getFavoriteMovies(){
        return movies;
    }

    public void setFavoriteMovies(ArrayList<Movie> movies){
        this.movies.clear();
        this.movies=movies;
    }
}
