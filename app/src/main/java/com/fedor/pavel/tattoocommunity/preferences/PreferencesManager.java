package com.fedor.pavel.tattoocommunity.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONObject;


public class PreferencesManager {

    public static final String KEY_PREFERENCES_FILTR_ALL_WORKS = "allWorksFilters";


    public static void saveData(Context context, String key,  JSONObject jsonObject) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("", jsonObject.toString());
        editor.commit();
    }



    public static String loadObject(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        return sharedPreferences.getString("", "");
    }



    public static void delData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
