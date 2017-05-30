package com.zackstrikesback.customsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.os.Build;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.app.SearchManager;
import android.content.Context;
import android.app.SearchableInfo;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            // Get the SearchView and set the searchable configuration
            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) menuItem.getActionView();

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());

            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(info);
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
