
package com.fedor.pavel.tattoocommunity.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.fedor.pavel.tattoocommunity.CreatePostActivity;
import com.fedor.pavel.tattoocommunity.ProfileSettingsActivity;
import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.adapters.PostRecyclerViewAdapter;
import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.data.FileManager;
import com.fedor.pavel.tattoocommunity.dialogs.DialogManager;
import com.fedor.pavel.tattoocommunity.listeners.OnLoadNexDataPartListener;
import com.fedor.pavel.tattoocommunity.listeners.OnPostLoadListener;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.fedor.pavel.tattoocommunity.task.LoadPostsTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProfileFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener,
        OnPostLoadListener, View.OnClickListener, OnLoadNexDataPartListener, DialogInterface.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageView imvPhoto, imvWall;

    private AppBarLayout appBarLayout;

    private RecyclerView rvPosts;

    private FloatingActionButton fbAddPost;

    private TextView tvPhone, tvPlace, tvName, tvEmail;

    private RadioGroup rgListState;

    private UserModel user;

    private String country;

    private String city;

    private Uri photoUri;

    private SwipeRefreshLayout swp;

    private PostRecyclerViewAdapter postsAdapter;

    private ProgressDialog progressDialog;

    private boolean isWallEdit = false;

    private boolean isRemovingPhoto = false;

    private static final String LOG_TAG = "ProfileFragment";

    public static final int REQUEST_CREATE_POST = 0;

    public static final int REQUEST_FROM_CAMERA_AVATAR = 1;

    public static final int REQUEST_FROM_GALLERY_AVATAR = 2;

    public static final int REQUEST_FROM_CAMERA_WALL = 3;

    public static final int REQUEST_FROM_GALLERY_WALL = 4;

    public static final int REQUEST_PROFILE_CONFIG = 5;

    public static final int RESULT_CODE_POST_CREATED = 200;

    public static final int RESULT_CODE_POST_EDIT = -200;

    public static final int RESULT_PROFILE_EDIT_OK = -5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        user = (UserModel) UserModel.getCurrentUser();

        if (user != null) {
            try {

                PlaceModel place = user.getPlace();

                if (place != null) {

                    place.fetchIfNeeded();

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        postsAdapter = new PostRecyclerViewAdapter(this);

        createProgressDialog();

        countNumOfPosts();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        findViews(view);

        prepareViews();

        findPosts(0);

        return view;
    }

    private void findViews(View view) {

        ((AppCompatActivity) getActivity())
                .setSupportActionBar((android.support.v7.widget.Toolbar) view.findViewById(R.id.fragment_profile_toolbar));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvName = (TextView) view.findViewById(R.id.fragment_profile_tv_name);

        tvPhone = (TextView) view.findViewById(R.id.fragment_profile_tv_phone);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_profile_appBarLayout);

        rvPosts = (RecyclerView) view.findViewById(R.id.fragment_profile_rv_posts);

        rgListState = (RadioGroup) view.findViewById(R.id.fragment_profile_rg_list_state);

        tvEmail = (TextView) view.findViewById(R.id.fragment_profile_tv_email);

        fbAddPost = (FloatingActionButton) view.findViewById(R.id.fragment_profile_fab_add_post);

        imvPhoto = (ImageView) view.findViewById(R.id.fragment_profile_imv_photo);

        tvPlace = (TextView) view.findViewById(R.id.fragment_profile_tv_place);

        imvWall = (ImageView) view.findViewById(R.id.fragment_profile_imv_wall);

        rvPosts.setAdapter(postsAdapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        swp = (SwipeRefreshLayout) view.findViewById(R.id.fragment_profile_swp);

    }

    private void prepareViews() {

        rgListState.setOnCheckedChangeListener(this);

        rgListState.check(R.id.fragment_profile_action_list);

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                findPosts(0);

            }
        });

        swp.setColorSchemeColors(R.color.colorPrimaryDark);

        filDataFromCurrentUser();

        appBarLayout.addOnOffsetChangedListener(this);

        fbAddPost.setOnClickListener(this);

        postsAdapter.loadDataWithPagination(rvPosts, this);

        imvPhoto.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(user.getPhotoUrl(), imvPhoto, displayImageOptions(R.drawable.user_photo_male));

        ImageLoader.getInstance().displayImage(user.getWallpaperUrl(), imvWall, displayImageOptions(R.drawable.defult_wall));

    }

    private void filDataFromCurrentUser() {

        tvName.setText(user.getFullName());

        tvPhone.setText(user.getPhone());

        tvEmail.setText(user.getEmail());

        String placeName = "-";

        PlaceModel placeModel = user.getPlace();

        if (placeModel != null) {
            city = placeModel.getCityId();

            if (city != null) {

                country = placeModel.getCountryId();

                if (country != null) {

                    placeName = country + ", " + city;

                }
            }

        }

        tvPlace.setText(placeName);

    }

    private void findPosts(int offset) {

        LoadPostsTask loadPostsTask = new LoadPostsTask(getContext(), true, this);

        loadPostsTask.execute(postsAdapter.getDataLimit(), offset);

    }

    public String getCity() {

        return city;

    }

    public String getCountry() {

        return country;

    }

    private void countNumOfPosts() {

        ParseQuery<PostModel> query = ParseQuery.getQuery(PostModel.class);

        query.whereEqualTo(PostModel.USER_ID_PARSE_KEY, UserModel.getCurrentUser().getObjectId());

        query.countInBackground(new CountCallback() {

            @Override
            public void done(int count, ParseException e) {

                Log.d(LOG_TAG, "count = " + count);
                postsAdapter.setTotalDataNumber(count);

            }
        });


    }

    public void showProgressDialog() {

        progressDialog.show();

    }

    public void dismissProgressDialog() {

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        Log.d(LOG_TAG, "verticalOffset = " + verticalOffset);

        if (verticalOffset == 0) {

            swp.setEnabled(true);


        } else {

            swp.setEnabled(false);


        }

    }

    @Override
    public void onPostsLoaded(List<PostModel> posts) {

        if (swp.isRefreshing()) {

            postsAdapter.clearItems();

            swp.setRefreshing(false);

        }

        if (postsAdapter.isLoadingData()) {

            postsAdapter.setIsLoadingData(false);

        }

        if (posts != null) {

            postsAdapter.addAllItems(posts);

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_profile_fab_add_post:

                startActivityForResult(new Intent(getContext(), CreatePostActivity.class), REQUEST_CREATE_POST);

                break;

            case R.id.fragment_profile_imv_photo:

                if (!isRemovingPhoto) {

                    DialogManager.showPhotoPikerDialog(getContext(), this, "Редактировать аватарку");

                    isWallEdit = false;
                }

                break;


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PROFILE_CONFIG && resultCode == RESULT_PROFILE_EDIT_OK) {


            filDataFromCurrentUser();

        }

        if (requestCode == REQUEST_CREATE_POST && resultCode == RESULT_CODE_POST_CREATED) {

            postsAdapter.clearItems();

            findPosts(0);

            return;

        }

        if (requestCode == REQUEST_CREATE_POST && resultCode == RESULT_CODE_POST_EDIT) {

            int position = data.getIntExtra(ParseSDKConstants.PARSE_MODEL_POSITION_INTENT_KEY, -1);

            if (position != -1) {

                try {

                    postsAdapter.getItem(position).unpin(ParseSDKConstants.PARSE_LABEL_RECEIVE_MODEL);

                    postsAdapter.notifyItemChanged(position);


                } catch (ParseException e) {

                    Log.d(LOG_TAG, "" + e);

                }
            }

            return;
        }

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_FROM_GALLERY_WALL || requestCode == REQUEST_FROM_GALLERY_AVATAR) {

                photoUri = data.getData();

            }

            Bitmap photo = ImageLoader.getInstance().loadImageSync(photoUri.toString(), displayImageOptions(R.drawable.user_photo_male));

            if (isWallEdit) {

                user.setWallpaper(photo);

            } else {

                user.setPhoto(photo);
            }

            progressDialog.show();

            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        displayRequestedPhoto();

                    }

                    if (progressDialog.isShowing()) {

                        progressDialog.dismiss();

                    }
                }
            });


        }
    }

    @Override
    public void getNextPart() {

        findPosts(postsAdapter.getOnlyItemCount());

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {


        switch (which) {


            case 0:

                requestPhotoFromGallery(isWallEdit ? REQUEST_FROM_GALLERY_WALL : REQUEST_FROM_GALLERY_AVATAR);

                break;

            case 1:

                requestPhotoFromCamera(isWallEdit ? REQUEST_FROM_CAMERA_WALL : REQUEST_FROM_CAMERA_AVATAR);

                break;

            case 2:

                isRemovingPhoto = true;

                user.remove(isWallEdit ? UserModel.WALLPAPER_PARSE_KEY : UserModel.PHOTO_PARSE_KEY);

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            ImageLoader.getInstance().displayImage(isWallEdit ? user.getWallpaperUrl() : user.getPhotoUrl()
                                    , isWallEdit ? imvWall : imvPhoto
                                    , displayImageOptions(isWallEdit ? R.drawable.defult_wall : R.drawable.user_photo_male));

                        }

                        isRemovingPhoto = false;

                    }
                });

                break;


        }


    }

    private void requestPhotoFromGallery(int requestKey) {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, requestKey);

    }

    private void requestPhotoFromCamera(int requestKey) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = FileManager.createFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {

                photoUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoUri);

                startActivityForResult(intent, requestKey);
            }
        }


    }

    private void displayRequestedPhoto() {


        if (!isWallEdit) {

            ImageLoader.getInstance().displayImage(photoUri.toString(), imvPhoto, displayImageOptions(R.drawable.user_photo_male));


        } else {
            ImageLoader.getInstance().displayImage(photoUri.toString(), imvWall, displayImageOptions(R.drawable.defult_wall));

        }

    }

    private void createProgressDialog() {

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Сохранение данных");

        progressDialog.setCancelable(false);

    }

    public static DisplayImageOptions displayImageOptions(@DrawableRes int drawableRes) {

        return new DisplayImageOptions.Builder().showImageOnLoading(drawableRes)
                .showImageForEmptyUri(drawableRes)
                .showImageOnFail(drawableRes)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    @Override
    public void onDestroy() {

        postsAdapter.deleteSharingFile();

        super.onDestroy();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.fragment_profile_action_grid:

                rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
                postsAdapter.loadDataWithPagination(rvPosts, this);
                postsAdapter.notifyDataSetChanged();

                break;


            case R.id.fragment_profile_action_list:

                rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
                postsAdapter.loadDataWithPagination(rvPosts, this);
                postsAdapter.notifyDataSetChanged();

                break;

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.navigation, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(LOG_TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {

            case R.id.action_profile_settings:

                Intent intent = new Intent(getContext(), ProfileSettingsActivity.class);

                startActivityForResult(intent, REQUEST_PROFILE_CONFIG);

                break;

        }


        return super.onOptionsItemSelected(item);
    }
}