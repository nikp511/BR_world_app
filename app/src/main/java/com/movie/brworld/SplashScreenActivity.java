package com.movie.brworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import constants.ConstantCodes;


public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);

                    if (mSharedPreferences.getBoolean(ConstantCodes.IS_LOGIN, false)) {
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();
                    }

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
