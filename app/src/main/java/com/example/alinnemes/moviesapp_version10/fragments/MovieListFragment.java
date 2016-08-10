package com.example.alinnemes.moviesapp_version10.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.GridViewAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.MovieManager;
import com.example.alinnemes.moviesapp_version10.Utility.PreferenceUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

public class MovieListFragment extends Fragment implements ProcessListener {

    //Views
    private GridView gridView;
    private ImageView noInternetImageView;
    private TextView noInternetTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog pdLoading;
    //array data
    private ArrayList<Movie> movies;
    private String sorting;
    private MovieManager movieManager;

    public MovieListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        movieManager = MovieManager.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieManager = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sorting = PreferenceUtilityClass.getPreferredSorting(getActivity());
        movieManager = MovieManager.getInstance();
        movieManager.setProcessListener(this);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviefragment_item, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        gridView = (GridView) rootView.findViewById(R.id.moviesPosterGridView);
        noInternetImageView = (ImageView) rootView.findViewById(R.id.noInternetIcon);
        noInternetTextView = (TextView) rootView.findViewById(R.id.noInternettextView);
        pdLoading = new ProgressDialog(getActivity());

        listMovies();

        noInternetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMovies();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(MainActivity.MOVIE_OBJECT, movies.get(position).getId());
                startActivity(intent);
            }
        });
        refreshContent();
        return rootView;
    }


    public void listMovies() {
        if (InternetUtilityClass.isOnline(getActivity())) {
            gridView.setVisibility(View.VISIBLE);
            noInternetImageView.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.GONE);

            boolean favorite = PreferenceUtilityClass.getPreferredFavorite(getActivity());


            if (favorite) {
                movieManager.getMovies(getActivity(), MovieManager.LIST_FAVORITES);
            } else if (sorting.equals(getString(R.string.pref_sorting_default))) {
                movieManager.getMovies(getActivity(), MovieManager.LIST_POPULAR);
            } else {
                movieManager.getMovies(getActivity(), MovieManager.LIST_TOP_RATED);
            }


        } else {
            gridView.setVisibility(View.GONE);
            noInternetImageView.setVisibility(View.VISIBLE);
            noInternetTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {
            listMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshContent() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listMovies();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setGridView() {
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), movies);
        gridViewAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public void onProcessStarted() {
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    public void onProcessEnded() {
        pdLoading.dismiss();
        movies = movieManager.getMoviesList();
        setGridView();
    }

    @Override
    public void onProcessUpdate() {
        pdLoading.setMessage("\tGetting Movies...");
        pdLoading.show();
    }

    @Override
    public void onProcessUpdateFromNetwork(){
        pdLoading.setMessage("\tGetting data from network...");
        pdLoading.show();
    }


}
