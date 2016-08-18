package com.example.alinnemes.moviesapp_version10.fragments;


import android.support.v4.app.Fragment;
import android.view.View;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends SuperClassFragment {

    @Override
    public void listMovies() {
        if (InternetUtilityClass.isOnline(getActivity())) {
            recyclerView.setVisibility(View.VISIBLE);
            informationImageView.setVisibility(View.GONE);
            informationTextView.setVisibility(View.GONE);

            movieManager.startListingMovies(getActivity(), MovieManager.LIST_FAVORITES, SplashActivity.fetchFromNetwork);

        } else {
            showInformationToUser(getString(R.string.no_internet_connection), R.drawable.no_internet_connection);
        }
    }
}
