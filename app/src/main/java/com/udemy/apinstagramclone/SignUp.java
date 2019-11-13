package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener{

    private Button btnBoxer, btnKickBoxer;
    private Switch swClass;

    private TextInputEditText txtInpName, txtInpPunchSpeed, txtInpPunchPower, txtInpKickSpeed, txtInpKickPower;
    private TextInputLayout txtInpLayKickSpeed, txtInpLayKickPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        btnBoxer = findViewById(R.id.btnBoxer);
        btnKickBoxer = findViewById(R.id.btnKickBoxer);
        swClass = findViewById(R.id.swClass);


        txtInpLayKickSpeed = findViewById(R.id.txtInpLayKickSpeed);
        txtInpLayKickPower = findViewById(R.id.txtInpLayKickPower);

        txtInpLayKickSpeed.setVisibility(View.GONE);
        txtInpLayKickPower.setVisibility(View.GONE);

        btnBoxer.setOnClickListener(SignUp.this);
        btnKickBoxer.setOnClickListener(SignUp.this);
        swClass.setOnCheckedChangeListener(SignUp.this);

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            buttonView.setText(R.string.switch_class_name_kickboxer);
            txtInpLayKickSpeed.setVisibility(View.VISIBLE);
            txtInpLayKickPower.setVisibility(View.VISIBLE);
        } else {
            buttonView.setText(R.string.switch_class_name_boxer);
            txtInpLayKickSpeed.setVisibility(View.GONE);
            txtInpLayKickPower.setVisibility(View.GONE);
        }
    }
}
