package com.fedor.pavel.tattoocommunity;

import android.app.Application;

import com.fedor.pavel.tattoocommunity.models.CityModel;
import com.fedor.pavel.tattoocommunity.models.CommentsModel;
import com.fedor.pavel.tattoocommunity.models.CountryModel;
import com.fedor.pavel.tattoocommunity.models.LikeModel;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class App extends Application {


    @Override
    public void onCreate() {

        super.onCreate();

        initializeParse();

        configureImageLoader();

    }

    private void initializeParse() {

        Parse.enableLocalDatastore(this);

        ParseUser.registerSubclass(UserModel.class);
        ParseObject.registerSubclass(PostModel.class);
        ParseObject.registerSubclass(CountryModel.class);
        ParseObject.registerSubclass(CityModel.class);
        ParseObject.registerSubclass(LikeModel.class);
        ParseObject.registerSubclass(PlaceModel.class);
        ParseObject.registerSubclass(CommentsModel.class);



        Parse.initialize(this, getResources().getString(R.string.parseApplicationId), getResources().getString(R.string.parseClientKey));

    }

    public void configureImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileCount(100)
                .diskCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(8)
                .build();

        ImageLoader.getInstance().init(config);

    }
}
