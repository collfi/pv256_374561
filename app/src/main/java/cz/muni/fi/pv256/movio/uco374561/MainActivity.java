package cz.muni.fi.pv256.movio.uco374561;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.db.Movie;

public class MainActivity extends AppCompatActivity {
    private GridView mGrid;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovies = new ArrayList<>(41);
        for (int i = 0; i < 40; i++) {
            Movie m  = new Movie();
            m.setCoverPath("cover");
            m.setTitle("Everest");
            m.setReleaseDate(i);
            mMovies.add(m);
        }
        mGrid = (GridView) findViewById(R.id.movies);
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "EVEREST " + mMovies.get(position).getReleaseDate(), Snackbar.LENGTH_LONG).show();;
                return false;
            }
        });
        mGrid.setAdapter(new MyAdapter(getApplicationContext(), mMovies));

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("release", mMovies.get(position).getReleaseDate());
                i.putExtra("title", mMovies.get(position).getTitle());
                i.putExtra("cover", mMovies.get(position).getCoverPath());
                startActivity(i);
            }
        });
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


        //Just to test the API, will be changed
//        new DownloadNowPlayingImages().execute();
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

//    public class DownloadNowPlayingImages extends AsyncTask<Void, Void, MyAdapter> {
//        @Override
//        protected MyAdapter doInBackground(Void... params) {
//            BufferedReader rd = null;
//            StringBuilder sb = null;
//            String line = null;
//            try {
//                URL url = new URL("http://api.themoviedb.org/3/movie/now_playing?api_key=" + Constants.API_KEY);
//
//                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//                urlConn.setDoInput(true);
//                urlConn.setUseCaches(false);
//                urlConn.setRequestMethod("GET");
//                urlConn.setRequestProperty("Content-Type", "application/json");
//                urlConn.connect();
//
//                rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
//                sb = new StringBuilder();
//
//                line = rd.readLine();
//                sb.append(line);
//            } catch (IOException ioe) {
//                Log.e("http", "url IOException");
//            }
//            String data = sb.toString();
//            try {
//                List<String> list = new ArrayList<>();
//                JSONObject j = new JSONObject(data);
//                JSONArray ja = j.getJSONArray("results");
//                for (int i = 0; i < ja.length(); i++) {
//                    list.add("http://image.tmdb.org/t/p/w154" + ja.getJSONObject(i).getString("poster_path"));
//                }
//                mAdapter = new MyAdapter(getApplicationContext(), list);
//                return mAdapter;
//
//            } catch (JSONException je) {
//                Log.e("adapter", "creating adapter error");
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(MyAdapter adapter) {
//            if (adapter == null) return;
//            mAdapter.notifyDataSetChanged();
//            mGrid.setAdapter(adapter);
//        }
//    }
}
