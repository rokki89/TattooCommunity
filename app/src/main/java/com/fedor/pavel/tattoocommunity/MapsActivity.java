package com.fedor.pavel.tattoocommunity;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Address;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


import com.fedor.pavel.tattoocommunity.fragments.ViewPlaceInfoFragment;
import com.fedor.pavel.tattoocommunity.listeners.FindAddressListener;
import com.fedor.pavel.tattoocommunity.listeners.GetLocationCallback;
import com.fedor.pavel.tattoocommunity.location.GeocodeManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
        , FindAddressListener, GetLocationCallback {

    private GoogleMap mMap;

    private Marker placeMarker;

    private String city;

    private String country;

    private String adminArea ;

    private String street;

    private LatLng latLng;

    public static final String LOG_TAG = "MapsActivity";

    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getLocationTitle() {

        return null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        setUpMap();


    }

    private void setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);


    }

    public void replaceFragment(Fragment fragment, int containerId) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();

    }

    public void deleteFragment(Fragment fragment){

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();

    }

    @Override
    public void onMapClick(LatLng latLng) {

        this.latLng = latLng;

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You");

        if (placeMarker == null) {

            placeMarker = mMap.addMarker(options);

        } else {

            placeMarker.setPosition(latLng);

        }

        GeocodeManager.getAddressFromLocation(this, latLng, this);

    }

    @Override
    public void onAddressFound(Address address) {

        if (address != null) {

            city = address.getLocality();

            country = address.getCountryName();

            adminArea = address.getAdminArea();

            street = null;

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i));
            }

            street = sb.toString();

            street = street.replace(",", "");


            if (country!=null&&!country.isEmpty()){

                street = street.replace(country, "");


            }

            if (adminArea!=null&&!adminArea.isEmpty()){

                street = street.replace(adminArea, "");


            }

            if (city!=null&&!city.isEmpty()){

               street =  street .replaceAll(city, "");


            }

            replaceFragment(new ViewPlaceInfoFragment(),R.id.activity_map_ll_address_container);

            return;

        }

           Log.d(LOG_TAG, "No address");


    }




}
