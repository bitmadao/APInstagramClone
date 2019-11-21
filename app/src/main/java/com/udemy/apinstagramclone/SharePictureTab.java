package com.udemy.apinstagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{

    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private ImageView imgTabSharePicture;
    private EditText edtTabSharePictureDescription;
    private Button btnTabSharePictureShareImage;

    private Bitmap uploadedBitmap;

    private ProgressDialog dialog;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sharePictureTabView = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        constraintLayout = sharePictureTabView.findViewById(R.id.tabShareImageConstraintLayout);

        if(Build.VERSION.SDK_INT > 21){
            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100,100);
            constraintLayout.addView(progressBar,params);
            progressBar.setVisibility(View.GONE);
        }
        btnTabSharePictureShareImage = sharePictureTabView.findViewById(R.id.btnTabSharePictureShareImage);

        imgTabSharePicture = sharePictureTabView.findViewById(R.id.imgTabSharePicture);
        edtTabSharePictureDescription = sharePictureTabView.findViewById(R.id.edtTabSharePictureDescription);

        btnTabSharePictureShareImage.setText(R.string.btn_tab_share_picture_share_image_before_choosing);
        btnTabSharePictureShareImage.setEnabled(false);

        imgTabSharePicture.setOnClickListener(SharePictureTab.this);
        btnTabSharePictureShareImage.setOnClickListener(SharePictureTab.this);

        return sharePictureTabView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgTabSharePicture:
                imgTabSharePictureTapped();
                break;
            case R.id.btnTabSharePictureShareImage:
                setBtnTabSharePictureShareImageTapped();
                break;
        }
    }

    private void imgTabSharePictureTapped(){

        if(android.os.Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                1000);
        } else {
            getChosenImage();
        }

    }

    private void setBtnTabSharePictureShareImageTapped(){
        if(uploadedBitmap != null) {
            if(edtTabSharePictureDescription.getText().toString().isEmpty()){
                FancyToast.makeText(getContext(),
                        getString(R.string.toast_tab_share_picture_must_describe),
                        FancyToast.LENGTH_LONG,
                        FancyToast.INFO,false)
                    .show();

            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                uploadedBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("pic.png",bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture",parseFile);
                parseObject.put("image_des",edtTabSharePictureDescription.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                if(Build.VERSION.SDK_INT <= 21) {
                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage(getString(R.string.generic_dialog_loading));
                    dialog.show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            FancyToast.makeText(getContext(),
                                    getString(R.string.toast_tab_share_picture_upload_success),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false)
                                .show();

                        } else {
                            FancyToast.makeText(getContext(),
                                    e.getMessage(),
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


            }

        } else {
            FancyToast.makeText(getContext(),
                    getString(R.string.toast_generic_error),
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false)
                .show();
        }

    }


    private void getChosenImage() {
//        FancyToast.makeText(getContext(),"External Storage Access granted.",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    uploadedBitmap = BitmapFactory.decodeFile(Uri.parse(picturePath).getPath());


                    imgTabSharePicture.setImageBitmap(uploadedBitmap);
                    btnTabSharePictureShareImage.setText(R.string.btn_tab_share_picture_share_image);
                    btnTabSharePictureShareImage.setEnabled(true);

                } catch (Exception e) {

                    Log.i("AppTag",e.getMessage());


                } // end exception
            } // resultCode
        }// requestCode
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }
} /// end of class
