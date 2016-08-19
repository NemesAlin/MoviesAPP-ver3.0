package com.example.alinnemes.moviesapp_version10.model.trailer;

import java.io.Serializable;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class Trailer implements Serializable {

    private String id;
    private long id_movie;
    private String site;
    private String key;
    private String name;

    public Trailer(String id, long id_movie, String name, String key, String site) {
        this.id = id;
        this.id_movie = id_movie;
        this.name = name;
        this.key = key;
        this.site = site;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", id_movie=" + id_movie +
                ", site='" + site + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
