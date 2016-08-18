package com.example.alinnemes.moviesapp_version10.Utility.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alin.nemes on 05-Aug-16.
 */
public class TrailerListViewAdapter extends ArrayAdapter<Trailer> {

    ArrayList<Trailer> trailers;
    private Context context;
    ViewHolder holder;
    boolean trailerTitleColor = false;

    public TrailerListViewAdapter(Context context, ArrayList<Trailer> trailers) {
        super(context, 0, trailers);
        this.context = context;
        this.trailers = trailers;
    }

    public void setTrailerTitleColorToBlack(boolean trailerTitleColor) {
        this.trailerTitleColor = trailerTitleColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if (convertView == null) {
            //instance the newViewHolder to hold the references
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_detail_item_trailer_list, parent, false);
            holder.playButton = (ImageView) convertView.findViewById(R.id.playIconImageView);
            holder.trailerTitle = (TextView) convertView.findViewById(R.id.trailerTitleView);

            //set tag to remember in holder the references for the widgets :)
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(R.mipmap.play_icon).into(holder.playButton);
        if (trailerTitleColor) {
            holder.trailerTitle.setTextColor(Color.BLACK);
        }
        holder.trailerTitle.setText(trailer.getName());
        return convertView;
    }

    public static class ViewHolder {
        ImageView playButton;
        TextView trailerTitle;
    }
}
