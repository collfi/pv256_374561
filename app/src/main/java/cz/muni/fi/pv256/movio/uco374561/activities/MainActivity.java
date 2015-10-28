package cz.muni.fi.pv256.movio.uco374561.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.fragments.DetailFragment;
import cz.muni.fi.pv256.movio.uco374561.fragments.GridFragment;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;

public class MainActivity extends AppCompatActivity implements GridFragment.OnItemSelectedListener {

    private List<Movie> mMovies;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.detail_fragment) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }


        //todo nic nerobit ked neni internet
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
//
//        TextView t = (TextView) findViewById(R.id.text_empty);
//        t.setText((isConnected) ? "No data" : "No connection");
//


        //old
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemSelected(Movie m) {
        if (mTwoPane) {
            DetailFragment detailFragment = DetailFragment.newInstance(m);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment,
            detailFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.grid_fragment,
                    DetailFragment.newInstance(m), null).addToBackStack("detail").commit();
        }



        /*
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().
                findFragmentById(R.id.detail_fragment);
        if (detailFragment == null) {
            // port
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.grid_fragment, DetailFragment.newInstance(m),
                    null).addToBackStack("detail").commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction().replace(R.id.grid_fragment, detailFragment,
                    null).addToBackStack("detail").commit();
            detailFragment.update(m);

        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
