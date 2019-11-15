package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogOutWelcome;
    private ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView txtWelcome = findViewById(R.id.txtWelcome);
        btnLogOutWelcome = findViewById(R.id.btnLogOutWelcome);
        btnLogOutWelcome.setOnClickListener(WelcomeActivity.this);

         user = ParseUser.getCurrentUser();

        txtWelcome.setText("Welcome " + user.getUsername());


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogOutWelcome:
                btnLogOutWelcomeTapped(user.getUsername());
                break;
        }
    }

    private void btnLogOutWelcomeTapped(String userName) {
        final String name = userName;
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    FancyToast.makeText(
                            WelcomeActivity.this,
                            name + " logged out successfully!",
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                            .show();
                    Intent intent = new Intent(WelcomeActivity.this,SignUpLoginActivity.class);
                    startActivity(intent);
                } else {
                    FancyToast.makeText(
                            WelcomeActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false)
                            .show();
                }
            }
        });
    }
}
