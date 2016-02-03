package com.fedor.pavel.tattoocommunity.data;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class PreferencesManager {

    private static PreferencesManager instance;

    private static Context context;

    public static final String APP_SETTINGS_PREFERENCES_NAME = "App settings";

    private PreferencesManager() {

    }

    public PreferencesManager getInstance() throws Exception {

        if(context!=null) {

            return instance;

        }else{

            throw new Exception("You must initialize manager first! Call " +
                    "initializePreferencesManager(Context context) method to initialize it");

        }
    }

    public static void initializePreferencesManager(Context context) {

        if (instance == null) {
            instance = new PreferencesManager();
        }

        if( PreferencesManager.context ==null) {
            PreferencesManager.context = context;
        }

    }

    public void saveSettings(String key,JSONObject settings){

        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, settings.toString());

        editor.commit();

    }

    public JSONObject loadSettings(String key) throws JSONException{

        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);

        return new JSONObject(sharedPreferences.getString(key,""));

    }

}
