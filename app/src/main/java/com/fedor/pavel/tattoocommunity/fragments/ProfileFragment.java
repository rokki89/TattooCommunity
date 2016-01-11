package com.fedor.pavel.tattoocommunity.fragments;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.views.FillingTextView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProfileFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private FillingTextView tvTitle, tvName;
    private ImageView imvPhoto;
    private AppBarLayout appBarLayout;

    private ParseUser user;

    private static final String LOG_TAG = "ProfileFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user = ParseUser.getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        findViews(view);

        prepareViews();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");


        query.whereEqualTo("user_id", user.getObjectId());


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    Log.d(LOG_TAG, "" + objects.get(0).getParseFile("photo").getUrl());

                } else {

                    Log.d(LOG_TAG, "" + e);

                }

            }
        });

        return view;
    }

    private void findViews(View view) {

        ((AppCompatActivity) getActivity())
                .setSupportActionBar((android.support.v7.widget.Toolbar) view.findViewById(R.id.fragment_profile_toolbar));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle = (FillingTextView) view.findViewById(R.id.tvTitle);

        tvName = (FillingTextView) view.findViewById(R.id.fragment_profile_tv_name);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_profile_appBarLayout);


    }

    private void prepareViews() {

        tvTitle.setText("Имя Фамилия");

        tvTitle.setVisibility(View.GONE);

        appBarLayout.addOnOffsetChangedListener(this);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        tvTitle.offsetTextSizeRatio(verticalOffset, appBarLayout, true);

        tvName.offsetTextSizeRatio(verticalOffset, appBarLayout, false);

    }
}
