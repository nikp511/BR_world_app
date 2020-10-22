package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.brworld.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SearchAdapter;
import application.MyApplication;
import pojo.PojoAllCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

/**
 * Created by Rp on 8/30/2016.
 */
public class SearchFragment extends Fragment {

    private View mParentView;

    private Context mContext;
    RelativeLayout mRelativeMain;
    ProgressBar mProgressBar;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;
    androidx.appcompat.widget.SearchView searchView;
    private List<PojoAllCategory.MovieDatum> mArrayData = new ArrayList<>();
    SearchAdapter searchAdapter;
    RecyclerView rvSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_search, container, false);

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
        toolbar.setTitle("Search");

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);

        searchView = mParentView.findViewById(R.id.searchView);

        rvSearch = mParentView.findViewById(R.id.rvSearch);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(mContext);
        rvSearch.setLayoutManager(mLayoutManager2);
        rvSearch.setItemAnimator(new DefaultItemAnimator());

        searchAdapter = new SearchAdapter(mContext, mArrayData, customClick);
        rvSearch.setAdapter(searchAdapter);


    }


    void listners() {

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                networkCallData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                networkCallData(newText);
                return true;
            }
        });


    }


    void networkCallData(String keyword) {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().Search(keyword).enqueue(mCallbackData);

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
                                mArrayData.clear();
                                searchAdapter.notifyDataSetChanged();
                            }

                        } else {
                            mArrayData.clear();
                            searchAdapter.notifyDataSetChanged();
                        }


                    } else {
                        mArrayData.clear();
                        searchAdapter.notifyDataSetChanged();
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

        searchAdapter = new SearchAdapter(mContext, mArrayData, customClick);
        rvSearch.setAdapter(searchAdapter);


    }


    SearchAdapter.CustomClick customClick = new SearchAdapter.CustomClick() {
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
