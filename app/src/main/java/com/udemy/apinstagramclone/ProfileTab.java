package com.udemy.apinstagramclone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment implements View.OnClickListener, GestureDetector.OnDoubleTapListener{

    private EditText edtTabProfileProfileName,
            edtTabProfileBio,
            edtTabProfileProfession,
            edtTabProfileHobbies,
            edtTabProfileSport;

    private Button btnTabProfileUpdateInfo;

    private ParseUser parseUser;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileTabView = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtTabProfileProfileName = profileTabView.findViewById(R.id.edtTabProfileProfileName);
        edtTabProfileBio = profileTabView.findViewById(R.id.edtTabProfileBio);
        edtTabProfileProfession = profileTabView.findViewById(R.id.edtTabProfileProfession);
        edtTabProfileHobbies = profileTabView.findViewById(R.id.edtTabProfileHobbies);
        edtTabProfileSport = profileTabView.findViewById(R.id.edtTabProfileSport);

        btnTabProfileUpdateInfo = profileTabView.findViewById(R.id.btnTabProfileUpdateInfo);

        parseUser = ParseUser.getCurrentUser();
        btnTabProfileUpdateInfo.setOnClickListener(ProfileTab.this);

        // TODO setTabProfileForm();


        // must return a view
        return profileTabView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTabProfileUpdateInfo:
                btnTabProfileUpdateInfoTapped();
        }


    }

    private void btnTabProfileUpdateInfoTapped(){
        parseUser.put("profileName", edtTabProfileProfileName.getText().toString());
        parseUser.put("profileBio", edtTabProfileBio.getText().toString());
        parseUser.put("profileProfession", edtTabProfileProfession.getText().toString());
        parseUser.put("profileHobbies", edtTabProfileHobbies.getText().toString());
        parseUser.put("profileSport", edtTabProfileSport.getText().toString());

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(getContext(),
                            "Profile update successful!", // TODO Profile-Update successful in strings
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                        .show();
                    // TODO setTabProfileForm();

                } else {
                    Log.i("AppTag",e.getMessage());
                    FancyToast.makeText(getContext(),
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false)
                        .show();
                }
            }
        });
    }

    private void setTabProfileForm() {
        if(parseUser.get("profileName") != null && !parseUser.get("profileName").toString().isEmpty()) {
            edtTabProfileProfileName.setText(parseUser.get("profileName").toString());
            edtTabProfileProfileName.setEnabled(false);
        }
/*
        edtTabProfileProfileName
        edtTabProfileBio
        edtTabProfileProfession
        edtTabProfileHobbies
        edtTabProfileSport
*/
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
