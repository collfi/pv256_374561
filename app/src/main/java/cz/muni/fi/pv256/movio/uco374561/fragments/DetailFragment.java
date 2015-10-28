package cz.muni.fi.pv256.movio.uco374561.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.Calendar;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;

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
    private View v;

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
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMovie != null) {
            update(mMovie);
        } else {
        }
    }

    public void update(Movie m) {
//        if (mCover == null) {
//            View view = inflater.inflate(R.layout.fragment_detail, container, false);
//            Log.i("QQQ", "oncreate view " + mMovie);
//            mPoster = (ImageView) view.findViewById(R.id.poster);
//            mCover = (ImageView) view.findViewById(R.id.cover);
//            mReleaseDate = (TextView) view.findViewById(R.id.release_date);
//            mTitle = (TextView) view.findViewById(R.id.title);
//            mOverview = (TextView) view.findViewById(R.id.overview);
//        }

        ImageLoader.getInstance().displayImage(m.getCoverPath(), mCover, options);
        ImageLoader.getInstance().displayImage(m.getPosterPath(), mPoster, options);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(m.getReleaseDate());
        mReleaseDate.setText(String.valueOf(cal.get(Calendar.YEAR)));
        mTitle.setText(m.getTitle());
        mOverview.setText(m.getOverview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", mMovie);
    }
}
