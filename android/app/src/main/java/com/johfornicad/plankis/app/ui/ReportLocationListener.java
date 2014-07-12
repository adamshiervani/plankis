package com.johfornicad.plankis.app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by adam on 3/26/14.
 */
public class ReportLocationListener implements android.location.LocationListener {

    private LocationManager locManager;
    private Context mContext;

    public ReportLocationListener(Context mContext) {
        this.mContext = mContext;

        //TODO: Check if gps setting is activated, if not then redirect to settings page
        //TODO: Use the wifi in some cases. Havent figured it out yet.
        // Use the location manager through GPS

        if (checkGps()) {
            locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            //Find the best provider = Either wifi or GPS
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);
            String provider = locManager.getBestProvider(criteria, true);
            locManager.requestLocationUpdates(provider, 0, 0, this);
            locManager.getLastKnownLocation(provider);
        } else {
            //TODO: Alert user to enable the GPS. Maybe even redirect the user to the GPS enable page
            Toast.makeText(mContext, "No, GPS!. ENABLE GPS!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkGps() {
        //TODO: CHECK IF GPS IS ACTIVE
        return true;
    }


    @Override
    public void onLocationChanged(Location location) {
        getReadableLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getReadableLocation(Location loc) {
        String readLocation = "";

        if (loc != null) {

            double longi = loc.getLongitude();
            double lat = loc.getLatitude();

            sendData(String.valueOf(lat), String.valueOf(longi));
            locManager.removeUpdates(this);

        } else {
            //TODO: Throw Error. No location
            Toast.makeText(mContext, "L0l. No Location", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendData(String lati, String longi) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        JsonObject json = new JsonObject();
        // Location
        json.addProperty("longitude", longi);
        json.addProperty("latitude", lati);

        //Get the device previously generated UUID
        String UUID = prefs.getString("UUID", "DEFAULT");
        json.addProperty("uuid", UUID);


        Ion.with(mContext, "http://192.168.56.1:3000/report")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        Toast.makeText(mContext, "POSTED TO THE DATABASE", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
