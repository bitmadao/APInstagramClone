package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtSignUpEmail, edtSignUpUsername, edtSignUpPassword, edtSignUpPasswordConfirm;

    private Button btnSignUpSignUp, btnSignUpAlreadySignedUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle(R.string.activity_sign_up_title);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtSignUpPasswordConfirm = findViewById(R.id.edtSignUpPasswordConfirm);

        btnSignUpSignUp = findViewById(R.id.btnSignUpSignUp);
        btnSignUpAlreadySignedUp = findViewById(R.id.btnSignUpAlreadySignedUp);

        btnSignUpSignUp.setOnClickListener(SignUpActivity.this);
        btnSignUpAlreadySignedUp.setOnClickListener(SignUpActivity.this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSignUpSignUp:
                btnSignUpSignUpTapped();
                break;

            case R.id.btnSignUpAlreadySignedUp:
                btnSignUpAlreadySignedUp();
                break;
        }
    }

    private void btnSignUpSignUpTapped() {
        boolean objection = false;
        StringBuilder objectionString = new StringBuilder();
        objectionString.append(getString(R.string.toast_sign_up_objection_stringbuilder));
        if(edtSignUpEmail.getText().toString().isEmpty()) {
            objection = true;
            objectionString.append(getString(R.string.toast_sign_up_need_email)).append("\n");

        }

        if(edtSignUpUsername.getText().toString().isEmpty()){
            objection = true;
            objectionString.append(getString(R.string.toast_sign_up_need_username)).append("\n");
        }

        if(edtSignUpPassword.getText().toString().isEmpty()
                || !edtSignUpPassword.getText().toString().equals(edtSignUpPasswordConfirm.getText().toString())
                    ){
            objection = true;
            objectionString.append(getString(R.string.toast_sign_up_need_password));
        }

        if(objection) {
            FancyToast.makeText(
                    SignUpActivity.this,
                    objectionString.toString(),
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false)
                .show();
            return;
        }

        final ParseUser appUser = new ParseUser();
        appUser.setEmail(edtSignUpEmail.getText().toString());
        appUser.setUsername(edtSignUpUsername.getText().toString());
        appUser.setPassword(edtSignUpPassword.getText().toString());

        appUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    FancyToast.makeText(
                            SignUpActivity.this,
                            String.format(getString(R.string.toast_sign_up_success), appUser.getUsername()),
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                        .show();

                } else {
                    FancyToast.makeText(
                            SignUpActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            true)
                        .show();
                }
            }
        });
    }

    private void btnSignUpAlreadySignedUp() {
        //do something
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }





} // class ends here
