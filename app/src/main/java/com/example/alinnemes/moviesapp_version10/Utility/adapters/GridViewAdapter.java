package com.example.alinnemes.moviesapp_version10.Utility.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 01-Aug-16.
 */
public class GridViewAdapter extends ArrayAdapter<Movie> {

    ArrayList<Movie> movies;
    private Context ctx;

    public GridViewAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
        this.ctx = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            //instance the newViewHolder to hold the references
            holder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.moviefragment_item_list, parent, false);
            holder.moviePoster = (ImageView) convertView.findViewById(R.id.posterImageView);

            //set tag to remember in holder the references for the widgets :)
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //fill each new reference view with concrete data
        Picasso.with(ctx).load(movie.getPoster_path()).into(holder.moviePoster);

        return convertView;
    }

    public static class ViewHolder {
        ImageView moviePoster;
    }
}
