package com.udemy.apinstagramclone;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SportsActivity extends AppCompatActivity {

    private StringBuilder allFoundAthletesStringBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
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

 */
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
                            SportsActivity.this,
                            boxer.get("name") +" object saved successfully! (Boxer)",
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false
                    ).show();
                } else {
                    FancyToast.makeText(
                            SportsActivity.this,
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
        /*
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

         */
    }

    public void btnGetAllTapped(){
        /*
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


         */

    }

    public void btnNextActivityTapped(){
        /*



         */
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
                            SportsActivity.this,
                            kickBoxer.get("name") +" object saved successfully! (KickBoxer)",
                            FancyToast.LENGTH_LONG,FancyToast.SUCCESS,
                            false
                    ).show();
                } else {
                    FancyToast.makeText(
                            SportsActivity.this,
                            e.toString(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                    ).show();
                }
            }
        });

    }
}
