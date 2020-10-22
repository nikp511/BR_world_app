package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.movie.brworld.PlayerMyActivity;
import com.movie.brworld.R;


import java.util.ArrayList;
import java.util.List;

import adapter.MoreLikeAdapter;
import application.MyApplication;
import constants.ConstantCodes;
import pojo.PojoDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

import static android.view.View.VISIBLE;

/**
 * Created by Rp on 8/30/2016.
 */
public class VideoDetailFragment extends Fragment {

    private View mParentView;

    private Context mContext;
    RelativeLayout mRelativeMain;
    ProgressBar mProgressBar;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;

    VideoView videoView;

    private RecyclerView rvMoreLikeThis;

    List<PojoDetail.LikeThi> mArrayMoreLike = new ArrayList<>();
    MoreLikeAdapter moreLikeAdapter;

    ImageView ivPlay, ivMovie, ivShare;
    TextView tvMovieDescription, tvMovieName;
    String menuCategory = "", movieUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_video_detail, container, false);

        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initialise();
        listners();

        networkCallDetailData();

        return mParentView;


    }

    void initialise() {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Video Detail");


        ivPlay = mParentView.findViewById(R.id.ivPlay);
        ivMovie = mParentView.findViewById(R.id.ivMovie);
        tvMovieName = mParentView.findViewById(R.id.tvMovieName);
        tvMovieDescription = mParentView.findViewById(R.id.tvMovieDescription);

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);

        ivShare = mParentView.findViewById(R.id.ivShare);

        rvMoreLikeThis = (RecyclerView) mParentView.findViewById(R.id.rvMoreLikeThis);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvMoreLikeThis.setLayoutManager(mLayoutManager1);
        rvMoreLikeThis.setItemAnimator(new DefaultItemAnimator());

    }


    void listners() {

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "BR WORLD");
                    String sAux = "\nI am watching " + tvMovieName.getText().toString() + "\n\nInstall BRWORLD  Mobile Movie Application Watching Free !!!!!\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.movie.brworld \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivityForResult(Intent.createChooser(i, "choose one"), 200);
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] arrayMovie = mSharedPreference.getString(ConstantCodes.SHARED_MOVE_ID, "").split(",");
                List<String> newMovieId = new ArrayList<>();
                for (int i = 0; i < arrayMovie.length; i++) {
                    newMovieId.add(arrayMovie[i]);
                }
                if (newMovieId.contains(getArguments().getString("movie_id"))) {
                    if (movieUrl != null) {
                        if (!movieUrl.equals("")) {

                            if (menuCategory.equals("youtube") || menuCategory.equals("upcoming")) {

                         /*   Fragment fragment = new MyYoutubeFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            ft.replace(R.id.frame_container, fragment).addToBackStack("BuilderBrokerFragment").commit();*/

                                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                                        getActivity(), "AIzaSyCNzx0HWgBa1_gqSf5zjcWIg-cO3O1YOFI", movieUrl);
                                getActivity().startActivity(intent);
                            } else {

                                Intent i = new Intent(getActivity(), PlayerMyActivity.class);
                                i.putExtra("path", movieUrl);
                                getActivity().startActivity(i);


                            }


                        } else {
                            Utils.showSnackBar(mRelativeMain, "Sorry, No Movie Found");
                        }
                    } else {
                        Utils.showSnackBar(mRelativeMain, "Sorry, No Movie Found");
                    }
                } else {
                    Utils.showSnackBar(mRelativeMain, "Please Click on Share button and Share Movie To Proceed.");
                }
            }
        });


    }


    void networkCallDetailData() {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().DetailInfo(getArguments().getString("movie_id")).enqueue(mCallbackHomedata);

            mProgressBar.setVisibility(VISIBLE);
        } else {
            Utils.showSnackBar(getActivity().findViewById(android.R.id.content), mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoDetail> mCallbackHomedata = new Callback<PojoDetail>() {

        @Override
        public void onResponse(Call<PojoDetail> call, Response<PojoDetail>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoDetail pojoParticipants = response.body();

                if (isAdded()) {
                    if (pojoParticipants.getStatus() == 1) {
                        if (pojoParticipants.getData() != null) {

                            mArrayMoreLike.clear();

                            if (pojoParticipants.getData().getMovieData() != null) {
                                if (pojoParticipants.getData().getMovieData().size() > 0) {

                                    menuCategory = "" + pojoParticipants.getData().getMovieData().get(0).getMenuCategory();
                                    movieUrl = "" + pojoParticipants.getData().getMovieData().get(0).getPath();
                                    tvMovieDescription.setText("" + pojoParticipants.getData().getMovieData().get(0).getDiscription());
                                    tvMovieName.setText("" + pojoParticipants.getData().getMovieData().get(0).getMovieName());

                                    if (pojoParticipants.getData().getMovieData().get(0).getMovieImage() != null) {
                                        if (!pojoParticipants.getData().getMovieData().get(0).getMovieImage().equals("")) {

                                            Glide.with(mContext).load(pojoParticipants.getData().getMovieData().get(0).getMovieImage()).into(ivMovie);
                                        }
                                    }
                                }
                            }

                            if (pojoParticipants.getData().getLikeThis() != null) {
                                if (pojoParticipants.getData().getLikeThis().size() > 0) {
                                    mArrayMoreLike.addAll(pojoParticipants.getData().getLikeThis());
                                    getLikeThis();
                                }
                            }


                        } else {

                            Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());


                        }


                    } else {
                        Utils.showSnackBar(mRelativeMain, "" + pojoParticipants.getMessage());
                    }

                    mProgressBar.setVisibility(View.GONE);
                }

            }

        }

        @Override
        public void onFailure(Call<PojoDetail> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);


        }
    };

    void getLikeThis() {
        moreLikeAdapter = new MoreLikeAdapter(mContext, mArrayMoreLike, customClick);
        rvMoreLikeThis.setAdapter(moreLikeAdapter);
    }


    MoreLikeAdapter.CustomClick customClick = new MoreLikeAdapter.CustomClick() {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {

            mSharedPreference.edit().putString(ConstantCodes.SHARED_MOVE_ID, getArguments().getString("movie_id") + "").apply();

        }
    }


}
