package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.manager.MovieManager;

public class NowPlayingFragment extends BaseClassFragment {
    public NowPlayingFragment() {
        super.setParam(MovieManager.LIST_NOW_PLAYING);
    }
}
