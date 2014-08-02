package com.johfornicad.plankis.app.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.johfornicad.plankis.app.MainActivity;
import com.johfornicad.plankis.app.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

//TODO: Inherit interface.
public class TicketFragment extends Fragment {
    public static View view;

    public TicketFragment() {
    }

    public static TicketFragment newInstance() {
        return new TicketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket,
                container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        //Get the device previously generated UUID
        Ion.with(getActivity(), MainActivity.BASE_URL +"/ticket/" + prefs.getString("UUID", ""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result.has("status")) {
                            Log.d("Adam", "Buyticket");
                            buyTicketFromProvider();
                        } else {
                            Log.d("Adam", result + "");
                            Toast.makeText(getActivity(), "Found ticket. It can be found in Sms app", Toast.LENGTH_SHORT).show();

                            setContent(result.get("from").getAsString(), result.get("message").getAsString());
                        }
                    }
                });

        return view;
    }

    private void buyTicketFromProvider() {
        //TODO: Buy ticket from provider.Automate city detection
//        SmsManager smsManager = SmsManager.getDefault();

//        smsManager.sendTextMessage("+467377747780", null, "I want a ticket from provider!", null, null);
        SmsManager sms = SmsManager.getDefault();
        PendingIntent sentPI;
        String SENT = "SMS_SENT";

        sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT), 0);

        sms.sendTextMessage("+46737747780", null, "Ticket", sentPI, null);
        Toast.makeText(getActivity(), "Bought Ticket", Toast.LENGTH_SHORT).show();

        //When the ticket is incoming, the smslistener will read it and send to backend
    }

    public void setContent(String from, String content) {

        if (from != null && content != null) {
            TextView txtMsg = (TextView) (view != null ? view.findViewById(R.id.msg) : null);
            if (txtMsg != null) {
                txtMsg.setText("From:" + from + "/=====/ Content: " + content);
            }

            Toast.makeText(getActivity(), "Content: " + content, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "From: " + from, Toast.LENGTH_SHORT).show();

            ContentValues values = new ContentValues();
            values.put("address", from);
            values.put("body", content);


            getActivity().getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



}
