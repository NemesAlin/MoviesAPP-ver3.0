package com.example.alinnemes.moviesapp_version10.model.movie;

import com.example.alinnemes.moviesapp_version10.model.review.Review;
import com.example.alinnemes.moviesapp_version10.model.trailer.Trailer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alin.nemes on 01-Aug-16.
 */
public class Movie implements Serializable {
    private long id;
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
    private String backdrop_path;
    private double vote_average;
    private int runtime;
    private double popularity;
    private boolean favorite;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    public Movie(long id, String title, String overview, String release_date, String poster_path, String backdrop_path, double vote_average, int runtime, double popularity, boolean favorite, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.runtime = runtime;
        this.popularity = popularity;
        this.favorite = favorite;
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", vote_average=" + vote_average +
                ", runtime=" + runtime +
                ", popularity=" + popularity +
                ", favorite=" + favorite +
                ", trailers=" + trailers +
                ", reviews=" + reviews +
                '}';
    }

}
