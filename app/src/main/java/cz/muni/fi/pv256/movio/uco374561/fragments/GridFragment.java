package cz.muni.fi.pv256.movio.uco374561.fragments;

import android.app.IntentService;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.IOException;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.activities.MainActivity;
import cz.muni.fi.pv256.movio.uco374561.adapters.MyAdapter;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.models.MovieResponse;
import retrofit.http.GET;
import retrofit.http.Path;

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
        mEmpty.setText(R.string.downloading);
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
            mEmpty.setText(R.string.no_connection);
            mGrid.setEmptyView(mEmpty);
        }
        return view;
    }


    public class DownloadNowPlayingImages extends AsyncTask<Void, Void, MyAdapter> {
        @Override
        protected MyAdapter doInBackground(Void... params) {
            Response response;
            String nextWeek = null;
            String inTheatres = null;
            try {
                final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2015-11-09&primary_release_date.lte=2015-11-16&sort_by=avg_rating.desc&api_key=" + "c331638cd30b7ab8a4b73dedbbb62193")
                        .build();
                Call call = client.newCall(request);
                response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                nextWeek = response.body().string();

                request = new Request.Builder()
                        .url("http://api.themoviedb.org/3/movie/now_playing?api_key=" + "c331638cd30b7ab8a4b73dedbbb62193")
                        .build();
                call = client.newCall(request);
                response = call.execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                inTheatres = response.body().string();

            } catch (IOException ioe) {
                Log.e("http", "url IOException");
                Toast.makeText(getActivity(), "Error while parsing downloaded data", Toast.LENGTH_LONG).show();
            }
            Gson gson = new Gson();
            MovieResponse res = gson.fromJson(nextWeek, MovieResponse.class);
            mMovies.addAll(res.results);
            res = gson.fromJson(inTheatres, MovieResponse.class);
            mMovies.addAll(res.results);

//            try {
//                JSONObject j = new JSONObject(inTheatres);
//                JSONArray ja = j.getJSONArray("results");
//                for (int i = 0; i < ja.length(); i++) {
//
//                    GsonBuilder builder = new GsonBuilder();
//                    Gson gson = builder.create();
//                    Movie m = gson.fro
////                    m.setTitle(ja.getJSONObject(i).getString("original_title"));
////                    m.setOverview(ja.getJSONObject(i).getString("overview"));
////                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////                    m.setReleaseDate(sdf.parse(ja.getJSONObject(i).getString("release_date")).getTime());
////                    String path = ja.getJSONObject(i).getString("poster_path");
////                    if (path.equals("null")) {
////                        path = "drawable://" + R.drawable.everest;
////                        m.setCoverPath(path);
////                    } else {
////                        m.setCoverPath("http://image.tmdb.org/t/p/w342" + path);
////                    }
////                    path = ja.getJSONObject(i).getString("backdrop_path");
////                    if (path.equals("null")) {
////                        path = "drawable://" + R.drawable.everest2;
////                        m.setPosterPath(path);
////                    } else {
////                        m.setPosterPath("http://image.tmdb.org/t/p/w1280" + path);
////                    }
//                    mMovies.add(m);
//                }


//            } catch (JSONException je) {
//                Log.e("adapter", "creating adapter error");
//            } catch (ParseException pa) {
//                Log.e("format", "date format exception");
//            }
            mAdapter = new MyAdapter(getActivity(), mMovies);
            return mAdapter;
        }

        @Override
        protected void onPostExecute(MyAdapter adapter) {
            if (mGrid == null) return;
            if (adapter == null || adapter.getCount() == 0) {
                mEmpty.setText(R.string.no_data);
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

    public class DownloadService extends IntentService {
        @Override
        protected void onHandleIntent(Intent intent) {

        }

        public DownloadService() {
            super("DownloadService");
        }
    }
}
