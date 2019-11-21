package com.udemy.apinstagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    private Bitmap capturedBitmap;

    private ProgressDialog dialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        this.setTitle(R.string.activity_social_media_title);

        relativeLayout = findViewById(R.id.activitySocialMediaRelativeLayout);

        if(Build.VERSION.SDK_INT > 21){
            progressBar = new ProgressBar(SocialMediaActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            relativeLayout.addView(progressBar,params);
            progressBar.setVisibility(View.GONE);
        }

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.postImageItem:
                postImageItemMethod();

                break;

            case R.id.logutUserItem:
                logoutUserItemMethod();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000 ) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000 && resultCode == RESULT_OK && data != null) {

            try {

                Uri capturedImage = data.getData();

                capturedBitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(),
                                capturedImage);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                capturedBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("share.png",bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture",parseFile);
                parseObject.put("image_des","_MENU_POST"); // 
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                if(Build.VERSION.SDK_INT <= 21) {
                    dialog = new ProgressDialog(SocialMediaActivity.this);
                    dialog.setMessage(getString(R.string.dialog_social_media_activity_posting));
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            FancyToast.makeText(getApplicationContext(),
                                    getString(R.string.toast_social_media_post_successful),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false)
                                .show();
                        } else {

                            Log.i("AppTag", e.getMessage());
                            FancyToast.makeText(getApplicationContext(),
                                    getString(R.string.toast_generic_error),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    false)
                                .show();
                        }
                        if(Build.VERSION.SDK_INT <= 21) {
                            dialog.dismiss();
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            } catch (Exception e) {
                Log.i("AppTag", e.getMessage());
            }


        }
    }

    private void captureImage(){
        //
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, 4000);
    }

    private void postImageItemMethod(){
        if(Build.VERSION.SDK_INT >= 23 &&
        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    3000);
        } else {
            captureImage();
        }
    }

    public void logoutUserItemMethod() {
        final String userName = ParseUser.getCurrentUser().getUsername();
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null){
                    FancyToast.makeText(SocialMediaActivity.this,
                            String.format(getString(R.string.toast_social_media_logout_successful), userName),
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false)
                            .show();
                    Intent intent = new Intent(SocialMediaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SocialMediaActivity.this.finish();
                } else {
                    Log.i("AppTag",e.getMessage());

                    FancyToast.makeText(SocialMediaActivity.this,
                            getString(R.string.toast_generic_error),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false)
                            .show();
                }
            }
        });
    }

} // end of class
