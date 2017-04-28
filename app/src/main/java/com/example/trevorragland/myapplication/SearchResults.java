package com.example.trevorragland.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trevorragland.myapplication.POJO.Recipe;
import com.example.trevorragland.myapplication.utils.ImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import self.philbrown.droidQuery.$;

import static com.example.trevorragland.myapplication.Login.encodeEmail;

/**
 * Created by AD on 4/26/2017.
 */

public class SearchResults extends AppCompatActivity {
    TextView tvTestText;
    private FirebaseAuth mAuth;
    int hitCount = 0;
    String[] recipeTitle;
    String[] recipeID;
    String[] imageUri;
    String[] category;
    String[] subCategory;
    String[] starRating;
    Bundle idBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //string search result
        String searchQuery = getIntent().getStringExtra("query");
        getRecipeThumbInfo(searchQuery);

        GridView gridview = (GridView) findViewById(R.id.gvThumbnails);
        final ImageAdapter check = new ImageAdapter(this);
        check.setSearchInfo(recipeTitle, recipeID, imageUri, category, subCategory, starRating);
        gridview.setAdapter(check);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String gotToID = check.getID(position);
                idBundle.putString("ID",gotToID);
                Intent recipeDisplayIntent = new Intent(SearchResults.this, RecipeDisplay.class);
                recipeDisplayIntent.putExtras(idBundle);
                startActivity(recipeDisplayIntent);
                finish();
            }
        });

    }

    public void getRecipeThumbInfo(String query) {

        try {
            //gets the JSON output from the website
            JSONObject obj = new JSONObject(query);

            //get the result count
            hitCount = obj.getInt("ResultCount");


            JSONArray tempArray = new JSONArray();
            JSONArray fini = new JSONArray();

            //Title
            recipeTitle = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject titles = obj.getJSONArray("Results").getJSONObject(i);
                if (titles.getString("Title") != null)
                    recipeTitle[i] = (titles.getString("Title"));
                else
                    i = 50;
            }

            //ID
            recipeID = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject ids = obj.getJSONArray("Results").getJSONObject(i);
                if (ids.getString("RecipeID") != null)
                    recipeID[i] = (ids.getString("RecipeID"));
                else
                    i = 50;
            }

            //category
            category = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject ids = obj.getJSONArray("Results").getJSONObject(i);
                if (ids.getString("Category") != null)
                    category[i] = (ids.getString("Category"));
                else
                    i = 50;
            }

            //subcategory
            subCategory = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject ids = obj.getJSONArray("Results").getJSONObject(i);
                if (ids.getString("Subcategory") != null)
                    subCategory[i] = (ids.getString("Subcategory"));
                else
                    i = 50;
            }

            //rating
            starRating = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject ids = obj.getJSONArray("Results").getJSONObject(i);
                if (ids.getString("StarRating") != null) {
                    String rate = (ids.getString("StarRating"));
                    rate = rate.substring(0,Math.min(rate.length(), 3));
                    starRating[i] = rate;
                }
                else
                    i = 50;
            }


            //pic url
            imageUri = new String[obj.getJSONArray("Results").length()];
            for (int i = 0; i < obj.getJSONArray("Results").length(); i++) {
                JSONObject ids = obj.getJSONArray("Results").getJSONObject(i);
                if (ids.getString("PhotoUrl") != null)
                    imageUri[i] = (ids.getString("PhotoUrl"));
                else
                    i = 50;
            }

        } catch (JSONException e) {
            System.err.println("Caught JSONException: " + e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.miHome:
                onMenuHomePressed(item);
                return true;
            case R.id.miProfile:
                onMenuProfilePressed(item);
                return true;
            case R.id.miPicture:
                onMenuPicPressed(item);
                return true;
            case R.id.miAddRecipe:
                onMenuAddRecipePressed(item);
                return true;
            case R.id.miLogout:
                onLogoutPressed(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuHomePressed(MenuItem item) {
        finish();
    }

    private void onMenuProfilePressed(MenuItem item) {
        Intent profileIntent = new Intent(SearchResults.this, Profile.class);
        startActivity(profileIntent);
        finish();
    }

    private void onMenuPicPressed(MenuItem item) {
        Intent picIntent = new Intent(SearchResults.this, pictureUpload.class);
        startActivity(picIntent);
        finish();
    }

    private void onMenuAddRecipePressed(MenuItem item) {
        Intent addRecipeIntent = new Intent(SearchResults.this, AddRecipe.class);
        startActivity(addRecipeIntent);
        finish();
    }

    private void onLogoutPressed(MenuItem item) {
        //*****logout**********
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        System.exit(0);
        //********************
    }
}

