package com.udemy.apinstagramclone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listViewTabUsers;
    private float listViewTabUsersAlpha;
    private ArrayList<String> arrayListUsernamesTabUsers;
    private ArrayAdapter arrayAdapter;
    private TextView txtTabUsersLoadingUsers;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View usersTabView = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listViewTabUsers = usersTabView.findViewById(R.id.listViewTabUsers);
        listViewTabUsersAlpha = listViewTabUsers.getAlpha();
        listViewTabUsers.setAlpha(0);
        arrayListUsernamesTabUsers = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, arrayListUsernamesTabUsers);

        listViewTabUsers.setOnItemClickListener(UsersTab.this);
        listViewTabUsers.setOnItemLongClickListener(UsersTab.this);

        txtTabUsersLoadingUsers = usersTabView.findViewById(R.id.txtTabUsersLoadingUsers);

        populateUsersListView();

        return usersTabView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(),UsersPostsActivity.class);
        intent.putExtra("username",(arrayListUsernamesTabUsers.get(position)));
        startActivity(intent);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayListUsernamesTabUsers.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(object != null && e == null) {

                    StringBuilder userMessageStringBuilder = new StringBuilder();
                    userMessageStringBuilder
                            .append(object.get("profileBio")).append("\n")
                            .append(object.get("profileProfession")).append("\n")
                            .append(object.get("profileHobbies")).append("\n")
                            .append(object.get("profileSport"));
                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                    prettyDialog
                            .setTitle(String.format(getString(R.string.dialog_tab_users_long_click),object.getUsername())) 
                            .setMessage(userMessageStringBuilder.toString())
                            .setIcon(R.drawable.person)
                            .addButton(
                                    getString(R.string.generic_dialog_ok),
                                    R.color.pdlg_color_white,
                                    R.color.pdlg_color_green,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    })
                                .show();

                }
            }
        });

        return true;
    }

    private void populateUsersListView() {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();


        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0) {
                        for(ParseUser user: objects){
                            arrayListUsernamesTabUsers.add(user.getUsername());
                        }
                        listViewTabUsers.setAdapter(arrayAdapter);
                        txtTabUsersLoadingUsers.animate().alpha(0).setDuration(2000).start();
                        listViewTabUsers.animate().alpha(listViewTabUsersAlpha).setDuration(3000);
                    }
                } else {
                    Log.i("AppTag",e.getMessage());
                    FancyToast.makeText(getContext(),
                                getString(R.string.toast_generic_error),
                                FancyToast.LENGTH_LONG,
                                FancyToast.ERROR,
                                false)
                            .show();
                }
            }
        });
    }

} // end of UsersTab fragment