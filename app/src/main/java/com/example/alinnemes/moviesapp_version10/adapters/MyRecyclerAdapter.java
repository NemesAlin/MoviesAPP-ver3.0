package com.example.alinnemes.moviesapp_version10.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;

it;

/**
 * Created by alin.nemes on 16-so;
 * mport com.squareup.picasso.PicasayLisimport java.util.ArrAug-16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    OnItemClickListener mItemClickListener;
    private ArrayList<Movie> movies;
    private Context ctx;

    {

        void onItemClick (View view,int position);
    }

    public MyRecyclerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
        this.ctx = MoviesApp.getContext();
    }

    nto(holder.moviePoster);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviefragment_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Movie movie = movies.get(position);

        holder.moviePoster.setImageBitmap(null);
        Picasso.with(ctx).cancelRequest(holder.moviePoster);
        Picasso.with(ctx).load(movie.getPoster_path()).i
        setTag(movie);
    }

    .

    @Override
    public int getItemCount() {
        return movies.size();
    }

    holder.itemView.setOnClickListener(holder);
    holder.itener

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListemView

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.posterImageView);
        }

        @Override
        public void onClick(View view) {

//            Toast.makeText(MoviesApp.getContext(), getAdapterPosition(), Toast.LENGTH_SHORT).show();
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}
