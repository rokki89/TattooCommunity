package com.fedor.pavel.tattoocommunity.fragments;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.adapters.ListMasterAdapter;
import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.listeners.OnLoadNexDataPartListener;
import com.fedor.pavel.tattoocommunity.listeners.OnMasterLoadListener;
import com.fedor.pavel.tattoocommunity.models.CityModel;
import com.fedor.pavel.tattoocommunity.models.CountryModel;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.fedor.pavel.tattoocommunity.task.LoadMastersTask;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pcc on 01.02.2016.
 */
public class MasterFragment extends Fragment implements View.OnClickListener,
        OnMasterLoadListener, OnLoadNexDataPartListener {

    private static final String LOG_TAG = "MasterFragment";


    private AppBarLayout appBarLayout;

    private TextView tvTitle;
    private TextView tvChoice;
    private LinearLayout llChoice;

    private ImageButton btnSettings, btnFilter, btnCloseChoice;

    private List<UserModel> models;
    private List<String> countries;
    private ListMasterAdapter adapter;
    private List<Integer> listCatId;
    private Set<String> namesCountry;

    private RecyclerView rvMasters;

    private List<PlaceModel> listPlaces;

    private JSONObject jsonFilter;
    private AutoCompleteTextView autocompleteCountry;
    private AutoCompleteTextView autocompleteCity;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        models = new ArrayList<>();
        adapter = new ListMasterAdapter(this);
        countries = new ArrayList<>();

        countNumOfMasters();

        jsonFilter = new JSONObject();

        View view = inflater.inflate(R.layout.fragment_list_master, container, false);

        findViews(view);


        findMasters();


        return view;
    }

    private void findViews(View view) {

        ((AppCompatActivity) getActivity())
                .setSupportActionBar((android.support.v7.widget.Toolbar) view.findViewById(R.id.fragment_list_masters_toolbar));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        rvMasters = (RecyclerView) view.findViewById(R.id.rv_list_masters);
        rvMasters.setAdapter(adapter);
        rvMasters.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.loadDataWithPagination(rvMasters, this);

        tvTitle = (TextView) view.findViewById(R.id.tv_list_masters);
        tvChoice = (TextView) view.findViewById(R.id.tv_chosen_country);
        tvTitle.setText(getString(R.string.master_list));

        llChoice = (LinearLayout) view.findViewById(R.id.ll_choice_frag_master_list);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_list_masters_appBarLayout);

        btnFilter = (ImageButton) view.findViewById(R.id.fragment_list_masters_action_filter);
        btnFilter.setOnClickListener(this);

        //btnSettings = (ImageButton) view.findViewById(R.id.fragment_list_masters_action_settings);
//        btnSettings.setOnClickListener(this);

        btnCloseChoice = (ImageButton) view.findViewById(R.id.btn_close_chosen_country);
        btnCloseChoice.setOnClickListener(this);

    }


    private void findMasters() {
        LoadMastersTask loadPostsTask = new LoadMastersTask(getContext(), this, jsonFilter);

        loadPostsTask.execute(10, adapter.getOnlyItemCount());

    }

    private void countNumOfMasters() {

        listPlaces  = new ArrayList<>();

        if(jsonFilter!=null && jsonFilter.length()==1) {

            String country = "";

            try {
                 country = jsonFilter.getString("1");
                Log.d(LOG_TAG, "country = " + country);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ParseQuery<PlaceModel> queryPlace = ParseQuery.getQuery(PlaceModel.class);

            if(country != ""){

                queryPlace.whereEqualTo(PlaceModel.COUNTRY_ID_PARSE_KEY, country);
            }

            try {
                listPlaces = queryPlace.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if(jsonFilter!=null && jsonFilter.length()==2){

        }

        ParseQuery<UserModel> query = ParseQuery.getQuery(UserModel.class);
        query.whereEqualTo("reg_type", 1);

        if(listPlaces.size()!=0){

            query.whereContainedIn(UserModel.PLACE_PARSE_KEY, listPlaces);
        }

        query.countInBackground(new CountCallback() {

            @Override
            public void done(int count, ParseException e) {

                Log.d(LOG_TAG, "count = " + count);
                adapter.setTotalDataNumber(count);

            }
        });
    }


    @Override
    public void onMasterLoaded(List<UserModel> users) {
        Log.d(LOG_TAG, "users.size = " + users.size());
        if (adapter.isLoadingData()) {
            Log.d(LOG_TAG, "photoAdapter.isLoadingData() = " + adapter.isLoadingData());
            adapter.setIsLoadingData(false);

            adapter.notifyItemChanged(adapter.getItemCount() - 1);

        }

        if (users != null) {

            adapter.addAllItems(users);

            rvMasters.scrollToPosition(adapter.getOnlyItemCount() - users.size());

        }


    }

    @Override
    public void getNextPart() {

        findMasters();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_list_masters_action_filter:

                showPopupFilter();

                break;
            //case R.id.fragment_list_masters_action_settings:


               // break;
            case R.id.btn_close_chosen_country:

                llChoice.setVisibility(View.GONE);
                tvChoice.setText("");
                jsonFilter = new JSONObject();
                adapter.clearItems();
                countNumOfMasters();
                findMasters();


                break;
        }

    }


    private void showPopupFilter() {

        namesCountry = new HashSet<String>();
        String[] countries = getResources().getStringArray(R.array.countries);
        for (int i = 0; i < countries.length; i++) {
            namesCountry.add(countries[i]);
        }

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_masters_list, null);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        helpBuilder.setView(layout);

        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

        btnSearch = (Button) layout.findViewById(R.id.popup_masterList_btn_search);
        btnSearch.setClickable(false);
        btnSearch.setEnabled(false);
        btnSearch.setSelected(false);
        autocompleteCity = (AutoCompleteTextView) layout.findViewById(R.id.popup_masterList_autocomplete_city);
        autocompleteCountry = (AutoCompleteTextView) layout.findViewById(R.id.popup_masterList_autocomplete_country);
        autocompleteCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String country = autocompleteCountry.getText().toString();
                if (namesCountry.contains(country)) {
                    if(jsonFilter.length()==0){
                        try {
                            jsonFilter.put("1", country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        jsonFilter = new JSONObject();
                        try {
                            jsonFilter.put("1", country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    tvChoice.setText(country);
                    autocompleteCity.setVisibility(View.VISIBLE);
                    autocompleteCity.requestFocus();
                    btnSearch.setClickable(true);
                    btnSearch.setEnabled(true);
                    btnSearch.setSelected(true);
                } else {
                    jsonFilter = new JSONObject();
                    autocompleteCity.setVisibility(View.GONE);
                    autocompleteCity.setText("");
                    btnSearch.setClickable(false);
                    btnSearch.setEnabled(false);
                    btnSearch.setSelected(false);
                }

            }

        });

        final ArrayAdapter<String> adapterAutocompl =
                new ArrayAdapter<>(getActivity(), R.layout.autocomplete_list_item, countries);
        autocompleteCountry.setAdapter(adapterAutocompl);

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                llChoice.setVisibility(View.VISIBLE);

                adapter.clearItems();
                countNumOfMasters();
                findMasters();

                helpDialog.dismiss();
            }

        });
    }


}
