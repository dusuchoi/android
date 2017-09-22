package kr.cds.mosquitoforecast;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DbHelper dbHelper;


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();   //최초화면
        MosquitoFragment mosquitoFragment = new MosquitoFragment();
        transaction.replace(R.id.content_frame, mosquitoFragment);
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
        WeatherFragment weatherFragment = new WeatherFragment();
        DefinitionFragment definitionFragment = new DefinitionFragment();

        int id = item.getItemId();
         if (id == R.id.nav_mosquito) {
            transaction.replace(R.id.content_frame, mosquitoFragment);
        }
        else if (id == R.id.nav_weather) {
            transaction.replace(R.id.content_frame, weatherFragment);
        }
        else if (id == R.id.nav_mosquitoExpression) {
            transaction.replace(R.id.content_frame, definitionFragment);
        }
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
