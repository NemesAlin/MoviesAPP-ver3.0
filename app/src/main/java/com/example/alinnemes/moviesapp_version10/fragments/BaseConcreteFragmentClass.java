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

    public void loadMoreMovies(ArrayList<Movie> moreMovies) {
        int cursorSize = adapter.getItemCount();
        for (int i = 0; i < savedMoviesListInstance.size(); i++) {
            for (int j = 0; j < moreMovies.size(); j++) {
                if (savedMoviesListInstance.get(i).getId() == moreMovies.get(j).getId()) {
                    moreMovies.remove(j);
                }
            }
        }
        savedMoviesListInstance.addAll(moreMovies);
        adapter.notifyDataSetChanged();
        adapter.notifyItemRangeChanged(cursorSize, savedMoviesListInstance.size() - 1);
    }

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

        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e("SCROLL", "END REACHED!!!, Page: " + page + " , TotalItems: " + totalItemsCount);
                requestMovies(param, page);
            }
        });
    }
}
