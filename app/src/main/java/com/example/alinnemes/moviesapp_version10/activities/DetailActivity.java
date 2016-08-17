package com.example.alinnemes.moviesapp_version10.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL_QUERTY = "detail";
    public static final String MOVIE_TRAILER_QUERY = "video";
    public static final String MOVIE_FROM_DB = "db";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        toolbar = (Toolbar) findViewById(R.id.detailToolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, new DetailFragment())
                    .commit();
        }
    }
}
