package com.example.alinnemes.moviesapp_version10.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.ViewUtility;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.ReviewListViewAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.adapters.TrailerListViewAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.manager.MovieManager;
import com.example.alinnemes.moviesapp_version10.activities.DetailActivity;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.movie.Movie;
import com.example.alinnemes.moviesapp_version10.model.review.Review;
import com.example.alinnemes.moviesapp_version10.model.trailer.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class DetailFragment extends Fragment implements ProcessListener {

    //views
    private ImageView favoriteMovieIV;
    private TextView releaseDateTV;
    private TextView runTimeTV;
    private TextView voteAverageTV;
    private TextView movieOverviewTV;
    private TextView trailersTV;
    private TextView reviewsTV;
    private ImageView moviePosterIV;
    private ListView movieTrailersList;
    private ListView movieReviewsList;
    private ProgressDialog pdLoading;
    private NestedScrollView layout;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView movieToolbarBackDropIV;
    //data
    private Movie movie;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private MovieManager movieManager;
    private int dominantColor;

    public DetailFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (movie != null) {
            this.onLoadEnded();
        }
        movieManager = MovieManager.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieManager.setProcessListener(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        movieManager = MovieManager.getInstance();
        movieManager.setProcessListener(this);
        pdLoading = new ProgressDialog(getActivity());

        Intent intent = getActivity().getIntent();
        movieManager.startDetailedMovieTask(getActivity(), intent.getExtras().getLong(MainActivity.MOVIE_OBJECT), DetailActivity.MOVIE_DETAIL_QUERTY, DetailActivity.MOVIE_FROM_DB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.detailToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.detailCollapsing_toolbar);
        movieToolbarBackDropIV = (ImageView) view.findViewById(R.id.detailImageViewToolBar);


        releaseDateTV = (TextView) view.findViewById(R.id.release_dateDETAILVIEW);
        runTimeTV = (TextView) view.findViewById(R.id.runtimeDETAILVIEW);
        voteAverageTV = (TextView) view.findViewById(R.id.voteAverageDETAILVIEW);
        movieOverviewTV = (TextView) view.findViewById(R.id.movieOverviewDETAILVIEW);
        moviePosterIV = (ImageView) view.findViewById(R.id.moviePosterImageViewDETAILVIEW);
        favoriteMovieIV = (ImageView) view.findViewById(R.id.favoriteMovieDETAILVIEW);
        trailersTV = (TextView) view.findViewById(R.id.trailersTextView);
        trailersTV.setVisibility(View.GONE);
        reviewsTV = (TextView) view.findViewById(R.id.reviewsTextView);
        reviewsTV.setVisibility(View.GONE);
        movieTrailersList = (ListView) view.findViewById(R.id.movieTrailersListDETAILVIEW);
        movieReviewsList = (ListView) view.findViewById(R.id.movieReviewsListDETAILVIEW);
        layout = (NestedScrollView) view.findViewById(R.id.DetailScrollView);

        favoriteMovieIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoviesDB moviesDB = new MoviesDB(getActivity());
                moviesDB.open();
                Movie movieToUpdate = moviesDB.getMovie(movie.getTitle());
                if (movieToUpdate.isFavorite()) {
                    Picasso.with(getActivity()).load(R.drawable.unfavorite_icon).into(favoriteMovieIV);
                    moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), false);
                    Toast.makeText(getActivity(), "Marked as unfavorite!", Toast.LENGTH_SHORT).show();
                } else {
                    Picasso.with(getActivity()).load(R.drawable.favorite_icon).into(favoriteMovieIV);
                    moviesDB.updateMovie(movieToUpdate.getId(), movieToUpdate.getTitle(), movieToUpdate.getOverview(), movieToUpdate.getRelease_date(), movieToUpdate.getPoster_path(), movieToUpdate.getBackdrop_path(), movieToUpdate.getVote_average(), movieToUpdate.getRuntime(), movieToUpdate.getPopularity(), true);
                    Toast.makeText(getActivity(), "Marked as favorite!", Toast.LENGTH_SHORT).show();
                }
                moviesDB.close();
            }
        });

        return view;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadStarted() {
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    public void onLoadEnded() {
        pdLoading.dismiss();
        this.movie = movieManager.getDetailedMovie();
        if (movie.getRuntime() == 0) {
            movieManager.startDetailedMovieTask(getActivity().getApplicationContext(), movie.getId(), DetailActivity.MOVIE_DETAIL_QUERTY, null);
        }
        if (movie.getRuntime() != 0 && movie.getTrailers() != null && movie.getTrailers().size() == 0) {
            movieManager.startDetailedMovieTask(getActivity().getApplicationContext(), movie.getId(), DetailActivity.MOVIE_TRAILER_QUERY, null);
        }
        if (movie.getTrailers().size() != 0 && movie.getReviews() != null && movie.getReviews().size() == 0) {
            ////STUPID METHOD!!!!!!!!
            movieManager.startListingMovieReviewsById(getActivity().getApplicationContext(), movie.getId());
        }

        if (movie != null) {
            setViews();
        }
        if (movie.getTrailers() != null && movie.getTrailers().size() != 0) {
            setTrailers();
        }

    }

    @Override
    public void onLoadProgress(String msg) {
        pdLoading.setMessage("\t" + msg);
        pdLoading.show();
    }

    public void setViews() {

        collapsingToolbar.setTitle(movie.getTitle());
        releaseDateTV.setText(movie.getRelease_date());
        voteAverageTV.setText(String.format(Locale.US, "%.2f/10", movie.getVote_average()));
        runTimeTV.setText(String.format(Locale.US, "%dmin", movie.getRuntime()));
        movieOverviewTV.setText(movie.getOverview());
        Picasso.with(getActivity()).load(movie.getPoster_path()).into(moviePosterIV);
        Picasso.with(getActivity()).load(movie.getBackdrop_path()).into(movieToolbarBackDropIV);
        if (!movie.getBackdrop_path().contains("null")) {
            collapsingToolbar.setBackgroundColor(Color.BLACK);
        }
        dominantColor = ViewUtility.getDominantColor(((BitmapDrawable) moviePosterIV.getDrawable()).getBitmap());

        if (dominantColor > Color.LTGRAY) {
            releaseDateTV.setTextColor(Color.BLACK);
            voteAverageTV.setTextColor(Color.BLACK);
            runTimeTV.setTextColor(Color.BLACK);
            movieOverviewTV.setTextColor(Color.BLACK);
        }
        layout.setBackgroundColor(dominantColor);


        if (movie.isFavorite()) {
            Picasso.with(getActivity()).load(R.drawable.favorite_icon).into(favoriteMovieIV);
        } else {
            Picasso.with(getActivity()).load(R.drawable.unfavorite_icon).into(favoriteMovieIV);
        }
    }

    public void setTrailers() {
        trailers = movie.getTrailers();
        if (trailers != null && trailers.size() != 0) {
            trailersTV.setVisibility(View.VISIBLE);
            TrailerListViewAdapter adapter = new TrailerListViewAdapter(getActivity(), trailers);
            if (dominantColor > Color.LTGRAY) {
                adapter.setTrailerTitleColorToBlack(true);
                trailersTV.setTextColor(Color.BLACK);
            }
            movieTrailersList.setAdapter(adapter);
            ViewUtility.justifyListViewHeightBasedOnChildren(movieTrailersList);
            movieTrailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Trailer trailer = trailers.get(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(DetailActivity.YOUTUBE_VIDEO_LINK + trailer.getKey())));
                }
            });
        } else {
            trailersTV.setVisibility(View.GONE);
        }
    }

    public void setReviews(ArrayList<Review> reviews) {

        if (reviews != null && reviews.size() != 0) {
            reviewsTV.setVisibility(View.VISIBLE);
            ReviewListViewAdapter adapter = new ReviewListViewAdapter(getActivity(), reviews);
            if (dominantColor > Color.LTGRAY) {
                adapter.setColorToBlack(true);
                reviewsTV.setTextColor(Color.BLACK);
            }
            movieReviewsList.setAdapter(adapter);
            ViewUtility.justifyListViewHeightBasedOnChildren(movieReviewsList);
        } else {
            reviewsTV.setVisibility(View.GONE);
        }
    }
}