package com.example.trevorragland.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.example.trevorragland.myapplication.utils.Constants;

import java.util.Map;

import static com.example.trevorragland.myapplication.R.id.etEmail;
import static com.example.trevorragland.myapplication.R.id.etPassword;
import static com.example.trevorragland.myapplication.R.id.etUsername;


public class Registration extends AppCompatActivity {
    private static final String LOG_TAG = Registration.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private Firebase mFirebaseRef;
    private String mUserName, mUserEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);

    /* Not sure if this goes here or on login
    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }*/

    public void onCreateAccountPressed(View view) {
        //Firebase account information
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mUserName = etUsername.getText().toString();   //maybe lowercase this one too
        mUserEmail = etEmail.getText().toString().toLowerCase();
        mPassword = etPassword.getText().toString();

        //checks
        boolean validEmail = isEmailVaild(mUserEmail);
        boolean validUserName = isUserNameValid(mUserName);
        boolean validPassword = isPasswordValid(mPassword);
        if (!validEmail || !validUserName || !validPassword) return;

        mAuthProgressDialog.show();

        //Account creation
        mFirebaseRef.createUser(mUserEmail, mPassword,
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        mAuthProgressDialog.dismiss();
                        Log.i(LOG_TAG, getString(R.string.log_message_aut_successful));
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Log.d(LOG_TAG, getString(R.string.log_error_occurred) + firebaseError);
                        mAuthProgressDialog.dismiss();

                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                            mEditTextEmailCreate.setError(getString(R.string.error_email_taken));
                        } else {
                            showErrorToast(firebaseError.getMessage());
                        }
                    }
                });
    }

    private void createUserInFirebaseHelper(final String encodedEmail) {

    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid), email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextUsernameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_no_valid));
            return false;
        }
        return true;
    }
    private void showErrorToast(String message) {
        Toast.makeText(Registration.this, message, Toast.LENGTH_LONG.show();
    }
}
