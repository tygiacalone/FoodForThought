package com.example.user.foodforthought;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ChatApplicationActivity extends ActionBarActivity {

    private EditText messageText; // Hold text from text field messageText
    private String recipientId;
    private String userId;
    ListView messagesList;
    MessageAdapter messageAdapter;
    ParseUser currentUser = ParseUser.getCurrentUser();
    private Timer redrawTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_application);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");
        ParseUser currentUser = ParseUser.getCurrentUser();

        messagesList = (ListView) findViewById(R.id.messageList);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);

        // Hides action bar
        setupActionBar();

        /** Intermittently check for new messages and refresh list of messages on screen */
        final ParseUser user = currentUser;
        redrawMessageList(user);

        redrawTimer = new Timer();
        redrawTimer.schedule(new TimerTask() {
            @Override
            public void run(){
                redrawMessageList(user);
            }
        },0, 10000); //10000ms == update every 10 seconds
    }

    //Arg = user's ID
    public void redrawMessageList(ParseUser currentUser){
        /** Go through the message list and refresh the messages presented on screen. */

        final ParseUser user = currentUser;
        String[] userIds = {currentUser.getUsername(), "John Doe"};

        // Build list of messages sent between the currentUser and recipient
        ParseQuery<ParseObject> query = ParseQuery.getQuery("message");
        query.whereContainedIn("recipient", Arrays.asList(userIds));
        query.whereContainedIn("sender", Arrays.asList(userIds)); // Replace with recipient username
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    // Draw the messages sequentially from top by LIFO
                    for( ParseObject singleMessage : messageList)
                    {
                        int sentByMe = 0;
                        if (singleMessage.get("sender").toString().equals(user.getUsername()))
                            sentByMe = 1;

                        String textBody = singleMessage.get("text").toString();
                        messageAdapter.addMessage(textBody, sentByMe);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error retrieving message list.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // Stores message and sends it to user and recipient's message list.
    public void sendButtonClickHandler(View view){

        //Intent intent = getIntent();
        //recipientId = intent.getStringExtra("RECIPIENT_ID");

        /** Save text field into a message */
        messageText = (EditText) findViewById(R.id.messageText);

        if (messageText.getText().toString().equals(""))
            return;

        /** Save message on user and recipient's message list */
        ParseObject message = new ParseObject("message");
        message.put("text", messageText.getText().toString());
        message.put("recipient", "John Doe");
        message.put("sender", currentUser.getUsername());

        message.saveInBackground();


        /** Update list of messages immediately when send is pressed */
        redrawMessageList(currentUser);

        messageText.setText("");
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
        getMenuInflater().inflate(R.menu.menu_chat_application, menu);
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
