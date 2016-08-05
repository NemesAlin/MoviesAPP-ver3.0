package com.example.alinnemes.moviesapp_version10.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.FetchMovieTask;
import com.example.alinnemes.moviesapp_version10.fragments.DetailFragment;
import com.example.alinnemes.moviesapp_version10.fragments.MovieListFragment;
import com.example.alinnemes.moviesapp_version10.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL_QUERTY = "detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = this.getIntent();
        Movie movie = (Movie) intent.getExtras().getSerializable(MainActivity.MOVIE_OBJECT);
        new FetchMovieTask(this).execute(String.valueOf(movie.getId()),MOVIE_DETAIL_QUERTY);
        if (savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(MOVIE_DETAIL_QUERTY,movie.getId());
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, detailFragment)
                    .commit();
        }
    }
}
