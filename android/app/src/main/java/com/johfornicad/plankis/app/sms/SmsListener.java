package com.johfornicad.plankis.app.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
                        if (isValidation(msgBody)) {
                            //Send validation to backend
                            sendValidationToBackend(msgBody, msg_from);
                        }
                        if (isTicket(msgBody)) {
                            //Send ticket to backend
                            sendTicketToBackend(msgBody, msg_from);
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

        Ion.with(mContext, "http://192.168.56.1:3000/ticket")
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

        Ion.with(mContext, "http://192.168.56.1:3000/ticket/validation")
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

    private boolean isTicket(String msg) {
        //TODO: Implement way to compare with THAT specific cities validation ticket
        return msg.contains("Ticket");

    }


    public boolean isValidation(String msgBody) {
        //TODO: Implement way to compare with THAT specific cities validation ticket
        return msgBody.contains("Validation");
    }


}