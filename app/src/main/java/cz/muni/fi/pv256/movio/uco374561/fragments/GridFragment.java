package cz.muni.fi.pv256.movio.uco374561.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.activities.MainActivity;
import cz.muni.fi.pv256.movio.uco374561.adapters.MyDbAdapter;
import cz.muni.fi.pv256.movio.uco374561.adapters.MyNetworkAdapter;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.providers.MovieManager;
import cz.muni.fi.pv256.movio.uco374561.services.DownloadService;
import cz.muni.fi.pv256.movio.uco374561.sync.MovieSyncAdapter;

/**
 * Created by collfi on 25. 10. 2015.
 */
public class GridFragment extends Fragment {
    public static final int NETWORK = 0;
    public static final int DB = 1;
    private int adapter;
    private StickyGridHeadersGridView mGrid;
    private ArrayList<Movie> mNetworkMovies;
    private ArrayList<Movie> mDbMovies;
    private OnItemSelectedListener l;
    private MyNetworkAdapter mNetworkAdapter;
    private MyDbAdapter mDbAdapter;

    private int mPosition;
    private TextView mEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mNetworkMovies = new ArrayList<>();
        } else {
            mNetworkMovies = savedInstanceState.getParcelableArrayList("list");
            mPosition = savedInstanceState.getInt("position");
        }
        setHasOptionsMenu(true);
        getActivity().registerReceiver(dataReceiver, new IntentFilter("cz.muni.fi.movio"));
        //todo asynctask
        MovieManager manager = new MovieManager(getActivity());
        mDbMovies = manager.getAll();

        mDbAdapter = new MyDbAdapter(getActivity(), mDbMovies);
        //refresh pri pridani
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
                Snackbar.make(view, mNetworkMovies.get(position).getTitle(), Snackbar.LENGTH_LONG).show();
                return true;
            }
        });

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                l.onItemSelected(mNetworkMovies.get(position));
            }
        });
        ViewStub empty = (ViewStub) view.findViewById(android.R.id.empty);
        mEmpty = (TextView) empty.inflate().findViewById(R.id.text_empty);
//        mEmpty.setText(R.string.downloading);
        mGrid.setEmptyView(mEmpty);

//        if (isConnected()) {
        if (mNetworkMovies.size() == 0) {
//        Just to test the API, will be changed
//                new DownloadNowPlayingImages().execute();
            adapter = NETWORK;
            Intent i = new Intent(getActivity(), DownloadService.class);
            getActivity().startService(i);

        } else {
            mNetworkAdapter = new MyNetworkAdapter(getActivity(), mNetworkMovies);
            mGrid.setAdapter(mNetworkAdapter);
            Log.i("QQQ", "---" + mPosition);
            mGrid.setSelection(mPosition);
            mGrid.smoothScrollToPosition(mPosition);
        }
//        } else {
//            mEmpty.setText(R.string.no_connection);
//            mGrid.setEmptyView(mEmpty);
//        }
        return view;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter == NETWORK) {
            outState.putParcelableArrayList("list", mNetworkMovies);
        } else {
            outState.putParcelableArrayList("list", mDbMovies);
        }
        outState.putInt("position", mGrid.getFirstVisiblePosition());
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Movie m);
    }

    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mNetworkMovies = intent.getParcelableArrayListExtra("movies");
            mNetworkAdapter = new MyNetworkAdapter(getActivity(), mNetworkMovies);
            mGrid.setAdapter(mNetworkAdapter);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(" QQQ", "on create options menu");

        if (adapter == NETWORK) {
            inflater.inflate(R.menu.menu_favorites, menu);
        }
        if (adapter == DB) {
            inflater.inflate(R.menu.menu_discover, menu);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(dataReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(" QQQ", "options menu selected");
        //noinspection SimplifiableIfStatement
        if (id == R.id.favorites) {
            Log.i("zzzz", mGrid + " - " + mDbAdapter + " - " + mDbAdapter.getCount());
            mGrid.setAdapter(mDbAdapter);
            adapter = DB;

            Log.i(" QQQ", "click favorites");
            getActivity().supportInvalidateOptionsMenu();
            return true;
        }

        if (id == R.id.discover) {
            if (mNetworkAdapter == null) {
                mNetworkAdapter = new MyNetworkAdapter(getActivity(), new ArrayList<Movie>());
            }
            mGrid.setAdapter(mNetworkAdapter);
            adapter = NETWORK;
            Log.i(" QQQ", "click discover");

            getActivity().supportInvalidateOptionsMenu();
            return true;
        }
        if (id == R.id.sync) {
            MovieSyncAdapter.syncImmediately(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }
}
