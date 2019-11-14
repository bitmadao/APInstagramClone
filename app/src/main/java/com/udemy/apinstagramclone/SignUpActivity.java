package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUpActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        Switch.OnCheckedChangeListener{

    private Button btnRegister, btnGetAll, btnNextActivity;

    private Switch swClass;
    private TextView txtGetData;

    private TextInputEditText txtInpName, txtInpPunchSpeed, txtInpPunchPower, txtInpKickSpeed, txtInpKickPower;
    private LinearLayout lLaySignUpFormKickBoxerSpecific;

    private StringBuilder allFoundAthletesStringBuilder;

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
        btnGetAll = findViewById(R.id.btnGetAll);
        btnNextActivity = findViewById(R.id.btnNextActivity);
        swClass = findViewById(R.id.swClass);
        txtGetData = findViewById(R.id.txtGetData);

        lLaySignUpFormKickBoxerSpecific = findViewById(R.id.lLaySignUpFormKickBoxerSpecific);

        lLaySignUpFormKickBoxerSpecific.setVisibility(View.GONE);

        btnRegister.setOnClickListener(SignUpActivity.this);
        btnGetAll.setOnClickListener(SignUpActivity.this);
        btnNextActivity.setOnClickListener(SignUpActivity.this);
        swClass.setOnCheckedChangeListener(SignUpActivity.this);

        ParseQuery<ParseObject> myQuery = ParseQuery.getQuery("KickBoxer");
        myQuery.whereGreaterThanOrEqualTo("punch_power",200).setLimit(2);


        myQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    String myString = "";
                    for (ParseObject object: objects){
                        myString += object.get("name") + "\n";
                    }
                    txtGetData.setTextSize(14f);
                    txtGetData.setText(myString);
                }
            }
        });

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
                    FancyToast.makeText(
                            SignUpActivity.this,
                            boxer.get("name") +" object saved successfully! (Boxer)",
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false
                    ).show();
                } else {
                    FancyToast.makeText(
                            SignUpActivity.this,
                            e.toString(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                    ).show();
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
                    FancyToast.makeText(
                            SignUpActivity.this,
                            kickBoxer.get("name") +" object saved successfully! (KickBoxer)",
                            FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                            false
                    ).show();
                } else {
                    FancyToast.makeText(
                            SignUpActivity.this,
                            e.toString(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                    ).show();
                }
            }
        });

    }


    public void btnRegisterTapped(){
        if(txtInpName.getText().toString().isEmpty()){
            FancyToast.makeText(SignUpActivity.this, "We need at least a name to register..", FancyToast.LENGTH_LONG,FancyToast.INFO,false ).show();
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

    public void btnGetAllTapped(){
        ParseQuery<ParseObject> queryAll;
        final String athleteAttribute;
        final String athleteAttributeKey;

        allFoundAthletesStringBuilder = new StringBuilder();
        allFoundAthletesStringBuilder.append("Found ");

        if(!swClass.isChecked()){
            //Boxer block
            queryAll = ParseQuery.getQuery("Boxer");
            allFoundAthletesStringBuilder.append("Boxers");
            athleteAttribute = "punch power ";
            athleteAttributeKey = "punch_power";

        } else {
            //KickBoxer block

            queryAll = ParseQuery.getQuery("KickBoxer");
            allFoundAthletesStringBuilder.append("KickBoxers");
            athleteAttribute = "kick power ";
            athleteAttributeKey = "kick_power";
        }

        allFoundAthletesStringBuilder.append(":\n");

        queryAll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    int i = 0;
                    if (objects.size() > 0) {
                        
                        for (ParseObject foundAthlete: objects) {

                            allFoundAthletesStringBuilder
                                    .append(foundAthlete.get("name"))
                                    .append(" with ")
                                    .append(athleteAttribute)
                                    .append("of ")
                                    .append(foundAthlete.get(athleteAttributeKey));

                            if(i < (objects.size() -1 )){
                                allFoundAthletesStringBuilder.append("\n");
                            }

                            i ++;
                        }

                        txtGetData.setTextSize(14f);

                        txtGetData.setText(allFoundAthletesStringBuilder.toString());

                        FancyToast.makeText(
                                SignUpActivity.this,
                                "Found " + objects.size() + " elements.",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                false
                        ).show();


                    } else{
                        FancyToast.makeText(
                            SignUpActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                        ).show();
                    }

                }
            }
        });


    }

    public void btnNextActivityTapped(){

        Intent intent = new Intent(SignUpActivity.this, SignUpLoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegister:
                btnRegisterTapped();
                break;

            case R.id.btnGetAll:
                btnGetAllTapped();
                break;

            case R.id.btnNextActivity:
                btnNextActivityTapped();

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            buttonView.setText(R.string.switch_class_name_kickboxer);
            lLaySignUpFormKickBoxerSpecific.setVisibility(View.VISIBLE);
            btnRegister.setText(R.string.btn_register_kickboxer);
            btnGetAll.setText(R.string.btn_get_all_kickboxer);

        } else {
            buttonView.setText(R.string.switch_class_name_boxer);
            lLaySignUpFormKickBoxerSpecific.setVisibility(View.GONE);
            btnRegister.setText(R.string.btn_register_boxer);
            btnGetAll.setText(R.string.btn_get_all_boxer);
        }
    }
}
