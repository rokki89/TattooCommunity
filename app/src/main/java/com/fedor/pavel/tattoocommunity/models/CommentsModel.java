package com.fedor.pavel.tattoocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Comments")
public class CommentsModel extends ParseObject {


    public static final String POST_ID_PARSE_KEY = "post_id";


    public static final String USER_ID_PARSE_KEY = "user_id";


    public static final String CONTENT_PARSE_KEY = "content";

    public CommentsModel() {

    }

    public CommentsModel(String postId, String userId, String content) {

        setPostId(postId);

        setUserId(userId);

        setContent(content);
    }

    public String getPostId() {

        return getString(POST_ID_PARSE_KEY);

    }

    public String getUserIdParseKey() {

        return getString(USER_ID_PARSE_KEY);

    }

    public String getContent() {

        return getString(CONTENT_PARSE_KEY);

    }

    public void setPostId(String postId) {

        put(POST_ID_PARSE_KEY, postId);

    }

    public void setUserId(String userId) {

        put(USER_ID_PARSE_KEY, userId);

    }

    public void setContent(String content) {

        put(CONTENT_PARSE_KEY, content);

    }


}
