package com.johfornicad.plankis.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by adam on 15.07.14.
 */
public class Initialize {
    Context mContext;
//   TODO: clean this up
   public Initialize(Context context){
        this.mContext = context;
   }

   public void setUUID(){
       SharedPreferences prefs = this.mContext.getSharedPreferences(MainActivity.class.getSimpleName(),
               Context.MODE_PRIVATE);

       //Assume true if the key does not yet exist
       if (prefs.getBoolean("first_launch", true)) {

           //Generate random UUID based on multiple variables
           final TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService(Context.TELEPHONY_SERVICE);

           final String tmDevice, tmSerial, androidId;
           tmDevice = "" + tm.getDeviceId();
           tmSerial = "" + tm.getSimSerialNumber();
           androidId = "" + android.provider.Settings.Secure.getString(this.mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

           UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

           setSharedPreferences("UUID", deviceUuid.toString());

       } else {
           SharedPreferences sp = PreferenceManager
                   .getDefaultSharedPreferences(this.mContext);
           sp.edit().putBoolean("first_launch", true).apply();
       }
   }

    public void setSharedPreferences(String key, String value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this.mContext);
        sp.edit().putString(key, value).apply();
    }
}
