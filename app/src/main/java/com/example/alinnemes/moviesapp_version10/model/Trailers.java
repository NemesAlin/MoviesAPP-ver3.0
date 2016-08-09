package com.example.alinnemes.moviesapp_version10.model;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class Trailers {

    private String id;
    private String site;
    private String key;
    private String name;

    public Trailers(String id, String site, String key, String name) {
        this.id = id;
        this.site = site;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Trailers{" +
                "id='" + id + '\'' +
                ", site='" + site + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
