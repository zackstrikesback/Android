package com.zackstrikesback.contentprovider;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.content.ContentValues;
import android.content.CursorLoader;

import android.database.Cursor;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("content");
        uriBuilder.authority("com.zackstrikesback.contentprovider.provider.Financial");
        uriBuilder.appendPath("financial");
        //uriBuilder.appendPath("3");

        Uri uri = uriBuilder.build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Log.d("data",
                        cursor.getString(cursor.getColumnIndex("transaction_id")) +
                                ", " + cursor.getString(cursor.getColumnIndex("date")) +
                                ", " + cursor.getString(cursor.getColumnIndex("description")) +
                                ", " + cursor.getString(cursor.getColumnIndex("amount")));
            }
        }
    }
}
