package com.example.alinnemes.moviesapp_version10.model.movie;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface MovieInteractor {

    Movie createMovie(long _id, String title, String overview, String release_date, String poster_path, String backdrop_path, double vote_average, int runtime, double popularity, boolean favorite);

    long updateMovie(long idToUpdate, String newTitle, String newOverview, String newReleaseDate, String newPosterPath, String newBackdropPath, double newVoteAverage, int newRunTime, double newPopularity, boolean newFavorite);

    Movie getMovie(String title);

    Movie getMovie(long idToSearch);
}
