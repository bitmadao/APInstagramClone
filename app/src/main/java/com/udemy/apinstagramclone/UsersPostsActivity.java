package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UsersPostsActivity extends AppCompatActivity {

    private String postsUsername;

    private TextView txtUsersPostsActivityTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        Intent recievedIntentObject = getIntent();

        postsUsername = recievedIntentObject.getStringExtra("username");

        txtUsersPostsActivityTop = findViewById(R.id.txtUsersPostsActivityTop);

        txtUsersPostsActivityTop.setText(postsUsername);
        txtUsersPostsActivityTop.setTextSize(30f);

    }
}
