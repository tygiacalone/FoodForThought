package com.example.user.foodforthought;

import android.content.Intent;
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

    private ListView profileListView;
    private ArrayList<String> profileItems;
    private ArrayAdapter<String> profileListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");

        ParseUser user = new ParseUser();
        user.setUsername("John");
        user.setPassword("Doe");
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

        // Set layout file for Activity
        setContentView(R.layout.activity_full_profile);

        // Find the ListView resource
        profileListView = (ListView) findViewById(R.id.profileList);

        // Create the list of items to be put on the profile
        // temp is a placeholder list
        profileItems = new ArrayList<String>();
        String[] temp = new String[] {"eBay", "Google", "Facebook"};
        profileItems.addAll(Arrays.asList(temp));

        // Create ArrayAdapter using the profileItems list
        profileListAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, profileItems);
        // Set the ArrayAdapter as the ListView's adapter
        profileListView.setAdapter(profileListAdapter);
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
}
