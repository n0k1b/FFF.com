package com.simple.reaz.fffcom.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.simple.reaz.fffcom.API.ApiClient;
import com.simple.reaz.fffcom.API.ApiInterface;
import com.simple.reaz.fffcom.Helper.InternetConnection;
import com.simple.reaz.fffcom.JobPost.JobPost;
import com.simple.reaz.fffcom.OnboardingScreen.PreferenceManager;
import com.simple.reaz.fffcom.OnboardingScreen.WelcomeActivity;
import com.simple.reaz.fffcom.R;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<ModelCategories> cat_list = new ArrayList<>();
    RecyclerView firstRecyclerView, secondRecyclerView,thirdRecyclerView;
    private PreferenceManager prefManager;
    ProgressDialog progressDoalog;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView homeLayout;
    LinearLayout net_connLayout;
    CardView net_conn;
    private static final int TIME_DELAY = 1500;
    private static long back_pressed;
    private  String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefManager = new PreferenceManager(this);
        prefManager.setFirstTimeLaunch(false);

        firstRecyclerView = findViewById(R.id.first_recycler_view);
        secondRecyclerView = findViewById(R.id.second_recycler_view);
        thirdRecyclerView = findViewById(R.id.third_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        homeLayout = findViewById(R.id.homeLayout);
        net_connLayout = findViewById(R.id.net_layout);
        net_conn = findViewById(R.id.net_conn);

        mobile=getIntent().getStringExtra("mobile");

        //------Internet connection checking.....
        final InternetConnection internetConnection = new InternetConnection(getApplicationContext());
        // --------Check if Internet is present
        if (!internetConnection.isConnectingToInternet()) {
            homeLayout.setVisibility(View.GONE);
            net_connLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            //  Toast.makeText(HomeActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            homeLayout.setVisibility(View.VISIBLE);
            net_connLayout.setVisibility(View.GONE);
            get_cata_list();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!internetConnection.isConnectingToInternet()) {
                    homeLayout.setVisibility(View.GONE);
                    net_connLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(HomeActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    homeLayout.setVisibility(View.VISIBLE);
                    net_connLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    get_cata_list();

                }

            }
        });

        net_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetConnection.isConnectingToInternet()) {
                    homeLayout.setVisibility(View.GONE);
                    net_connLayout.setVisibility(View.VISIBLE);
                    //  Toast.makeText(HomeActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    homeLayout.setVisibility(View.VISIBLE);
                    net_connLayout.setVisibility(View.GONE);
                    get_cata_list();
                }
            }
        });

        changeStatusBarColor();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.job_post) {
            Intent intent=new Intent(HomeActivity.this,JobPost.class);
            intent.putExtra("mobile",mobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.jobpost) {
            Intent intent = new Intent(HomeActivity.this, JobPost.class);
            intent.putExtra("mobile",mobile);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        } else if (id == R.id.logout) {
            prefManager.setFirstTimeLaunch(true);
            Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void get_cata_list() {
        progressDoalog = new ProgressDialog(HomeActivity.this);
        progressDoalog.setMessage("Loading...");
        progressDoalog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelCategories>> call = apiInterface.get_cata_list();
        call.enqueue(new Callback<List<ModelCategories>>() {
            @Override
            public void onResponse(Call<List<ModelCategories>> call, Response<List<ModelCategories>> response) {
                cat_list = response.body();
                if (cat_list != null) {
                    // Toast.makeText(HomeActivity.this, "ok", Toast.LENGTH_SHORT).show();

                    firstRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    firstRecyclerView.setNestedScrollingEnabled(false);
                    firstRecyclerView.setAdapter(new HomeAdapter(HomeActivity.this, cat_list));
                    secondRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    secondRecyclerView.setAdapter(new HomeAdapter2(HomeActivity.this, cat_list));
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ModelCategories>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }
}
