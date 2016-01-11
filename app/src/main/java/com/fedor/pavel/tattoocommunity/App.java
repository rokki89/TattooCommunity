package com.fedor.pavel.tattoocommunity;

import android.app.Application;

import com.fedor.pavel.tattoocommunity.models.CityModel;
import com.fedor.pavel.tattoocommunity.models.CommentsModel;
import com.fedor.pavel.tattoocommunity.models.CountryModel;
import com.fedor.pavel.tattoocommunity.models.LikeModel;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.TattooCategoryModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initializeParse();

    }

    private void initializeParse() {

        ParseUser.registerSubclass(UserModel.class);
        ParseObject.registerSubclass(PostModel.class);
        ParseObject.registerSubclass(CountryModel.class);
        ParseObject.registerSubclass(CityModel.class);
        ParseObject.registerSubclass(LikeModel.class);
        ParseObject.registerSubclass(PlaceModel.class);
        ParseObject.registerSubclass(CommentsModel.class);
        ParseObject.registerSubclass(TattooCategoryModel.class);

        Parse.initialize(this, getResources().getString(R.string.parseApplicationId), getResources().getString(R.string.parseClientKey));

    }
}
