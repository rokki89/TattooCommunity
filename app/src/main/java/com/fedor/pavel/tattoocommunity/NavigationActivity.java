package com.fedor.pavel.tattoocommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.fedor.pavel.tattoocommunity.fragments.AllPhotosFragment;
import com.fedor.pavel.tattoocommunity.fragments.MasterFragment;
import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private final String LOG_TAG = "NavigationActivity";

    private ProgressDialog progressDialog;

    public static final int REQUEST_LOGIN = 1;

    public static final int LOGIN_RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        prepareProgressDialog();

        findViews();

        prepareNavigationView();

        if(UserModel.getCurrentUser()!=null){

            try {
                UserModel.getCurrentUser().fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            replaceFragment(new AllPhotosFragment(), R.id.activity_navigation_fragment_container);
            selectedNavItem(2);

        }else {

            try {

                UserModel.logOut();

                UserModel.logIn("xxx@xxx.com", "1234");

                replaceFragment(new AllPhotosFragment(), R.id.activity_navigation_fragment_container);
                selectedNavItem(2);

            } catch (ParseException e) {

            }

           /* startActivityForResult(new Intent(this, LoginActivity.class),REQUEST_LOGIN);*/

        }

    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        prepareDrawer(toolbar);
    }

    private void prepareNavigationView() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.item_nav_myWorks);
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

    private void prepareProgressDialog() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Загрузка данных");

        progressDialog.setCancelable(false);

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

                selectedNavItem(0);

                replaceFragment(new ProfileFragment(), R.id.activity_navigation_fragment_container);

                break;

            case R.id.item_nav_masters:

                if (getSupportFragmentManager().findFragmentById(R.id.activity_navigation_fragment_container) instanceof MasterFragment) {

                    break;

                }

                selectedNavItem(1);

                replaceFragment(new MasterFragment(), R.id.activity_navigation_fragment_container);

                break;

            case R.id.item_nav_sketches:
                if (getSupportFragmentManager().findFragmentById(R.id.activity_navigation_fragment_container) instanceof AllPhotosFragment) {

                    break;

                }

                selectedNavItem(2);

                replaceFragment(new AllPhotosFragment(), R.id.activity_navigation_fragment_container);

                break;


            case R.id.item_nav_news:


                break;

            case R.id.item_nav_favorite:


                break;

            case R.id.item_nav_logout:

                if(UserModel.getCurrentUser()!=null){

                    UserModel.logOut();

                    startActivityForResult(new Intent(this, LoginActivity.class),REQUEST_LOGIN);

                }


                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_LOGIN && resultCode == LOGIN_RESULT_OK) {

            replaceFragment(new AllPhotosFragment(), R.id.activity_navigation_fragment_container);


        }

    }

    private void selectedNavItem(int index){

        int navSize = navigationView.getMenu().size();

        for(int i=0; i<navSize; i++){

            navigationView.getMenu().getItem(i).setChecked(false);

        }

            navigationView.getMenu().getItem(index).setChecked(true);


    }
}
