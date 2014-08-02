package com.johfornicad.plankis.app.splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.johfornicad.plankis.app.MainActivity;
import com.johfornicad.plankis.app.R;
import com.johfornicad.plankis.app.splash.adapter.SplashScreenPagerAdapter;
import com.johfornicad.plankis.app.ui.ReportLocationListener;
import com.johfornicad.plankis.app.utils.Typefaces;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.UUID;

public class SplashScreen extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    SplashScreenPagerAdapter mSplasScreennPagerAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



    //Get preferences
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplication());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        TextView swipe = (TextView) findViewById(R.id.splash_instructions_swipe);
//        swipe.setVisibility(View.INVISIBLE);

        if(sp.getBoolean("first_launch", true)){
//            swipe.setVisibility(View.INVISIBLE);

            // Set UUID
            this.setUUID();

            // Set location
            this.setCity();
        }



        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mSplasScreennPagerAdapter =
                new SplashScreenPagerAdapter(
                        getSupportFragmentManager());
        mViewPager.setAdapter(mSplasScreennPagerAdapter);


    }

    public void setPreference(String key, String value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplication());
        sp.edit().putString(key, value).apply();
    }


    public void setUUID(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);


        //Generate random UUID based on multiple variables
        final TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        setPreference("UUID", deviceUuid.toString());

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        sp.edit().putBoolean("first_launch", false).apply();

    }

    public void setCity(){
        //get the users location
        new ReportLocationListener(getApplication(), new ReportLocationListener.CustomLocationListener(){
            @Override
            public void onLocationInterface(Location location) {


                if (location == null) {
                    Toast.makeText(getApplication(), "L0l. No Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());

                JsonObject json = new JsonObject();


                // Location
                json.addProperty("longitude", location.getLongitude());
                json.addProperty("latitude", location.getLatitude());

                //Get the device previously generated UUID
                String UUID = prefs.getString("UUID", "");
                json.addProperty("uuid", UUID);


                // HTTP REQUEST
                Ion.with(getApplication(), MainActivity.BASE_URL + "/setup")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if(result == null || result.isJsonNull()){return;}

                                Toast.makeText(getApplication(), "Done! Initialization is done!", Toast.LENGTH_SHORT).show();

                                setPreference("city", result.get("city").getAsString());
                                setPreference("ticketfrom", result.get("ticketfrom").getAsString());
                                setPreference("ticketpattern", result.get("ticketpattern").getAsString());

                            }
                        });
            }
        }).startTracking();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}



