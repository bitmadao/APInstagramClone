package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button btnBoxer;
    private Button btnKickBoxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        btnBoxer = findViewById(R.id.btnBoxer);
        btnKickBoxer = findViewById(R.id.btnKickBoxer);

        btnBoxer.setOnClickListener(SignUp.this);
        btnKickBoxer.setOnClickListener(SignUp.this);


    }

    public void btnBoxerTapped(){
        ParseObject boxer = new ParseObject("Boxer");

        boxer.put("punch_speed", 200);
        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){
                    Toast.makeText(SignUp.this, "boxer object saved successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }    public void btnKickBoxerTapped(){
        final ParseObject kickBoxer = new ParseObject("KickBoxer");

        kickBoxer.put("name","Tim");
        kickBoxer.put("punch_speed", 200);
        kickBoxer.put("punch_power", 30);
        kickBoxer.put("kick_speed",300);
        kickBoxer.put("kick_power",60);
        kickBoxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null){
                    Toast.makeText(SignUp.this, kickBoxer.get("name")+ " object saved successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void helloWorldTapped(View view){
        btnBoxerTapped();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnBoxer:
                btnBoxerTapped();
                break;
            case R.id.btnKickBoxer:
                btnKickBoxerTapped();
                break;
        }
    }
}
