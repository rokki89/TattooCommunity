package com.fedor.pavel.tattoocommunity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.fedor.pavel.tattoocommunity.comstants.ParseSDKConstants;
import com.fedor.pavel.tattoocommunity.fragments.ViewPlaceInfoFragment;
import com.fedor.pavel.tattoocommunity.fragments.ProfileFragment;
import com.fedor.pavel.tattoocommunity.listeners.GetLocationCallback;
import com.fedor.pavel.tattoocommunity.models.PlaceModel;
import com.fedor.pavel.tattoocommunity.models.UserModel;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener, GetLocationCallback {

    private TextView tvSetPlace;

    private EditText edtName, edtEmail, edtPhone;

    private TextInputLayout tilName, tilEmail, tilPhone;

    private NestedScrollView scrollView;

    private UserModel user;

    public static final int REQUEST_ADD_WORK_PLACE = 1;

    private static final String LOG_TAG = "ProfSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);


        user = (UserModel) UserModel.getCurrentUser();

        findViews();

        prepareViews();

        fillFieldsFromCurrentUser();


    }

    private void findViews() {

        edtName = (EditText) findViewById(R.id.activity_profile_settings_edtFullName);

        tilName = (TextInputLayout) findViewById(R.id.activity_profile_settings_tilFullName);

        tilEmail = (TextInputLayout) findViewById(R.id.activity_profile_settings_tilEmail);

        edtEmail = (EditText) findViewById(R.id.activity_profile_settings_edtEmail);

        edtPhone = (EditText) findViewById(R.id.activity_profile_settings_edtPhone);

        tilPhone = (TextInputLayout) findViewById(R.id.activity_profile_settings_tilPhone);

        tvSetPlace = (TextView) findViewById(R.id.activity_profile_tv_place);

        scrollView = (NestedScrollView) findViewById(R.id.nestedScroll);




    }

    private void prepareViews() {

        prepareToolbar();

        edtName.addTextChangedListener(new MultiViewTextWatcher(edtName));

        edtEmail.addTextChangedListener(new MultiViewTextWatcher(edtEmail));

        edtPhone.addTextChangedListener(new MultiViewTextWatcher(edtPhone));

        tvSetPlace.setOnClickListener(this);
    }

    private void prepareToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


    }

    private boolean validateFullName() {


        if (edtName.getText().toString().trim().isEmpty()) {

            tilName.setErrorEnabled(true);

            requestFocus(edtName);

            tilName.setError(getResources().getText(R.string.activitySettingsNameValidationError));

            return false;
        }

        tilName.setErrorEnabled(false);

        user.setFullName(edtName.getText().toString().trim());

        return true;

    }

    private boolean validateEmail() {

        String email = edtEmail.getText().toString().trim().toLowerCase();

        tilEmail.setErrorEnabled(true);

        if (email.isEmpty()) {

            tilEmail.setError("Введите email");

            requestFocus(edtEmail);

            return false;
        }

        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {

            tilEmail.setError("Не верный формат (xxx@xxx.xx)");

            requestFocus(edtEmail);

            return false;

        }

        tilEmail.setErrorEnabled(false);

        user.setEmail(email);

        return true;
    }

    private boolean validatePhone() {


        if (edtPhone.getText().toString().trim().isEmpty()) {

            tilPhone.setErrorEnabled(true);

            requestFocus(edtPhone);

            tilPhone.setError(getResources().getText(R.string.activitySettingsPhoneValidationError));

            return false;
        }


        tilName.setErrorEnabled(false);

        user.setPhone(edtPhone.getText().toString().trim().replace(")", " ")
                .replace("(", "").replace(" ", "").replace("*", ""));

        return true;
    }

    public void replaceFragment(Fragment fragment, int containerId) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(containerId, fragment);

        transaction.commit();

    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    private void fillFieldsFromCurrentUser() {

        String name = user.getFullName();

        String email = user.getEmail();

        String phone = user.getPhone();

        if (name != null && !name.isEmpty()) {

            edtName.setText(name);

        }

        if (email != null && !email.isEmpty()) {

            edtEmail.setText(email);

        }

        if (phone != null && !phone.isEmpty()) {

            edtPhone.setText(phone);

        }

        if (user.getPlace() != null) {

            replaceFragment(new ViewPlaceInfoFragment(), R.id.activity_profile_rl_placeContainer);

        }

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

                applyEdit();

                return true;

            case android.R.id.home:


                this.finish();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.activity_profile_tv_place:

                startActivityForResult(new Intent(this, MapsActivity.class), REQUEST_ADD_WORK_PLACE);

                break;


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_WORK_PLACE && resultCode == RESULT_OK) {

            scrollView.scrollTo(0,0);

            fillFieldsFromCurrentUser();

        }
    }

    private void applyEdit() {


        if (validateFullName()
                && validateEmail()
                && validatePhone()
                ) {

            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        setResult(ProfileFragment.RESULT_PROFILE_EDIT_OK);

                        finish();

                    }

                }
            });


        }
    }

    @Override
    public LatLng getLatLng() {
        return null;
    }

    @Override
    public String getCountry() {

        PlaceModel placeModel = user.getPlace();

        if (placeModel != null) {

            return placeModel.getCountryId();
        }

        return null;

    }

    @Override
    public String getCity() {

        PlaceModel placeModel = user.getPlace();

        if (placeModel != null) {

            return placeModel.getCityId();
        }

        return null;
    }

    @Override
    public String getStreet() {

        PlaceModel placeModel = user.getPlace();

        if (placeModel != null) {

            return placeModel.getStreet();
        }

        return null;
    }

    @Override
    public String getLocationTitle() {

        PlaceModel placeModel = user.getPlace();

        if (placeModel != null) {

            return placeModel.getName();
        }

        return null;


    }

    class MultiViewTextWatcher implements android.text.TextWatcher {

        private View view;

        public MultiViewTextWatcher(View view) {

            this.view = view;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {

                case R.id.activity_profile_settings_edtFullName:

                    validateFullName();

                    break;

                case R.id.activity_profile_settings_edtEmail:

                    validateEmail();

                    break;

                case R.id.activity_profile_settings_edtPhone:

                    validatePhone();

                    break;

            }

        }

    }

}
