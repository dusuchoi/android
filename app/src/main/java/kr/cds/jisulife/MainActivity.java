package kr.cds.jisulife;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CommunicationListener {

    public static DbHelper dbHelper;
    public String location = "1100000000";
    WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getsInstance(this);
        if (savedInstanceState == null) {
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
                inputMethodManager.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);

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

        //MobileAds.initialize(this, "ca-app-pub-4712412246131543~2639273924");

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
        } else if (id == R.id.nav_mosquito) {
            transaction.replace(R.id.content_frame, mosquitoFragment);
        } else if (id == R.id.nav_expression) {
            transaction.replace(R.id.content_frame, expressionFragment);
        } else if (id == R.id.nav_air) {
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
    public void sendData(String location) {
        this.location = location;
        weatherFragment.setLocation(location);
    }
}
