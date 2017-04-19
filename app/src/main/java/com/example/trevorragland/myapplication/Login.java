package com.example.trevorragland.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trevorragland.myapplication.POJO.User;
import com.example.trevorragland.myapplication.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import static java.net.URI.create;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String mUserEmail, mPassword;
    private static final String LOG_TAG = Login.class.getSimpleName();
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 9001;
    Bundle userBundle = new Bundle();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userLocation = database.getReference().child("users");

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

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
                            AuthCredential credential = EmailAuthProvider.getCredential(mUserEmail, mPassword);
                            linkWithCredential(credential);

                            Intent mainIntent = new Intent(Login.this, Main.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                });
    }

    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            updateUI(true);

            //Bundles up the user info to send to main
            userBundle.putString("userName",acct.getDisplayName());
            userBundle.putString("userEmail",acct.getEmail());
            userBundle.putString("userID",acct.getId());
            userBundle.putString("userPic",acct.getPhotoUrl().toString());

            Intent mainIntent = new Intent(Login.this, Main.class);
            mainIntent.putExtras(userBundle);
            startActivity(mainIntent);
            finish();
        } else {        // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    //gives the option to add log out button i think
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

    //FIND OUT WHERE TO PUT THIS FOR GOOGLE
    public void createDatabaseGoogleUser(String username, String email, String pic) {
        /* newstuff start */
        HashMap<String, Object> joined = new HashMap<String, Object>();
        joined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        User googleUser = new User(username, email, joined, null, URI.create(pic));
        userLocation.child(encodeEmail(email)).setValue(googleUser);
                        /* newstuff end */
    }

    //FIND OUT WHERE TO PUT THIS FOR GOOGLE
    private void linkWithCredential (AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "linkWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}
