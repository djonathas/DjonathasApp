package com.example.catolica.djonathasapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_GOOGLE_SING_IN = 1;
    private static final String TAG = "login";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i(TAG, "onAuthStateChanged Signin Google Plus:signed_in" + user.getUid());
                    Toast.makeText(LoginActivity.this, user.getDisplayName(), Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "onAuthStateChanged Signin Google Plus:signed_out");

                }
            }
        };
    }
}
