package com.example.alinnemes.moviesapp_version10.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.MovieManager;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.TrailerListViewAdapter;
import com.example.alinnemes.moviesapp_version10.Utility.ViewUtility;
import com.example.alinnemes.moviesapp_version10.activities.MainActivity;
import com.example.alinnemes.moviesapp_version10.activities.SettingsActivity;
import com.example.alinnemes.moviesapp_version10.data.MoviesDB;
import com.example.alinnemes.moviesapp_version10.model.Movie;
import com.example.alinnemes.moviesapp_version10.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class DetailFragment extends Fragment implements ProcessListener {

    //views
    private ImageView favoriteMovieIV;
    private TextView movieTitleTV;
    private TextView releaseDateTV;
    private TextView runTimeTV;
    private TextView voteAverageTV;
    private TextView movieOverviewTV;
    private ImageView moviePosterIV;
    private ListView movieTrailersList;
    private ProgressDialog pdLoading;
    private ScrollView layout;
    //data
    private Movie movie;
    private ArrayList<Trailer> trailers;
    private MovieManager movieManager;

    public DetailFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
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
        setRetainInstance(true);
        setHasOptionsMenu(true);

        movieManager = MovieManager.getInstance();
        movieManager.setProcessListener(this);
        pdLoading = new ProgressDialog(getActivity());

        Intent intent = getActivity().getIntent();
        movie = (Movie) intent.getExtras().getSerializable(MainActivity.MOVIE_OBJECT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        movieTitleTV = (TextView) view.findViewById(R.id.movieTitleDETAILVIEW);
        releaseDateTV = (TextView) view.findViewById(R.id.release_dateDETAILVIEW);
        runTimeTV = (TextView) view.findViewById(R.id.runtimeDETAILVIEW);
        voteAverageTV = (TextView) view.findViewById(R.id.voteAverageDETAILVIEW);
        movieOverviewTV = (TextView) view.findViewById(R.id.movieOverviewDETAILVIEW);
        moviePosterIV = (ImageView) view.findViewById(R.id.moviePosterImageViewDETAILVIEW);
        favoriteMovieIV = (ImageView) view.findViewById(R.id.favoriteMovieDETAILVIEW);
        movieTrailersList = (ListView) view.findViewById(R.id.movieTrailersListDETAILVIEW);
        layout = (ScrollView) view.findViewById(R.id.DetailScrollView);


        if (movie.getRuntime() == 0) {
            movieManager.startDetailedMovieTask(getActivity(), movie.getId());
        } else {
            setViews();

        }
        if (movie.getTrailers().size() == 0) {
            movieManager.startTrailersMovieTask(getActivity(), movie.getId());
        } else {
            setTrailers();
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        action_refresh.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
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
        setViews();
        setTrailers();
    }

    @Override
    public void onLoadProgress(String msg) {
        pdLoading.setMessage("\t" + msg);
        pdLoading.show();
    }

    public void setViews() {

        movieTitleTV.setText(movie.getTitle());
        releaseDateTV.setText(movie.getRelease_date());
        voteAverageTV.setText(String.format(Locale.US, "%.2f/10", movie.getVote_average()));
        runTimeTV.setText(String.format(Locale.US, "%dmin", movie.getRuntime()));
        movieOverviewTV.setText(movie.getOverview());
        Picasso.with(getActivity()).load(movie.getPoster_path()).into(moviePosterIV);
        int color = ViewUtility.getDominantColor(((BitmapDrawable) moviePosterIV.getDrawable()).getBitmap());
        if (color > Color.LTGRAY) {
            releaseDateTV.setTextColor(Color.BLACK);
            voteAverageTV.setTextColor(Color.BLACK);
            runTimeTV.setTextColor(Color.BLACK);
            movieOverviewTV.setTextColor(Color.BLACK);
            movieTrailersList.setBackgroundColor(Color.BLACK);
        }
        layout.setBackgroundColor(color);


        if (movie.isFavorite()) {
            Picasso.with(getActivity()).load(R.drawable.favorite_icon).into(favoriteMovieIV);
        } else {
            Picasso.with(getActivity()).load(R.drawable.unfavorite_icon).into(favoriteMovieIV);
        }
    }

    public void setTrailers() {
        trailers = movie.getTrailers();
        TrailerListViewAdapter adapter = new TrailerListViewAdapter(getActivity(), trailers);
        movieTrailersList.setAdapter(adapter);
        ViewUtility.justifyListViewHeightBasedOnChildren(movieTrailersList);

        movieTrailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Trailer trailer = trailers.get(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
            }
        });
    }
}
