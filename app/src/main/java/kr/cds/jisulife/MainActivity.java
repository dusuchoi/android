package kr.cds.jisulife;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CommunicationListener{

    public static DbHelper dbHelper;
    public String location = "1100000000";
    WeatherFragment weatherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getsInstance(this);
        if(savedInstanceState == null){
            dbHelper = DbHelper.getsInstance(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
              getSupportActionBar().setTitle("");

        MobileAds.initialize(this, "ca-app-pub-4712412246131543~2639273924");
        AdView mAdView = (AdView)this.findViewById(R.id.adView);

         AdRequest adRequest = new AdRequest.Builder().build();

         mAdView.loadAd(adRequest);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();   //최초화면

        weatherFragment = new WeatherFragment();
        weatherFragment.setLocation(location);
        transaction.replace(R.id.content_frame, weatherFragment);
        transaction.commit();

        dbHelper.getWritableDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MosquitoFragment mosquitoFragment = new MosquitoFragment();
        weatherFragment = new WeatherFragment();
        ExpressionFragment expressionFragment = new ExpressionFragment();
        AirFragment airFragment = new AirFragment();
        LocationFragment locationFragment = new LocationFragment();

        int id = item.getItemId();
        if (id == R.id.nav_weather) {
            transaction.replace(R.id.content_frame, weatherFragment);
        }
        else if (id == R.id.nav_mosquito) {
            transaction.replace(R.id.content_frame, mosquitoFragment);
        }
        else if (id == R.id.nav_expression) {
            transaction.replace(R.id.content_frame, expressionFragment);
        }
        else if (id == R.id.nav_air) {
            transaction.replace(R.id.content_frame, airFragment);
        }
        else if (id == R.id.nav_location) {
            transaction.replace(R.id.content_frame, locationFragment);
        }
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void sendData(String location) {
        this.location = location;
        weatherFragment.setLocation(location);
    }
}
