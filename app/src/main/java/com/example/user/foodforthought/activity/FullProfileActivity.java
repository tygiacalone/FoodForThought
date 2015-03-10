package com.example.user.foodforthought.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.user.foodforthought.adapter.ListViewAdapter;
import com.example.user.foodforthought.R;
import com.parse.*;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;


public class FullProfileActivity extends ActionBarActivity {

    private ArrayList<String> profileItems;
    private ListView profileListView;
    private ListViewAdapter profileListViewAdapter;
    private String parseID;
    ArrayList<String> companyNames = new ArrayList<>(Arrays.asList("eBay", "Facebook", "Google"));
    ArrayList<String> companyTitles = new ArrayList<>(Arrays.asList("intern1", "intern2", "intern3"));
    ArrayList<String> companyDates = new ArrayList<>(Arrays.asList("2012-2013", "2013-2014", "2014-2015"));
    ArrayList<String> companyExperience = new ArrayList<>(Arrays.asList("a b c d", "i j k l", "w x y z"));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");

        // Set layout file for Activity
        setContentView(R.layout.activity_full_profile);

        // Hides action bar
        setupActionBar();

        // Gets the Parse object ID so we know where to pull from
        Intent intent = getIntent();
        parseID = intent.getStringExtra("ID");
        System.out.println(parseID);

        // Find the ListView resource
        profileListView = (ListView) findViewById(R.id.profileList);
        profileListViewAdapter = new ListViewAdapter(this, companyNames,
                companyTitles, companyDates, companyExperience);

        // Set the ListView Adapter
        profileListView.setAdapter(profileListViewAdapter);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_profile, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().hide();
        }
    }
}
