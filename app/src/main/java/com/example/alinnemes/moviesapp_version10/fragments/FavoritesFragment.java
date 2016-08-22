package com.example.alinnemes.moviesapp_version10.fragments;


import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;

public class FavoritesFragment extends SuperClassFragment {
    public FavoritesFragment() {
        super.setParam(MovieManager.LIST_FAVORITES);
    }
}
