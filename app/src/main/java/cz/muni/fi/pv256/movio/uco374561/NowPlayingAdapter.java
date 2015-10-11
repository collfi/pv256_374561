package cz.muni.fi.pv256.movio.uco374561;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by collfi on 11. 10. 2015.
 */
public class NowPlayingAdapter extends BaseAdapter {

    private List<String> mList;
    private List<Bitmap> mBitmaps;
    private LayoutInflater inflater;

    public NowPlayingAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        mList = new ArrayList<>(list);
        mBitmaps = new ArrayList<>();
        for (String s : mList) {
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(s).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                mBitmaps.add(bitmap);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getCount() {
        return mList.size();
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
            view = inflater.inflate(R.layout.grid_item, parent, false);
            view.setId(position);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.image.setImageBitmap(mBitmaps.get(position));
//        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.image, options);
        return view;
    }

    static class ViewHolder {
        ImageView image;
    }
}
