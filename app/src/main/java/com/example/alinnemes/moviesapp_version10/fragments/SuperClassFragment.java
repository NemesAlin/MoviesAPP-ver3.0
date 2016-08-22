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

import com.example.alinnemes.moviesapp_version10.presenters.MainPresenterImpl;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.utilities.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.MyRecyclerAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.model.RecyclerItemClickListener;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.views.MovieView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class SuperClassFragment extends Fragment implements ProcessListener, MovieView {

    //Views
    public RecyclerView recyclerView;
    public ImageView informationImageView;
    public TextView informationTextView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ProgressDialog pdLoading;
    //array data
    public ArrayList<Movie> movies;
    public String param;
    public MainPresenterImpl mainPresenter = new MainPresenterImpl();

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fetchFromNetwork = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.settingListenerForManager(null);
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
        mainPresenter.creatingNewManager();
        mainPresenter.settingListenerForManager(this);

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menufragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviefragment_item, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        informationImageView = (ImageView) rootView.findViewById(R.id.noInternetIcon);
        informationTextView = (TextView) rootView.findViewById(R.id.noInternettextView);
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setCancelable(false);

        listMovies(param);

        informationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMovies(param);
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

        refreshContent();
        return rootView;
    }


    public void listMovies(String param) {
        if (InternetUtilityClass.isOnline(getActivity())) {
            informationImageView.setVisibility(View.GONE);
            informationTextView.setVisibility(View.GONE);
//            if (movies != null && movies.size()!=0) {
//                this.onLoadEnded();
//            } else
            mainPresenter.onRequestingMoviesList(getActivity().getApplicationContext(), param);

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
                        listMovies(param);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setGridView() {

        final MyRecyclerAdapter adapter = new MyRecyclerAdapter(movies, getContext());
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        if (movies != null) {
            if (movies.size() != 0) {
                recyclerView.setAdapter(adapter);
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
        movies = mainPresenter.onLoadedMoviesListFinished();
        setGridView();
    }

    @Override
    public void onLoadProgress(String msg) {
        pdLoading.setMessage("\t" + msg);
        pdLoading.show();
    }

    public void showInformationToUser(String msg, int img) {
        recyclerView.setVisibility(View.GONE);
        informationImageView.setVisibility(View.VISIBLE);
        informationTextView.setVisibility(View.VISIBLE);
        informationImageView.setImageResource(img);
        informationTextView.setText(msg);
    }

    public void setParam(String param) {
        this.param = param;
    }

}
