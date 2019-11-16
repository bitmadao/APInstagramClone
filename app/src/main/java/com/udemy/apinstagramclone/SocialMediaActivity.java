package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SocialMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        this.setTitle(R.string.activity_social_media_title);


    }
}
