package com.example.alinnemes.moviesapp_version10.fragments;


import com.example.alinnemes.moviesapp_version10.manager.MovieManager;

public class FavoritesFragment extends BaseClassFragment {
    public FavoritesFragment() {
        super.setParam(MovieManager.LIST_FAVORITES);
    }
}
