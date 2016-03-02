package com.fedor.pavel.tattoocommunity.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fedor.pavel.tattoocommunity.adapters.PostRecyclerViewAdapter;
import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.listeners.OnLoadNexDataPartListener;
import com.fedor.pavel.tattoocommunity.listeners.OnPostLoadListener;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.fedor.pavel.tattoocommunity.preferences.PreferencesManager;
import com.fedor.pavel.tattoocommunity.task.LoadPostsTask;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AllPhotosFragment extends Fragment implements View.OnClickListener,
        OnPostLoadListener, OnLoadNexDataPartListener, RadioGroup.OnCheckedChangeListener {

    private static final String LOG_TAG = "AllPhotosFragment";


    private AppBarLayout appBarLayout;

    private String[] categories;

    private Button   btnFilter;

    private List<PostModel> models;

    private PostRecyclerViewAdapter photoAdapter;

    private List<Integer> listCatId;

    private TextView tvChangedCategories;

    private TextView tvLastWorks;

    private RecyclerView rvPhotos;

    private RadioGroup rgListState;

    private CheckBox chbAllCat, chb1, chb0, chb2, chb3, chb4, chb5, chb6, chb7, chb8,
            chb9, chb10, chb11, chb12;

    private Point p;

    private String catStr = "";

    private JSONObject jsonFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        models = new ArrayList<>();
        photoAdapter = new PostRecyclerViewAdapter(this);
        jsonFilter = new JSONObject();


        countNumOfPosts();

        View view = inflater.inflate(R.layout.fragment_all_photos, container, false);

        findViews(view);


        findPosts();


        return view;
    }

    private void findViews(View view) {
        ((AppCompatActivity) getActivity())
                .setSupportActionBar((android.support.v7.widget.Toolbar) view.findViewById(R.id.fragment_all_photos_toolbar));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        rvPhotos = (RecyclerView) view.findViewById(R.id.rv_photos);

        rvPhotos.setAdapter(photoAdapter);

        /*rvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));

        photoAdapter.loadDataWithPagination(rvPhotos, this);*/

        rgListState = (RadioGroup) view.findViewById(R.id.fragment_all_photos_rg_list_state);

        rgListState.setOnCheckedChangeListener(this);

        rgListState.check(R.id.fragment_all_photos_action_grid);

        tvChangedCategories = (TextView) view.findViewById(R.id.tv_all_photos_changed_categories);

        tvLastWorks = (TextView) view.findViewById(R.id.tv_last_works);

        tvLastWorks.setText(getString(R.string.last_photos));

        appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_all_photos_appBarLayout);


        btnFilter = (Button) view.findViewById(R.id.fragment_all_photos_action_filter);

        btnFilter.setOnClickListener(this);


    }

    private void findPosts() {

        LoadPostsTask loadPostsTask = new LoadPostsTask(getContext(),this, jsonFilter);

        loadPostsTask.execute(photoAdapter.getDataLimit(), photoAdapter.getOnlyItemCount());

    }

    private void countNumOfPosts() {

        ParseQuery<PostModel> query = ParseQuery.getQuery(PostModel.class);

        if(jsonFilter.length()>0) {
            query.whereContainedIn(PostModel.CATEGORY_ID_PARSE_KEY, listCatId);

        }
        query.countInBackground(new CountCallback() {

            @Override
            public void done(int count, ParseException e) {

                Log.d(LOG_TAG, "count = " + count);
                photoAdapter.setTotalDataNumber(count);

            }
        });


    }

    @Override
    public void onPostsLoaded(List<PostModel> posts) {



        if (photoAdapter.isLoadingData()) {

            photoAdapter.setIsLoadingData(false);

            photoAdapter.notifyItemChanged(photoAdapter.getItemCount() - 1);

        }

        if (posts != null) {

            photoAdapter.addAllItems(posts);

            rvPhotos.scrollToPosition(photoAdapter.getOnlyItemCount() - posts.size());

        }


    }

    @Override
    public void getNextPart() {



        findPosts();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_all_photos_action_filter:
                showPopupFilter(v);
                break;


        }

    }

    private void showPopupFilter(View view) {


        int popupWidth = 300;
        int popupHeight = 600;

        p = new Point();
        p.x = 120;
        p.y = 150;


        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_all_photos_filtr, null);


        final PopupWindow popup = new PopupWindow(getActivity());
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 30;
        int OFFSET_Y = 30;


        popup.showAsDropDown(view);

        categories = getResources().getStringArray(R.array.tattooCategories);

        chbAllCat = (CheckBox) layout.findViewById(R.id.chb_allWorks_allPhotos_fragment);
        chb0 = (CheckBox) layout.findViewById(R.id.chb_0_allPhotos_fragment);
        chb1 = (CheckBox) layout.findViewById(R.id.chb_1_allPhotos_fragment);
        chb2 = (CheckBox) layout.findViewById(R.id.chb_2_allPhotos_fragment);
        chb3 = (CheckBox) layout.findViewById(R.id.chb_3_allPhotos_fragment);
        chb4 = (CheckBox) layout.findViewById(R.id.chb_4_allPhotos_fragment);
        chb5 = (CheckBox) layout.findViewById(R.id.chb_5_allPhotos_fragment);
        chb6 = (CheckBox) layout.findViewById(R.id.chb_6_allPhotos_fragment);
        chb7 = (CheckBox) layout.findViewById(R.id.chb_7_allPhotos_fragment);
        chb8 = (CheckBox) layout.findViewById(R.id.chb_8_allPhotos_fragment);
        chb9 = (CheckBox) layout.findViewById(R.id.chb_9_allPhotos_fragment);
        chb10 = (CheckBox) layout.findViewById(R.id.chb_10_allPhotos_fragment);
        chb11 = (CheckBox) layout.findViewById(R.id.chb_11_allPhotos_fragment);
        chb12 = (CheckBox) layout.findViewById(R.id.chb_12_allPhotos_fragment);

        chb0.setText(categories[0]);
        chb1.setText(categories[1]);
        chb2.setText(categories[2]);
        chb3.setText(categories[3]);
        chb4.setText(categories[4]);
        chb5.setText(categories[5]);
        chb6.setText(categories[6]);
        chb7.setText(categories[7]);
        chb8.setText(categories[8]);
        chb9.setText(categories[9]);
        chb10.setText(categories[10]);
        chb11.setText(categories[11]);
        chb12.setText(categories[12]);

        for (int i = 0; i < PreferencesManager.getAllSum(getActivity(), PreferencesManager.KEY_PREFERENCES_FILTR_ALL_WORKS).size(); i++) {
            int cat = PreferencesManager.loadInt(getActivity(), PreferencesManager.KEY_PREFERENCES_FILTR_ALL_WORKS, "key" + i);

            if (cat == 0) chb0.setChecked(true);
            else if (cat == 1) chb1.setChecked(true);
            else if (cat == 2) chb2.setChecked(true);
            else if (cat == 3) chb3.setChecked(true);
            else if (cat == 4) chb4.setChecked(true);
            else if (cat == 5) chb5.setChecked(true);
            else if (cat == 6) chb6.setChecked(true);
            else if (cat == 7) chb7.setChecked(true);
            else if (cat == 8) chb8.setChecked(true);
            else if (cat == 9) chb9.setChecked(true);
            else if (cat == 10) chb10.setChecked(true);
            else if (cat == 11) chb11.setChecked(true);
            else if (cat == 12) chb12.setChecked(true);
        }

        Button close = (Button) layout.findViewById(R.id.btn_ok_popup_allPhotos_fragment);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                catStr = "";
                listCatId = new ArrayList<>();
                jsonFilter = new JSONObject();

                if (chb0.isChecked()) {
                    makeFilter(0);
                }
                if (chb1.isChecked()) {
                    makeFilter(1);
                }
                if (chb2.isChecked()) {
                    makeFilter(2);
                }
                if (chb3.isChecked()) {
                    makeFilter(3);
                }
                if (chb4.isChecked()) {
                    makeFilter(4);
                }
                if (chb5.isChecked()) {
                    makeFilter(5);
                }
                if (chb6.isChecked()) {
                    makeFilter(6);
                }
                if (chb7.isChecked()) {
                    makeFilter(7);
                }
                if (chb8.isChecked()) {
                    makeFilter(8);
                }
                if (chb9.isChecked()) {
                    makeFilter(9);
                }
                if (chb10.isChecked()) {
                    makeFilter(10);
                }
                if (chb11.isChecked()) {
                    makeFilter(11);
                }
                if (chb12.isChecked()) {
                    makeFilter(12);
                }

                if (chbAllCat.isChecked() || listCatId.size()==0) {
                    listCatId.clear();
                    PreferencesManager.delData(getActivity(), PreferencesManager.KEY_PREFERENCES_FILTR_ALL_WORKS);
                    catStr = (getString(R.string.all_works));

                }

                tvChangedCategories.setText(catStr);


                if (listCatId.size() > 0) {
                    PreferencesManager.delData(getActivity(), PreferencesManager.KEY_PREFERENCES_FILTR_ALL_WORKS);
                    PreferencesManager.saveDataIntList(getActivity(), PreferencesManager.KEY_PREFERENCES_FILTR_ALL_WORKS, listCatId);

                } else {

                    jsonFilter = new JSONObject();
                }

                popup.dismiss();
                photoAdapter.clearItems();
                countNumOfPosts();
                findPosts();


            }
        });
    }

    public void makeFilter(int checkboxNum){

        if(jsonFilter==null){
            jsonFilter = new JSONObject();
        }
        listCatId.add(checkboxNum);

        if(catStr== (getString(R.string.all_works))){
            catStr = "";
        }

        if (catStr != "") {
            catStr += ", ";
        }
        catStr += categories[checkboxNum];

                try {
                    jsonFilter.put("" + jsonFilter.length(), checkboxNum);
                } catch (JSONException e) {
                    e.printStackTrace();

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){

            case R.id.fragment_all_photos_action_grid:

                rvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
                photoAdapter.loadDataWithPagination(rvPhotos, this);
                photoAdapter.notifyDataSetChanged();

                break;

            case R.id.fragment_all_photos_action_list:

                rvPhotos.setLayoutManager(new LinearLayoutManager(getContext()));
                photoAdapter.loadDataWithPagination(rvPhotos, this);
                photoAdapter.notifyDataSetChanged();

                break;
        }



    }
}
