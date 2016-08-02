package com.example.alinnemes.moviesapp_version10.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.fragments.DetailFragment;
import com.example.alinnemes.moviesapp_version10.fragments.MovieListFragment;

public class DetailActivity extends AppCompatActivity {

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
