package com.example.user.foodforthought.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.foodforthought.R;
import com.example.user.foodforthought.util.SystemUiHider;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;
import org.json.JSONException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LoginActivity extends Activity {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    private static final String request = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)";
    Activity thisActivity;
    protected LocationManager lM;
    protected myLocationListener ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupActionBar();
        setUpdateState();

        Parse.initialize(this, "vKMS21EgxqmkWPbZ4KMRc4p7PmUWONtatA4ZM2bn", "6gMhVDU5xcakoNIXDpBeykmyCuy3ka0e7pVkm59C");
        lM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ll = new myLocationListener();
            lM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,ll);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        thisActivity = this;

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });


        // Check if already been logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Go to MainActivity
            Intent intent = new Intent(thisActivity, MainActivity.class);
            startActivity(intent);
        }


        /* Set custom font on button - doesn't work on XML button
        Typeface helvetica = Typeface.createFromAsset(getApplicationContext().getAssets(), "HelveticaInseratLTStd-Roman.otf");
        Button loginLinkedInButton = (Button) findViewById(R.id.login_li_button);
        loginLinkedInButton.setTypeface(helvetica);
        */

    }
    public void logoutButtonClickHandler(View view){
        LISessionManager.getInstance(getApplicationContext()).clearSession();
        setUpdateState();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.logOut();
            Toast.makeText(getApplicationContext(),
                    "Logged out of Parse.",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void linkedInAuthenticateClickHandler(View view){

        /**
         * For Samuel and Antonio to fill out
         * @param view
         */


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        final ParseUser user = new ParseUser();
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                setUpdateState();
                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
                apiHelper.getRequest(LoginActivity.this, request, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {
                        try{
                            JSONObject profile = apiResponse.getResponseDataAsJson();

                            String userId = profile.has("id")?profile.getString("id"):"dummy";
                            String emailAddress = profile.has("emailAddress")?profile.getString("emailAddress"):"dummy";
                            user.setUsername(userId);
                            user.setPassword(userId);
                            Location location = lM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            user.setEmail(emailAddress);

                            // other fields can be set just like with ParseObject
                            if(location != null)
                            {
                                ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
                                user.put("location", point);
                                lM.removeUpdates(ll);
                                ll = null;
                            }

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //After finishing, go to MainActivity
                                        Intent intent = new Intent(thisActivity, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ParseUser.logInInBackground(user.getUsername(), user.getUsername(), new LogInCallback() {
                                            public void done(ParseUser user, com.parse.ParseException e) {
                                                if (user != null) {

                                                    //After finishing, go to MainActivity
                                                    Intent intent = new Intent(thisActivity, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            e.toString(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }catch(JSONException e)
                        {

                        }

                      }

                    @Override
                    public void onApiError(LIApiError LIApiError) {

                    }
                });
            }
            @Override
            public void onAuthError(LIAuthError error) {
                setUpdateState();
                ((TextView) findViewById(R.id.at)).setText(error.toString());
                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);;

        //Get LinkedIn data to create user database
        /**
         * ********************************************************
         * Replace values below with appropriate data from LinkedIn
         * ********************************************************
         */


        /*
        Resources res = getResources();

        Drawable drawable = res.getDrawable(R.drawable.test);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("picture.txt", data);
        file.saveInBackground();

        ParseObject userPicture = new ParseObject("UserPicture");
        userPicture.put("mediatype", "image");
        userPicture.put("mediaurl", file);
        userPicture.saveInBackground();


        */

        // Login with Parse
        /** 2nd field (password which is "54321") has to be replaced with a unique identifier for each user. (LinkedIn ID - Samuel knows what I mean) */
                                                  // field, "54321"
//        ParseUser.logInInBackground(user.getUsername(), user.getUsername(), new LogInCallback() {
//                    public void done(ParseUser user, com.parse.ParseException e) {
//                        if (user != null) {
//
//                            //After finishing, go to MainActivity
//                            Intent intent = new Intent(thisActivity, MainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    e.toString(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();

        ((TextView) findViewById(R.id.at)).setText(accessTokenValid? session.getAccessToken().toString(): "Login failed" );

    }

    public class myLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged (Location location)

        {

        }

        @Override
        public void onProviderDisabled (String provider)

        {

            Toast.makeText (getApplicationContext (), "Gps Disabled", Toast.LENGTH_SHORT).show ();

        }

        @Override
        public void onProviderEnabled (String provider)

        {

            Toast.makeText (getApplicationContext (), "Gps Enabled", Toast.LENGTH_SHORT).show ();

        }

        @Override
        public void onStatusChanged (String provider, int status, Bundle extras)

        {

        }
    }
    private static Scope buildScope(){
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }
}
