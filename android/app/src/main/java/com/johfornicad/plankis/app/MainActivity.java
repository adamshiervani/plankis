package com.johfornicad.plankis.app;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.johfornicad.plankis.app.gcm.GoogleCloud;
import com.johfornicad.plankis.app.ui.ReportLocationListener;
import com.johfornicad.plankis.app.ui.ReportView;
import com.johfornicad.plankis.app.utils.TypefaceSpan;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MainActivity extends FragmentActivity {

//    public final static String BASE_URL = "http://107.170.133.137:3000";
//    public final static String BASE_URL = "http://192.168.1.23:3000";
    public final static String BASE_URL = "http://192.168.56.1:3000";
//    public final static String BASE_URL = "http://192.168.178.141:3000";
//    public final static String BASE_URL = "http://192.168.178.52:3000";
//    public final static String BASE_URL = "http://192.168.1.23:3000";
//    public final static String BASE_URL = "http://192.168.0.16:3000";
//    public final static String BASE_URL = "http://192.168.178.52:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Initialize init = new Initialize(getApplication());

        //TODO: Splash screen

//        //Set the UUID
        init.setUUID();

        //Initialize the Google Cloud Messaging
        init.setUUID();
        new GoogleCloud(MainActivity.this, getApplicationContext()).initialize();


        SpannableString s = new SpannableString("Plankplaneraren");
        s.setSpan(new TypefaceSpan(this, "Qwigley-Regular.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setTitle(s);



        if (savedInstanceState == null) {
            Fragment fragment = new ReportView();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        //Set custom font for the actionbar title
        SpannableString s = new SpannableString(getString(R.string.app_name));
            s.setSpan(new TypefaceSpan(this, "Qwigley-Regular.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        actionBar.setTitle(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_report:
                    new ReportLocationListener(this, new ReportLocationListener.CustomLocationListener() {
                        @Override
                        public void onLocationInterface(Location location) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                            JsonObject json = new JsonObject();

                            json.addProperty("longitude", location.getLongitude());
                            json.addProperty("latitude", location.getLatitude());
                            json.addProperty("uuid", prefs.getString("UUID", ""));

                            Ion.with(getApplicationContext(),  MainActivity.BASE_URL + "/report" )
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        Toast.makeText(getApplicationContext(), "lol", Toast.LENGTH_LONG);
                                    }
                                });

                        }


                    }).startTracking();
                    return true;

            case R.id.action_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
