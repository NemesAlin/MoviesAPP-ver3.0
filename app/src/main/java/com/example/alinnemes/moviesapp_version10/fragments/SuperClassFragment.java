package com.example.alinnemes.moviesapp_version10.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.MyRecyclerAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.RecyclerItemClickListener;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.model.Movie;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class SuperClassFragment extends Fragment implements ProcessListener {

    //Views
    public RecyclerView recyclerView;
    public ImageView informationImageView;
    public TextView informationTextView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ProgressDialog pdLoading;
    //array data
    public ArrayList<Movie> movies;
    public MovieManager movieManager;

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fetchFromNetwork = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieManager.setProcessListener(null);
        SplashActivity.fetchFromNetwork = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieManager = new MovieManager();
        movieManager.setProcessListener(this);

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menuFragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviefragment_item, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
//        gridView = (GridView) rootView.findViewById(R.id.moviesPosterGridView);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        informationImageView = (ImageView) rootView.findViewById(R.id.noInternetIcon);
        informationTextView = (TextView) rootView.findViewById(R.id.noInternettextView);
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setCancelable(false);

        listMovies();

        informationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMovies();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(MainActivity.MOVIE_OBJECT, movies.get(position).getId());
                startActivity(intent);
            }
        }));

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra(MainActivity.MOVIE_OBJECT, movies.get(position).getId());
//                startActivity(intent);
//            }
//        });

        refreshContent();
        return rootView;
    }


    public void listMovies() {
        if (InternetUtilityClass.isOnline(getActivity())) {
//            gridView.setVisibility(View.VISIBLE);
            informationImageView.setVisibility(View.GONE);
            informationTextView.setVisibility(View.GONE);

            movieManager.startListingMovies(getActivity(), MovieManager.LIST_POPULAR, SplashActivity.fetchFromNetwork);

        } else {
            showInformationToUser(getString(R.string.no_internet_connection), R.drawable.no_internet_connection);
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
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.switchColorAccent));
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
//        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), movies);
//        gridViewAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        final MyRecyclerAdapter adapter = new MyRecyclerAdapter(movies, getContext());
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(adapter);

//        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2) ;
//        recyclerView.setLayoutManager(linearLayoutManager);


//         adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<Movie>() {
//                    @Override
//                    public void onItemClick(View view, Movie movie) {
//
//            }
//        });


        if (movies != null) {
            if (movies.size() != 0) {
//                gridView.setAdapter(gridViewAdapter);
            } else
                showInformationToUser(getString(R.string.no_movies_to_show), R.drawable.sad_face);
        } else {
            showInformationToUser(getString(R.string.no_movies_to_show), R.drawable.sad_face);
        }
    }

    @Override
    public void onLoadStarted() {
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    public void onLoadEnded() {
        pdLoading.dismiss();
        movies = movieManager.getMoviesList();
        setGridView();
    }

    @Override
    public void onLoadProgress(String msg) {
        pdLoading.setMessage("\t" + msg);
        pdLoading.show();
    }

    public void showInformationToUser(String msg, int img) {
//        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        informationImageView.setVisibility(View.VISIBLE);
        informationTextView.setVisibility(View.VISIBLE);
        informationImageView.setImageResource(img);
        informationTextView.setText(msg);
    }

}
