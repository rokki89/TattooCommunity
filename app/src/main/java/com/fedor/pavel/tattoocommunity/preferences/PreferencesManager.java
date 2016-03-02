package com.fedor.pavel.tattoocommunity.preferences;

import android.content.Context;
import android.content.SharedPreferences;



import java.util.List;
import java.util.Map;


public class PreferencesManager {

    public static final String KEY_PREFERENCES_FILTR_ALL_WORKS = "allWorksFilters";


    public static void saveDataIntList(Context context, String prefName, List<Integer> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i =0; i<list.size(); i++){
            editor.putInt("key" + i, list.get(i));
        }

        editor.commit();
    }



    public static int loadInt(Context context, String prefName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public static Map<String, ?> getAllSum(Context context, String prefName) {
        Map<String, ?> allPreferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE).getAll();
        return allPreferences;
    }



    public static void delData(Context context, String prefName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
