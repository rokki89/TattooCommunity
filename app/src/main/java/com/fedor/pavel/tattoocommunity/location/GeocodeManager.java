package com.fedor.pavel.tattoocommunity.location;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.listeners.FindAddressListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GeocodeManager {


    private static final String LOG_TAG = "GeocodeManager";


    public static void getAddressFromLocation(final Context context, final LatLng latLng, final FindAddressListener listener) {

        AsyncTask<LatLng, Void, Address> getAddressTask = new AsyncTask<LatLng, Void, Address>() {


            @Override
            protected Address doInBackground(LatLng... params) {

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                String result = null;

                try {

                    List<Address> addressList = geocoder.getFromLocation(params[0].latitude, params[0].longitude, 1);

                    if (addressList != null && addressList.size() > 0) {


                        Address address = addressList.get(0);


                        return address;

                    }


                    return null;


                } catch (IOException e) {

                    Log.e(LOG_TAG, "Unable connect to Geocoder", e);

                    return null;

                }

            }

            @Override
            protected void onPostExecute(Address address) {

                super.onPostExecute(address);

                listener.onAddressFound(address);


            }
        };

        getAddressTask.execute(latLng);

    }


}
