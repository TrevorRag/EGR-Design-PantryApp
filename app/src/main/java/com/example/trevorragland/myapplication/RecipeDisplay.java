package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trevorragland.myapplication.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;
import java.net.URL;
import java.util.Arrays;

import self.philbrown.droidQuery.$;
import self.philbrown.droidQuery.AjaxOptions;
import self.philbrown.droidQuery.AjaxTask;
import self.philbrown.droidQuery.Function;

import static java.security.AccessController.getContext;

/**
 * Created by AD on 4/19/2017.
 */

public class RecipeDisplay extends AppCompatActivity {
    final String apiKey = Constants.BIGOVEN_API_KEY;
    String recipeID = null;
    private FirebaseAuth mAuth;
    ImageView ivRecipeThumb;
    TextView tvRecipeName;
    TextView tvIngredientList;
    TextView tvPreparationList;
    private String[] ingredientList;
    String allIngredients = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String LOG_TAG = RecipeDisplay.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //layout resources
        ivRecipeThumb = (ImageView) findViewById(R.id.ivRecipeThumb);
        tvRecipeName = (TextView) findViewById(R.id.tvRecipeName);
        tvIngredientList = (TextView) findViewById(R.id.tvIngredientList);
        tvPreparationList = (TextView) findViewById(R.id.tvPreparationList);

        //RecipeID
        recipeID = getIntent().getStringExtra("ID");
        recipeFetch(recipeID);
    }

    public void recipeFetch(final String recipeId) {

        DatabaseReference myRef = database.getReference().child("recipes").child(recipeId);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    try {
                        //gets the JSON output from the website
                        System.out.println(value);
                        JSONObject obj = new JSONObject(value);

                        //**************Picture Start**************
                        final String url = obj.getString("PhotoUrl");
                        Picasso.with(RecipeDisplay.this)
                                .load(url)
                                .resize(512, 512)
                                .centerCrop()
                                .into(ivRecipeThumb);
                        //**************Picture End****************

                        //**************Title Start****************
                        String title = obj.getString("Title");
                        $.with(tvRecipeName).val(title);
                        //**************Title End******************

                        //**************Ingredients Start**********
                        JSONArray tempArray = new JSONArray();
                        JSONArray fini = new JSONArray();

                        //siphons the extra information out of the ingredients array
                        for (int i = 0; i < 7; i++) {
                            JSONObject ingredients = obj.getJSONArray("Ingredients").getJSONObject(i);
                            tempArray.put(ingredients.getString("Name").trim() + ": ");
                            if (ingredients.getString("Quantity").trim() != "null")
                                tempArray.put(ingredients.getString("Quantity").trim());
                            if (ingredients.getString("Unit").trim() != "null")
                                tempArray.put(" " + ingredients.getString("Unit").trim());
                            if (ingredients.getString("PreparationNotes").trim() != "null")
                                tempArray.put(" " + ingredients.getString("PreparationNotes").trim() + "\n");
                            else
                                tempArray.put(" \n");
                            fini.put(tempArray);
                            fini.remove(1);
                            String getIngredient = fini.getString(0);
                            ingredientList = getIngredient.split("\\\\n\",\"");
                        }

                        //turns the array into an easy to read string
                        for (int i = 0; i < ingredientList.length; i++) {
                            String temp = ingredientList[i];
                            temp = temp.replaceAll("\\[", "");
                            temp = temp.replaceAll("\"", "");
                            temp = temp.replaceAll(",", "");
                            temp = temp.replaceAll("\\]", "");
                            temp = temp.replaceAll("\\\\n", "");
                            temp = temp.replaceAll("\\\\", "");
                            temp += "\r\n\r\n";
                            allIngredients += temp;
                        }
                        $.with(tvIngredientList).val(allIngredients);
                        //**************Ingredients End************

                        //**************Instructions Start*********
                        String prep = obj.getString("Instructions");
                        $.with(tvPreparationList).val(prep);
                        //**************Instructions End***********
                    } catch (JSONException e) {
                        System.err.println("Caught JSONException: " + e.getMessage());
                    }
                } else {
                    finish();
                    $.with(RecipeDisplay.this).toast("Recipe not found.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(LOG_TAG, "Failed to read value.", error.toException());
            }
        });
        /************************BIGOVEN RETRIEVAL***********************************
        JSONObject obj = new JSONObject();


        final String recipeUrl = "https://api2.bigoven.com/recipe/"
                + recipeId
                + "?api_key=" + apiKey;

        $.ajax(new AjaxOptions().url(recipeUrl).type("GET").dataType("json").context(this).success(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                //get the title
                String pls = params[0].toString();

                try {
                    //gets the JSON output from the website
                    JSONObject obj = new JSONObject(pls);

                    //**************Picture Start**************
                    final String url = obj.getString("PhotoUrl");
                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics());
                    $.with(ivRecipeThumb).image(url, px, px, new Function() {
                        @Override
                        public void invoke($ droidQuery, Object... params) {
                            Log.e("Images", params[0].toString());
                            new DownloadImageTask(ivRecipeThumb).execute(url);
                        }
                    });
                    //**************Picture End****************

                    //**************Title Start****************
                    String title = obj.getString("Title");
                    $.with(tvRecipeName).val(title);
                    //**************Title End******************

                    //**************Ingredients Start**********
                    JSONArray tempArray = new JSONArray();
                    JSONArray fini = new JSONArray();

                    //siphons the extra information out of the ingredients array
                    for (int i = 0; i < 7; i++) {
                        JSONObject ingredients = obj.getJSONArray("Ingredients").getJSONObject(i);
                        tempArray.put(ingredients.getString("Name").trim() + ": ");
                        tempArray.put(ingredients.getString("Quantity").trim());
                        tempArray.put(" " + ingredients.getString("Unit").trim());
                        tempArray.put(" " + ingredients.getString("PreparationNotes").trim() + "\n");
                        fini.put(tempArray);
                        fini.remove(1);
                        String getIngredient = fini.getString(0);
                        ingredientList = getIngredient.split("\\\\n\",\"");
                    }

                    //turns the array into an easy to read string
                    for (int i = 0; i < ingredientList.length; i++) {
                        String temp = ingredientList[i];
                        temp = temp.replaceAll("\\[","");
                        temp = temp.replaceAll("\"","");
                        temp = temp.replaceAll(",","");
                        temp = temp.replaceAll("\\]","");
                        temp = temp.replaceAll("\\\\n","");
                        temp = temp.replaceAll("\\\\","");
                        temp += "\r\n\r\n";
                        allIngredients += temp;
                    }
                    $.with(tvIngredientList).val(allIngredients);
                    //**************Ingredients End************

                    //**************Instructions Start*********
                    String prep = obj.getString("Instructions");
                    $.with(tvPreparationList).val(prep);
                    //**************Instructions End***********
                }
                catch (JSONException e) {
                    System.err.println("Caught JSONException: " + e.getMessage());
                }

            }
        }).error(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                int statusCode = (Integer) params[1];
                String error = (String) params[2];
                Log.e("Ajax", statusCode + " " + error);
            }
        }));
         ******************************************************************************************/
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
        Intent profileIntent = new Intent(RecipeDisplay.this, Profile.class);
        startActivity(profileIntent);
        finish();
    }

    private void onMenuPicPressed(MenuItem item) {
        Intent picIntent = new Intent(RecipeDisplay.this, pictureUpload.class);
        startActivity(picIntent);
        finish();
    }

    private void onMenuAddRecipePressed(MenuItem item) {
        Intent addRecipeIntent = new Intent(RecipeDisplay.this, AddRecipe.class);
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
