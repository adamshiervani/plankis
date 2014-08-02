package com.johfornicad.plankis.app.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.johfornicad.plankis.app.MainActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ReportView extends ListFragment {


    public ReportView() {
    }

    public static ReportView newInstance() {
        return new ReportView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Ion.with(getActivity(), MainActivity.BASE_URL + "/report?city=" + prefs.getString("city", ""))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        //TODO: Check if the result is null. Nullpoiner. Toast for not any connection
                        String station, area, timestamp, id, rank;

                        JsonArray jsonArray = new JsonArray();

                        //TODO: When the backend only send the correct vales, you dont have to create a new JSON array. Just pass it over to the adapter
                        for (int i = 0; i < result.size(); i++) {
                            station = result.get(i).getAsJsonObject().get("station").getAsString();
                            area = result.get(i).getAsJsonObject().get("area").getAsString();
                            id = result.get(i).getAsJsonObject().get("_id").getAsString();
                            timestamp = result.get(i).getAsJsonObject().get("timestamp").getAsString();
                            rank = result.get(i).getAsJsonObject().get("rank").getAsString();

                            JsonObject json = new JsonObject();

                            // Location
                            json.addProperty("station", station);
                            json.addProperty("area", area);
                            json.addProperty("timestamp", timestamp);
                            json.addProperty("_id", id);
                            json.addProperty("rank", rank);

                            jsonArray.add(json);
                            Log.d("Adam", "Adam");
                        }

                        ReportAdapter adapter = new ReportAdapter(getActivity(), jsonArray);
                        setListAdapter(adapter);

                    }
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
        view.requestLayout();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
