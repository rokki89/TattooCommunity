package com.fedor.pavel.tattoocommunity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;



import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.data.FileManager;
import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.fedor.pavel.tattoocommunity.views.ProportionalImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;


public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {


    private ProportionalImageView imvPhoto;

    private EditText edtTitle;

    private AppCompatSpinner spType, spCategory;

    private ArrayAdapter<String> spTypeAdapter;

    private ArrayAdapter<String> spCategoryAdapter;

    private PostModel postModel = new PostModel();

    private FrameLayout flPanel;

    private ImageButton ibCamera, ibGallery;

    private ProgressDialog progressDialog;

    private int position;

    private Uri photoUri;

    public static final int REQUEST_FROM_GALLERY = 1;

    private static final int REQUEST_FROM_CAM = 2;

    private int resultCode = ProfileFragment.RESULT_CODE_POST_CREATED;

    private boolean isPhotoSelected = false;

    private static final String LOG_TAG = "CreatePostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);

        createProgressDialog();

        spTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.postType));

        spCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tattooCategories));

        findViews();

        if (!getDataFormIntent()) {

            postModel.setUserId(UserModel.getCurrentUser().getObjectId());

            postModel.setType(spType.getSelectedItemPosition());

            postModel.setCategoryId(spCategory.getSelectedItemPosition());
        }

        prepareViews();

        getDataFormIntent();

    }

    private void findViews() {

        spType = (AppCompatSpinner) findViewById(R.id.activity_create_post_sp_type);

        spCategory = (AppCompatSpinner) findViewById(R.id.activity_create_post_sp_category);

        imvPhoto = (ProportionalImageView) findViewById(R.id.activity_create_post_imv_photo);

        flPanel = (FrameLayout) findViewById(R.id.activity_create_post_fl_panel);

        ibCamera = (ImageButton) findViewById(R.id.activity_create_post_ib_camera);

        ibGallery = (ImageButton) findViewById(R.id.activity_create_post_ib_gallery);

        edtTitle = (EditText) findViewById(R.id.activity_create_post_edt_title);

    }

    private void prepareToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    private void prepareViews() {

        prepareToolbar();

        spType.setAdapter(spTypeAdapter);

        spCategory.setAdapter(spCategoryAdapter);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                postModel.setType(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                postModel.setCategoryId(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imvPhoto.displayWithProportion(postModel);

        imvPhoto.setOnClickListener(this);

        ibCamera.setOnClickListener(this);

        ibGallery.setOnClickListener(this);


    }

    private void showPanel() {

        if (flPanel.getAlpha() == 1f) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(flPanel, "alpha", 1f, 0f);// изменяет прозрачность от 0 до 1
            anim.setDuration(350); // длительность анимации
            anim.start();
        } else {
            ObjectAnimator anim = ObjectAnimator.ofFloat(flPanel, "alpha", 0f, 1f);// изменяет прозрачность от 0 до 1
            anim.setDuration(350);// длительность анимации
            anim.start();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_create_post_imv_photo:

                showPanel();

                break;

            case R.id.activity_create_post_ib_camera:

                requestPhotoFromCamera();

                break;

            case R.id.activity_create_post_ib_gallery:

                requestPhotoFromGallery();

                break;

        }

    }

    private void requestPhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_FROM_GALLERY);

    }

    private void requestPhotoFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = FileManager.createFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {

                photoUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoUri);

                startActivityForResult(intent, REQUEST_FROM_CAM);
            }
        }


    }

    private void createProgressDialog() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);

    }

    private void showProgress(String message) {

        progressDialog.setMessage(message);

        progressDialog.show();

    }

    private void dismissProgress() {

        if (progressDialog.isShowing()) {

            progressDialog.dismiss();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FROM_GALLERY && resultCode == RESULT_OK) {


            String url = data.getDataString();

            Bitmap photo = ImageLoader.getInstance().loadImageSync(url, displayImageOptions());

            postModel.setPhoto(photo);

            imvPhoto.displayWithProportion(postModel, url);

            isPhotoSelected = true;

            Log.d(LOG_TAG, url);

            flPanel.setAlpha(0f);


            return;

        } else if (requestCode == REQUEST_FROM_CAM && resultCode == RESULT_OK) {


            Bitmap photo = ImageLoader.getInstance().loadImageSync(photoUri.toString(), displayImageOptions());

            postModel.setPhoto(photo);

            imvPhoto.displayWithProportion(postModel, photoUri.toString());

            isPhotoSelected = true;

            flPanel.setAlpha(0f);

        }


    }

    public static DisplayImageOptions displayImageOptions() {

        return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_photo)
                .showImageForEmptyUri(R.drawable.default_photo)
                .showImageOnFail(R.drawable.default_photo)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_apply:

                if (isPhotoSelected) {

                    postModel.setTitle(edtTitle.getText().toString());

                    showProgress(getResources().getText(R.string.savingDataProgressMessage).toString());

                    postModel.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                Intent intent = new Intent();

                                if (position != -1) {

                                    intent.putExtra(ParseSDKConstants.PARSE_MODEL_POSITION_INTENT_KEY, position);
                                }

                                setResult(resultCode, intent);

                                CreatePostActivity.this.finish();

                            }

                            dismissProgress();
                        }
                    });


                } else {

                    if (flPanel.getAlpha() == 0f) {
                        showPanel();
                    }

                    Snackbar.make(flPanel, "Выберите фото", Snackbar.LENGTH_LONG).show();

                }

                return true;

            case android.R.id.home:


                this.finish();

                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    private boolean getDataFormIntent() {

        boolean isEdit = false;

        String postId = getIntent().getStringExtra(ParseSDKConstants.PARSE_KEY_OBJECT_ID);

        position = getIntent().getIntExtra(ParseSDKConstants.PARSE_MODEL_POSITION_INTENT_KEY, -1);

        if (postId != null && !postId.isEmpty() && position != -1) {

            resultCode = ProfileFragment.RESULT_CODE_POST_EDIT;

            isEdit = true;

            ParseQuery<PostModel> query = ParseQuery.getQuery(PostModel.class);

            query.fromPin(ParseSDKConstants.PARSE_LABEL_RECEIVE_MODEL);

            showProgress(getResources().getText(R.string.loadDataProgressMessage).toString());

            query.getInBackground(postId, new GetCallback<PostModel>() {

                @Override
                public void done(PostModel object, ParseException e) {

                    dismissProgress();

                    if (e == null) {

                        postModel = object;

                        edtTitle.setText(object.getTitle());

                        spCategory.setSelection(postModel.getCategoryId(), true);

                        spType.setSelection(postModel.getType(), true);

                        imvPhoto.displayWithProportion(postModel);

                        isPhotoSelected = true;

                    }

                }
            });

        }

        return isEdit;

    }


}
