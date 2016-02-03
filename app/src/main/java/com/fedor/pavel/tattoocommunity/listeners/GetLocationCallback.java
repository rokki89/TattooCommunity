package com.fedor.pavel.tattoocommunity.listeners;


import com.google.android.gms.maps.model.LatLng;

public interface GetLocationCallback {


    LatLng getLatLng();

    String getCountry();

    String getCity();

    String getStreet();

    String getLocationTitle();


}
