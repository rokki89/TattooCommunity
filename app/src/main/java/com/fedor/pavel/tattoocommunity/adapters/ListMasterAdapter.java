package com.fedor.pavel.tattoocommunity.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.fragments.MasterFragment;
import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pcc on 19.01.2016.
 */
public class ListMasterAdapter extends LoadDataRecyclerViewAdapter<UserModel, RecyclerView.ViewHolder> {


    private String[] postType;

    private String[] tattooCategories;

    private LayoutInflater inflater;

    private Context context;

    private MasterFragment fragment;

    private static final String LOG_TAG = "PostRVAdapter";


    public ListMasterAdapter(MasterFragment fragment) {

        this.inflater = LayoutInflater.from(fragment.getContext());

        postType = fragment.getResources().getStringArray(R.array.postType);

        tattooCategories = fragment.getResources().getStringArray(R.array.tattooCategories);

        addFooter(R.layout.item_rv_footer_progress_bar);

        this.context = fragment.getContext();

        this.fragment = fragment;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder holder = new LinearLayoutItemsViewHolder(inflater.inflate(R.layout.item_master_list, parent, false));


            if(viewType==ViewTyp.VIEW_TYPE_FOOTER) {
                holder = new FooterViewHolder(inflater.inflate(footers.get(0), parent, false));
            }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LinearLayoutItemsViewHolder) {

            bindLinearLayoutItemsViewHolder((LinearLayoutItemsViewHolder) holder, position);

        }


    }

    public void bindLinearLayoutItemsViewHolder(LinearLayoutItemsViewHolder holder, int position) {

        UserModel userModel = items.get(position);

        try {

            userModel.getPlace().fetchIfNeeded();

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvName.setText(userModel.getFullName());

        ImageLoader.getInstance().displayImage(userModel.getPhotoUrl(), holder.imgAvatar, ProfileFragment.displayImageOptions(R.drawable.user_photo_male));

        PlaceModel placeModel = userModel.getPlace();

        if (placeModel != null) {
            Log.d(LOG_TAG, "placeModel");
           String city = placeModel.getCityId();

           String country = placeModel.getCountryId();

            if (city != null) {

                holder.tvCity.setText(city);

            }
            if (country != null) {

                holder.tvCountry.setText(country);

            }
        }


    }


    public class LinearLayoutItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


        TextView tvName, tvCountry, tvCity;

        CircleImageView imgAvatar;

        LinearLayout linearLayout;


        public LinearLayoutItemsViewHolder(View itemView) {

            super(itemView);

            findViews();

            prepareViews();

        }

        private void findViews() {

            tvName = (TextView) itemView.findViewById(R.id.item_master_list_name);

            tvCountry = (TextView) itemView.findViewById(R.id.item_master_list_country);

            tvCity = (TextView) itemView.findViewById(R.id.item_master_list_city);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.item_master_list_ava);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_master_list_LL);

        }

        private void prepareViews() {
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        }



    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        FrameLayout flProgressContainer;


        public FooterViewHolder(View itemView) {
            super(itemView);

            flProgressContainer = (FrameLayout) itemView.findViewById(R.id.progress_bar_container);
        }
    }



}
