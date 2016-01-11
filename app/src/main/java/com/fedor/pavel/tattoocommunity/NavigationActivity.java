package com.fedor.pavel.tattoocommunity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    // TODO: 09.01.2016 Преопределить вьюхи и расширить их поведение (добавить круглую аватарку)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);



        findViews();

        prepareNavigationView();

        ParseUser.logOut();


        ParseUser.logInInBackground("pavelbjorn@gmai.com", "1234", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {
                    replaceFragment(new ProfileFragment(), R.id.activity_navigation_fragment_container);
                }

            }
        });



        /*replaceFragment(new ProfileFragment(), R.id.activity_navigation_fragment_container);*/

    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        prepareDrawer(toolbar);
    }

    private void prepareNavigationView() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.item_nav_myWorks);
    }

    private void replaceFragment(Fragment fragment, int containerId) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();

    }

    private void prepareDrawer(Toolbar toolbar) {


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();

    }

    private void findViews() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_nav_myWorks:

                if (getSupportFragmentManager().findFragmentById(R.id.activity_navigation_fragment_container) instanceof ProfileFragment) {

                    break;

                }

                replaceFragment(new ProfileFragment(), R.id.activity_navigation_fragment_container);

                break;

            case R.id.item_nav_masters:


                break;

            case R.id.item_nav_sketches:


                break;

            case R.id.item_nav_news:


                break;

            case R.id.item_nav_favorite:


                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
