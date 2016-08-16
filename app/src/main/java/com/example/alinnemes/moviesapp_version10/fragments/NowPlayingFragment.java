package com.example.alinnemes.moviesapp_version10.fragments;

import android.view.View;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class NowPlayingFragment extends SuperClassFragment {

    @Override
    public void listMovies() {
        if (InternetUtilityClass.isOnline(getActivity())) {
            gridView.setVisibility(View.VISIBLE);
            informationImageView.setVisibility(View.GONE);
            informationTextView.setVisibility(View.GONE);

            movieManager.startListingMovies(getActivity(), MovieManager.LIST_NOW_PLAYING, SplashActivity.fetchFromNetwork);

        } else {
            showInformationToUser(getString(R.string.no_internet_connection), R.drawable.no_internet_connection);
        }
    }
}
