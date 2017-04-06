package com.example.trevorragland.myapplication;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.trevorragland.myapplication.Login.encodeEmail;

public class Profile extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        final TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        final TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);

        getCurrentUser();
        DatabaseReference userLocation = database.getReference().child("users").child(encodeEmail(email));
        tvUsername.setText(name);
        tvEmail.setText(email);

        userLocation.child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phoneNumber = dataSnapshot.getValue(String.class);
                tvPhone.setText(phoneNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //call this for user information
    public void getCurrentUser() {

        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }
}
