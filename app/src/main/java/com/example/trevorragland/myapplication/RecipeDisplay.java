package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trevorragland.myapplication.utils.Constants;

import java.net.URL;

import self.philbrown.droidQuery.$;
import self.philbrown.droidQuery.AjaxOptions;
import self.philbrown.droidQuery.Function;

/**
 * Created by AD on 4/19/2017.
 */

public class RecipeDisplay extends AppCompatActivity {
    final String apiKey = Constants.BIGOVEN_API_KEY;
    ImageView ivRecipeThumb;
    TextView tvRecipeName;
    TextView tvIngredientList;
    TextView tvPreparationList;
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

        recipeFetch("907321");

    }

    public void recipeFetch(String recipeId) {
        String recipeUrl = "https://api2.bigoven.com/recipe/"
                + recipeId
                + "?api_key=" + apiKey;

        $.ajax(new AjaxOptions().url(recipeUrl).type("GET").dataType("json").context(this).success(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                //droidQuery.alert((String) params[0]);
               // $("textare#ExampleMessage").text(result.ExampleMessage);
                $.with(tvPreparationList).data("Instructions");
            }
        }).error(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                int statusCode = (Integer) params[1];
                String error = (String) params[2];
                Log.e("Ajax", statusCode + " " + error);
            }
        }));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuHomePressed(MenuItem item) {
        Intent mainIntent = new Intent(RecipeDisplay.this, Main.class);
        startActivity(mainIntent);
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
}
