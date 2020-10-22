package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.brworld.R;

import java.util.ArrayList;
import java.util.List;

import adapter.NotificationAdapter;
import application.MyApplication;
import constants.ConstantCodes;
import pojo.PojoNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class NotificationFragment extends Fragment {

    private View mParentView;
    MyApplication mApplication;
    private Context mContext;
    private SharedPreferences mSharedPreference;
    private RelativeLayout mRelativeMain;
    private ProgressBar mProgressBar;

    private RecyclerView rvNotification;

    NotificationAdapter notificationAdapter;

    List<PojoNotification.Datum> mArraryNotifications = new ArrayList<>();


    TextView tv_no_record;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_notification, container, false);
        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initailise();
        listners();

        networkCallNotification();


        return mParentView;

    }

    void initailise() {

        Toolbar toolbar;
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);


        tv_no_record = mParentView.findViewById(R.id.tv_no_record);
        rvNotification = mParentView.findViewById(R.id.rvNotification);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(mContext);
        rvNotification.setLayoutManager(mLayoutManager2);
        rvNotification.setItemAnimator(new DefaultItemAnimator());


    }

    void listners() {


    }


    void networkCallNotification() {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().Notification(mSharedPreference.getString(ConstantCodes.LOGIN_USER_ID, "")).enqueue(mCallbackProductList);

            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Utils.showSnackBar(getActivity().findViewById(android.R.id.content), mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoNotification> mCallbackProductList = new Callback<PojoNotification>() {

        @Override
        public void onResponse(Call<PojoNotification> call, Response<PojoNotification>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoNotification pojoParticipants = response.body();

                if (isAdded()) {
                    if (pojoParticipants.getStatus() == 1) {
                        if (pojoParticipants.getData() != null) {


                            if (pojoParticipants.getData().size() > 0) {
                                mArraryNotifications.clear();
                                mArraryNotifications.addAll(pojoParticipants.getData());
                                getNotification();


                            } else {
                                if (!TextUtils.isEmpty(pojoParticipants.getMessage())) {
                                    tv_no_record.setVisibility(View.VISIBLE);
                                    tv_no_record.setText(pojoParticipants.getMessage() + "");

                                } else {
                                    tv_no_record.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {

                            if (!TextUtils.isEmpty(pojoParticipants.getMessage())) {
                                tv_no_record.setVisibility(View.VISIBLE);
                                tv_no_record.setText(pojoParticipants.getMessage() + "");

                            } else {
                                tv_no_record.setVisibility(View.VISIBLE);
                            }

                        }


                    } else {
                        if (!TextUtils.isEmpty(pojoParticipants.getMessage())) {
                            tv_no_record.setVisibility(View.VISIBLE);
                            tv_no_record.setText(pojoParticipants.getMessage() + "");

                        } else {
                            tv_no_record.setVisibility(View.VISIBLE);
                        }
                    }

                    mProgressBar.setVisibility(View.GONE);
                }

            }

        }

        @Override
        public void onFailure(Call<PojoNotification> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);


        }
    };

    void getNotification() {

        notificationAdapter = new NotificationAdapter(mContext, mArraryNotifications, customClick);
        rvNotification.setAdapter(notificationAdapter);


    }

    NotificationAdapter.CustomClick customClick = new NotificationAdapter.CustomClick() {
        @Override
        public void itemClick(String movieId) {

            Fragment fragment = new VideoDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle b = new Bundle();
            b.putString("movie_id", movieId);

            fragment.setArguments(b);
            ft.replace(R.id.frame_container, fragment).addToBackStack("BuilderBrokerFragment").commit();

        }
    };

}
