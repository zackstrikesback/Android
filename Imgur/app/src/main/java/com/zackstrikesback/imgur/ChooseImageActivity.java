package com.zackstrikesback.imgur;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zackstrikesback.imgur.ImgurAuthorization;
import com.zackstrikesback.imgur.LoginActivity;
import com.zackstrikesback.imgur.RefreshAccessTokenTask;

public class ChooseImageActivity extends ActionBarActivity {

    private static final int REQ_CODE_PICK_IMAGE = 1;
    public String user = ImgurAuthorization.getInstance().user();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedIn = ImgurAuthorization.getInstance().isLoggedIn();
        menu.findItem(R.id.menu_login).setVisible(!loggedIn);
        menu.findItem(R.id.menu_logout).setVisible(loggedIn);
        menu.findItem(R.id.menu_gallery).setVisible(loggedIn);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_login) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menu_logout) {
            ImgurAuthorization.getInstance().logout();
            startActivity(new Intent(getApplicationContext(), ChooseImageActivity.class));
            Toast.makeText(this, R.string.logged_out, Toast.LENGTH_LONG).show();
            return true;
        }
        else if (item.getItemId() == R.id.menu_gallery){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menu_upload) {
            startActivity(new Intent(getApplicationContext(), ChooseImageActivity.class));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void pickImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQ_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQ_CODE_PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    getChooseImageFragment().setImage(selectedImage);
                }
        }
    }

    private ChooseImageFragment getChooseImageFragment() {
        return (ChooseImageFragment) getSupportFragmentManager().findFragmentById(R.id.choose_image_fragment);
    }

    public void copyLink(View view) {
        getChooseImageFragment().copyLink(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RefreshAccessTokenTask().execute();
    }

}