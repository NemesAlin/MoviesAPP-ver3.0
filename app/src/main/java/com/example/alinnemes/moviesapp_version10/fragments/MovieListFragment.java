package com.example.alinnemes.moviesapp_version10.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    //Views
    private GridView gridView;
    private ImageView noInternetimageView;
    private TextView noInternetTextView;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rootview;
    }

    public void listMovies() {
        if (UtilityClass.isOnline(getActivity())) {
            noInternetimageView.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.GONE);

            String sorting = UtilityClass.getPreferredSorting(getActivity());

            new FetchMovieTask(getActivity()).execute(sorting);

            MoviesDB moviesDB = new MoviesDB(getActivity());
            moviesDB.open();
            ArrayList<Movie> movies = moviesDB.getAllMovies();
            moviesDB.close();

            gridView.setAdapter(new GridViewAdapter(getContext(), movies));
        } else {
            noInternetimageView.setVisibility(View.VISIBLE);
            noInternetTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

}
