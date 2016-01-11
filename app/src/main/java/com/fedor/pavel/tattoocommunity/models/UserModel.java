package com.fedor.pavel.tattoocommunity.models;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;


@ParseClassName("_User")
public class UserModel extends ParseUser {

    public static final String FULL_NAME_PARSE_KEY = "full_name";

    public static final String REG_TYPE_PARSE_KEY = "reg_type";

    public static final String SKYPE_PARSE_KEY = "skype";

    public static final String PHOTO_PARSE_KEY = "photo";

    public static final String PHONE_PARSE_KEY = "phone";

    public static final String WALLPAPER_PARSE_KEY = "wallpaper";

    public static final String ABOUT_SELF_PARSE_KEY = "aboutSelf";


    public UserModel() {

    }

    public UserModel(String fullName, int regType, String phone, String aboutSelf) {

        setFullName(fullName);

        setRegType(regType);

        setPhone(phone);

        setAboutSelf(aboutSelf);

    }

    public String getFullName() {
        return getString(FULL_NAME_PARSE_KEY);
    }

    public int getRegType() {

        return getInt(REG_TYPE_PARSE_KEY);

    }

    public String getSkype() {
        return getString(SKYPE_PARSE_KEY);
    }

    public String getPhotoUrl() {

        return getUrlFromParseFile(PHOTO_PARSE_KEY);

    }

    public String getPhoneUrl() {

        return getString(PHONE_PARSE_KEY);

    }

    public String getWallpaperUrl() {

        return getUrlFromParseFile(WALLPAPER_PARSE_KEY);

    }

    public String getAboutSelf() {

        return getString(ABOUT_SELF_PARSE_KEY);

    }

    public void setFullName(String fullName) {

        put(FULL_NAME_PARSE_KEY, fullName);
    }

    public void setRegType(int regType) {

        put(REG_TYPE_PARSE_KEY, regType);

    }

    public void setSkype(String skype) {

        put(SKYPE_PARSE_KEY, skype);

    }

    public void setPhoto(Bitmap bitmap) {

        put(PHOTO_PARSE_KEY, new ParseFile("userPhoto.jpg", toByteArray(bitmap)));

    }

    public void setPhone(String phone) {

        put(PHONE_PARSE_KEY, phone);

    }

    public void setWallpaper(Bitmap bitmap) {

        put(WALLPAPER_PARSE_KEY, new ParseFile("wallpaper.jpg", toByteArray(bitmap)));

    }

    public void setAboutSelf(String aboutSelf) {

        put(ABOUT_SELF_PARSE_KEY, aboutSelf);

    }

    private String getUrlFromParseFile(String key) {

        ParseFile file = getParseFile(key);

        if (file == null) {
            return "";
        }

        return file.getUrl();
    }

    private byte[] toByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }
}
