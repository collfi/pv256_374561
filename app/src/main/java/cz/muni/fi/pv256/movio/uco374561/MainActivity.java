package cz.muni.fi.pv256.movio.uco374561;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.db.Movie;
import cz.muni.fi.pv256.movio.uco374561.fragments.DetailFragment;
import cz.muni.fi.pv256.movio.uco374561.fragments.GridFragment;

public class MainActivity extends AppCompatActivity implements GridFragment.OnItemSelectedListener{

    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        setContentView(R.layout.activity_main);

//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();

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
    public void onItemSelected(Movie m) {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().
                findFragmentById(R.id.detail_fragment);
        if (detailFragment == null) {
            // port
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.grid_fragment, DetailFragment.newInstance(m),
                    null).addToBackStack("detail").commit();
        } else {
            detailFragment.update(m);

        }
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
