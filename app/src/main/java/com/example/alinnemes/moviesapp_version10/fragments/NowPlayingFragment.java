package com.example.alinnemes.moviesapp_version10.fragments;

import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;

public class NowPlayingFragment extends SuperClassFragment {
    public NowPlayingFragment() {
        super.setParam(MovieManager.LIST_NOW_PLAYING);
    }
}
