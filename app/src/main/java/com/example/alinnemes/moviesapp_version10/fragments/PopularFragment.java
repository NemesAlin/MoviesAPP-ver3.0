package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;

public class PopularFragment extends SuperClassFragment {

    public PopularFragment() {
        super.setParam(MovieManager.LIST_POPULAR);
    }
}
