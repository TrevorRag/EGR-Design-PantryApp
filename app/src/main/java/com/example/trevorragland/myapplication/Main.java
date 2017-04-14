package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.ImageView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by AD on 3/16/2017.
 */

public class Main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView ivUserInformation;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String email;
    private String profilePic;
    private String name;
    private String googleUser;
    URL pic = null;

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

        /* This block is the user information from the Google account information */
        storage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = getIntent().getStringExtra("userEmail");
        profilePic = getIntent().getStringExtra("userPic");
        name = getIntent().getStringExtra("userName");
        googleUser = getIntent().getStringExtra("userID");
        /*                                                                       */

        //sets the Google URL to imageview
        if (isValidUrl(pic, profilePic)) {
            new DownloadImageTask((ImageView) findViewById(R.id.ivUserInformation))
                    .execute(profilePic);
        }
        //elseif get pic from database.
    }
    
    //we haven't implemented this, so this button just closes the app
    public void onMyRecipePressed(View view) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        System.exit(0);
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

    //Turns the specified URL (Google profile pic) into a bitmap
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuHomePressed(MenuItem item) {
        Intent mainIntent = new Intent(Main.this, Main.class);
        startActivity(mainIntent);
        finish();
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
