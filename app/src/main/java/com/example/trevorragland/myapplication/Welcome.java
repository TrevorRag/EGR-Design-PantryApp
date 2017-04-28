package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by AD on 4/27/2017.
 */

public class Welcome extends AppCompatActivity {
    Button bLogin;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        bLogin = (Button) findViewById(R.id.bIn);
        bRegister = (Button) findViewById(R.id.bUp);
        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignInPressed(v);
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignUpPressed(v);
            }
        });
    }

    private void onSignInPressed (View view) {
        Intent loginIntent = new Intent(Welcome.this, Login.class);
        startActivity(loginIntent);
        finish();
    }

    private void onSignUpPressed (View view) {
        Intent registerIntent = new Intent(Welcome.this, Registration.class);
        startActivity(registerIntent);
        finish();
    }
}
