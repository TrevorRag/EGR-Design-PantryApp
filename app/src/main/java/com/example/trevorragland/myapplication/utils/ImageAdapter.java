package com.example.trevorragland.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trevorragland.myapplication.R;
import com.example.trevorragland.myapplication.RecipeDisplay;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by AD on 4/26/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_cell,parent,false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.ivThumbCell);
        TextView title= (TextView) convertView.findViewById(R.id.tvTitleCell);
        TextView category= (TextView) convertView.findViewById(R.id.tvCatCell);
        TextView subCategory= (TextView) convertView.findViewById(R.id.tvSubCell);
        TextView rating= (TextView) convertView.findViewById(R.id.tvRateCell);

        title.setText(recipeTitle[position]);
        category.setText(rCategory[position]);
        subCategory.setText(rSubCategory[position]);
        rating.setText("   Rating: " + starRating[position]);
        new DownloadImageTask(img).execute(imageUri[position]);
        return convertView;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}