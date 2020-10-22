package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.movie.brworld.R;

import java.util.ArrayList;
import java.util.List;

import adapter.HomeAllCategoryAdapter;
import adapter.HomeBannerAdapter;
import adapter.NotificationAdapter;
import application.MyApplication;
import constants.ConstantCodes;
import pojo.PojoHome;
import pojo.PojoNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

/**
 * Created by Rp on 8/30/2016.
 */
public class HomeFragment extends Fragment {

    private View mParentView;

    private Context mContext;
    RelativeLayout mRelativeMain;
    ProgressBar mProgressBar;

    MyApplication mApplication;
    private SharedPreferences mSharedPreference;

    private RecyclerView rvPopularBr, rvPopularBollywood, rvAllCategory;

    HomeAllCategoryAdapter homeAllCategoryAdapter;
    HomeBannerAdapter homeBannerAdapter;
    List<PojoHome.MovieDatum> mArrayCategory = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_home, container, false);

        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initialise();
        listners();

        networkCallHomeData();

        return mParentView;
    }

    void initialise() {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("BR World");

        mRelativeMain = mParentView.findViewById(R.id.relative_main);
        mProgressBar = mParentView.findViewById(R.id.progressbar);

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


        tvHome.setTextColor(getResources().getColor(R.color.white));
        tvMovie.setTextColor(getResources().getColor(R.color.black));
        tvSeries.setTextColor(getResources().getColor(R.color.black));
        tvUpcoming.setTextColor(getResources().getColor(R.color.black));

        ivHome.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
        ivMovie.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
        ivSeries.setColorFilter(ContextCompat.getColor(mContext, R.color.black));
        ivUpcoming.setColorFilter(ContextCompat.getColor(mContext, R.color.black));


        rvPopularBr = (RecyclerView) mParentView.findViewById(R.id.rvPopularBr);
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvPopularBr.setLayoutManager(mLayoutManager4);
        rvPopularBr.setItemAnimator(new DefaultItemAnimator());


        rvPopularBollywood = (RecyclerView) mParentView.findViewById(R.id.rvPopularBollywood);
        RecyclerView.LayoutManager mLayoutManager554 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvPopularBollywood.setLayoutManager(mLayoutManager554);
        rvPopularBollywood.setItemAnimator(new DefaultItemAnimator());


        rvAllCategory = (RecyclerView) mParentView.findViewById(R.id.rvAllCategory);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAllCategory.setLayoutManager(mLayoutManager3);
        rvAllCategory.setItemAnimator(new DefaultItemAnimator());

    }


    void listners() {
    }


    void networkCallHomeData() {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().Home(mSharedPreference.getString(ConstantCodes.LOGIN_USER_ID, "")).enqueue(mCallbackHomedata);

            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Utils.showSnackBar(getActivity().findViewById(android.R.id.content), mContext.getResources().getString(R.string
                    .message_connection));
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private Callback<PojoHome> mCallbackHomedata = new Callback<PojoHome>() {

        @Override
        public void onResponse(Call<PojoHome> call, Response<PojoHome>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoHome pojoParticipants = response.body();

                if (isAdded()) {
                    if (pojoParticipants.getStatus() == 1) {
                        if (pojoParticipants.getData() != null) {

                            mArrayCategory.clear();

                            //first banner
                            if (pojoParticipants.getData().getFirstBanners() != null) {
                                if (pojoParticipants.getData().getFirstBanners().size() > 0) {
                                    List<String> path = new ArrayList<>();
                                    List<String> pathId = new ArrayList<>();
                                    for (int i = 0; i < pojoParticipants.getData().getFirstBanners().size(); i++) {
                                        path.add(pojoParticipants.getData().getFirstBanners().get(i).getPath());
                                        pathId.add(pojoParticipants.getData().getFirstBanners().get(i).getMovieId());
                                    }

                                    getFirstBanner(path, pathId);
                                }
                            }

                            //second banner
                            if (pojoParticipants.getData().getSecoundBanners() != null) {
                                if (pojoParticipants.getData().getSecoundBanners().size() > 0) {
                                    List<String> path = new ArrayList<>();
                                    List<String> pathId = new ArrayList<>();
                                    for (int i = 0; i < pojoParticipants.getData().getSecoundBanners().size(); i++) {
                                        path.add(pojoParticipants.getData().getSecoundBanners().get(i).getPath());
                                        pathId.add(pojoParticipants.getData().getSecoundBanners().get(i).getMovieId());
                                    }

                                    getSecondtBanner(path, pathId);
                                }
                            }

                            // all category
                            if (pojoParticipants.getData().getMovieData() != null) {
                                if (pojoParticipants.getData().getMovieData().size() > 0) {
                                    mArrayCategory.addAll(pojoParticipants.getData().getMovieData());
                                    getAllCategory();
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
        public void onFailure(Call<PojoHome> call, Throwable t) {
            Utils.showSnackBar(mRelativeMain, getString(R.string
                    .message_something_wrong));
            mProgressBar.setVisibility(View.GONE);


        }
    };

    void getFirstBanner(List<String> banner, List<String> pathIds) {

        homeBannerAdapter = new HomeBannerAdapter(mContext, banner, pathIds, bannerClick);
        rvPopularBr.setAdapter(homeBannerAdapter);
        rvPopularBr.addItemDecoration(new LinePagerIndicatorDecoration());
    }

    void getSecondtBanner(List<String> banner, List<String> pathIds) {

        homeBannerAdapter = new HomeBannerAdapter(mContext, banner, pathIds, bannerClick);
        rvPopularBollywood.setAdapter(homeBannerAdapter);
        rvPopularBollywood.addItemDecoration(new LinePagerIndicatorDecoration());

    }

    void getAllCategory() {

        homeAllCategoryAdapter = new HomeAllCategoryAdapter(mContext, mArrayCategory, customClick);
        rvAllCategory.setAdapter(homeAllCategoryAdapter);

    }

    HomeBannerAdapter.CustomClick bannerClick = new HomeBannerAdapter.CustomClick() {
        @Override
        public void itemClick(String id) {
            Fragment fragment = new VideoDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle b = new Bundle();
            b.putString("movie_id", id);

            fragment.setArguments(b);
            ft.replace(R.id.frame_container, fragment).addToBackStack("BuilderBrokerFragment").commit();
        }
    };

    HomeAllCategoryAdapter.CustomClick customClick = new HomeAllCategoryAdapter.CustomClick() {
        @Override
        public void itemClick(String id) {

            //Toast.makeText(mContext, "movie" + id, Toast.LENGTH_SHORT).show();
            Fragment fragment = new VideoDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle b = new Bundle();
            b.putString("movie_id", id);

            fragment.setArguments(b);
            ft.replace(R.id.frame_container, fragment).addToBackStack("BuilderBrokerFragment").commit();
        }


    };


    public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

        private int colorActive = 0xFFFFFFFF;
        private int colorInactive = 0x66FFFFFF;

        private final float DP = Resources.getSystem().getDisplayMetrics().density;

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private final int mIndicatorHeight = (int) (DP * 16);

        /**
         * Indicator stroke width.
         */
        private final float mIndicatorStrokeWidth = DP * 2;

        /**
         * Indicator width.
         */
        private final float mIndicatorItemLength = DP * 16;
        /**
         * Padding between indicators.
         */
        private final float mIndicatorItemPadding = DP * 4;

        /**
         * Some more natural animation interpolation
         */
        private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

        private final Paint mPaint = new Paint();

        public LinePagerIndicatorDecoration() {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(mIndicatorStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            int itemCount = parent.getAdapter().getItemCount();

            // center horizontally, calculate width and subtract half from center
            float totalLength = mIndicatorItemLength * itemCount;
            float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
            float indicatorTotalWidth = totalLength + paddingBetweenItems;
            float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

            // center vertically in the allotted space
            float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


            // find active page (which should be highlighted)
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int activePosition = layoutManager.findFirstVisibleItemPosition();
            if (activePosition == RecyclerView.NO_POSITION) {
                return;
            }

            // find offset of active page (if the user is scrolling)
            final View activeChild = layoutManager.findViewByPosition(activePosition);
            int left = activeChild.getLeft();
            int width = activeChild.getWidth();

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
        }

        private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
            mPaint.setColor(colorInactive);

            // width of item indicator including padding
            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            float start = indicatorStartX;
            for (int i = 0; i < itemCount; i++) {
                // draw the line for every item
                c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth;
            }
        }

        private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                    int highlightPosition, float progress, int itemCount) {
            mPaint.setColor(colorActive);

            // width of item indicator including padding
            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            if (progress == 0F) {
                // no swipe, draw a normal indicator
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
            } else {
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                // calculate partial highlight
                float partialLength = mIndicatorItemLength * progress;

                // draw the cut off highlight
                c.drawLine(highlightStart + partialLength, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

                // draw the highlight overlapping to the next item as well
                if (highlightPosition < itemCount - 1) {
                    highlightStart += itemWidth;
                    c.drawLine(highlightStart, indicatorPosY,
                            highlightStart + partialLength, indicatorPosY, mPaint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = mIndicatorHeight;
        }
    }

}
