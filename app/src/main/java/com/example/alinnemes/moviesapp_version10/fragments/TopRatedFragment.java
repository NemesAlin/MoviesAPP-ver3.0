package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.manager.MovieManager;

public class TopRatedFragment extends BaseConcreteFragmentClass {

    public TopRatedFragment() {
        super.setParam(MovieManager.LIST_TOP_RATED);
    }

}
