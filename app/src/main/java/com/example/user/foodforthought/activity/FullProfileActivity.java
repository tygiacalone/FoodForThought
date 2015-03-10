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

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("USER_ID");

        TextView fullProfileMatchName = (TextView) findViewById(R.id.fullProfileMatchName);
        fullProfileMatchName.setText(currentUser);

        retrieveImage(currentUser);

        /** Query database for data pertaining to the currentUser to fill out the profile*/

        // Find the ListView resource
        profileListView = (ListView) findViewById(R.id.profileList);
        profileListViewAdapter = new ListViewAdapter(this, companyNames,
                companyTitles, companyDates, companyExperience);

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
            System.out.println(objectID);
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
