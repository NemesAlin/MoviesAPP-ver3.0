package com.example.alinnemes.moviesapp_version10.fragments;


import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

public class FavoritesFragment extends BaseAbstractFragmentClass {
    public FavoritesFragment() {
        super.setParam(MovieManager.LIST_FAVORITES);
    }

    @Override
    public void loadMoreMovies(ArrayList<Movie> moreMovies) {

    }

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fetchFromNetwork = false;
        requestMovies(param,1);
    }

}
