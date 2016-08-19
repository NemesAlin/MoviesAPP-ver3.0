package com.example.alinnemes.moviesapp_version10.Utility.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.review.Review;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 18-Aug-16.
 */

public class ReviewListViewAdapter extends ArrayAdapter<Review> {

    boolean changeColor = false;
    private Context context;

    public ReviewListViewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
        this.context = context;
    }

    public void setColorToBlack(boolean changeColor) {
        this.changeColor = changeColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            //instance the newViewHolder to hold the references
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.review_item_listview, parent, false);
            holder.reviewAuthor = (TextView) convertView.findViewById(R.id.reviewAutor);
            holder.reviewContent = (TextView) convertView.findViewById(R.id.reviewContent);

            //set tag to remember in holder the references for the widgets :)
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (changeColor) {
            holder.reviewAuthor.setTextColor(Color.BLACK);
            holder.reviewContent.setTextColor(Color.BLACK);
        }
        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
        return convertView;
    }

    public static class ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;
    }
}
