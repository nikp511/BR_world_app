package com.movie.brworld;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import application.MyApplication;
import pojo.PojoCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;


/**
 * Created by jaydeep on 26/6/18.
 */

public class ForgotPassowordActivity extends AppCompatActivity {
    private Toolbar toolbar;

    ProgressBar mProgressBar;

    TextView btnOtp, btnSubmit;
    EditText etMobile, etPassword, etOTP;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;

    private Context mContext;
    private RelativeLayout mRelativeMain;

    TextInputLayout tlOTP, tlPassword;

    String otp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mApplication = (MyApplication) this.getApplicationContext();

        mContext = ForgotPassowordActivity.this;

        mProgressBar = findViewById(R.id.progressbar_forgot_password);

        mRelativeMain = findViewById(R.id.activity_main);

        etMobile = findViewById(R.id.etEmail);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        tlPassword = findViewById(R.id.tlPassword);
        tlOTP = findViewById(R.id.tlOTP);

        btnOtp = findViewById(R.id.btnOtp);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etOTP.getText().toString().equals("")) {
                    tlOTP.setErrorEnabled(false);

                    if (etOTP.getText().toString().equals(otp)) {
                        tlOTP.setErrorEnabled(false);

                        if (!etPassword.getText().toString().equals("")) {
                            tlPassword.setErrorEnabled(false);
                            networkCallNewPassword();

                        } else {
                            tlPassword.setErrorEnabled(true);
                            tlPassword.setError("Enter New Password");
                        }

                    } else {
                        tlOTP.setErrorEnabled(true);
                        tlOTP.setError("Invalid OTP");
                    }

                } else {
                    tlOTP.setErrorEnabled(true);
                    tlOTP.setError("Please Enter OTP");
                }

            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateMobile()) {
                    networkCallForgotPassword();
                }

            }
        });
    }

    boolean validateMobile() {
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            Utils.showSnackBar(mRelativeMain, "Enter Mobile No");
            return false;
        } else {
            return true;
        }
    }

    private void networkCallForgotPassword() {
        if (mApplication.isInternetConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mApplication.getRetroFitInterface().ForgotPassword("0", etMobile.getText().toString(), null).enqueue(mCallbackNew);

        } else {
            Utils.showSnackBar(mRelativeMain, mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoCommon> mCallbackNew = new Callback<PojoCommon>() {

        @Override
        public void onResponse(Call<PojoCommon> call, Response<PojoCommon>
                response) {
            Log.e("successsss", "sucesssss");
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoCommon pojoParticipants = response.body();
                Log.e("successsss11", "sucesssss");

                if (pojoParticipants.getStatus() == 1) {

                    otp = "" + pojoParticipants.getOtp();
                    tlOTP.setVisibility(View.VISIBLE);
                    tlPassword.setVisibility(View.VISIBLE);
                    btnOtp.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);

                } else {
                    Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                }
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<PojoCommon> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            Log.e("faillll", "failll");
            mProgressBar.setVisibility(View.GONE);

        }
    };


    private void networkCallNewPassword() {
        if (mApplication.isInternetConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mApplication.getRetroFitInterface().ForgotPassword("1", etMobile.getText().toString(), etPassword.getText().toString()).enqueue(mCallbackNewPwd);

        } else {
            Utils.showSnackBar(mRelativeMain, mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoCommon> mCallbackNewPwd = new Callback<PojoCommon>() {

        @Override
        public void onResponse(Call<PojoCommon> call, Response<PojoCommon>
                response) {
            Log.e("successsss", "sucesssss");
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoCommon pojoParticipants = response.body();
                Log.e("successsss11", "sucesssss");

                if (pojoParticipants.getStatus() == 1) {
                    Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                    Intent i = new Intent(ForgotPassowordActivity.this, LoginActivity.class);
                    startActivity(i);

                    finish();

                } else {
                    Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                }
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<PojoCommon> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            Log.e("faillll", "failll");
            mProgressBar.setVisibility(View.GONE);

        }
    };


}
