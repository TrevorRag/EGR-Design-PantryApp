package com.example.trevorragland.myapplication.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trevorragland.myapplication.R;

import java.util.Arrays;

/**
 * Created by AD on 4/26/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Context aContext;
    // references to our images
    private String[] recipeTitle;
    private String[] recipeID;
    private String[] imageUri;
    private String[] rCategory;
    private String[] rSubCategory;
    private String[] starRating;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setSearchInfo (String[] title, String[] id, String[] uri, String[] category, String[] subCategory, String[] rating) {
        recipeTitle = new String[title.length];
        System.arraycopy(title, 0, recipeTitle, 0, title.length);
        recipeID = new String[id.length];
        System.arraycopy(id, 0, recipeID, 0, id.length);
        imageUri = new String[uri.length];
        System.arraycopy(uri, 0, imageUri, 0, uri.length);
        rCategory = new String[category.length];
        System.arraycopy(category, 0, category, 0, category.length);
        rSubCategory = new String[subCategory.length];
        System.arraycopy(subCategory, 0, subCategory, 0, subCategory.length);
        starRating = new String[rating.length];
        System.arraycopy(rating, 0, starRating, 0, rating.length);
    }

    public int getCount() {
        return recipeTitle.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public String getID(int position) {
        return recipeID[position];
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        TextView categoryView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            categoryView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(500,256));
            textView.setPadding(8, 8, 8, 8);
            categoryView.setLayoutParams(new GridView.LayoutParams(500,256));
            categoryView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
            categoryView = (TextView) convertView;
        }
        categoryView.setText(rCategory[position]);
        textView.setText(recipeTitle[position]);
        return textView;
    }


}