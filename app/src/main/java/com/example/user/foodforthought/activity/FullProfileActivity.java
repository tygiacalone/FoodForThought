package com.example.user.foodforthought.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.user.foodforthought.adapter.ListViewAdapter;
import com.example.user.foodforthought.R;
import com.parse.*;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class FullProfileActivity extends ActionBarActivity {

    private ArrayList<String> profileItems;
    private ListView profileListView;
    private ListViewAdapter profileListViewAdapter;
    private String currentUser;
    ArrayList<String> companyNames1 = new ArrayList<>(Arrays.asList("Hulu", "eBay"));
    ArrayList<String> companyTitles1 = new ArrayList<>(Arrays.asList("Software Engineering Intern",
            "Front End & Design Intern"));
    ArrayList<String> companyDates1 = new ArrayList<>(Arrays.asList("2014-2015", "2013-2014"));
    ArrayList<String> companyExperience1 = new ArrayList<>(Arrays.asList(
            "Helped implement the mobile app for Hulu and product managed a new feature",
            "Helped design and developed the front end for a tool that will improve and automate the " +
                    "seller onboarding experience"));
    ArrayList<String> companyNames2 = new ArrayList<>(Arrays.asList("Cisco", "Qualcomm"));
    ArrayList<String> companyTitles2 = new ArrayList<>(Arrays.asList("Worldwide Sales Intern",
            "Software Engineering Intern"));
    ArrayList<String> companyDates2 = new ArrayList<>(Arrays.asList("2014-2015", "2011-2012"));
    ArrayList<String> companyExperience2 = new ArrayList<>(Arrays.asList(
            "Marketed company products to several large firms in accordance with their interests",
            "Worked on an internal testing tool that would significantly decrease testing times"));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");

        // Set layout file for Activity
        setContentView(R.layout.activity_full_profile);

        // Hides action bar
        setupActionBar();

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("USER_ID");

        String userID;

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", currentUser);
        try {
            String objectID = query.getFirst().getObjectId();
            userID = objectID;
        } catch (Exception e) {
            return;
        }

        System.out.println("user ID = " + userID);

        TextView fullProfileMatchName = (TextView) findViewById(R.id.fullProfileMatchName);
        fullProfileMatchName.setText(currentUser);

        retrieveImage(currentUser);

        /** Query database for data pertaining to the currentUser to fill out the profile*/

        // Find the ListView resource
        profileListView = (ListView) findViewById(R.id.profileList);
        if (userID.equals("eopfB0aSkv")) {
            profileListViewAdapter = new ListViewAdapter(this, companyNames1,
                    companyTitles1, companyDates1, companyExperience1);
        }
        else {
            profileListViewAdapter = new ListViewAdapter(this, companyNames2,
                    companyTitles2, companyDates2, companyExperience2);
        }

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

    private void retrieveImage(String name) {

        String imageID;

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", name);
        try {
            String objectID = query.getFirst().getString("imageID");
            imageID = objectID;
        } catch (Exception e) {
            return;
        }

        ParseQuery<ParseObject> pictureQuery = ParseQuery.getQuery("UserPicture");
        try {
            ParseObject foundIMG = pictureQuery.get(imageID);

            // Create a ParseFile to grab the image from the database
            ParseFile applicantResume = (ParseFile)foundIMG.get("mediaurl");
            applicantResume.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    // data has the bytes for the picture
                    if (e == null) {
                        // Replace the image in the imageView with the one in the database
                        ImageView image = (ImageView) findViewById(R.id.imageView);
                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        image.setImageBitmap(bMap);
                    } else {
                        // something went wrong
                    }
                }
            });

        } catch (ParseException e) {
            // Message if failed
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, e.toString(), duration);
            toast.show();
        }
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
