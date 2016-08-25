package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

public class PopularFragment extends BaseConcreteFragmentClass {

    public PopularFragment() {
        super.setParam(MovieManager.LIST_POPULAR);
    }

}
