package com.example.user.foodforthought;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.*;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");
        setContentView(R.layout.activity_main);
    }


    // Responds to clicking image on activity_main.xml.
    // Should redirect to profile.xml
    public void clickMainProfileHandler(View view)
    {
        Intent intent = new Intent(this, FullProfileActivity.class);
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
