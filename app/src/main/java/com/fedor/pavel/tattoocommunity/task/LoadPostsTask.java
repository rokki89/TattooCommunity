package com.fedor.pavel.tattoocommunity.task;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.fedor.pavel.tattoocommunity.listeners.OnPostLoadListener;
import com.fedor.pavel.tattoocommunity.models.LikeModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadPostsTask extends AsyncTask<Integer, Void, ArrayList<PostModel>> {

    private OnPostLoadListener listener;

    private final String LOG_TAG = "LoadPostsTask";

    private Context context;

    private JSONObject jsonObject;

    private boolean forProfile = false;


    public LoadPostsTask(Context context,boolean forProfile ,OnPostLoadListener listener) {

        this.listener = listener;

        this.context = context;

        this.forProfile = forProfile;

    }


    public LoadPostsTask(Context context, boolean forProfile ,OnPostLoadListener listener, JSONObject jsonObject) {

        this.listener = listener;

        this.context = context;

        this.jsonObject = jsonObject;

        this.forProfile = forProfile;

    }

    public LoadPostsTask(Context context,OnPostLoadListener listener) {

        this.listener = listener;

        this.context = context;

        this.forProfile = false;

    }


    public LoadPostsTask(Context context,OnPostLoadListener listener, JSONObject jsonObject) {

        this.listener = listener;

        this.context = context;

        this.jsonObject = jsonObject;

        this.forProfile = false;

    }

    @Override
    protected ArrayList<PostModel> doInBackground(Integer... params) {

        ArrayList<Integer> categories = new ArrayList<>();

        ParseQuery<PostModel> query = ParseQuery.getQuery(PostModel.class);


        if (jsonObject != null && jsonObject.length() > 0) {

            for (int i = 0; i < jsonObject.length(); i++) {
                try {
                    int category = jsonObject.getInt("" + i);
                    categories.add(category);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            query.whereContainedIn(PostModel.CATEGORY_ID_PARSE_KEY, categories);
        }

        query.orderByDescending("createdAt");

        if (forProfile) {

            query.whereEqualTo(PostModel.USER_ID_PARSE_KEY, UserModel.getCurrentUser().getObjectId());

        }

        if (!isHaveInternet()) {

            query.fromLocalDatastore();

        } else {

            query.setLimit(params[0]);

            query.setSkip(params[1]);

        }


        try {


            ArrayList<PostModel> posts = new ArrayList<>(query.find());


            ParseObject.unpinAllInBackground("Posts");

            ParseObject.pinAllInBackground("Posts", posts);


            getLikes(posts);

            return posts;

        } catch (ParseException e) {

            e.printStackTrace();

            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<PostModel> posts) {
        super.onPostExecute(posts);

        listener.onPostsLoaded(posts);

    }

    private void getLikes(ArrayList<PostModel> posts) {


        ArrayList<String> postsId = new ArrayList<>();

        for (PostModel post : posts) {

            postsId.add(post.getObjectId());

        }


        ParseQuery<LikeModel> query = ParseQuery.getQuery("Like");

        query.whereEqualTo(LikeModel.USER_ID_PARSE_KEY, UserModel.getCurrentUser().getObjectId());

        query.whereContainedIn(LikeModel.POST_ID_PARSE_KEY, postsId);


        if (!isHaveInternet()) {

            query.fromLocalDatastore();

        }

        try {

            ArrayList<LikeModel> likes = new ArrayList<>(query.find());

            ParseObject.unpinAllInBackground("Likes");

            ParseObject.pinAllInBackground("Likes", likes);

            for (PostModel post : posts) {

                for (LikeModel like : likes) {

                    if (post.getObjectId().equals(like.getPostId())) {

                        post.setLikeModel(like);

                        break;
                    }

                }

                if (post.getLike() == null) {

                    post.setLikeModel(new LikeModel());

                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public boolean isHaveInternet() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null;

    }

}
