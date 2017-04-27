package com.example.trevorragland.myapplication.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.trevorragland.myapplication.R;

/**
 * Created by AD on 4/26/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds = {R.drawable.fastfood};

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(256, 256));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public Integer[] popThumbs (Integer[] urls) {
        System.arraycopy( urls, 0, mThumbIds, 0, urls.length );
        return mThumbIds;
    }
    // references to our images

}