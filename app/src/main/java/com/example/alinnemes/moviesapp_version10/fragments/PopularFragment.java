package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.manager.MovieManager;

public class PopularFragment extends BaseClassFragment {

    public PopularFragment() {
        super.setParam(MovieManager.LIST_POPULAR);
    }
}
