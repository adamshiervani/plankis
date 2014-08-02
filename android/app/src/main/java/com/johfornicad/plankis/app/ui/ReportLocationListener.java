package com.johfornicad.plankis.app.ui;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by adam on 3/26/14.
 */
public class ReportLocationListener implements android.location.LocationListener {

    private LocationManager locManager;
    private Context mContext;
    public CustomLocationListener listener;
    public ReportLocationListener(Context mContext, CustomLocationListener customLocationListener) {
        this.mContext = mContext;
        this.listener = customLocationListener;

    }

    public boolean checkGps() {
        //TODO: CHECK IF GPS IS ACTIVE
        return true;
    }

    public void startTracking(){
        //TODO: Check if gps setting is activated, if not then redirect to settings page
        //TODO: Use the wifi in some cases. Havent figured it out yet.
        // Use the location manager through GPS

        if (checkGps()) {
            locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            //Find the best provider = Either wifi or GPS
            Criteria criteria = new Criteria();

            //API level 9 and up
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
            criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);

            String provider = locManager.getBestProvider(criteria, true);
            locManager.requestLocationUpdates(provider, 0, 0, this);
            locManager.getLastKnownLocation(provider);
        } else {
            //TODO: Alert user to enable the GPS. Maybe even redirect the user to the GPS enable page
            Toast.makeText(mContext, "No, GPS!. ENABLE GPS!", Toast.LENGTH_SHORT).show();
        }
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
        listener.onLocationInterface(loc);
        locManager.removeUpdates(this);
    }

    public interface CustomLocationListener{
        void onLocationInterface(Location location);
    }

}
