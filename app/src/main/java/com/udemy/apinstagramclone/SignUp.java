package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SignUp extends AppCompatActivity
        implements
        View.OnClickListener,
        Switch.OnCheckedChangeListener{

    private Button btnRegister;
    private Switch swClass;

    private TextInputEditText txtInpName, txtInpPunchSpeed, txtInpPunchPower, txtInpKickSpeed, txtInpKickPower;
    private LinearLayout lLaySignUpFormKickBoxerSpecific;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        txtInpName = findViewById(R.id.txtInpName);
        txtInpPunchSpeed = findViewById(R.id.txtInpPunchSpeed);
        txtInpPunchPower = findViewById(R.id.txtInpPunchPower);
        txtInpKickSpeed = findViewById(R.id.txtInpKickSpeed);
        txtInpKickPower = findViewById(R.id.txtInpKickPower);

        btnRegister = findViewById(R.id.btnRegister);
        swClass = findViewById(R.id.swClass);

        lLaySignUpFormKickBoxerSpecific = findViewById(R.id.lLaySignUpFormKickBoxerSpecific);

        lLaySignUpFormKickBoxerSpecific.setVisibility(View.GONE);

        btnRegister.setOnClickListener(SignUp.this);
        swClass.setOnCheckedChangeListener(SignUp.this);

    }

    public void registerBoxer(String name, int punchSpeed, int punchPower){
        final ParseObject boxer = new ParseObject("Boxer");

        boxer.put("name",name);
        boxer.put("punch_speed",punchSpeed);
        boxer.put("punch_power",punchPower);

        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(SignUp.this, boxer.get("name") +" object saved successfully! (Boxer)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registerKickBoxer(String name, int punchSpeed, int punchPower, int kickSpeed, int kickPower){
        final ParseObject kickBoxer = new ParseObject("KickBoxer");

        kickBoxer.put("name", name);
        kickBoxer.put("punch_speed", punchSpeed);
        kickBoxer.put("punch_power", punchPower);
        kickBoxer.put("kick_speed",kickSpeed);
        kickBoxer.put("kick_power",kickPower);

        kickBoxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(SignUp.this, kickBoxer.get("name") + " object saved successfully! (KickBoxer)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void btnRegisterTapped(){
        if(txtInpName.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "We need at least a name to register..", Toast.LENGTH_LONG).show();
        } else {
            String name = txtInpName.getText().toString();
            int punchSpeed = 2000;
            int punchPower = 200;

            if(!txtInpPunchSpeed.getText().toString().isEmpty()){
                punchSpeed = Integer.parseInt(txtInpPunchSpeed.getText().toString());
            }

            if (!txtInpPunchPower.getText().toString().isEmpty()){
                punchPower = Integer.parseInt(txtInpPunchPower.getText().toString());
            }



            if(!swClass.isChecked()){

                registerBoxer(name,punchSpeed,punchPower);

            } else {
                int kickSpeed = 3000;
                int kickPower = 300;

                if(!txtInpKickSpeed.getText().toString().isEmpty()){
                    kickSpeed = Integer.parseInt(txtInpKickSpeed.getText().toString());
                }

                if(!txtInpKickPower.getText().toString().isEmpty()){
                    kickPower = Integer.parseInt(txtInpKickPower.getText().toString());
                }

                registerKickBoxer(name,punchSpeed,punchPower,kickSpeed,kickPower);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegister:
                btnRegisterTapped();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            buttonView.setText(R.string.switch_class_name_kickboxer);
            lLaySignUpFormKickBoxerSpecific.setVisibility(View.VISIBLE);
            btnRegister.setText(R.string.btn_register_kickboxer);
        } else {
            buttonView.setText(R.string.switch_class_name_boxer);
            lLaySignUpFormKickBoxerSpecific.setVisibility(View.GONE);
            btnRegister.setText(R.string.btn_register_boxer);
        }
    }
}
