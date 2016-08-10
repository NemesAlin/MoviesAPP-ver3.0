package com.example.alinnemes.moviesapp_version10.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.MovieManager;
import com.example.alinnemes.moviesapp_version10.Utility.ProcessListener;
import com.example.alinnemes.moviesapp_version10.Utility.TrailerListViewAdapter;
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
    //data
    private Movie movie;
    private ArrayList<Trailer> trailers;
    private long idMovie;
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
        movieManager = null;
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
        idMovie = intent.getExtras().getLong(MainActivity.MOVIE_OBJECT);


        MoviesDB moviesDB = new MoviesDB(getActivity());
        moviesDB.open();
        movie = moviesDB.getMovie(idMovie);
        moviesDB.close();


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

    public void justifyListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 100;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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
    public void onProcessStarted() {
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    public void onProcessEnded() {
        pdLoading.dismiss();
    }

    @Override
    public void onProcessUpdate() {
        pdLoading.setMessage("\tGetting Movie...");
        pdLoading.show();
    }

    @Override
    public void onProcessUpdateFromNetwork() {
        pdLoading.setMessage("\tGetting data from network...");
        pdLoading.show();
    }

    public void setViews() {

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
    }

    public void setTrailers() {
        trailers = movie.getTrailers();
        TrailerListViewAdapter adapter = new TrailerListViewAdapter(getActivity(), trailers);
        movieTrailersList.setAdapter(adapter);
        justifyListViewHeightBasedOnChildren(movieTrailersList);

        movieTrailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Trailer trailer = trailers.get(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
            }
        });
    }
}
