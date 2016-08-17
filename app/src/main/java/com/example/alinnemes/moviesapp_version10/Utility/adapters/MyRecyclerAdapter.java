package com.example.alinnemes.moviesapp_version10.Utility.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    private ArrayList<Movie> movies;
    private Context ctx;

    public MyRecyclerAdapter(ArrayList<Movie> movies, Context ctx) {
        this.movies = movies;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviefragment_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.moviePoster.setImageBitmap(null);
        Picasso.with(ctx).cancelRequest(holder.moviePoster);
        Picasso.with(ctx).load(movie.getPoster_path()).into(holder.moviePoster);
        holder.itemView.setTag(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.posterImageView);
        }
    }

}
