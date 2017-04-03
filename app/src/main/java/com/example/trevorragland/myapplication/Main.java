package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by AD on 3/16/2017.
 */

public class Main extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    //we haven't implemented this, so this button just closes the app
    public void onMyRecipePressed (View view) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        System.exit(0);
    }

    public void onAddRecipePressed (View view) {
        Intent addRecipeIntent = new Intent(Main.this, AddRecipe.class);
        startActivity(addRecipeIntent);
    }

    public void onAccountPressed (View view) {
        ImageView userIcon = (ImageView) findViewById(R.id.ivUserInformation);
        userIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Double click image to go to the Profile page
                Intent profileIntent = new Intent(Main.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        userIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Long click image to go to upload image page
                Intent pictureUploadIntent = new Intent(Main.this, pictureUpload.class);
                startActivity(pictureUploadIntent);
                return true;
            }
        });
    }
}
