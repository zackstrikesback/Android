package com.zackstrikesback.poll;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;

public class Results extends ListActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static DBPoll[] polls;

    DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        myDB = DBAdapter.getInstance(this);
        polls = myDB.getPolls();

        ResultAdapter customAdapter = new ResultAdapter(this, new ArrayList<>(Arrays.asList(polls)));
        //setListAdapter(customAdapter);

        getListView().setAdapter(customAdapter);
    }
    /*public void onListItemClick(ListView l, View v, int position, long id) {
        int i = (int) id;
        switch(i) {
            case 0:
                poll = "Yes = " + p1y + ", No = " + p1n;
                break;
            case 1:
                poll = "Yes = " + p2y + ", No = " + p2n;
                break;
            case 2:
                poll = "Yes = " + p3y + ", No = " + p3n;
                break;
            case 3:
                poll = "Yes = " + p4y + ", No = " + p4n;
                break;
            case 4:
                poll = "Yes = " + p5y + ", No = " + p5n;
                break;
            case 5:
                poll = "Yes = " + p6y + ", No = " + p6n;
                break;
            case 6:
                poll = "Yes = " + p7y + ", No = " + p7n;
                break;
            case 7:
                poll = "Yes = " + p8y + ", No = " + p8n;
                break;
            case 8:
                poll = "Yes = " + p9y + ", No = " + p9n;
                break;
            case 9:
                poll = "Yes = " + p10y + ", No = " + p10n;
                break;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(poll);
        alertDialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
}
