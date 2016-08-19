package com.example.alinnemes.moviesapp_version10.model.trailer;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 19-Aug-16.
 */
public interface TrailerInteractor {

    void createTrailer(String id, long idMovie, String name, String key, String site);

    long updateTrailer(String idToUpdate, String newName, String newKey, String newSite);

    ArrayList<Trailer> getTrailers(long idMovie);

    Trailer getTrailer(String id);
}
