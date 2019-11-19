package com.udemy.apinstagramclone;


import android.content.Intent;
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

import com.parse.LogOutCallback;
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

    private Button btnTabProfileUpdateInfo, btnTabProfileLogout;

    private ParseUser parseUser;

    //state changes in btnTabProfileUpdateInfoTapped() and setTabProfileForm()
    private boolean btnTabProfileUpdateInfoShouldUpdate = true;

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
        btnTabProfileLogout = profileTabView.findViewById(R.id.btnTabProfileLogout);

        parseUser = ParseUser.getCurrentUser();
        btnTabProfileUpdateInfo.setOnClickListener(ProfileTab.this);
        btnTabProfileLogout.setOnClickListener(ProfileTab.this);
        btnTabProfileLogout.setText(
                String.format(getString(R.string.btn_tab_profile_logout),
                parseUser.getUsername())
            );


        setTabProfileForm();

        try {
            if (parseUser.get("profileName").toString().isEmpty()) {
                Log.i("AppTag", "isEmpty Block");
            } else {
                Log.i("AppTag", "!isEmpty Block");
            }


        } catch(Exception e) {
            Log.i("AppTag", e.getMessage());
        }

        try{
            if(edtTabProfileProfileName.getText().toString().isEmpty()) {
                Log.i("AppTag", "EditText is empty.");
            } else {
                Log.i("AppTag","EditText is not empty.");
            }
        } catch (Exception e) {
            Log.i("AppTag", e.getMessage());
        }


        // must return a view
        return profileTabView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTabProfileUpdateInfo:
                btnTabProfileUpdateInfoTapped();
                break;

            case R.id.btnTabProfileLogout:
                btnTabProfileLogoutTapped();
                break;
        }


    }

    private void btnTabProfileUpdateInfoTapped() {

        if(btnTabProfileUpdateInfoShouldUpdate) {
            boolean somethingToUpdate = false;

            if (!edtTabProfileProfileName.getText().toString().isEmpty() &&
                    !edtTabProfileProfileName.getText().toString().equals((parseUser.get("profileName") + ""))
            ) {

                parseUser.put("profileName", edtTabProfileProfileName.getText().toString());
                somethingToUpdate = true;

            } else if(parseUser.get("profileName") != null && parseUser.get("profileName").toString().length() > 0 &&
                    edtTabProfileProfileName.getText().toString().isEmpty()) {
                parseUser.put("profileName", "");
                somethingToUpdate = true;
            }

            if (!edtTabProfileBio.getText().toString().isEmpty() &&
                    !edtTabProfileBio.getText().toString().equals(parseUser.get("profileBio") + "")) {
                parseUser.put("profileBio", edtTabProfileBio.getText().toString());
                somethingToUpdate = true;
            } else if(parseUser.get("profileName") != null && parseUser.get("profileName").toString().length() > 0 &&
                    edtTabProfileBio.getText().toString().isEmpty()) {
                parseUser.put("profileName", "");
                somethingToUpdate = true;
            }

            if (!edtTabProfileProfession.getText().toString().isEmpty() &&
                    !edtTabProfileProfession.getText().toString().equals(parseUser.get("profileProfession") + "")) {
                parseUser.put("profileProfession", edtTabProfileProfession.getText().toString());
            } else if(parseUser.get("profileProfession") != null && parseUser.get("profileProfession").toString().length() > 0 &&
                    edtTabProfileProfession.getText().toString().isEmpty()) {
                parseUser.put("profileProfession", "");
                somethingToUpdate = true;
            }

            if (!edtTabProfileHobbies.getText().toString().isEmpty() &&
                    !edtTabProfileHobbies.getText().toString().equals(parseUser.get("profileHobbies") + "")) {
                parseUser.put("profileHobbies", edtTabProfileHobbies.getText().toString());
                somethingToUpdate = true;
            } else if(parseUser.get("profileHobbies") != null && parseUser.get("profileHobbies").toString().length() > 0 &&
                    edtTabProfileHobbies.getText().toString().isEmpty()) {
                parseUser.put("profileHobbies", "");
                somethingToUpdate = true;
            }

            if (!edtTabProfileSport.getText().toString().isEmpty() &&
                    !edtTabProfileSport.getText().toString().equals(parseUser.get("profileSport") + "")) {
                parseUser.put("profileSport", edtTabProfileSport.getText().toString());
                somethingToUpdate = true;
            } else if(parseUser.get("profileSport") != null && parseUser.get("profileSport").toString().length() > 0 &&
                    edtTabProfileSport.getText().toString().isEmpty()) {
                parseUser.put("profileSport", "");
                somethingToUpdate = true;
            }

            if(somethingToUpdate) {
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(getContext(),
                                    getString(R.string.toast_tab_profile_profile_update_success),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false)
                                    .show();
                            setTabProfileForm();

                        } else {
                            Log.i("AppTag", e.getMessage());
                            FancyToast.makeText(getContext(),
                                    e.getMessage(), // TODO make user friendly error
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    false)
                                    .show();
                        }
                    }
                });
            } else {
                FancyToast.makeText(getContext(),
                        "No changes made", // TODO add to strings.xml
                        FancyToast.LENGTH_LONG,
                        FancyToast.INFO,
                        false)
                    .show();
                setTabProfileForm();
            }
        } else {
            btnTabProfileUpdateInfoShouldUpdate = true;

            if(parseUser.get("profileName") != null) {
                edtTabProfileProfileName.setText(parseUser.get("profileName").toString());
            } else {
                edtTabProfileProfileName.setText("");
            }
            
            if(parseUser.get("profileBio") != null) {
                edtTabProfileBio.setText(parseUser.get("profileBio").toString());
            } else {
                edtTabProfileBio.setText("");
            }
            
            if(parseUser.get("profileProfession") != null) {
                edtTabProfileProfession.setText(parseUser.get("profileProfession").toString());
            } else {
                edtTabProfileProfession.setText("");
            }
            
            if(parseUser.get("profileHobbies") != null) {
                edtTabProfileHobbies.setText(parseUser.get("profileHobbies").toString());
            } else {
                edtTabProfileHobbies.setText("");
            }
            
            if(parseUser.get("profileSport") != null) {
                edtTabProfileSport.setText(parseUser.get("profileSport").toString());
            } else {
                edtTabProfileSport.setText("");
            }
            edtTabProfileProfileName.setEnabled(true);

            edtTabProfileBio.setEnabled(true);
            edtTabProfileProfession.setEnabled(true);
            edtTabProfileHobbies.setEnabled(true);
            edtTabProfileSport.setEnabled(true);
            btnTabProfileUpdateInfo.setText(R.string.btn_tab_profile_update_info);


        }
    }

    private void btnTabProfileLogoutTapped() {
        ((SocialMediaActivity)getActivity()).logoutUserItemMethod();
    }
    private void setTabProfileForm() {
        btnTabProfileUpdateInfo.setText(R.string.btn_tab_profile_update_info_alternate);
        btnTabProfileUpdateInfoShouldUpdate = false;
        edtTabProfileProfileName.setEnabled(false);
        edtTabProfileBio.setEnabled(false);
        edtTabProfileProfession.setEnabled(false);
        edtTabProfileHobbies.setEnabled(false);
        edtTabProfileSport.setEnabled(false);

        if(parseUser.get("profileName") != null && parseUser.get("profileName").toString().length() > 0) {
            edtTabProfileProfileName.setText(
                    String.format(getString(
                            R.string.tab_profile_profile_name_txt_template),
                            parseUser.get("profileName").toString()
                    )
                );
        }

        if(parseUser.get("profileBio") != null && parseUser.get("profileBio").toString().length() > 0) {
            edtTabProfileBio.setText(
                    String.format(getString(
                            R.string.tab_profile_bio_txt_template),
                            parseUser.get("profileBio").toString()
                    )
            );

        }

        if(parseUser.get("profileProfession") != null && parseUser.get("profileBio").toString().length() > 0) {
            edtTabProfileProfession.setText(
                    String.format(getString(
                            R.string.tab_profile_profession_txt_template),
                            parseUser.get("profileProfession").toString()
                    )
            );

        }

        if(parseUser.get("profileHobbies") != null && parseUser.get("profileHobbies").toString().length() > 0) {
            edtTabProfileHobbies.setText(
                    String.format(getString(
                            R.string.tab_profile_hobbies_txt_template),
                            parseUser.get("profileHobbies").toString()
                    )
            );

        }

        if(parseUser.get("profileSport") != null && parseUser.get("profileSport").toString().length() > 0) {
            edtTabProfileSport.setText(
                    String.format(getString(
                            R.string.tab_profile_sport_name_txt_template),
                            parseUser.get("profileSport").toString()
                    )
            );
        }
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
