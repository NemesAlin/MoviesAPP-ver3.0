package com.example.alinnemes.moviesapp_version10.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.Utility.UtilityClass;
import com.example.alinnemes.moviesapp_version10.fragments.MovieListFragment;

/*TODO: Your app will:
        ● Upon launch, present the user with an grid arrangement of movie posters.
        ● Allow your user to change sort order via a setting:
            ○ The sort order can be by most popular, or by top rated
        ● Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
            ○ original title
            ○ movie poster image thumbnail
            ○ A plot synopsis (called overview in the api)
            ○ user rating (called vote_average in the api)
            ○ release date
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
