package com.movie.brworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import application.MyApplication;
import pojo.PojoCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class RegistrationActivity extends AppCompatActivity {

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;
    private RelativeLayout mRelativeMain;
    private ProgressBar mProgressBar;

    LinearLayout llLogin;
    EditText etName, etEmail, etMobile, etPassword, etConfirmPassword;
    TextView btnSubmit;
    TextInputLayout tlName, tlEmail, tlMobile, tlPassword, tlConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApplication = (MyApplication) getApplicationContext();
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

        initailise();
        listers();
    }

    void initailise() {

        mRelativeMain = findViewById(R.id.relative_main);
        mProgressBar = findViewById(R.id.progressbar);

        llLogin = findViewById(R.id.llLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tlName = findViewById(R.id.tlName);
        tlEmail = findViewById(R.id.tlEmail);
        tlMobile = findViewById(R.id.tlMobile);
        tlPassword = findViewById(R.id.tlPassword);
        tlConfirmPassword = findViewById(R.id.tlConfirmPassword);

        btnSubmit = findViewById(R.id.btnSubmit);

    }

    void listers() {

        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);

                finish();


            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validations()) {

                    networkCallRegister();

                }

            }
        });

    }


    void networkCallRegister() {
        if (mApplication.isInternetConnected()) {
            mApplication.getRetroFitInterface().RegisterUser(etName.getText().toString() + "", etMobile.getText().toString() + "",
                    etEmail.getText().toString() + "", etPassword.getText().toString() + "").enqueue(mCallbackStaticStatus);

            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Utils.showSnackBar(mRelativeMain, getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Callback for network call for sub sub category
     */
    private Callback<PojoCommon> mCallbackStaticStatus = new Callback<PojoCommon>() {

        @Override
        public void onResponse(Call<PojoCommon> call, Response<PojoCommon>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {

                PojoCommon pojoParticipants = response.body();

                Utils.showSnackBar(mRelativeMain, pojoParticipants.getMessage() + "");

                if (pojoParticipants.getStatus() == 1) {

                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);

                    finish();

                }


                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<PojoCommon> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);

        }
    };

    boolean validations() {

        return validateName() && validateEmail() && validateMobile() && validatePassword() && validateConfimPassword() && validateMatchPasword();
    }

    boolean validateName() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            tlName.setErrorEnabled(true);
            tlName.setError("Enter Name");
            return false;
        } else {
            tlName.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateEmail() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            tlEmail.setErrorEnabled(true);
            tlEmail.setError("Enter Email");
            return false;
        } else {
            tlEmail.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateMobile() {
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            tlMobile.setErrorEnabled(true);
            tlMobile.setError("Enter Mobile");
            return false;
        } else {
            tlMobile.setErrorEnabled(false);
            return true;
        }
    }

    boolean validatePassword() {
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            tlPassword.setErrorEnabled(true);
            tlPassword.setError("Enter Password");
            return false;
        } else {
            tlPassword.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateConfimPassword() {
        if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            tlConfirmPassword.setErrorEnabled(true);
            tlConfirmPassword.setError("Enter Confirm Password");
            return false;
        } else {
            tlConfirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    boolean validateMatchPasword() {

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            tlConfirmPassword.setErrorEnabled(true);
            tlConfirmPassword.setError("Password does not match");
            return false;
        } else {
            tlConfirmPassword.setErrorEnabled(false);
            return true;
        }

    }

}
