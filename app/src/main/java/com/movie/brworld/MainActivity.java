package com.movie.brworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import application.MyApplication;
import constants.ConstantCodes;
import fragment.AllCategoryFragment;
import fragment.HomeFragment;
import fragment.MyAccountFragment;
import fragment.NotificationFragment;
import fragment.SearchFragment;
import pojo.PojoNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    MyApplication mApplication;

    SharedPreferences mSharedPreference;

    NavigationView navigationView;

    LinearLayout llHome, llMovie, llSeries, llUpcoming;
    ImageView ivHome, ivMovie, ivSeries, ivUpcoming;
    TextView tvUpcoming, tvSeries, tvMovie, tvHome, tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        mApplication = (MyApplication) this.getApplicationContext();
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        setDrawer();

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvName = (TextView) headerView.findViewById(R.id.tvName);
        tvName.setText("Hello, " + mSharedPreference.getString(ConstantCodes.LOGIN_USER_NAME, ""));

        llHome = findViewById(R.id.llHome);
        llMovie = findViewById(R.id.llMovie);
        llSeries = findViewById(R.id.llSeries);
        llUpcoming = findViewById(R.id.llUpcoming);

        ivHome = findViewById(R.id.ivHome);
        ivMovie = findViewById(R.id.ivMovie);
        ivSeries = findViewById(R.id.ivSeries);
        ivUpcoming = findViewById(R.id.ivUpcoming);

        tvHome = findViewById(R.id.tvHome);
        tvMovie = findViewById(R.id.tvMovie);
        tvSeries = findViewById(R.id.tvSeries);
        tvUpcoming = findViewById(R.id.tvUpcoming);


        changeFragment(new HomeFragment(), "");
        setAppTitle("BR World");

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new HomeFragment(), "");
            }
        });

        llMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeFragment(new AllCategoryFragment(), "Movies");

                networkCallNotification();

                Fragment fragment = new AllCategoryFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();
            }
        });

        llSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeFragment(new AllCategoryFragment(), "Series");

                networkCallNotification();

                Fragment fragment = new AllCategoryFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frame_container, fragment, "Series").addToBackStack("BuilderBrokerFragment").commit();
            }
        });

        llUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // changeFragment(new AllCategoryFragment(), "Upcoming Movies");

                networkCallNotification();

                Fragment fragment = new AllCategoryFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frame_container, fragment, "Upcoming Movies").addToBackStack("BuilderBrokerFragment").commit();
            }
        });


    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void changeFragment(Fragment targetFragment, String tag) {

        networkCallNotification();

        tvHome.setTextColor(getResources().getColor(R.color.black));
        tvMovie.setTextColor(getResources().getColor(R.color.black));
        tvSeries.setTextColor(getResources().getColor(R.color.black));
        tvUpcoming.setTextColor(getResources().getColor(R.color.black));

        ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black));
        ivMovie.setColorFilter(ContextCompat.getColor(this, R.color.black));
        ivSeries.setColorFilter(ContextCompat.getColor(this, R.color.black));
        ivUpcoming.setColorFilter(ContextCompat.getColor(this, R.color.black));

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, targetFragment, tag)
                .commit();
    }

    private FragmentManager.OnBackStackChangedListener mBackStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    updateDrawerToggle();
                }
            };

    /***
     * Update drawer toggle
     */
    private void updateDrawerToggle() {
        if (drawerToggle == null) {
            return;
        }
        boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
        drawerToggle.setDrawerIndicatorEnabled(isRoot);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
            getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
            getSupportActionBar().setHomeButtonEnabled(!isRoot);
        }
        if (isRoot) {
            drawerToggle.syncState();
        }
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            showExitAlert("e", "Are you sure you want to exit ?");
        }
    }

    void showExitAlert(final String type, String msg) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                MainActivity.this);
// Setting Dialog Message
        alertDialog2.setMessage(msg);
// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        if (type.equals("e")) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        } else if (type.equals("l")) {

                            if (mSharedPreference.getBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, false)) {
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestEmail()
                                        .build();

                                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // There's immediate result available.

                                            mSharedPreference.edit().putBoolean(ConstantCodes.IS_LOGIN, false).apply();
                                            mSharedPreference.edit().putBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, false).apply();

                                            mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_ID, "").apply();
                                            mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_NAME, "").apply();
                                            mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_MOBILE, "").apply();
                                            mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_EMAIL, "").apply();
                                            mSharedPreference.edit().putString(ConstantCodes.SHARED_MOVE_ID, "").apply();

                                            Intent i = new Intent(MainActivity.this, LoginActivity.class);

                                            startActivity(i);

                                            finish();
                                        } else {
                                            Toast.makeText(MainActivity.this, ""
                                                    + R.string.message_something_wrong, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            } else {
                                mSharedPreference.edit().putBoolean(ConstantCodes.IS_LOGIN, false).apply();
                                mSharedPreference.edit().putBoolean(ConstantCodes.SIGN_IN_WITH_GOOGLE, false).apply();

                                mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_ID, "").apply();
                                mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_NAME, "").apply();
                                mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_MOBILE, "").apply();
                                mSharedPreference.edit().putString(ConstantCodes.LOGIN_USER_EMAIL, "").apply();
                                mSharedPreference.edit().putString(ConstantCodes.SHARED_MOVE_ID, "").apply();

                                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                                startActivity(i);

                                finish();
                            }
                        }

                    }
                });
// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
// Showing Alert Dialog
        alertDialog2.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String title = "";
        int id = menuItem.getItemId();
        Fragment targetFragment = null;
        title = "Home";

        toolbar.setVisibility(View.VISIBLE);

        if (id == R.id.drawer_broker_home) {
            targetFragment = new HomeFragment();
            title = "BR World";
        }

        if (id == R.id.drawer_movie) {
            targetFragment = new AllCategoryFragment();
            title = "Movies";

            closeDrawer();
            Fragment fragment = new AllCategoryFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        }

        if (id == R.id.drawer_series) {
            targetFragment = new AllCategoryFragment();
            title = "Series";

            closeDrawer();
            Fragment fragment = new AllCategoryFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "Series").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        }

        if (id == R.id.drawer_youtube) {
            targetFragment = new AllCategoryFragment();
            title = "YouTube";

            closeDrawer();
            Fragment fragment = new AllCategoryFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "YouTube").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        }

        if (id == R.id.drawer_my_account) {
            targetFragment = new MyAccountFragment();
            title = "My Account";

            closeDrawer();
            Fragment fragment = new MyAccountFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "My Account").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        }

        if (id == R.id.drawer_search) {
            targetFragment = new SearchFragment();
            title = "Search";

            closeDrawer();
            Fragment fragment = new SearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();

            return true;


        }

        if (id == R.id.drawer_privacy_policy) {

            String url = "https://www.brworlds.com/privacy-policy.php";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        if (id == R.id.drawer_invite_people) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "BR WORLD");
                String sAux = "\nInstall BRWORLD  Mobile Movie Application Watching Free !!!!!\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.movie.brworld \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }

        }

        if (id == R.id.drawer_logout) {

            showExitAlert("l", "Are you sure you want to logout ?");
        }


        if (targetFragment != null) {
            setAppTitle(title);
            closeDrawer();
            changeFragment(targetFragment, title);
        }

        return true;

    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setAppTitle(String title) {
        toolbar.setTitle(title);
    }

    TextView itemMessagesBadgeTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_drawer, menu);


        MenuItem itemMessages = menu.findItem(R.id.notification);

        RelativeLayout badgeLayout = (RelativeLayout) itemMessages.getActionView();
        itemMessagesBadgeTextView = (TextView) badgeLayout.findViewById(R.id.badge_textView);
        itemMessagesBadgeTextView.setVisibility(View.GONE); // initially hidden

        ImageView iconButtonMessages = badgeLayout.findViewById(R.id.badge_icon_button);
        iconButtonMessages.setImageResource(R.drawable.ic_notifications_black_24dp);
        iconButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NotificationFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();

            }
        });

        networkCallNotification();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        networkCallNotification();

        if (item.getItemId() == R.id.search) {
            //closeDrawer();
            Fragment fragment = new SearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        } else if (item.getItemId() == R.id.notification) {

            // closeDrawer();
            Fragment fragment = new NotificationFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.frame_container, fragment, "Movies").addToBackStack("BuilderBrokerFragment").commit();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void setDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utils.hideKeyboard(MainActivity.this);
            }
        };

        updateDrawerToggle();
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(MainActivity.this);
                onBackPressed();
            }
        });
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(mBackStackChangedListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    void networkCallNotification() {
        if (mApplication.isInternetConnected()) {

            mApplication.getRetroFitInterface().Notification(mSharedPreference.getString(ConstantCodes.LOGIN_USER_ID, "")).enqueue(mCallbackProductList);

        }
    }


    private Callback<PojoNotification> mCallbackProductList = new Callback<PojoNotification>() {

        @Override
        public void onResponse(Call<PojoNotification> call, Response<PojoNotification>
                response) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                PojoNotification pojoParticipants = response.body();

                if (pojoParticipants.getTodayNotiCount() != null) {
                    if (pojoParticipants.getTodayNotiCount() == 0) {
                        itemMessagesBadgeTextView.setVisibility(View.GONE);
                        itemMessagesBadgeTextView.setText("");
                    } else {
                        itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
                        itemMessagesBadgeTextView.setText("" + pojoParticipants.getTodayNotiCount());
                    }
                } else {
                    itemMessagesBadgeTextView.setVisibility(View.GONE);
                    itemMessagesBadgeTextView.setText("");
                }
            }

        }

        @Override
        public void onFailure(Call<PojoNotification> call, Throwable t) {
            itemMessagesBadgeTextView.setVisibility(View.GONE);
            itemMessagesBadgeTextView.setText("");


        }
    };


}

