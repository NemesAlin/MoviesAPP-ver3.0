package com.example.alinnemes.moviesapp_version10.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.MoviesApp;
import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.trailer.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 22-Aug-16.
 */
public class MyRecyclerAdapterForTrailers extends RecyclerView.Adapter<MyRecyclerAdapterForTrailers.ViewHolder> {

    private Context context;
    private boolean trailerTitleColor = false;
    private ArrayList<Trailer> trailers;

    public MyRecyclerAdapterForTrailers(ArrayList<Trailer> trailers) {
        this.context = MoviesApp.getContext();
        this.trailers = trailers;
    }

    public void setTrailerTitleColorToBlack(boolean trailerTitleColor) {
        this.trailerTitleColor = trailerTitleColor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_listview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        Picasso.with(context).load(R.mipmap.play_icon).into(holder.playButton);
        if (trailerTitleColor) {
            holder.trailerTitle.setTextColor(Color.BLACK);
        }
        holder.trailerTitle.setText(trailer.getName());
        holder.itemView.setTag(trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playButton;
        TextView trailerTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            playButton = (ImageView) itemView.findViewById(R.id.playIconImageView);
            trailerTitle = (TextView) itemView.findViewById(R.id.trailerTitleView);
        }
    }

}