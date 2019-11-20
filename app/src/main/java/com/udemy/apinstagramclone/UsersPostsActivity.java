package com.udemy.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout activityUsersPostsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);
        Intent receivedIntentObject = getIntent();
        postsUsername = receivedIntentObject.getStringExtra("username");
        setTitle(String.format("%s's posts",postsUsername)); //TODO add to strings.xml

        activityUsersPostsLinearLayout = findViewById(R.id.activityUsersPostsLinearLayout);

        populateUsersPosts();

    }

    private void populateUsersPosts() {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username",postsUsername);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this); //TODO deprecated class, find modern alternative, refactor other occurrences
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() > 0 && e == null) {

                    for (ParseObject post : objects) {

                        final TextView postDescription = new TextView(UsersPostsActivity.this);
                        postDescription.setText((String) post.get("image_des")); // TODO think about nulls
                        ParseFile postPicture = (ParseFile) post.get("picture");

                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data != null && e == null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPostsActivity.this);
                                    LinearLayout.LayoutParams imageViewParams =
                                            new LinearLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                                );

                                    imageViewParams.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(imageViewParams);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams textViewParams =
                                            new LinearLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                                );
                                    textViewParams.setMargins(5,5,5,15);
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
                    dialog.dismiss();
                } else {
                    FancyToast.makeText(UsersPostsActivity.this,
                            e.getMessage(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                        );
                    dialog.dismiss();
                }
            }
        });

    } // end of populateUsersPosts()
} // end of class
