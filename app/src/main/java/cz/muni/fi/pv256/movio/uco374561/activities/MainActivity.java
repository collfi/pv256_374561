package cz.muni.fi.pv256.movio.uco374561.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.BuildConfig;
import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.fragments.DetailFragment;
import cz.muni.fi.pv256.movio.uco374561.fragments.GridFragment;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements GridFragment.OnItemSelectedListener {

    private List<Movie> mMovies;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BuildConfig.logging) {
            Log.i("Logging", "PAID VERSION");
        }
        if (findViewById(R.id.detail_container) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemSelected(Movie m) {
        if (mTwoPane) {
            DetailFragment detailFragment = DetailFragment.newInstance(m);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,
                    detailFragment).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("movie", m);
            startActivity(intent);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
