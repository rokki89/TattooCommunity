package com.fedor.pavel.tattoocommunity.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.listeners.OnMasterLoadListener;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcc on 28.01.2016.
 */
public class LoadMastersTask extends AsyncTask<Integer, Void, ArrayList<UserModel>> {

    private OnMasterLoadListener listener;

    private final String LOG_TAG = "LoadPostsTask";

    private Context context;

    private JSONObject jsonObject;


    public LoadMastersTask(Context context, OnMasterLoadListener listener, JSONObject jsonObject) {

        this.listener = listener;

        this.context = context;

        this.jsonObject = jsonObject;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<UserModel> doInBackground(Integer... params) {

        if(jsonObject!=null){
            Log.d(LOG_TAG, "jsonObject == NULL");
        }


       List<PlaceModel> listPlaces  = new ArrayList<>();

        if(jsonObject!=null && jsonObject.length()==1) {

            String country = "";

            try {
                country = jsonObject.getString("1");
                Log.d(LOG_TAG, "country = " + country);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ParseQuery<PlaceModel> queryPlace = ParseQuery.getQuery(PlaceModel.class);

            if(country != ""){

                queryPlace.whereEqualTo(PlaceModel.COUNTRY_ID_PARSE_KEY, country);
            }

            try {
                listPlaces = queryPlace.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if(jsonObject!=null && jsonObject.length()==2){

        }

        ParseQuery<UserModel> query = ParseQuery.getQuery(UserModel.class);
        query.whereEqualTo("reg_type", 1);

        query.orderByDescending("createdAt");

        if(listPlaces.size()!=0){

            query.whereContainedIn(UserModel.PLACE_PARSE_KEY, listPlaces);
        }

        if (!isHaveInternet()) {

            query.fromLocalDatastore();

        } else {

            query.setLimit(params[0]);

            query.setSkip(params[1]);

        }


        try {



            ArrayList<UserModel> posts = new ArrayList<>(query.find());


            ParseObject.unpinAllInBackground("_User");

            ParseObject.pinAllInBackground("_User", posts);



            return posts;

        } catch (ParseException e) {

            e.printStackTrace();

            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<UserModel> posts) {
        Log.d(LOG_TAG, "onPostExecute = " + posts.size());
        super.onPostExecute(posts);

        listener.onMasterLoaded(posts);

    }

    private void getLikes(ArrayList<UserModel> posts) {




    }

    public boolean isHaveInternet() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null;

    }

}
