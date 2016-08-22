package com.example.alinnemes.moviesapp_version10.Utility.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.review.Review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 22-Aug-16.
 */
public class MyRecyclerAdapterForReviews extends RecyclerView.Adapter<MyRecyclerAdapterForReviews.ViewHolder> {

    boolean changeColor = false;
    private ArrayList<Review> reviews;
    private Context ctx;

    public MyRecyclerAdapterForReviews(Context ctx, ArrayList<Review> reviews) {
        this.ctx = ctx;
        this.reviews = reviews;
    }

    public void setColorToBlack(boolean changeColor) {
        this.changeColor = changeColor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_listview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);

        if (changeColor) {
            holder.reviewAuthor.setTextColor(Color.BLACK);
            holder.reviewContent.setTextColor(Color.BLACK);
        }
        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
        holder.itemView.setTag(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.reviewAuthor);
            reviewContent = (TextView) itemView.findViewById(R.id.reviewContent);
        }
    }

}