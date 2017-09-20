package com.zackstrikesback.poll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.button;

public class Polls extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public static int NUM_ITEMS;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static DBPoll[] polls;

    private ViewPager mPager;
    private PagerAdapter mAdapter;
    DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls);

        myDB = DBAdapter.getInstance(this);
        polls = myDB.getPolls();
        NUM_ITEMS = polls.length;

        Toolbar toolbar = (Toolbar) findViewById(R.id.uxMyToolbar);
        setSupportActionBar(toolbar);

        mAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.uxPager);
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Button yButton = (Button)findViewById(R.id.yesBTN);
        yButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int y = mPager.getCurrentItem()+1;
                myDB.votePoll(y, true);
                Toast.makeText(mPager.getContext(), "Yes +1", Toast.LENGTH_SHORT).show();
            }
        });

        Button nButton = (Button)findViewById(R.id.noBTN);
        nButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = mPager.getCurrentItem()+1;
                myDB.votePoll(n, false);
                Toast.makeText(mPager.getContext(), "No +1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.uxActionMenu) {
            Intent explicitIntent = new Intent(Polls.this, MainActivity.class);
            startActivity(explicitIntent);
        }else if (id == R.id.uxActionCompose) {
            Intent explicitIntent = new Intent(Polls.this, Create.class);
            startActivity(explicitIntent);
        }else if (id == R.id.uxActionResults){
            Intent explicitIntent = new Intent(Polls.this, Results.class);
            startActivity(explicitIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.uxActionMenu) {
            Intent explicitIntent = new Intent(Polls.this, MainActivity.class);
            startActivity(explicitIntent);
        }else if (item.getItemId() == R.id.uxActionCompose) {
            Intent explicitIntent = new Intent(Polls.this, Create.class);
            startActivity(explicitIntent);
        }else if (item.getItemId() == R.id.uxActionResults){
            Intent explicitIntent = new Intent(Polls.this, Results.class);
            startActivity(explicitIntent);
        }
        return false;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
            View tv = v.findViewById(R.id.uxText);
            int num = mNum+1;
            ((TextView)tv).setText("Poll #" + num);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            int n = polls.length;
            String poll[][] = new String[n][3];
            for(int i = 0; i < n; i++){
                poll[i][0] = polls[i].getName();
                poll[i][1] = polls[i].getQuestion();
                poll[i][2] = polls[i].getImage();
            }
            PollAdapter customAdapter = new PollAdapter(getActivity(), new ArrayList<>(Arrays.asList(polls[mNum])));
            setListAdapter(customAdapter);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position){
            return ArrayListFragment.newInstance(position);
        }
        @Override
        public int getCount(){
            return NUM_ITEMS;
        }
    }
}