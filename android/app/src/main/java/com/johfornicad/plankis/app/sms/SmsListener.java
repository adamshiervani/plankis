package com.johfornicad.plankis.app.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.johfornicad.plankis.app.MainActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SmsListener extends BroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Initialize the context
        this.mContext = context;

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {

                        //Get variables
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();

                        //Check if it's a validation or Ticket
                        String msgBody = msgs[i].getMessageBody();
                        if (isValidation(msgBody, msg_from)) {
                            //Send validation to backend
                            sendValidationToBackend(msgBody, msg_from);
                            Toast.makeText(this.mContext, "Incoming validation", Toast.LENGTH_SHORT).show();

                        }
                        if (isTicket(msgBody, msg_from)) {
                            //Send ticket to backend
                            sendTicketToBackend(msgBody, msg_from);
                            Toast.makeText(this.mContext, "Incoming ticket", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }

    }

    private void sendTicketToBackend(String msgBody, String msg_from) {
        SharedPreferences prefs = mContext.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);


        JsonObject json = new JsonObject();

        // Validation
        json.addProperty("msgTicket", msgBody);
        json.addProperty("msgFrom", msg_from);

        //Get the device previously generated UUID
        String UUID = prefs.getString("UUID", "");
        json.addProperty("uuid", UUID);

        Ion.with(mContext, MainActivity.BASE_URL + "/ticket")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        Log.d("Adam", "Ticket is sent to server");
                        Toast.makeText(mContext, "Ticket is sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendValidationToBackend(String msg_validation, String msg_from) {
        SharedPreferences prefs = mContext.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        JsonObject json = new JsonObject();

        // Validation message
        json.addProperty("msgfrom", msg_from);
        json.addProperty("msgvalidation", msg_validation);

        //Get the device previously generated UUID
        json.addProperty("uuid", prefs.getString("UUID", ""));

        Ion.with(mContext, MainActivity.BASE_URL + "    /ticket/validation")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d("Adam", "Validation is sent to server");

                        Toast.makeText(mContext, "Validation is sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isTicket(String msg, String msg_from) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        return msg_from.contains(prefs.getString("ticketfrom", "")) && msg.contains(prefs.getString("ticketpattern", ""));
    }


    public boolean isValidation(String msg, String msg_from) {
//        TODO: Figure out validation messages in cities
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        return msg_from.contains(prefs.getString("ticketfrom", "")) && msg.contains(prefs.getString("ticketpattern", ""));
        return msg.contains("validation");
    }


}