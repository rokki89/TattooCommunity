package com.fedor.pavel.tattoocommunity.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Like")
public class LikeModel extends ParseObject {


    public static final String POST_ID_PARSE_KEY = "post_id";

    public static final String USER_ID_PARSE_KEY = "user_id";

    public static final String IS_LIKE_PARSE_KEY = "is_like";



    public LikeModel() {

        setLike(false);

    }

    public LikeModel(String postId, String userId, boolean isLike) {

        setPostId(postId);

        setUserId(userId);

        setLike(isLike);

    }


    public String getPostId() {

        return getString(POST_ID_PARSE_KEY);

    }

    public String getUserId() {

        return getString(USER_ID_PARSE_KEY);

    }

    public Boolean isLike() {

        return getBoolean(IS_LIKE_PARSE_KEY);

    }


    public void setPostId(String postId) {

        put(POST_ID_PARSE_KEY, postId);

    }

    public void setUserId(String userId) {

        put(USER_ID_PARSE_KEY, userId);

    }

    public void setLike(boolean isLike) {

        put(IS_LIKE_PARSE_KEY, isLike);

    }


}
