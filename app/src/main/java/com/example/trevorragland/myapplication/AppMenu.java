package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by AD on 4/12/2017.
 */

public class AppMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent mainIntent = new Intent(AppMenu.this, Main.class);
        startActivity(mainIntent);
    }

    private void onMenuProfilePressed(MenuItem item) {
        Intent profileIntent = new Intent(AppMenu.this, Profile.class);
        startActivity(profileIntent);
    }

    private void onMenuPicPressed(MenuItem item) {
        Intent picIntent = new Intent(AppMenu.this, pictureUpload.class);
        startActivity(picIntent);
    }

    private void onMenuAddRecipePressed(MenuItem item) {
        Intent addRecipeIntent = new Intent(AppMenu.this, AddRecipe.class);
        startActivity(addRecipeIntent);
    }
}
