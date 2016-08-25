package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class NowPlayingFragment extends BaseConcreteFragmentClass {

    public NowPlayingFragment() {
        super.setParam(MovieManager.LIST_NOW_PLAYING);
    }

}
