package com.example.alinnemes.moviesapp_version10.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.alinnemes.moviesapp_version10.Utility.FetchMovieTask;
import com.example.alinnemes.moviesapp_version10.Utility.GridViewAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.UtilityClass;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    //Views
    private GridView gridView;
    private ImageView noInternetimageView;
    private TextView noInternetTextView;
    //array data
    private ArrayList<Movie> movies;
    private String sorting;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sorting = UtilityClass.getPreferredSorting(getActivity());
//        new FetchMovieTask(getActivity()).execute(sorting);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.moviefragment_item, container, false);

        gridView = (GridView) rootview.findViewById(R.id.moviesPosterGridView);
        noInternetimageView = (ImageView) rootview.findViewById(R.id.noInternetIcon);
        noInternetTextView = (TextView) rootview.findViewById(R.id.noInternettextView);

        listMovies();

        noInternetimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMovies();
            }
        });


        return rootview;
    }

    public void listMovies() {
        if (UtilityClass.isOnline(getActivity())) {
            noInternetimageView.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.GONE);

            boolean favorite = UtilityClass.getPreferredFavorite(getActivity());

            MoviesDB moviesDB = new MoviesDB(getActivity());
            moviesDB.open();
            if (favorite) {
                movies = moviesDB.getFavoriteMovies();
            } else if (sorting.equals(getString(R.string.pref_sorting_default))) {
                movies = moviesDB.getPopularMovies();
            } else {
                movies = moviesDB.getTopRatedMovies();
            }
            moviesDB.close();
            GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), movies);
            gridViewAdapter.notifyDataSetChanged();
            gridView.setAdapter(gridViewAdapter);
        } else {
            noInternetimageView.setVisibility(View.VISIBLE);
            noInternetTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MoviesDB moviesDB = new MoviesDB(getActivity());
                        moviesDB.open();
                Movie movieToIntent = movies.get(position);
                movieToIntent = moviesDB.getMovie(movieToIntent.getTitle());
//                new FetchMovieTask(getActivity()).execute(String.valueOf(movieToIntent.getId()),DetailActivity.MOVIE_DETAIL_QUERTY);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(MainActivity.MOVIE_OBJECT,movieToIntent);
                startActivity(intent);
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
                moviesDB.close();
            }
        });
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

}
