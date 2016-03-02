package com.fedor.pavel.tattoocommunity.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.listeners.OnCountriesLoadListener;
import com.fedor.pavel.tattoocommunity.models.CityModel;
import com.fedor.pavel.tattoocommunity.models.CountryModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Loading list of Countries and Cities from Server and save in data base
 */
public class LoadCountriesTask extends AsyncTask<Void, Void, Integer> {

    private static final String LOG_TAG = "LoadCountriesTask";

    public static final int LOAD_CODE_OK = 200;

    private OnCountriesLoadListener listener;

    private Context context;

    public LoadCountriesTask(Context context) {

        this.context = context;

    }

    public LoadCountriesTask(Context context,OnCountriesLoadListener listener) {

        this.listener = listener;

        this.context = context;

    }

    @Override
    protected Integer doInBackground(Void... params) {




        /*Hear getting cities */

        if (isHaveInternet()) {
            ParseQuery<CityModel> cityQuery = ParseQuery.getQuery(CityModel.class);

            try {

            /*Saving to Parse local data base*/
                ParseObject.unpinAll(ParseSDKConstants.PARSE_LABEL_CITIES_MODEL);
                ParseObject.pinAll(ParseSDKConstants.PARSE_LABEL_CITIES_MODEL, cityQuery.find());

            } catch (ParseException e) {

                Log.d(LOG_TAG, "cityQuery-> " + e);

                return e.getCode();

            }

        /*Getting countries*/

            ParseQuery<CountryModel> countryQuery = ParseQuery.getQuery(CountryModel.class);


            try {
                ParseObject.unpinAll(ParseSDKConstants.PARSE_LABEL_COUNTRIES_MODEL);
                ParseObject.pinAll(ParseSDKConstants.PARSE_LABEL_COUNTRIES_MODEL, countryQuery.find());

            } catch (ParseException e) {

                Log.d(LOG_TAG, "countryQuery -> " + e);

                return e.getCode();

            }
        }

        return LOAD_CODE_OK;

    }


    @Override
    protected void onPostExecute(Integer responseCode) {

        super.onPostExecute(responseCode);

        if (responseCode == LOAD_CODE_OK) {

            listener.loadSuccessful();

        } else {

            listener.loadFailed(responseCode);

        }

        // TODO: 21.01.2016 Handle result code

    }

    public boolean isHaveInternet() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null;

    }
}
