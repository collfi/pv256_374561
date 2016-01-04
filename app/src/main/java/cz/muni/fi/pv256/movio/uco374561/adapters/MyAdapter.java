package cz.muni.fi.pv256.movio.uco374561.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.R;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;

/**
 * Created by collfi on 27. 12. 2015.
 */
public class MyAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private LayoutInflater inflater;
    private List<Movie> mMovies;
    private DisplayImageOptions options;
    private int[] headers;

    public MyAdapter(Context context, List<Movie> list, int[] headers) {
        this.headers = headers;
        inflater = LayoutInflater.from(context);
        mMovies = list;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            Log.i("","inflate radku "+ position);
            view = inflater.inflate(R.layout.grid_item, parent, false);
            view.setId(position);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            Log.i("", "recyklace radku " + position);
            holder = (ViewHolder) view.getTag();
        }
        if (mMovies.get(position).getCoverPath() == null) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.no_image, holder.image, options);

        } else {
            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w342" + mMovies.get(position).getCoverPath(), holder.image, options);
        }
        return view;
    }

    static class ViewHolder {
        ImageView image;
    }

    @Override
    public int getCountForHeader(int header) {
        if (headers.length > 1) {
            return 20;
        } else return mMovies.size();
    }

    @Override
    public int getNumHeaders() {
        return headers.length;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_header, parent, false);
        }
        TextView t = (TextView) convertView.findViewById(R.id.text);
        t.setText(headers[position]);

        return convertView;
    }
}
