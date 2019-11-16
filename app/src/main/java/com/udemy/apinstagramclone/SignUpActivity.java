package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    private ConstraintLayout rootLayoutSignUpActivity;
    private TextInputEditText edtSignUpEmail, edtSignUpUsername, edtSignUpPassword, edtSignUpPasswordConfirm;

    private Button btnSignUpSignUp, btnSignUpAlreadySignedUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle(R.string.activity_sign_up_title);
        rootLayoutSignUpActivity = findViewById(R.id.rootLayoutSignUpActivity);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtSignUpPasswordConfirm = findViewById(R.id.edtSignUpPasswordConfirm);

        btnSignUpSignUp = findViewById(R.id.btnSignUpSignUp);
        btnSignUpAlreadySignedUp = findViewById(R.id.btnSignUpAlreadySignedUp);

        edtSignUpPasswordConfirm.setOnKeyListener(SignUpActivity.this);

        rootLayoutSignUpActivity.setOnClickListener(SignUpActivity.this);
        btnSignUpSignUp.setOnClickListener(SignUpActivity.this);
        btnSignUpAlreadySignedUp.setOnClickListener(SignUpActivity.this);

        if(ParseUser.getCurrentUser() != null){
            /*
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Log.i("logoutTag","A user was logged out");
                    } else {
                        Log.i("logoutTag", e.getMessage());
                    }
                }
            });
             */
            transitionToSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rootLayoutSignUpActivity:
                rootSignUpActivityLayoutTapped();
                break;

            case R.id.btnSignUpSignUp:
                btnSignUpSignUpTapped();
                break;

            case R.id.btnSignUpAlreadySignedUp:
                btnSignUpAlreadySignedUp();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(v.getId() == R.id.edtSignUpPasswordConfirm) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                onClick(btnSignUpSignUp);

            }
        }
        return false;
    }

    private void rootSignUpActivityLayoutTapped() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch(Exception e) {
            Log.i("ErrorTag", e.getMessage());
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

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage(
                String.format(
                        getString(R.string.progress_sign_up_sign_up),
                        edtSignUpUsername.getText().toString())
            );
        progressDialog.show();

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
                    progressDialog.dismiss();
                    transitionToSocialMediaActivity();

                } else {
                    FancyToast.makeText(
                            SignUpActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            true)
                        .show();
                    progressDialog.dismiss();
                }

            }
        });


    }

    private void btnSignUpAlreadySignedUp() {
        //do something
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void transitionToSocialMediaActivity(){

        Intent intent = new Intent(SignUpActivity.this,SocialMediaActivity.class);
        startActivity(intent);
    }

} // class ends here
