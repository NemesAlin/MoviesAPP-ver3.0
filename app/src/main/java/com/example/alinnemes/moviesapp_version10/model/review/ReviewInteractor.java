package com.example.alinnemes.moviesapp_version10.model.review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface ReviewInteractor {
    void createReview(String id, long idMovie, String author, String content);

    long updateReview(String idToUpdate, String newAuthor, String newContent);

    ArrayList<Review> getReviews(long idMovie);

    Review getReview(String id);
}
