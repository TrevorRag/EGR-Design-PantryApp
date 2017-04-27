package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.trevorragland.myapplication.POJO.Recipe;
import com.example.trevorragland.myapplication.Profile.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.trevorragland.myapplication.Login.encodeEmail;

/**
 * Created by trial on 3/16/2017.
 */

public class AddRecipe extends AppCompatActivity {
    //Variables
    private String mRecipeName, mRecipeIngredients, mRecipePrep;
    EditText etRecipeName;
    EditText etRecipeIngredients;
    EditText etRecipePreparation;
    //for Firebase Database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    //current user email
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        etRecipeName = (EditText) findViewById(R.id.recipeName);
        etRecipeIngredients= (EditText) findViewById(R.id.recipeIngredients);
        etRecipePreparation= (EditText) findViewById(R.id.cookingProcess);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //function to saveRecipe in Firebase
    public void saveRecipe(View view){
        //Ingredients
        mRecipeName= etRecipeName.getText().toString();
        mRecipeIngredients= etRecipeIngredients.getText().toString();
        mRecipePrep= etRecipePreparation.getText().toString();

        //gets the basic user information from firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        //points to which tree you are going to add the recipe to
        DatabaseReference recipeLocation = database.getReference().child("users").child(encodeEmail(email)).child("Recipes");

        //builds the recipe using the Recipe POJO
        Recipe currentRecipe = new Recipe (mRecipeIngredients, mRecipePrep);

        //adds the recipe to the firebase database
        recipeLocation.child(mRecipeName).setValue(currentRecipe);

        etRecipeIngredients.setText("");
        etRecipeName.setText("");
        etRecipePreparation.setText("");
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
        Intent profileIntent = new Intent(AddRecipe.this, Profile.class);
        startActivity(profileIntent);
        finish();
    }

    private void onMenuPicPressed(MenuItem item) {
        Intent picIntent = new Intent(AddRecipe.this, pictureUpload.class);
        startActivity(picIntent);
        finish();
    }

    private void onMenuAddRecipePressed(MenuItem item) {
        Intent addRecipeIntent = new Intent(AddRecipe.this, AddRecipe.class);
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
