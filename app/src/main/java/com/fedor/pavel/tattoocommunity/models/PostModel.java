package com.fedor.pavel.tattoocommunity.models;


import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

@ParseClassName("Post")
public class PostModel extends ParseObject {


    private LikeModel like;


    public static final String NUM_OF_LIKES_PARSE_KEY = "num_of_likes";

    public static final String TITLE_PARSE_KEY = "title";

    public static final String USER_ID_PARSE_KEY = "user_id";

    public static final String PHOTO_PARSE_KEY = "photo";

    public static final String CATEGORY_ID_PARSE_KEY = "category_id";

    public static final String TYPE_PARSE_KEY = "type";

    public static final String DESCRIPTION_PARSE_KEY = "description";

    public static final String PHOTO_WIDTH_PARSE_KEY = "photo_width";

    public static final String PHOTO_HEIGHT_PARSE_KEY = "photo_height";

    public PostModel() {

    }

    public PostModel(String title, String userId, int categoryId, String description, int type, Bitmap photo) {

        setTitle(title);

        setUserId(userId);

        setCategoryId(categoryId);

        setDescription(description);

        setType(type);

        setPhoto(photo);

    }

    public PostModel(String title, String userId, int categoryId, String description, int type) {

        setTitle(title);

        setUserId(userId);

        setCategoryId(categoryId);

        setDescription(description);

        setType(type);

    }

    public String getTitle() {

        return getString(TITLE_PARSE_KEY);

    }

    public String getUserId() {

        return getString(USER_ID_PARSE_KEY);

    }

    public String getPhotoUrl() {

        ParseFile photo = getParseFile(PHOTO_PARSE_KEY);

        if (photo == null) {

            return "";

        }

        return photo.getUrl();

    }

    public int getCategoryId() {

        return getInt(CATEGORY_ID_PARSE_KEY);

    }

    public int getType() {

        return getInt(TYPE_PARSE_KEY);

    }

    public LikeModel getLike() {

        return like;

    }

    public int getNumOfLikes() {

        return getInt(NUM_OF_LIKES_PARSE_KEY);

    }

    public String getDescription() {

        return getString(DESCRIPTION_PARSE_KEY);

    }

    public int getPhotoWidth() {

        return getInt(PHOTO_WIDTH_PARSE_KEY);

    }

    public int getPhotoHeight() {

        return getInt(PHOTO_HEIGHT_PARSE_KEY);

    }

    public void setNumOfLikes(int numOfLikes) {

        put(NUM_OF_LIKES_PARSE_KEY, numOfLikes);

    }

    public void setTitle(String title) {

        put(TITLE_PARSE_KEY, title);

    }

    public void setPhoto(Bitmap photo) {

        put(PHOTO_PARSE_KEY, new ParseFile("postPhoto.jpg", toByteArray(photo)));

        put(PHOTO_WIDTH_PARSE_KEY, photo.getWidth());

        put(PHOTO_HEIGHT_PARSE_KEY, photo.getHeight());

    }

    public void setType(int type) {

        put(TYPE_PARSE_KEY, type);

    }

    public void setUserId(String userId) {

        put(USER_ID_PARSE_KEY, userId);

    }

    public void setCategoryId(int categoryId) {

        put(CATEGORY_ID_PARSE_KEY, categoryId);

    }

    public void setDescription(String description) {

        put(DESCRIPTION_PARSE_KEY, description);

    }

    public void setLikeModel(LikeModel like) {

        this.like = like;

    }

    private byte[] toByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }


}
