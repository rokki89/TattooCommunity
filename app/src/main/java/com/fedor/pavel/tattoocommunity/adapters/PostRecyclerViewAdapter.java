package com.fedor.pavel.tattoocommunity.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.models.LikeModel;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.fedor.pavel.tattoocommunity.views.ProportionalImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;


public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.GridViewHolder> {

    private ArrayList<PostModel> posts = new ArrayList<>();

    private String[] postType;

    private String [] tattooCategories;

    private LayoutInflater inflater;

    private static final String LOG_TAG = "PostRVAdapter";


    public PostRecyclerViewAdapter(Context context, List<PostModel> posts) {

        this.inflater = LayoutInflater.from(context);

        this.posts.addAll(posts);

        postType = context.getResources().getStringArray(R.array.postType);

        tattooCategories = context.getResources().getStringArray(R.array.tattooCategories);

    }

    public PostRecyclerViewAdapter(Context context) {

        this.inflater = LayoutInflater.from(context);

        postType = context.getResources().getStringArray(R.array.postType);

        tattooCategories = context.getResources().getStringArray(R.array.tattooCategories);

    }

    public void addPost(PostModel post) {

        posts.add(post);


    }

    public void addAllPosts(List<PostModel> posts) {

        int start = this.posts.size();

        this.posts.addAll(posts);

        int end = posts.size();

        notifyItemRangeInserted(start, end);

    }

    public void removePost(int position) throws ArrayIndexOutOfBoundsException {

        posts.remove(position);

        notifyItemRemoved(position);

    }

    public void clear() {

        posts.clear();

        notifyDataSetChanged();

    }



    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Log.d(LOG_TAG,"onCreateViewHolder");

        return new GridViewHolder(inflater.inflate(R.layout.item_recycler_view_posts_list, parent, false));
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {

        Log.d(LOG_TAG,"onBindViewHolder");

        PostModel postModel = posts.get(position);

        holder.imvPhoto.displayWithProportion(postModel);

        //ImageLoader.getInstance().displayImage(postModel.getPhotoUrl(), holder.imvPhoto, displayImageOptions());

        holder.tvTitle.setText(postModel.getTitle());

        holder.tvCategory.setText(tattooCategories[postModel.getCategoryId()]);

        holder.tvType.setText(postType[postModel.getType()]);

        holder.chbLike.setChecked(postModel.getLike().isLike());

        holder.chbLike.setText(postModel.getNumOfLikes() + "");

        Log.d(LOG_TAG, "onBindViewHolder");

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        Log.d(LOG_TAG,"getItemCount");

        return posts.size();

    }

    public static DisplayImageOptions displayImageOptions() {

        return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_panel_gradient)
                .showImageForEmptyUri(R.drawable.bg_panel_gradient)
                .showImageOnFail(R.drawable.bg_panel_gradient)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        ProportionalImageView imvPhoto;

        ImageButton ibMenu, ibShare, ibComments;

        CheckBox chbLike;

        TextView tvTitle, tvCategory, tvType;

        public GridViewHolder(View itemView) {

            super(itemView);

            findViews();

            prepareViews();


        }

        private void findViews() {

            imvPhoto = (ProportionalImageView) itemView.findViewById(R.id.item_rv_posts_imv_photo);

            ibMenu = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_menu);

            ibComments = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_comments);

            ibShare = (ImageButton) itemView.findViewById(R.id.item_rv_posts_ib_share);

            chbLike = (CheckBox) itemView.findViewById(R.id.item_rv_posts_chb_like);

            tvTitle = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_title);

            tvCategory = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_category);

            tvType = (TextView) itemView.findViewById(R.id.item_rv_posts_tv_type);

        }

        private void prepareViews() {

            ibMenu.setOnClickListener(this);

            ibShare.setOnClickListener(this);

            ibComments.setOnClickListener(this);

            chbLike.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.item_rv_posts_chb_like:

                    changeLikeStatus();

                    break;


            }


        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        }

        private void changeLikeStatus() {

            PostModel postModel = posts.get(getAdapterPosition());

            postModel.increment(PostModel.NUM_OF_LIKES_PARSE_KEY, chbLike.isChecked() ? 1 : -1);

            postModel.saveInBackground();

            chbLike.setText(postModel.getNumOfLikes() + "");


            LikeModel model = postModel.getLike();

            model.setUserId(UserModel.getCurrentUser().getObjectId());

            model.setPostId(postModel.getObjectId());

            model.setLike(chbLike.isChecked());


            model.saveEventually();


        }
    }


}
