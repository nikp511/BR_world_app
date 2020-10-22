package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.movie.brworld.ForgotPassowordActivity;
import com.movie.brworld.LoginActivity;
import com.movie.brworld.R;

import application.MyApplication;
import constants.ConstantCodes;
import pojo.PojoCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

/**
 * Created by Rp on 8/30/2016.
 */
public class MyAccountFragment extends Fragment {

    private View mParentView;

    private Context mContext;
    RelativeLayout mRelativeMain;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;

    EditText etName, etEmail, etMobile, etAddress, etOldPassword, etNewPassword, etConfirmPassword;
    TextView btnUpdateDetail, btnUpdatePwd;
    ProgressBar mProgressBar;
    CardView cvPwd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_my_account, container, false);

        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initialise();
        listners();
        if (mSharedPreference.getBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, false)) {
            cvPwd.setVisibility(View.GONE);
        } else {
            cvPwd.setVisibility(View.VISIBLE);
        }

        return mParentView;
    }

    void initialise() {


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("My Account");

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);

        cvPwd = mParentView.findViewById(R.id.cvPwd);
        etName = mParentView.findViewById(R.id.etName);
        etEmail = mParentView.findViewById(R.id.etEmail);
        etMobile = mParentView.findViewById(R.id.etMobile);
        etAddress = mParentView.findViewById(R.id.etAddress);
        etOldPassword = mParentView.findViewById(R.id.etOldPassword);
        etNewPassword = mParentView.findViewById(R.id.etNewPassword);
        etConfirmPassword = mParentView.findViewById(R.id.etConfirmPassword);
        btnUpdateDetail = mParentView.findViewById(R.id.btnUpdateDetail);
        btnUpdatePwd = mParentView.findViewById(R.id.btnUpdatePwd);

        etName.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_NAME, ""));
        etAddress.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_ADDRESS, ""));
        etEmail.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_EMAIL, ""));
        etMobile.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_MOBILE, ""));
    }


    void listners() {

        btnUpdateDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().equals("")) {
                    networkCallUpdateDetail();
                } else {
                    Utils.showSnackBar(mRelativeMain, "Enter Name");
                }
            }
        });

        btnUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etOldPassword.getText().toString().equals("")) {

                    if (!etNewPassword.getText().toString().equals("")) {

                        if (!etConfirmPassword.getText().toString().equals("")) {

                            if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                                networkCallUpdatePwd();
                            } else {
                                Utils.showSnackBar(mRelativeMain, "Confirm Password Does Not Match");
                            }

                        } else {
                            Utils.showSnackBar(mRelativeMain, "Enter Confirm Password");
                        }

                    } else {
                        Utils.showSnackBar(mRelativeMain, "Enter New Password");
                    }

                } else {
                    Utils.showSnackBar(mRelativeMain, "Enter Old Password");
                }
            }
        });

    }

    private void networkCallUpdateDetail() {
        if (mApplication.isInternetConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mApplication.getRetroFitInterface().UpdateProfile("0",
                    mSharedPreference.getString(ConstantCodes.LOGIN_USER_ID, ""),
                    etName.getText().toString(),
                    etAddress.getText().toString(), null, null).enqueue(mCallbackDetail);

        } else {
            Utils.showSnackBar(mRelativeMain, mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoCommon> mCallbackDetail = new Callback<PojoCommon>() {

        @Override
        public void onResponse(Call<PojoCommon> call, Response<PojoCommon>
                response) {
            Log.e("successsss", "sucesssss");
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoCommon pojoParticipants = response.body();
                Log.e("successsss11", "sucesssss");

                if (pojoParticipants.getStatus() == 1) {

                    Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                    if (pojoParticipants.getData() != null) {
                        mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_NAME, "" + pojoParticipants.getData().getUsername()).apply();
                        mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_ADDRESS, "" + pojoParticipants.getData().getAddress()).apply();

                        etName.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_NAME, ""));
                        etAddress.setText("" + mSharedPreference.getString(ConstantCodes.LOGIN_USER_ADDRESS, ""));
                    }

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


    private void networkCallUpdatePwd() {
        if (mApplication.isInternetConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mApplication.getRetroFitInterface().UpdateProfile("1",
                    mSharedPreference.getString(ConstantCodes.LOGIN_USER_ID, ""),
                    null,
                    null, etOldPassword.getText().toString() + "",
                    etNewPassword.getText().toString() + "").enqueue(mCallbackChangePwd);

        } else {
            Utils.showSnackBar(mRelativeMain, mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoCommon> mCallbackChangePwd = new Callback<PojoCommon>() {

        @Override
        public void onResponse(Call<PojoCommon> call, Response<PojoCommon>
                response) {
            Log.e("successsss", "sucesssss");
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoCommon pojoParticipants = response.body();
                Log.e("successsss11", "sucesssss");

                if (pojoParticipants.getStatus() == 1) {
                    Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                    etOldPassword.setText("");
                    etNewPassword.setText("");
                    etConfirmPassword.setText("");


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
