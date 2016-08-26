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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.activities.SplashActivity;
import com.example.alinnemes.moviesapp_version10.adapters.MyRecyclerAdapter;
import com.example.alinnemes.moviesapp_version10.listeners.OnItemClickListener;
import com.example.alinnemes.moviesapp_version10.listeners.ProcessListener;
import com.example.alinnemes.moviesapp_version10.listeners.RefreshListener;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.presenters.MainPresenterImpl;
import com.example.alinnemes.moviesapp_version10.utilities.InternetUtilityClass;
import com.example.alinnemes.moviesapp_version10.views.MainView;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public abstract class BaseAbstractFragmentClass extends Fragment implements ProcessListener, MainView, RefreshListener {

    //Views
    public RecyclerView recyclerView;
    private ImageView informationImageView;
    private TextView informationTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog pdLoading;
    public ProgressBar progressBar;
    //array data
    public String param;
    public MyRecyclerAdapter adapter;
    public ArrayList<Movie> savedMoviesListInstance = new ArrayList<>();

    private MainPresenterImpl mainPresenter = new MainPresenterImpl(this);

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fetchFromNetwork = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
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
        mainPresenter.setProcessListener(this);
        mainPresenter.setRefreshListener(this);


    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menufragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviefragment_item, container, false);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        informationImageView = (ImageView) rootView.findViewById(R.id.noInternetIcon);
        informationTextView = (TextView) rootView.findViewById(R.id.noInternettextView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.footerLoadingProgressBar);
        progressBar.setVisibility(View.GONE);
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setCancelable(false);

        requestMovies(param, 1);

        informationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMovies(param, 1);
            }
        });

        refreshContent();
        return rootView;
    }


    public void requestMovies(String param, int page) {
        if (InternetUtilityClass.isOnline(MoviesApp.getContext())) {
            informationImageView.setVisibility(View.GONE);
            informationTextView.setVisibility(View.GONE);

            mainPresenter.onRequestingMoviesList(param, page);

        } else {
            showInformationToUser(getString(R.string.no_internet_connection), R.drawable.no_internet_connection);
            if (mSwipeRefreshLayout.isRefreshing()) {
                endRefresh();
            }
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
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SplashActivity.fetchFromNetwork = true;
                        requestMovies(param, 1);
                    }
                }, 3000);
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    @Override
    public void listMovies(final ArrayList<Movie> movies) {

        final MyRecyclerAdapter adapter = new MyRecyclerAdapter(movies);
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new ScaleInLeftAnimator());

        recyclerView.setAdapter(adapter);

        if (movies != null) {
            savedMoviesListInstance = movies;
        }
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoviesApp.getContext(), DetailActivity.class);
                intent.putExtra(MainActivity.MOVIE_OBJECT, savedMoviesListInstance.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public abstract void loadMoreMovies(ArrayList<Movie> moreMovies);


    public void onErrorOccurred() {
        showInformationToUser(getString(R.string.no_movies_to_show), R.drawable.sad_face);
    }

    @Override
    public void onLoadStarted(String msg) {
        pdLoading.setMessage(String.format(Locale.US, getString(R.string.progressMsg), msg));
        pdLoading.show();
    }

    @Override
    public void onLoadEnded() {
        pdLoading.dismiss();
    }

    @Override
    public void onLoadProgress(Movie movie) {

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

    @Override
    public void endRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
