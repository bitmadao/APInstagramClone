package com.udemy.apinstagramclone;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener {

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
                } // TODO error handling
            }
        });
    }


}
