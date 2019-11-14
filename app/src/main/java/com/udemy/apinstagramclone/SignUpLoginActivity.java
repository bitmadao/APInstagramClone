package com.udemy.apinstagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    // For User Sign Up form
    private LinearLayout signUpUserLayout;
    private TextInputEditText edtSignUpUserName, edtSignUpPassword, edtSignUpPasswordConfirm;
    private Button btnSignUp;

    // For User Sign In form
    private LinearLayout signInUserLayout;
    private TextInputEditText edtSignInUserName, edtSignInPassword;
    private Button btnSignIn;

    private TextView txtAlreadySignedUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_up_login);

        txtAlreadySignedUp = findViewById(R.id.txtAlreadySignedUp);
        txtAlreadySignedUp.setOnClickListener(SignUpLoginActivity.this);

        // For User Sign Up form
        signUpUserLayout = findViewById(R.id.signUpUserLayout);
        edtSignUpUserName = findViewById(R.id.tiEdtSignUpUserName);
        edtSignUpPassword = findViewById(R.id.tiEdtSignUpPassword);
        edtSignUpPasswordConfirm = findViewById(R.id.tiEdtSignUpPasswordConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(SignUpLoginActivity.this);

        // For User Sign In form
        signInUserLayout = findViewById(R.id.signInUserLayout);
        signInUserLayout.setVisibility(View.INVISIBLE);
        edtSignInUserName = findViewById(R.id.tiEdtSignInUserName);
        edtSignInPassword = findViewById(R.id.tiEdtSignInPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(SignUpLoginActivity.this);



    }

    private void txtAlreadySignedUpTapped() {
        if(signInUserLayout.getVisibility() == View.VISIBLE) {
            signUpUserLayout.setVisibility(View.VISIBLE);
            signInUserLayout.setVisibility(View.INVISIBLE);
            txtAlreadySignedUp.setText(R.string.txt_already_signed_up_true);

        } else {
            signUpUserLayout.setVisibility(View.INVISIBLE);
            signInUserLayout.setVisibility(View.VISIBLE);
            txtAlreadySignedUp.setText(R.string.txt_already_signed_up_false);
        }
    }

    private void btnSignUpTapped(){
        if(edtSignUpUserName.getText().toString().isEmpty()){
           edtSignUpUserName.setHint(R.string.hint_input_choose_user_name_insist);
           return;
        }

        if(!edtSignUpPassword.getText().toString().equals(edtSignUpPasswordConfirm.getText().toString())){
            edtSignUpPasswordConfirm.setHint(R.string.hint_input_confirm_password_insist);
            return;
        }

        final ParseUser appUser = new ParseUser();
        appUser.setUsername(edtSignUpUserName.getText().toString());
        appUser.setPassword(edtSignUpPassword.getText().toString());

        appUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    FancyToast.makeText(
                            SignUpLoginActivity.this,
                            "Successfully created user " +
                                    appUser.getUsername(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                        .show();

                } else {
                    FancyToast.makeText(
                            SignUpLoginActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            true)
                        .show();
                }
            }
        });

    }

    private void btnSignInTapped(){

        ParseUser.logInInBackground(
                edtSignInUserName.getText().toString(),
                edtSignInPassword.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null && e == null){
                            FancyToast.makeText(
                                    SignUpLoginActivity.this,
                                    "Sign In successful! :)",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false)
                                .show();
                        } else if (e != null) {
                            FancyToast.makeText(
                                    SignUpLoginActivity.this,
                                    e.getMessage() + " :(",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    false)
                                .show();

                        } else {
                            FancyToast.makeText(SignUpLoginActivity.this,
                                    "No user with this username/password combination... o_O",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.CONFUSING,
                                    false)
                                .show();
                        }

                    }
                });

    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.txtAlreadySignedUp:
                txtAlreadySignedUpTapped();

            case R.id.btnSignUp:
                btnSignUpTapped();
                break;

            case R.id.btnSignIn:
                btnSignInTapped();
                break;
        }
    }
}
