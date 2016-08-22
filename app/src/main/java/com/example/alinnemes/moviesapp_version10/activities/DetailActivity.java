package com.example.alinnemes.moviesapp_version10.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL_QUERTY = "detail";
    public static final String MOVIE_TRAILER_QUERY = "videos";
    public static final String MOVIE_REVIEW_QUERY = "reviews";
    public static final String MOVIE_FROM_DB = "db";
    public static final String YOUTUBE_VIDEO_LINK = "http://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, new DetailFragment())
                    .commit();
        }
    }
}
