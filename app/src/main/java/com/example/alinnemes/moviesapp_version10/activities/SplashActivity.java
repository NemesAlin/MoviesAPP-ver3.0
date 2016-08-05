package com.example.alinnemes.moviesapp_version10.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.example.alinnemes.moviesapp_version10.Utility.FetchMovieTask;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new FetchMovieTask(this).execute("popular");
        new FetchMovieTask(this).execute("top_rated");
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}