package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPostsActivity extends AppCompatActivity {

    private String postsUsername;

    private ConstraintLayout constraintLayout;
    private LinearLayout activityUsersPostsLinearLayout;

    private ProgressDialog dialog;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);
        Intent receivedIntentObject = getIntent();
        postsUsername = receivedIntentObject.getStringExtra("username");
        setTitle(String.format(getString(R.string.activity_users_posts_title),postsUsername));

        constraintLayout = findViewById(R.id.activityUsersPostsConstraintLayout);
        activityUsersPostsLinearLayout = findViewById(R.id.activityUsersPostsLinearLayout);

        if(Build.VERSION.SDK_INT > 21){
            progressBar = new ProgressBar(UsersPostsActivity.this, null, android.R.attr.progressBarStyleLarge);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100,100);
            constraintLayout.addView(progressBar,params);
            progressBar.setVisibility(View.GONE);
        }

        populateUsersPosts();

    }

    private void populateUsersPosts() {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username",postsUsername);
        parseQuery.orderByDescending("createdAt");


        if (android.os.Build.VERSION.SDK_INT <= 21) {
            dialog = new ProgressDialog(this); 
            dialog.setMessage(getString(R.string.generic_dialog_loading));
            dialog.show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {

                    for (ParseObject post : objects) {
                        String descriptionString;

                        if (post.get("image_des") != null) {
                            descriptionString = post.get("image_des").toString();
                        } else {
                            descriptionString = getString(R.string.users_posts_no_description_provided);
                        }

                        final TextView postDescription = new TextView(UsersPostsActivity.this);
                        postDescription.setText(descriptionString);
                        ParseFile postPicture = (ParseFile) post.get("picture");

                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UsersPostsActivity.this);
                                    LinearLayout.LayoutParams imageViewParams =
                                            new LinearLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                            );

                                    imageViewParams.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(imageViewParams);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams textViewParams =
                                            new LinearLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                            );
                                    textViewParams.setMargins(5, 5, 5, 15);
                                    postDescription.setLayoutParams(textViewParams);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.RED);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);

                                    activityUsersPostsLinearLayout.addView(postImageView);
                                    activityUsersPostsLinearLayout.addView(postDescription);


                                }
                            }
                        });

                    }
                } else if (e != null) {
                    FancyToast.makeText(UsersPostsActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                    ).show();

                } else {

                    FancyToast.makeText(UsersPostsActivity.this,
                            String.format(getString(R.string.toast_users_posts_has_no_posts), postsUsername),
                            FancyToast.LENGTH_LONG,
                            FancyToast.CONFUSING,
                            false
                    ).show();
                    finish();
                }
                if (android.os.Build.VERSION.SDK_INT <= 21){
                    dialog.dismiss();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

        });


    } // end of populateUsersPosts()
} // end of class
