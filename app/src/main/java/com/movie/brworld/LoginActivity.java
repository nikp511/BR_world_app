package com.movie.brworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import application.MyApplication;
import constants.ConstantCodes;
import helpers.SMSListner;
import helpers.SMSReceiver;
import pojo.PojoCommon;
import pojo.PojoLogin;
import pojo.PojoLoginOTPObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;


public class LoginActivity extends AppCompatActivity {


    TextView btnLogin, tvRegister, tvForgetPassword;
    EditText etName, etPassword;
    RelativeLayout mRelativeMain;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;

    MyApplication mApplication;

    PojoLoginOTPObject pojoLoginOTPObject;

    String GetUserID = "";
    String GetUserEmailID = "";
    String GetUserMobile = "";
    String GetUserName = "";


    LinearLayout llRegister;

    GoogleSignInClient mGoogleSignInClient;

    ImageView ivGoogle;

    TextInputLayout tlName, tlPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApplication = (MyApplication) this.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initailize();
        listners();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    void initailize() {

        mProgressBar = findViewById(R.id.progressbar);
        mRelativeMain = findViewById(R.id.relative_main);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.register);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);

        tlName = findViewById(R.id.tlName);
        tlPassword = findViewById(R.id.tlPassword);

        llRegister = findViewById(R.id.llRegister);

        ivGoogle = findViewById(R.id.ivGoogle);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            // Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show();

            Log.e("email", account.getEmail() + "");
            Log.e("token", account.getIdToken() + "");
            Log.e("id", account.getId() + "");
            Log.e("name", account.getDisplayName());
            Log.e("name2", account.getGivenName());

            networkCallSocialLogin(account.getDisplayName(), account.getId(), account.getEmail());


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Result: ", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    void listners() {

        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                if (account != null) {

                    networkCallSocialLogin(account.getDisplayName(), account.getId(), account.getEmail());

                } else {

                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 100);
                }

            }
        });

        llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);

                finish();
            }
        });


        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, ForgotPassowordActivity.class);
                startActivity(i);

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    Utils.hideKeyboard(LoginActivity.this);
                    networkCallLogin();


                }

            }
        });


    }


    void networkCallLogin() {
        if (mApplication.isInternetConnected()) {
            mApplication.getRetroFitInterface().LoginUser(etName.getText().toString() + "",
                    etPassword.getText().toString() + "").enqueue(mCallbackStaticStatus);

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
    private Callback<PojoLogin> mCallbackStaticStatus = new Callback<PojoLogin>() {

        @Override
        public void onResponse(Call<PojoLogin> call, Response<PojoLogin>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {

                PojoLogin pojoParticipants = response.body();

                Utils.showSnackBar(mRelativeMain, pojoParticipants.getMessage() + "");

                if (pojoParticipants.getStatus() == 1) {

                    if (pojoParticipants.getData() != null) {
                        if (pojoParticipants.getData().size() > 0) {

                            mSharedPreferences.edit().putBoolean(ConstantCodes.IS_LOGIN, true).apply();

                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_ID, pojoParticipants.getData().get(0).getId()).apply();
                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_NAME, pojoParticipants.getData().get(0).getUsername()).apply();
                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_MOBILE, pojoParticipants.getData().get(0).getMobile()).apply();
                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_EMAIL, pojoParticipants.getData().get(0).getEmail()).apply();

                            mSharedPreferences.edit().putBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, false).apply();

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                            finish();
                        }
                    }


                }


                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<PojoLogin> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);

        }
    };


    void networkCallSocialLogin(String username, String token, String email) {
        if (mApplication.isInternetConnected()) {
            mApplication.getRetroFitInterface().GoogleLogin(username,
                    token, email).enqueue(mCallbackSocialLogin);

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
    private Callback<PojoLogin> mCallbackSocialLogin = new Callback<PojoLogin>() {

        @Override
        public void onResponse(Call<PojoLogin> call, Response<PojoLogin>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {

                PojoLogin pojoParticipants = response.body();

                Utils.showSnackBar(mRelativeMain, pojoParticipants.getMessage() + "");

                if (pojoParticipants.getStatus() == 1) {

                    if (pojoParticipants.getData() != null) {
                        if (pojoParticipants.getData().size() > 0) {

                            mSharedPreferences.edit().putBoolean(ConstantCodes.IS_LOGIN, true).apply();

                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_ID, pojoParticipants.getData().get(0).getId()).apply();
                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_NAME, pojoParticipants.getData().get(0).getUsername()).apply();
                            // mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_MOBILE, pojoParticipants.getData().get(0).getMobile()).apply();
                            mSharedPreferences.edit().putString(ConstantCodes.LOGIN_USER_EMAIL, pojoParticipants.getData().get(0).getEmail()).apply();

                            mSharedPreferences.edit().putBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, true).apply();

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                            finish();
                        }
                    }


                }


                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<PojoLogin> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);

        }
    };

    boolean validation() {


        return validateName() && validatePassword();

    }


    boolean validateName() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            tlName.setErrorEnabled(true);
            tlName.setError("Enter Email");
            return false;
        } else {
            tlName.setErrorEnabled(false);
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


}
