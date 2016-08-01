package com.example.alinnemes.moviesapp_version10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.GridViewAdapter;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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

        GridView gridView = (GridView) rootview.findViewById(R.id.moviesPosterGridView);
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/6FxOPJ9Ysilpq0IgkrMJ7PubFhq.jpg", 8.4, 16.688744));
        movies.add(new Movie(157336, "Interstellar", "Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.", "2014-11-05", "http://image.tmdb.org/t/p/w342/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/811DjJTon9gD6hZ8nCjSitaIXFQ.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/lFSSLTlFozwpaGlO31OoUeirBgQ.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/inVq3FRqcYIRl2la8iZikYYxFNR.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/bIXbMvEKhlLnhdXttTf2ZKvLZEP.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/kqjL17yufvn9OVLyXYpvtyrFfak.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/5N20rQURev5CNDcMjHVUZhpoCNC.jpg", 8.4, 16.688744));
        movies.add(new Movie(3,"d","d","d","http://image.tmdb.org/t/p/w342/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 8.4, 16.688744));

        gridView.setAdapter(new GridViewAdapter(getContext(), movies));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rootview;
    }
}
