package com.fedor.pavel.tattoocommunity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUserName, edtPassword;

    private TextInputLayout tilUserName, tilPassword;

    private Button btnLogin, btnSingUp, btnLoginWith;


    private ProgressDialog progressDialog;

    private static final String LOG_TAG = "LogInFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acctivity_dialog_login);


        createProgressDialog();

        prepareView();
    }


    private void prepareView() {

        /*Find views*/

        edtUserName = (EditText) findViewById(R.id.login_fragment_edt_userName);

        edtPassword = (EditText) findViewById(R.id.login_fragment_edt_password);

        btnLogin = (Button) findViewById(R.id.login_fragment_btn_login);

        btnSingUp = (Button) findViewById(R.id.login_fragment_btn_sign_up);

        tilUserName = (TextInputLayout) findViewById(R.id.login_fragment_til_userName);

        tilPassword = (TextInputLayout) findViewById(R.id.login_fragment_til_password);

       // btnLoginWith = (Button) findViewById(R.id.login_fragment_btn_loginWith);

        /*Set views behavior*/

        btnSingUp.setOnClickListener(this);

        btnLogin.setOnClickListener(this);

//        btnLoginWith.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_fragment_btn_login:

                login(edtUserName.getText().toString().trim().toLowerCase(), edtPassword.getText().toString().trim());

                break;

            case R.id.login_fragment_btn_sign_up:

                //startActivity(new Intent(this, SingUpActivity.class));

                break;

         /*   case R.id.login_fragment_btn_loginWith:

                DialogManager.showLoginWithDialog(getActivity());

                break;*/

        }
    }

    public void login(String userName, String password){

        progressDialog.show();

        ParseUser.logInInBackground(userName, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null) {

                    setResult(NavigationActivity.LOGIN_RESULT_OK);

                    finish();

                } else {

                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                        Snackbar.make(btnLogin, "Wrong password or user name", Snackbar.LENGTH_LONG).show();

                    }

                    Log.d(LOG_TAG, "" + e + ", " + e.getCode());

                }

                progressDialog.dismiss();

            }
        });

    }

    private void createProgressDialog() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);

        progressDialog.setMessage("Authorization in progress ");

    }


}
