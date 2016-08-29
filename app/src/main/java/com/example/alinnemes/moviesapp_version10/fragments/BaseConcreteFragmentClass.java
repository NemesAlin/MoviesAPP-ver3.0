package com.example.alinnemes.moviesapp_version10.fragments;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.adapters.MyRecyclerAdapter;
import com.example.alinnemes.moviesapp_version10.listeners.EndlessRecyclerViewScrollListener;
import com.example.alinnemes.moviesapp_version10.listeners.OnItemClickListener;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

/**
 * Created by alin.nemes on 24-Aug-16.
 */
public class BaseConcreteFragmentClass extends BaseAbstractFragmentClass {

    @Override
    public void listMovies(final ArrayList<Movie> movies) {

        adapter = new MyRecyclerAdapter(movies);

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

        gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e("SCROLL", "END REACHED!!!, Page: " + page + " , TotalItems: " + totalItemsCount);
                adapter.showLoading(true);
//                progressBar.setVisibility(View.VISIBLE);
                requestMovies(param, page);
            }
        });

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case MyRecyclerAdapter.VIEWTYPE_LOADER:
                        return 2;
                    case MyRecyclerAdapter.VIEWTYPE_ITEM:
                        return 1; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });


    }

    public void loadMoreMovies(ArrayList<Movie> moreMovies) {
//        progressBar.setVisibility(View.GONE);
        adapter.showLoading(false);
        int cursorSize = adapter.getItemCount();
        savedMoviesListInstance.addAll(moreMovies);
        adapter.notifyItemRangeChanged(cursorSize, savedMoviesListInstance.size() - 1);
        adapter.notifyDataSetChanged();
    }

}
