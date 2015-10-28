package cz.muni.fi.pv256.movio.uco374561.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.Constants;
import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.activities.MainActivity;
import cz.muni.fi.pv256.movio.uco374561.adapters.MyAdapter;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;

/**
 * Created by collfi on 25. 10. 2015.
 */
public class GridFragment extends Fragment {
    private StickyGridHeadersGridView mGrid;
    private ArrayList<Movie> mMovies;
    private OnItemSelectedListener l;
    private MyAdapter mAdapter;
    private int mPosition;
    private TextView mEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mMovies = new ArrayList<>();
        } else {
            mMovies = savedInstanceState.getParcelableArrayList("list");
            mPosition = savedInstanceState.getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        mGrid = (StickyGridHeadersGridView) view.findViewById(R.id.movies);
        l = (MainActivity) getActivity();
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, mMovies.get(position).getTitle(), Snackbar.LENGTH_LONG).show();
                return true;
            }
        });

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                l.onItemSelected(mMovies.get(position));
            }
        });
        ViewStub empty = (ViewStub) view.findViewById(android.R.id.empty);
        mEmpty = (TextView) empty.inflate().findViewById(R.id.text_empty);
        mEmpty.setText("Downloading....");
        mGrid.setEmptyView(mEmpty);

        if (isConnected()) {
            if (mMovies.size() == 0) {
//        Just to test the API, will be changed
                new DownloadNowPlayingImages().execute();
            } else {
                mAdapter = new MyAdapter(getActivity(), mMovies);
                mGrid.setAdapter(mAdapter);
                Log.i("QQQ", "---" + mPosition);
                mGrid.setSelection(mPosition);
                mGrid.smoothScrollToPosition(mPosition);
            }
        } else {
            mEmpty.setText("No connection");
            mGrid.setEmptyView(mEmpty);
        }
        return view;
    }


    public class DownloadNowPlayingImages extends AsyncTask<Void, Void, MyAdapter> {
        @Override
        protected MyAdapter doInBackground(Void... params) {
            Log.i("QQQ", "Downloading");
            BufferedReader rd = null;
            StringBuilder sb = null;
            String line = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/now_playing?api_key=" + Constants.API_KEY);

                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.connect();

                rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                sb = new StringBuilder();

                line = rd.readLine();
                sb.append(line);
            } catch (IOException ioe) {
                Log.e("http", "url IOException");
            }
            String data = sb.toString();
            try {
                JSONObject j = new JSONObject(data);
                JSONArray ja = j.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    Movie m = new Movie();
                    m.setTitle(ja.getJSONObject(i).getString("original_title"));
                    m.setOverview(ja.getJSONObject(i).getString("overview"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    m.setReleaseDate(sdf.parse(ja.getJSONObject(i).getString("release_date")).getTime());
                    String path = ja.getJSONObject(i).getString("poster_path");
                    if (path.equals("null")) {
                        path = "drawable://" + R.drawable.everest;
                        m.setCoverPath(path);
                    } else {
                        m.setCoverPath("http://image.tmdb.org/t/p/w342" + path);
                    }
                    path = ja.getJSONObject(i).getString("backdrop_path");
                    if (path.equals("null")) {
                        path = "drawable://" + R.drawable.everest2;
                        m.setPosterPath(path);
                    } else {
                        m.setPosterPath("http://image.tmdb.org/t/p/w1280" + path);
                    }
                    mMovies.add(m);
                }


            } catch (JSONException je) {
                Log.e("adapter", "creating adapter error");
            } catch (ParseException pa) {
                Log.e("format", "date format exception");
            }


            ///
            BufferedReader rd2 = null;
            StringBuilder sb2 = null;
            String line2 = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/upcoming?api_key=" + Constants.API_KEY);

                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.connect();

                rd2 = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                sb2 = new StringBuilder();

                line2 = rd2.readLine();
                sb2.append(line2);
            } catch (IOException ioe) {
                Log.e("http", "url IOException");
            }
            String data2 = sb.toString();
            try {
                JSONObject j = new JSONObject(data2);
                JSONArray ja = j.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    Movie m = new Movie();
                    m.setTitle(ja.getJSONObject(i).getString("original_title"));
                    m.setOverview(ja.getJSONObject(i).getString("overview"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    m.setReleaseDate(sdf.parse(ja.getJSONObject(i).getString("release_date")).getTime());
                    String path = ja.getJSONObject(i).getString("poster_path");
                    if (path.equals("null")) {
                        path = "drawable://" + R.drawable.everest;
                        m.setCoverPath(path);
                    } else {
                        m.setCoverPath("http://image.tmdb.org/t/p/w342" + path);
                    }
                    path = ja.getJSONObject(i).getString("backdrop_path");
                    if (path.equals("null")) {
                        path = "drawable://" + R.drawable.everest2;
                        m.setPosterPath(path);
                    } else {
                        m.setPosterPath("http://image.tmdb.org/t/p/w1280" + path);
                    }
                    mMovies.add(m);
                }


            } catch (JSONException je) {
                Log.e("adapter", "creating adapter error");
            } catch (ParseException pa) {
                Log.e("format", "date format exception");
            }
            mAdapter = new MyAdapter(getActivity(), mMovies);
            return mAdapter;
        }

        @Override
        protected void onPostExecute(MyAdapter adapter) {
            if (adapter == null || adapter.getCount() == 0) {
                mEmpty.setText("No movies");
                mGrid.setEmptyView(mEmpty);
            } else {
                mEmpty.setVisibility(View.GONE);
                mGrid.setAdapter(adapter);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", mMovies);
        outState.putInt("position", mGrid.getFirstVisiblePosition());
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Movie m);
    }
}
