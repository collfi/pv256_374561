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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.activities.MainActivity;
import cz.muni.fi.pv256.movio.uco374561.adapters.MyAdapter;
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
    public static final int[] NETWORK_HEADERS = {R.string.opening, R.string.theatres};
    public static final int[] DB_HEADERS = {R.string.favorites};
    private int source;
    private StickyGridHeadersGridView mGrid;
    private ArrayList<Movie> mNetworkMovies;
    private ArrayList<Movie> mDbMovies;
    private OnItemSelectedListener l;
    private MyAdapter mNetworkAdapter;
    private MyAdapter mDbAdapter;
    private MovieManager mManager;
    private int mPosition;
    private TextView mEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        source = NETWORK;
        mManager = new MovieManager(getActivity());
        if (savedInstanceState == null) {
            mNetworkMovies = new ArrayList<>();
            mDbMovies = mManager.getAll();
        } else {
            source = savedInstanceState.getInt("source");
            mNetworkMovies = savedInstanceState.getParcelableArrayList("networkList");
            mDbMovies = savedInstanceState.getParcelableArrayList("dbList");
            mPosition = savedInstanceState.getInt("position");
        }
        getActivity().registerReceiver(dataReceiver, new IntentFilter("cz.muni.fi.movio"));
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
                if (source == NETWORK) {
                    Snackbar.make(view, mNetworkMovies.get(position).getTitle(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, mDbMovies.get(position).getTitle(), Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
        });

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (source == NETWORK) {
                    l.onItemSelected(mNetworkMovies.get(position));
                } else {
                    l.onItemSelected(mDbMovies.get(position));
                }
            }
        });
        ViewStub empty = (ViewStub) view.findViewById(android.R.id.empty);
        mEmpty = (TextView) empty.inflate().findViewById(R.id.text_empty);
        mEmpty.setText(R.string.downloading);
        mGrid.setEmptyView(mEmpty);
        if (isConnected()) {
            if (source == NETWORK) {
                if (mNetworkMovies.size() == 0) {
                    Intent i = new Intent(getActivity(), DownloadService.class);
                    getActivity().startService(i);

                } else {
                    mNetworkAdapter = new MyAdapter(getActivity(), mNetworkMovies, NETWORK_HEADERS);
                    mGrid.setAdapter(mNetworkAdapter);
                    mGrid.setSelection(mPosition);
                    mGrid.smoothScrollToPosition(mPosition);
                }
            } else {
                if (mDbMovies.size() == 0) {
                    mGrid.setAdapter(new ArrayAdapter<Movie>(getActivity(), R.layout.grid_item));
                    mEmpty.setText(R.string.no_data);
                } else {
                    mDbAdapter = new MyAdapter(getActivity(), mDbMovies, DB_HEADERS);
                    mGrid.setAdapter(mDbAdapter);
                    mGrid.setSelection(mPosition);
                    mGrid.smoothScrollToPosition(mPosition);
                }
            }
        } else {
            mEmpty.setText(R.string.no_connection);
        }
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
        if (source == NETWORK) {
            outState.putInt("source", NETWORK);
        } else {
            outState.putInt("source", DB);
        }
        outState.putParcelableArrayList("networkList", mNetworkMovies);
        outState.putParcelableArrayList("dbList", mDbMovies);
        outState.putInt("position", mGrid.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);

    }

    public interface OnItemSelectedListener {
        void onItemSelected(Movie m);
    }

    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mNetworkMovies = intent.getParcelableArrayListExtra("movies");
            if (mNetworkMovies == null || mNetworkMovies.size() == 0) {
                mEmpty.setText(R.string.no_data);
                return;
            }
            mNetworkAdapter = new MyAdapter(getActivity(), mNetworkMovies, NETWORK_HEADERS);
            mGrid.setAdapter(mNetworkAdapter);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (source == NETWORK) {
            inflater.inflate(R.menu.menu_favorites, menu);
            return;
        }
        if (source == DB) {
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
        if (id == R.id.favorites) {
            mDbMovies = mManager.getAll();
            if (mDbMovies.size() == 0) {
                mGrid.setAdapter(new ArrayAdapter<Movie>(getActivity(), R.layout.grid_item));
                mEmpty.setText(R.string.no_data);
            } else {
                mDbAdapter = new MyAdapter(getActivity(), mDbMovies, DB_HEADERS);
                mGrid.setAdapter(mDbAdapter);
            }
            source = DB;
            getActivity().supportInvalidateOptionsMenu();
            return true;
        }

        if (id == R.id.discover) {
            if (mNetworkMovies.size() == 0) {
                mGrid.setAdapter(new ArrayAdapter<Movie>(getActivity(), R.layout.grid_item));
                mEmpty.setText(R.string.no_data);
            } else {
                mNetworkAdapter = new MyAdapter(getActivity(), mNetworkMovies, NETWORK_HEADERS);
                mGrid.setAdapter(mNetworkAdapter);
            }
            source = NETWORK;
            getActivity().supportInvalidateOptionsMenu();
            return true;
        }
        if (id == R.id.sync) {
            MovieSyncAdapter.syncImmediately(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (source == DB) {
            mDbMovies = mManager.getAll();
            if (mDbMovies == null || mDbMovies.size() == 0) {
                mGrid.setAdapter(new ArrayAdapter<Movie>(getActivity(), R.layout.grid_item));
                mEmpty.setText(R.string.no_data);
                source = DB;
            } else {
                mDbAdapter = new MyAdapter(getActivity(), mDbMovies, DB_HEADERS);
                mGrid.setAdapter(mDbAdapter);
            }
        } else {
            if (mNetworkMovies == null || mNetworkMovies.size() == 0) {
                mGrid.setAdapter(new ArrayAdapter<Movie>(getActivity(), R.layout.grid_item));
                mEmpty.setText(R.string.no_data);
                source = NETWORK;
            } else {
                mDbAdapter = new MyAdapter(getActivity(), mNetworkMovies, DB_HEADERS);
                mGrid.setAdapter(mDbAdapter);
            }
        }
    }
}
