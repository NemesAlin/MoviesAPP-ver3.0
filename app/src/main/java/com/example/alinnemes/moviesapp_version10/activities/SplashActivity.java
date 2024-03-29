package com.example.alinnemes.moviesapp_version10.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {
    public static boolean fetchFromNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        fetchFromNetwork = true;
        finish();
    }
}
