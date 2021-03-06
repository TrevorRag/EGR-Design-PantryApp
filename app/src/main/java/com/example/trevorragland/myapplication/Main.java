package com.example.trevorragland.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.trevorragland.myapplication.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import self.philbrown.droidQuery.$;
import self.philbrown.droidQuery.AjaxOptions;
import self.philbrown.droidQuery.Function;


/**
 * Created by AD on 3/16/2017.
 */

public class Main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView ivUserInformation;
    SearchView svSearch;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference recipeLocation = database.getReference().child("recipes");
    private String email;
    private String profilePic;
    private String name;
    private String googleUser;
    private  String pls;
    URL pic = null;
    private String apiKey = Constants.BIGOVEN_API_KEY;
    Bundle idBundle = new Bundle();
    String recipeID = null;
    private int web = 499640;
    private int rip = 500000;

    private static final String LOG_TAG = Main.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ivUserInformation = (ImageView) findViewById(R.id.ivUserInformation);
        svSearch = (SearchView) findViewById(R.id.svRecipeSearch);

        /* This block is the user information from the Google account information */
        storage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = getIntent().getStringExtra("userEmail");
        profilePic = getIntent().getStringExtra("userPic");
        name = getIntent().getStringExtra("userName");
        googleUser = getIntent().getStringExtra("userID");
        /*                                                                       */

        //sets the Google URL to imageview
        Picasso.with(Main.this)
                .load(profilePic)
                .resize(512, 512)
                .centerCrop()
                .into(ivUserInformation);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(Main.this,"Searching for: "+ s,Toast.LENGTH_SHORT).show();
                s = s.replaceAll(" ","+");
                recipeSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public void recipeSearch(String searchWord) {
        String recipeUrl = "https://api2.bigoven.com/recipes?pg=1&rpp=25&title_kw="
                + searchWord
                + "&api_key=" + apiKey;

        $.ajax(new AjaxOptions().url(recipeUrl).type("GET").dataType("json").context(this).success(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                Intent searchIntent = new Intent(Main.this, SearchResults.class);
                //JSON text
                String query = params[0].toString();
                //send JSON text to search result
                searchIntent.putExtra("query", query);
                //start search result
                startActivity(searchIntent);
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

    public void onMyRecipePressed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setMessage("Feel free to restart.").setTitle("Finished");
        AlertDialog dialog = builder.create();
        for (int counter = web; counter <= rip; counter++) {
            createDatabaseRecipe(Integer.toString(counter),this);
        }
        web = rip + 1;
        rip += 500;
    }

    public void onAddRecipePressed(View view) {
        Intent addRecipeIntent = new Intent(Main.this, AddRecipe.class);
        startActivity(addRecipeIntent);
    }

    public void onAccountPressed(View view) {
        ivUserInformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Double click image to go to the Profile page
                Intent profileIntent = new Intent(Main.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        ivUserInformation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Long click image to go to upload image page
                Intent pictureUploadIntent = new Intent(Main.this, pictureUpload.class);
                startActivity(pictureUploadIntent);
                return true;
            }
        });
    }

    //Checks to make sure the new URL is valid.
    //URL comes from Google+ profile pic.
    public boolean isValidUrl(URL url, String urlString) {
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }

        if (url != null) {
            return true;
        }
        return false;
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

    }

    private void onMenuProfilePressed(MenuItem item) {
        Intent profileIntent = new Intent(Main.this, Profile.class);
        startActivity(profileIntent);
    }

    private void onMenuPicPressed(MenuItem item) {
        Intent picIntent = new Intent(Main.this, pictureUpload.class);
        startActivity(picIntent);
    }

    private void onMenuAddRecipePressed(MenuItem item) {
        Intent addRecipeIntent = new Intent(Main.this, AddRecipe.class);
        startActivity(addRecipeIntent);
    }

    private void onLogoutPressed(MenuItem item) {
        //*****logout**********
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        System.exit(0);
        //********************
    }

    public void createDatabaseRecipe(String recipeId, Context hope) {
        recipeID = recipeId;
        Context thisOne = hope;
        final String recipeUrl = "https://api2.bigoven.com/recipe/"
                + recipeID
                + "?api_key=" + apiKey;

        $.ajax(new AjaxOptions().url(recipeUrl).type("GET").dataType("json").context(thisOne).success(new Function() {
            @Override
            public void invoke($ droidQuery, Object... params) {
                //get the title
                pls = params[0].toString();
                try {
                    //gets the JSON output from the website
                    JSONObject obj = new JSONObject(pls);
                    String temp = obj.getString("RecipeID");
                    temp = temp.replaceAll("\"","");
                    recipeLocation.child(temp).setValue(pls);
                }
                catch  (JSONException e) {
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

    }


    //Todo grab profile pic from database
    /*public void getFile(Uri profilePic) {
        File localFile = File.createTempFile(/*randaom stuff*//*);
        storage.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }*/
}
