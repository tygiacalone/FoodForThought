package com.example.user.foodforthought.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.foodforthought.adapter.MatchesListAdapter;
import com.example.user.foodforthought.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;


public class MatchesListActivity extends ActionBarActivity {
    ListView matchesList;
    MatchesListAdapter matchesAdapter;
    ParseUser currentUser = ParseUser.getCurrentUser();
    Timer redrawTimer;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");

        names = new ArrayList<String>();

        matchesList = (ListView) findViewById(R.id.matchesList);
        matchesAdapter = new MatchesListAdapter(this);
        matchesList.setAdapter(matchesAdapter);

        matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long l) {
                openConversation(matchesAdapter.getItem(position));
            }
        });

        // Hides action bar
        setupActionBar();

        redrawMatchesList(currentUser);
/*
        redrawTimer = new Timer();
        redrawTimer.schedule(new TimerTask() {
            @Override
            public void run(){
                redrawMatchesList(user);
            }
        },0, 2000); //2000ms == update every 2 seconds
*/
    }

    // Go through the message list and refresh the messages presented on screen.
    public void redrawMatchesList(ParseUser currentUser){

        // All who have swiped the current user right
        // Build list of messages sent between the currentUser and recipient
        ParseQuery<ParseObject> swipedUser = ParseQuery.getQuery("swipe");
        swipedUser.whereEqualTo("recipient", currentUser.getUsername());
        swipedUser.setLimit(900);

        // All swiped right by current user
        ParseQuery<ParseObject> swipedByUser = ParseQuery.getQuery("swipe");
        swipedByUser.whereEqualTo("sender", currentUser.getUsername());
        swipedByUser.setLimit(900);

        // We want the set of users which share usernames between the two different sets. Aka, recipient and sender are the same
        ParseQuery<ParseObject> query = swipedUser.whereMatchesKeyInQuery("sender", "recipient", swipedByUser);
        query.whereMatchesKeyInQuery("recipient","sender", swipedByUser);

        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {

                    // Unlikely anyone will ever have this many matches!
                    if (messageList.size() > 850)
                        for( int i = 0; i <= (messageList.size() - 850); i++ )
                            messageList.get(i).deleteInBackground();

                    // Draw the messages sequentially from top by LIFO
                    matchesAdapter.clearList();
                    for( int i = 0; i < messageList.size(); i++)
                    {
                        int pos = i;
                        ParseObject singleMessage = messageList.get(pos);
                        String name = singleMessage.get("sender").toString();
                        int sentByMe = 0;
                        //if (singleMessage.get("recipient").toString().equals(user.getUsername()))
                            matchesAdapter.addMatch(name, pos);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error retrieving message list.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void openConversation(Pair<String,Integer> match){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", match.first);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for( ParseUser user : userList) { //userList should only ever be length == 1
                        String uniqueId = user.getUsername(); /** Replace with unique (LinkedIn) ID */
                        Intent intent = new Intent(getApplicationContext(), ChatApplicationActivity.class);
                        intent.putExtra("RECIPIENT_ID", uniqueId);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        // Refresh list
        redrawMatchesList(currentUser);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matches_list, menu);
        return true;
    }

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
