package com.example.alinnemes.moviesapp_version10.model.review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 18-Aug-16.
 */
public class Review {

    private String id;
    private long id_movie;
    private String author;
    private String content;

    public Review(String id, long id_movie, String author, String content) {
        this.id = id;
        this.id_movie = id_movie;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getId_movie() {
        return id_movie;
    }

    public void setId_movie(long id_movie) {
        this.id_movie = id_movie;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", id_movie=" + id_movie +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static class Reviews {
        ArrayList<Review> reviews;
    }
}
