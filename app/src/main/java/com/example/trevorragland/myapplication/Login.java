package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String mUserEmail, mPassword;
    private static final String LOG_TAG = Login.class.getSimpleName();
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 9001;

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(Login.this, Registration.class);
                startActivity(registerIntent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        findViewById(R.id.login_with_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_with_google:
                        googleSignIn();
                        break;
                }
            }
        });
    }

    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void signIn(View view) {
        mUserEmail = etUsername.getText().toString().toLowerCase();
        mPassword = etPassword.getText().toString();

        boolean validUserName = isUserNameValid(mUserEmail);
        boolean validPassword = isPasswordValid(mPassword);
        if (!validUserName || !validPassword) return;

        mAuth.signInWithEmailAndPassword(mUserEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithEmail", task.getException());
                            Toast.makeText(Login.this, "Username or Password is incorrect!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent profileIntent = new Intent(Login.this, Profile.class);
                            startActivity(profileIntent);
                            finish();
                        }
                    }
                });
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            etPassword.setError(getResources().getString(R.string.error_invalid_password_no_valid));
            return false;
        }
        return true;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            etUsername.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            updateUI(true);
            Intent profileIntent = new Intent(Login.this, Profile.class);
            startActivity(profileIntent);
            finish();
        } else {        // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.login_with_google).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            findViewById(R.id.login_with_google).setVisibility(View.VISIBLE);
            // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
