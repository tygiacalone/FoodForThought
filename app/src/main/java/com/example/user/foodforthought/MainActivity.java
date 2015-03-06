package com.example.user.foodforthought;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.*;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");
        setContentView(R.layout.activity_main);

        // Hides action bar
        setupActionBar();
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

    // Responds to clicking image on activity_main.xml.
    // Should redirect to profile.xml
    public void clickMainProfileHandler(View view)
    {
        Intent intent = new Intent(this, FullProfileActivity.class);
        startActivity(intent);
    }

    //Make a match
    public void yesMatchClickHandler(View view){
        final ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("swipe");
        query.whereEqualTo("recipient", "John Doe");
        query.whereEqualTo("sender", currentUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recipientList, ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),
                            "Already swiped this user!",
                            Toast.LENGTH_LONG).show();
                } else {
                    // If never seen before, add to DB
                    ParseObject swipe = new ParseObject("swipe");
                    swipe.put("recipient", "John Doe");
                    swipe.put("sender", currentUser.getUsername());

                    swipe.saveInBackground();
                }
            }
        });


    }

    public void matchesButtonClickHandker(View view)
    {
        Intent intent = new Intent(this, MatchesListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatementhe d
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
