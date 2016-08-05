package com.example.alinnemes.moviesapp_version10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.FetchMovieTask;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailFragment extends Fragment {

    //views
    private TextView movieTitleTV;
    private TextView releaseDateTV;
    private TextView runTimeTV;
    private TextView voteAverageTV;
    private TextView movieOverviewTV;
    private ImageView moviePosterIV;
    private ImageView favoriteMovieIV;
    //data
    private Movie movie;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        new FetchMovieTask(getActivity()).execute(String.valueOf(idMovie));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        long idMovie;
        Bundle bundle = this.getArguments();
        idMovie = bundle.getLong(DetailActivity.MOVIE_DETAIL_QUERTY, 0);
        MoviesDB moviesDB = new MoviesDB(getActivity());
        moviesDB.open();
        movie = moviesDB.getMovie(idMovie);
        moviesDB.close();

        movieTitleTV = (TextView) view.findViewById(R.id.movieTitleDETAILVIEW);
        releaseDateTV = (TextView) view.findViewById(R.id.release_dateDETAILVIEW);
        runTimeTV = (TextView) view.findViewById(R.id.runtimeDETAILVIEW);
        voteAverageTV = (TextView) view.findViewById(R.id.voteAverageDETAILVIEW);
        movieOverviewTV = (TextView) view.findViewById(R.id.movieOverviewDETAILVIEW);
        moviePosterIV = (ImageView) view.findViewById(R.id.moviePosterImageViewDETAILVIEW);
        favoriteMovieIV = (ImageView) view.findViewById(R.id.favoriteMovieDETAILVIEW);

        movieTitleTV.setText(movie.getTitle());
        releaseDateTV.setText(movie.getRelease_date());
        voteAverageTV.setText(String.format(Locale.US, "%.2f/10", movie.getVote_average()));
        runTimeTV.setText(String.format(Locale.US, "%dmin", movie.getRuntime()));
        movieOverviewTV.setText(movie.getOverview());
        Picasso.with(getActivity()).load(movie.getPoster_path()).into(moviePosterIV);
        if (movie.isFavorite()) {
            Picasso.with(getActivity()).load(R.drawable.favorite_icon).into(favoriteMovieIV);
        } else {
            Picasso.with(getActivity()).load(R.drawable.unfavorite_icon).into(favoriteMovieIV);
        }


        favoriteMovieIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoviesDB moviesDB = new MoviesDB(getActivity());
                moviesDB.open();
                Movie movieToUpdate = moviesDB.getMovie(movie.getTitle());
                if (movieToUpdate.isFavorite()) {
                    Picasso.with(getActivity()).load(R.drawable.unfavorite_icon).into(favoriteMovieIV);
                    moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getRuntime(), false);
                    Toast.makeText(getActivity(), "Marked as unfavorite!", Toast.LENGTH_SHORT).show();
                } else {
                    Picasso.with(getActivity()).load(R.drawable.favorite_icon).into(favoriteMovieIV);
                    moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getRuntime(), true);
                    Toast.makeText(getActivity(), "Marked as favorite!", Toast.LENGTH_SHORT).show();
                }
                moviesDB.close();
            }
        });

        return view;
    }
}
