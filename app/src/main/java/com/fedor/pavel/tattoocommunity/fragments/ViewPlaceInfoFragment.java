package com.fedor.pavel.tattoocommunity.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fedor.pavel.tattoocommunity.MapsActivity;
import com.fedor.pavel.tattoocommunity.ProfileSettingsActivity;
import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.listeners.GetLocationCallback;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;


public class ViewPlaceInfoFragment extends Fragment implements View.OnClickListener {


    private GetLocationCallback locationCallback;

    private AppCompatEditText edtTitle, edtStreet;

    private TextView tvCity, tvCountry;

    private Button btnClose, btnSave;

    private UserModel user;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        user = (UserModel) UserModel.getCurrentUser();

        prepareProgressDialog();

        if (getActivity() instanceof GetLocationCallback) {

            locationCallback = (GetLocationCallback) getActivity();


        }


        View view = inflater.inflate(R.layout.fragment_address_for_map, container, false);

        findViews(view);

        prepareView();

        return view;
    }

    private void findViews(View view) {

        tvCountry = (TextView) view.findViewById(R.id.item_popup_window_tv_county);

        tvCity = (TextView) view.findViewById(R.id.item_popup_window_tv_city);

        edtStreet = (AppCompatEditText) view.findViewById(R.id.item_popup_window_tv_street);

        edtTitle = (AppCompatEditText) view.findViewById(R.id.item_popup_window_edt_title);

        btnClose = (Button) view.findViewById(R.id.fragment_address_for_map_btn_close);

        btnSave = (Button) view.findViewById(R.id.fragment_address_for_map_btn_add);

        if (getActivity() instanceof ProfileSettingsActivity) {

            btnClose.setVisibility(View.GONE);

            btnSave.setVisibility(View.GONE);

            edtTitle.setEnabled(false);

            edtTitle.setFocusable(false);

            edtTitle.setKeyListener(null);

            edtStreet.setEnabled(false);

            edtStreet.setFocusable(false);

            edtStreet.setKeyListener(null);


        }


    }

    private void prepareView() {


        String country = locationCallback.getCountry();

        String city = locationCallback.getCity();

        String street = locationCallback.getStreet();

        String title = locationCallback.getLocationTitle();


        tvCountry.setText(country != null ? country : "-");

        tvCity.setText(city != null ? city : "-");

        edtStreet.setText(street != null ? street : "-");

        edtTitle.setText(title != null ? title : "");

        btnClose.setOnClickListener(this);

        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (getActivity() instanceof MapsActivity) {

            MapsActivity activity = (MapsActivity) locationCallback;

            switch (v.getId()) {

                case R.id.fragment_address_for_map_btn_close:


                    activity.deleteFragment(this);


                    break;

                case R.id.fragment_address_for_map_btn_add:

                    PlaceModel placeModel = user.getPlace();

                    if (placeModel == null) {

                        placeModel = new PlaceModel();

                        user.setPlace(placeModel);

                    }

                    placeModel.setName(edtTitle.getText().toString().trim());

                    placeModel.setStreet(edtStreet.getText().toString().trim());

                    placeModel.setCountryId(tvCountry.getText().toString().trim());

                    placeModel.setCityId(tvCity.getText().toString().trim());

                    placeModel.setCoordinates(activity.getLatLng().latitude, activity.getLatLng().longitude);

                    activity.setResult(Activity.RESULT_OK);

                    activity.finish();

                    break;

            }
        }
    }

    private void prepareProgressDialog() {

        progressDialog = new ProgressDialog(getContext());

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
}
