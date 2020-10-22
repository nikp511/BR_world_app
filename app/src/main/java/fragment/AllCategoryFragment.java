package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.brworld.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AllCategoryAdapter;
import application.MyApplication;
import pojo.PojoAllCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

/**
 * Created by Rp on 8/30/2016.
 */
public class AllCategoryFragment extends Fragment {

    private View mParentView;

    private Context mContext;
    RelativeLayout mRelativeMain;
    ProgressBar mProgressBar;
    TextView tv_no_record;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;

    private RecyclerView recyclerview;

    private List<PojoAllCategory.MovieDatum> mArrayData = new ArrayList<>();

    AllCategoryAdapter allCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_all_category, container, false);

        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initialise();
        listners();


        return mParentView;


    }

    void initialise() {


        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("" + getTag().toString());

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);
        tv_no_record = mParentView.findViewById(R.id.tv_no_record);

        ImageView ivHome, ivMovie, ivSeries, ivUpcoming;
        TextView tvUpcoming, tvSeries, tvMovie, tvHome;

        ivHome = getActivity().findViewById(R.id.ivHome);
        ivMovie = getActivity().findViewById(R.id.ivMovie);
        ivSeries = getActivity().findViewById(R.id.ivSeries);
        ivUpcoming = getActivity().findViewById(R.id.ivUpcoming);

        tvHome = getActivity().findViewById(R.id.tvHome);
        tvMovie = getActivity().findViewById(R.id.tvMovie);
        tvSeries = getActivity().findViewById(R.id.tvSeries);
        tvUpcoming = getActivity().findViewById(R.id.tvUpcoming);


        if (getTag().equals("Movies")) {

            tvHome.setTextColor(getResources().getColor(R.color.black));
            tvMovie.setTextColor(getResources().getColor(R.color.white));
            tvSeries.setTextColor(getResources().getColor(R.color.black));
            tvUpcoming.setTextColor(getResources().getColor(R.color.black));

            ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivMovie.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            ivSeries.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivUpcoming.setColorFilter(ContextCompat.getColor(mContext, R.color.black));

            networkCallData("movie");


        } else if (getTag().equals("Series")) {

            tvHome.setTextColor(getResources().getColor(R.color.black));
            tvMovie.setTextColor(getResources().getColor(R.color.black));
            tvSeries.setTextColor(getResources().getColor(R.color.white));
            tvUpcoming.setTextColor(getResources().getColor(R.color.black));

            ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivMovie.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivSeries.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            ivUpcoming.setColorFilter(ContextCompat.getColor(mContext, R.color.black));

            networkCallData("series");


        } else if (getTag().equals("Upcoming Movies")) {

            tvHome.setTextColor(getResources().getColor(R.color.black));
            tvMovie.setTextColor(getResources().getColor(R.color.black));
            tvSeries.setTextColor(getResources().getColor(R.color.black));
            tvUpcoming.setTextColor(getResources().getColor(R.color.white));

            ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivMovie.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivSeries.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
            ivUpcoming.setColorFilter(ContextCompat.getColor(mContext, R.color.white));

            networkCallData("upcoming");


        } else if (getTag().equals("YouTube")) {

            networkCallData("youtube");
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);

        recyclerview = (RecyclerView) mParentView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


    }


    void listners() {


    }


    void networkCallData(String menuCategory) {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().AllCategory(menuCategory).enqueue(mCallbackData);

            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Utils.showSnackBar(getActivity().findViewById(android.R.id.content), mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoAllCategory> mCallbackData = new Callback<PojoAllCategory>() {

        @Override
        public void onResponse(Call<PojoAllCategory> call, Response<PojoAllCategory>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoAllCategory pojoParticipants = response.body();

                if (isAdded()) {
                    if (pojoParticipants.getStatus() == 1) {
                        if (pojoParticipants.getData() != null) {


                            if (pojoParticipants.getData().getMovieData().size() > 0) {
                                mArrayData.clear();
                                mArrayData.addAll(pojoParticipants.getData().getMovieData());
                                getAllData();


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
                }
                mProgressBar.setVisibility(View.GONE);

            }

        }

        @Override
        public void onFailure(Call<PojoAllCategory> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);


        }
    };

    void getAllData() {

        allCategoryAdapter = new AllCategoryAdapter(mContext, mArrayData, customClick);
        recyclerview.setAdapter(allCategoryAdapter);


    }


    AllCategoryAdapter.CustomClick customClick = new AllCategoryAdapter.CustomClick() {
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
