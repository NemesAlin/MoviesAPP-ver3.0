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

import java.util.ArrayList;

/**
 * Created by alin.nemes on 16-Aug-16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEWTYPE_ITEM = 1;
    public static final int VIEWTYPE_LOADER = 2;
    protected boolean showLoader;
    OnItemClickListener mItemClickListener;
    private ArrayList<Movie> movies;
    private Context ctx;

    public MyRecyclerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
        this.ctx = MoviesApp.getContext();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item_layout, parent, false);
            return new LoaderViewHolder(v);
        } else if (viewType == VIEWTYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviefragment_item_list, parent, false);
            return new ItemsViewHolder(v);

        }
        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Loader ViewHolder
        if (holder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) holder;
            if (showLoader) {
                loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressBar.setVisibility(View.GONE);
            }
        } else {
            Movie movie = movies.get(position);
            ItemsViewHolder itemsViewHolder = (ItemsViewHolder) holder;
            itemsViewHolder.movieTextPoster.setText("");
            itemsViewHolder.moviePoster.setImageBitmap(null);
            itemsViewHolder.movieTextPoster.setBackgroundResource(0);
            Picasso.with(ctx).cancelRequest(itemsViewHolder.moviePoster);
            if (!movie.getPoster_path().contains("null")) {
                Picasso.with(ctx).load(movie.getPoster_path()).into(itemsViewHolder.moviePoster);
            } else {
                itemsViewHolder.movieTextPoster.setText(movie.getTitle());
                itemsViewHolder.movieTextPoster.setBackgroundResource(R.drawable.simple_border);
            }

            holder.itemView.setOnClickListener(itemsViewHolder);
            holder.itemView.setTag(movie);
        }
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;
        public TextView movieTextPoster;


        public ItemsViewHolder(View itemView) {
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

    public class LoaderViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar mProgressBar;

        public LoaderViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.footerLoadingProgressBar);
        }
    }

}
