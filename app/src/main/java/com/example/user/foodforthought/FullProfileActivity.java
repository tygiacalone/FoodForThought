package com.example.user.foodforthought;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.parse.*;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;


public class FullProfileActivity extends ActionBarActivity {

    private ArrayList<String> profileItems;
    private ListView profileListView;
    private ListViewAdapter profileListViewAdapter;
    private String companyNames[] = {"eBay", "Facebook", "Google"};
    private String companyJobs[] = {"intern1", "intern2", "intern3"};
    private String companyDates[] = {"2012-2013", "2013-2014", "2014-2015"};
    private String companyInfo[] = {"a b c d", "i j k l", "w x y z"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");

        ParseUser user = new ParseUser();
        user.setUsername("John Doe");
        user.setPassword("12345");
        user.setEmail("jdoe@example.com");

        // other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });

        ParseUser.logInInBackground("John Doe", "12345", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Sign up failed. Look at the ParseException to see what happened.
                }
            }
        });

        // Set layout file for Activity
        setContentView(R.layout.activity_full_profile);

        // Hides action bar
        setupActionBar();

        // Find the ListView resource
        profileListView = (ListView) findViewById(R.id.profileList);
        profileListViewAdapter = new ListViewAdapter(this, companyNames,
                companyJobs, companyDates, companyInfo);

        // Set the ListView Adapter
        profileListView.setAdapter(profileListViewAdapter);

/*
        // Create the list of items to be put on the profile
        // temp is a placeholder list
        profileItems = new ArrayList<String>();
        String[] temp = new String[] {user.getUsername(), user.getEmail()};

        profileItems.addAll(Arrays.asList(temp));

        // Add second profile
        user = ParseUser.getCurrentUser();

        // Add second profile info to list.
        String[] temp2 = new String[] {user.getUsername(), user.getEmail()};
        profileItems.addAll(Arrays.asList(temp2)); */
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
