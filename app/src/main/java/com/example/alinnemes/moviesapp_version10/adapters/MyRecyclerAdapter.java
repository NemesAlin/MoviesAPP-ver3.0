package com.example.alinnemes.moviesapp_version10.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.listeners.OnItemClickListener;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    OnItemClickListener mItemClickListener;
    private ArrayList<Movie> movies;
    private Context ctx;
    protected boolean showLoader;

    public MyRecyclerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
        this.ctx = MoviesApp.getContext();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviefragment_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.movieTextPoster.setText("");
        holder.moviePoster.setImageBitmap(null);
        Picasso.with(ctx).cancelRequest(holder.moviePoster);
        if (!movie.getPoster_path().contains("null")) {
            Picasso.with(ctx).load(movie.getPoster_path()).into(holder.moviePoster);
        } else {
//            Picasso.with(ctx).load(R.drawable.picture_not_yet_available).into(holder.moviePoster);
            holder.movieTextPoster.setText(movie.getTitle());
        }

        holder.itemView.setOnClickListener(holder);
        holder.itemView.setTag(movie);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }



    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;
        public TextView movieTextPoster;
        public ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.posterImageView);
            movieTextPoster = (TextView) itemView.findViewById(R.id.imageViewText);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}
