package cz.muni.fi.pv256.movio.uco374561.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.providers.MovieManager;

/**
 * Created by collfi on 25. 10. 2015.
 */
public class DetailFragment extends Fragment {
    private Movie mMovie;
    private DisplayImageOptions options;
    private ImageView mPoster;
    private ImageView mCover;
    private TextView mReleaseDate;
    private TextView mTitle;
    private TextView mOverview;
    private FloatingActionButton mAdd;
    private View v;
    private Movie mCurrentMovie;
    private MovieManager mManager;
    private View.OnClickListener mAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mManager.createMovie(mCurrentMovie);
            mAdd.setImageResource(R.drawable.ic_remove_white_24dp);
            v.setOnClickListener(mRemoveListener);
        }
    };
    private View.OnClickListener mRemoveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mManager.deleteMovie(mCurrentMovie);
            mAdd.setImageResource(R.drawable.ic_add_white_24dp);
            v.setOnClickListener(mAddListener);
        }
    };

    public static DetailFragment newInstance(Movie m) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", m);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable("movie");
        }
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable("movie");
        }
        mManager = new MovieManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail, container, false);
        mPoster = (ImageView) v.findViewById(R.id.poster);
        mCover = (ImageView) v.findViewById(R.id.cover);
        mReleaseDate = (TextView) v.findViewById(R.id.release_date);
        mTitle = (TextView) v.findViewById(R.id.title);
        mOverview = (TextView) v.findViewById(R.id.overview);
        mAdd = (FloatingActionButton) v.findViewById(R.id.add);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMovie != null) {
            update(mMovie);
        }
    }

    public void update(Movie m) {
        mCurrentMovie = m;
        if (m.getCoverPath() == null) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.everest, mCover, options);
        } else {
            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w342" + m.getCoverPath(), mCover, options);
        }
        if (m.getPosterPath() == null) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.everest2, mPoster, options);
        } else {
            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w1280" + m.getPosterPath(), mPoster, options);
        }
        mReleaseDate.setText(m.getReleaseDate());
        mTitle.setText(m.getTitle());
        mOverview.setText(m.getOverview());

        if (mManager.getMovie(String.valueOf(mCurrentMovie.getMovieId())) == null) {
            mAdd.setOnClickListener(mAddListener);
            mAdd.setImageResource(R.drawable.ic_add_white_24dp);
        } else {
            mAdd.setOnClickListener(mRemoveListener);
            mAdd.setImageResource(R.drawable.ic_remove_white_24dp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", mMovie);
    }

}
