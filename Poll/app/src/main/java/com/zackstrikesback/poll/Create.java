package com.zackstrikesback.poll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Create extends AppCompatActivity {
    @BindView(R.id.name) EditText name;
    @BindView(R.id.query) EditText query;
    @BindView(R.id.image) Switch image;
    @BindView(R.id.up) Button upload;
    @BindView(R.id.ok) Button ok;
    @BindView(R.id.can) Button can;
    @BindView(R.id.cam) Button camera;
    @BindView(R.id.uxImageView) ImageView selected;
    private static final int REQUEST_CODE = 100;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "ImageUp";
    private String imagePath = null;
    private File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ButterKnife.bind(this);

        upload.setEnabled(false);
        camera.setEnabled(false);
        name.getText().clear();
        query.getText().clear();

        image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    upload.setEnabled(true);
                    camera.setEnabled(true);
                }
                else {
                    upload.setEnabled(false);
                    camera.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.ok, R.id.can, R.id.up, R.id.cam})
    public void onClick(Button button) {
        if(button == ok) {
            String N = name.getText().toString();
            String Q = query.getText().toString();
            String I = imagePath;

            if(TextUtils.isEmpty(N)) {
                name.setError("Field cannot be empty.");
            }
            if(TextUtils.isEmpty(Q)) {
                query.setError("Field cannot be empty.");
            }
            else {
                DBAdapter db = DBAdapter.getInstance(this);
                db.addPoll(N, Q, I);
                Toast.makeText(this, "Poll Added", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
        else if(button == can) {
            Intent explicitIntent = new Intent(Create.this, MainActivity.class);
            startActivity(explicitIntent);
        }else if(button == upload) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
        }else if (button == camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            filePath = getOutputMediaFile(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath)); // set the image file name

            // Check if there is a camera application
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = data.getData();
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        selected.setImageBitmap(imageBitmap);
                        selected.setVisibility(View.VISIBLE);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        imagePath = finalFile.getAbsolutePath();
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_LONG).show();
                    }
                    break;
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        if (data == null) {
                            Bitmap imageBitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                            selected.setImageBitmap(imageBitmap);
                            selected.setVisibility(View.VISIBLE);
                            imagePath = filePath.getAbsolutePath();
                            break;
                        }
                        else{
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            selected.setImageBitmap(imageBitmap);
                            imagePath = filePath.getAbsolutePath();;
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        // User cancelled the image capture
                        Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        // Image capture failed, advise user
                        Toast.makeText(this, "Image capture failed", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        Log.d("sdcard state=", Environment.getExternalStorageState());

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PollApp");
        Log.d("dir=", mediaStorageDir.toString());

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        Log.d("mediaFile", mediaFile.getAbsolutePath());

        return mediaFile;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
