package com.fedor.pavel.tattoocommunity.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedor.pavel.tattoocommunity.CreatePostActivity;
import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.data.FileManager;
import com.fedor.pavel.tattoocommunity.dialogs.DialogManager;
import com.fedor.pavel.tattoocommunity.fragments.AllPhotosFragment;
import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.fedor.pavel.tattoocommunity.models.LikeModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.fedor.pavel.tattoocommunity.views.ProportionalImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostRecyclerViewAdapter extends LoadDataRecyclerViewAdapter<PostModel, RecyclerView.ViewHolder> {


    private String[] postType;

    private String[] tattooCategories;

    private LayoutInflater inflater;

    private Context context;

    private ProfileFragment fragment;

    private AllPhotosFragment allPhotosFragment;

    private UserModel user;

    private static final String LOG_TAG = "PostRVAdapter";

//    public PostRecyclerViewAdapter(Context context, List<PostModel> posts) {
//
//        this.inflater = LayoutInflater.from(context);
//
//        items.addAll(posts);
//
//        postType = context.getResources().getStringArray(R.array.postType);
//
//        tattooCategories = context.getResources().getStringArray(R.array.tattooCategories);
//
//        addFooter(R.layout.item_rv_footer_progress_bar);
//
//        this.context = context;
//
//    }
//
//    public PostRecyclerViewAdapter(Context context) {
//
//        this.inflater = LayoutInflater.from(context);
//
//        postType = context.getResources().getStringArray(R.array.postType);
//
//        tattooCategories = context.getResources().getStringArray(R.array.tattooCategories);
//
//        addFooter(R.layout.item_rv_footer_progress_bar);
//
//        this.context = context;
//
//    }

    public PostRecyclerViewAdapter(ProfileFragment fragment) {

        this.inflater = LayoutInflater.from(fragment.getContext());

        postType = fragment.getResources().getStringArray(R.array.postType);

        tattooCategories = fragment.getResources().getStringArray(R.array.tattooCategories);

        addFooter(R.layout.item_rv_footer_progress_bar);

        this.context = fragment.getContext();

        this.fragment = fragment;

    }

    public PostRecyclerViewAdapter(AllPhotosFragment allPhotosFragment) {

        this.inflater = LayoutInflater.from(allPhotosFragment.getContext());

        postType = allPhotosFragment.getResources().getStringArray(R.array.postType);

        tattooCategories = allPhotosFragment.getResources().getStringArray(R.array.tattooCategories);

        addFooter(R.layout.item_rv_footer_progress_bar);

        this.context = allPhotosFragment.getContext();

        this.allPhotosFragment = allPhotosFragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        RecyclerView.ViewHolder holder = null;

        switch (viewType) {

            case ViewTyp.VIEW_TYPE_ITEM:

                if(parent.getId()==R.id.rv_photos){
                    holder = new LinearLayoutItemsViewHolder(inflater.inflate(R.layout.item_rv_posts_list_all, parent, false));
                }else if(parent.getId()==R.id.fragment_profile_rv_posts){
                    holder = new LinearLayoutItemsViewHolder(inflater.inflate(R.layout.item_recycler_view_posts_list, parent, false));
                }

                break;

            case ViewTyp.VIEW_TYPE_FOOTER:

                holder = new FooterViewHolder(inflater.inflate(footers.get(0), parent, false));

                break;
            case ViewTyp.VIEW_TYPE_GRID:

                holder = new GridLayoutItemsViewHolder(inflater.inflate(R.layout.item_post, parent, false));

                break;


        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LinearLayoutItemsViewHolder) {

            bindLinearLayoutItemsViewHolder((LinearLayoutItemsViewHolder) holder, position);

        } else if (holder instanceof FooterViewHolder) {

            bindFooterViewHolder((FooterViewHolder) holder);

        } else if (holder instanceof GridLayoutItemsViewHolder) {

            bindGridLayoutItemsViewHolder((GridLayoutItemsViewHolder) holder, position);
        }


    }

    public void bindLinearLayoutItemsViewHolder(LinearLayoutItemsViewHolder holder, int position) {

        PostModel postModel = items.get(position);

        holder.imvPhoto.displayWithProportion(postModel);

        holder.tvTitle.setText(postModel.getTitle());

        holder.tvCategory.setText(tattooCategories[postModel.getCategoryId()]);

        holder.tvType.setText(postType[postModel.getType()]);

        holder.chbLike.setChecked(postModel.getLike().isLike());

        if(holder.tvName!=null){

            user = new UserModel();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("objectId", postModel.getUserId());
            try {
                user = (UserModel)query.getFirst();
                holder.tvName.setText(user.getFullName());
                ImageLoader.getInstance().displayImage(user.getPhotoUrl(), holder.imgAvatar, setLoaderOptions());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d(LOG_TAG, user.getFullName());


        }


    }

    public void bindGridLayoutItemsViewHolder(GridLayoutItemsViewHolder holder, int position) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        holder.imvPhoto.getLayoutParams().height = width/3 - 5;
        holder.imvPhoto.getLayoutParams().width = width/3 - 5;

        PostModel postModel = items.get(position);


        ImageLoader.getInstance().displayImage(postModel.getPhotoUrl(), holder.imvPhoto, setLoaderOptions());

    }

    public void bindFooterViewHolder(FooterViewHolder holder) {

        if (!isLoadingData) {

            holder.flProgressContainer.setScaleY(0f);

            holder.flProgressContainer.setVisibility(View.GONE);


        } else {

            holder.flProgressContainer.setVisibility(View.VISIBLE);

            holder.flProgressContainer.setScaleY(1f);
        }

    }

    public class LinearLayoutItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        ProportionalImageView imvPhoto;

        ImageButton ibRemove, ibShare, ibEdit;

        CheckBox chbLike;

        TextView tvTitle, tvCategory, tvType, tvName;

        CircleImageView imgAvatar;

        public LinearLayoutItemsViewHolder(View itemView) {

            super(itemView);

            findViews();

            prepareViews();

        }

        private void findViews() {

            try {
                tvName = (TextView) itemView.findViewById(R.id.item_rv_posts_imv_all_name);
                imgAvatar = (CircleImageView) itemView.findViewById(R.id.item_rv_posts_imv_all_avatar);
            }catch (NullPointerException e){

            }

            try {
                ibRemove = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_remove);
                ibEdit = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_edit);
                ibShare = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_share);
            }catch (NullPointerException e){

            }

            imvPhoto = (ProportionalImageView) itemView.findViewById(R.id.item_rv_posts_imv_photo);

            chbLike = (CheckBox) itemView.findViewById(R.id.item_rv_posts_chb_like);

            tvTitle = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_title);

            tvCategory = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_category);

            tvType = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_type);

        }

        private void prepareViews() {

            if(ibRemove!=null){
                ibRemove.setOnClickListener(this);
                ibEdit.setOnClickListener(this);
                ibShare.setOnClickListener(this);
            }else if(tvName!=null){
                tvName.setOnClickListener(this);
                imgAvatar.setOnClickListener(this);
            }

            chbLike.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {


            switch (v.getId()) {

                case R.id.item_rv_posts_chb_like:

                    changeLikeStatus();

                    break;

                case R.id.item_rv_posts_ib_remove:

                    DialogManager.showChoiceDialog(context, "Удалить пост?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == DialogInterface.BUTTON_POSITIVE) {

                                items.get(getAdapterPosition()).deleteInBackground();

                                removeItem(getAdapterPosition());

                            }
                        }
                    });

                    break;

                case R.id.item_rv_posts_ib_share:

                    sharePost();

                    break;

                case R.id.item_rv_posts_ib_edit:


                    items.get(getAdapterPosition()).pinInBackground(ParseSDKConstants.PARSE_LABEL_RECEIVE_MODEL, new SaveCallback() {

                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                Intent intent = new Intent(fragment.getContext(), CreatePostActivity.class);

                                intent.putExtra(ParseSDKConstants.PARSE_KEY_OBJECT_ID, items.get(getAdapterPosition()).getObjectId());

                                intent.putExtra(ParseSDKConstants.PARSE_MODEL_POSITION_INTENT_KEY,getAdapterPosition());

                                fragment.startActivityForResult(intent, ProfileFragment.REQUEST_CREATE_POST);

                            }

                        }
                    });

                    break;


            }


        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        }

        private void changeLikeStatus() {

            PostModel postModel = items.get(getAdapterPosition());

            postModel.increment(PostModel.NUM_OF_LIKES_PARSE_KEY, chbLike.isChecked() ? 1 : -1);

            postModel.saveInBackground();

            chbLike.setText(postModel.getNumOfLikes() + "");

            LikeModel model = postModel.getLike();

            model.setUserId(UserModel.getCurrentUser().getObjectId());

            model.setPostId(postModel.getObjectId());

            model.setLike(chbLike.isChecked());


            model.saveEventually();


        }

        private void sharePost() {

            AsyncTask<Void, Void, Uri> task = new AsyncTask<Void, Void, Uri>() {

                private PostModel post = items.get(getAdapterPosition());

                @Override
                protected void onPreExecute() {

                    fragment.showProgressDialog();
                    super.onPreExecute();
                }

                @Override
                protected Uri doInBackground(Void... params) {


                    FileOutputStream outputStream = null;

                    File file = null;


                    try {


                        file = FileManager.createFile(context);

                        outputStream = new FileOutputStream(file);

                        Bitmap bm = ImageLoader.getInstance().loadImageSync(post.getPhotoUrl(), loadImageOptions());

                        bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    } catch (IOException | NullPointerException e) {

                        Log.d(LOG_TAG, "" + e);

                        return null;

                    } finally {

                        if (outputStream != null) {

                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    return Uri.fromFile(file);

                }

                @Override
                protected void onPostExecute(Uri uri) {
                    super.onPostExecute(uri);

                    fragment.dismissProgressDialog();

                    if (uri != null) {

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                "#" + ((UserModel) UserModel.getCurrentUser()).getFullName()
                                        + " #" + fragment.getCountry().getName() + " #" + fragment.getCity().getName() +
                                        (post.getTitle().isEmpty() ? "" : "#" + post.getTitle().replace("_", ""))
                                        + " #" + postType[post.getType()]
                                        + " #" + tattooCategories[post.getCategoryId()]);

                        shareIntent.setType("image/*");

                        context.startActivity(Intent.createChooser(shareIntent, "Опубликовать в:"));

                    }

                }
            };

            task.execute();

        }

        public DisplayImageOptions loadImageOptions() {

            return new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .imageScaleType(ImageScaleType.NONE)
                    .build();
        }

    }


    public class GridLayoutItemsViewHolder extends RecyclerView.ViewHolder{

        ImageView imvPhoto;


        public GridLayoutItemsViewHolder(View itemView) {

            super(itemView);

            findViews();


        }

        private void findViews() {

            imvPhoto = (ImageView) itemView.findViewById(R.id.img_photo_post);

        }



    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        FrameLayout flProgressContainer;


        public FooterViewHolder(View itemView) {
            super(itemView);

            flProgressContainer = (FrameLayout) itemView.findViewById(R.id.progress_bar_container);
        }
    }

    public DisplayImageOptions setLoaderOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_photo)
                .showImageForEmptyUri(R.drawable.default_photo)
                .showImageOnFail(R.drawable.default_photo)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }

}
