package cz.muni.fi.pv256.movio.uco374561;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.db.Movie;

/**
 * Created by collfi on 11. 10. 2015.
 */
public class MyAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Movie> mMovies;

    public MyAdapter(Context context, List<Movie> list) {
        inflater = LayoutInflater.from(context);
        mMovies = list;
    }


    @Override
    public int getCount() {
        return mMovies.size();
    }
    //todo
    @Override
    public Object getItem(int position) {
        return position;
    }
    //todo
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
        holder.image.setImageResource(R.drawable.everest);
//        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.image, options);
        return view;
    }

    static class ViewHolder {
        ImageView image;
    }
}
