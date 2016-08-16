package com.example.alinnemes.moviesapp_version10.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alinnemes.moviesapp_version10.R;
import com.example.alinnemes.moviesapp_version10.fragments.FavoritesFragment;
import com.example.alinnemes.moviesapp_version10.fragments.PopularFragment;
import com.example.alinnemes.moviesapp_version10.fragments.NowPlayingFragment;
import com.example.alinnemes.moviesapp_version10.fragments.TopRatedFragment;

import java.util.ArrayList;
import java.util.List;
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
    public static final String MOVIE_OBJECT = "movie_object";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PopularFragment())
//                    .commit();
//        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new PopularFragment(), "POPULAR");
        adapter.addFragment(new TopRatedFragment(), "TOP RATED");
        adapter.addFragment(new NowPlayingFragment(), "NOW PLAYING");
        adapter.addFragment(new FavoritesFragment(), "FAVORITES");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
