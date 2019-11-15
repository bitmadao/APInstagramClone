package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    private TextInputEditText edtLoginEmail, edtLoginPassword;

    private Button btnLoginLogin, btnLoginNeedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(R.string.activity_login_title);

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginNeedAccount = findViewById(R.id.btnLoginNeedAccount);

        edtLoginPassword.setOnKeyListener(LoginActivity.this);
        btnLoginLogin.setOnClickListener(LoginActivity.this);
        btnLoginNeedAccount.setOnClickListener(LoginActivity.this);

        if(ParseUser.getCurrentUser() != null){
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
        }


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnLoginLogin:
                btnLoginLoginTapped();
                break;
            case R.id.btnLoginNeedAccount:
                finish();
                break;
        }
    }

    private void btnLoginLoginTapped() {
        boolean objection = false;
        StringBuilder objectionString = new StringBuilder();
        objectionString.append(getString(R.string.toast_login_objection_stringbuilder));

        if(edtLoginEmail.getText().toString().isEmpty()){
            objection = true;
            objectionString.append(getString(R.string.toast_login_need_username)).append("\n");

        }

        if(edtLoginPassword.getText().toString().isEmpty()){
            objection = true;
            objectionString.append(getString(R.string.toast_login_need_password));
        }

        if(objection){
            FancyToast.makeText(
                    LoginActivity.this,
                    objectionString.toString(),
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false)
                .show();
            return;
        }

        ParseUser.logInInBackground(
                edtLoginEmail.getText().toString(),
                edtLoginPassword.getText().toString(),
                new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    FancyToast.makeText(
                            LoginActivity.this,
                            String.format(getString(R.string.toast_login_login_success),user.getUsername()),
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                        .show();

                    Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                    startActivity(intent);

                    edtLoginEmail.setText("");
                    edtLoginPassword.setText("");


                } else {
                    FancyToast.makeText(
                            LoginActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false)
                        .show();
                }

            }
        }); // end of ParseUser.loginInBackground()

    } // end of btnLoginLoginTapped()

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            onClick(btnLoginLogin);
        }

        return false;
    }
} // end of class
