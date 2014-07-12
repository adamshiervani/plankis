package com.johfornicad.plankis.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.johfornicad.plankis.app.R;

import static java.lang.System.currentTimeMillis;

public class FeedAdapter extends BaseAdapter {
    private final Context context;
    private final JsonArray content;

    public FeedAdapter(Context context, JsonArray jsonArray) {
        this.context = context;
        this.content = jsonArray;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position).getAsJsonObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //Get Layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.feed_row_layout, parent, false);

        //Initialize the view components
        TextView stationTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.station) : null);
        TextView areaTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.area) : null);
        TextView stationPicTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.stationPic) : null);
        TextView timestampTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.time) : null);

        //TODO: Think of if ranking should exist, if so how do we remove spam. Maybe only ability to report if in area.
        TextView rankTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.rank) : null);


        //Check if the components are null
        if (stationTxt != null &&
                areaTxt != null &&
                stationPicTxt != null &&
                timestampTxt != null &&
                rankTxt != null) {

            //Declare the Station and area
            String station = content.get(position).getAsJsonObject().get("station").getAsString();
            String area = content.get(position).getAsJsonObject().get("area").getAsString();
            long timestamp = content.get(position).getAsJsonObject().get("timestamp").getAsLong();
            final String id = content.get(position).getAsJsonObject().get("_id").getAsString();
            String rank = content.get(position).getAsJsonObject().get("rank").getAsString();


            //Set the text
            stationTxt.setText(station);
            areaTxt.setText(area);
            areaTxt.setText(area);
            stationPicTxt.setText(getFirstCharacter(area));
            timestampTxt.setText(readableTimestamp(timestamp));
            rankTxt.setText("Confirmed " + rank + " times");
        }

        return rowView;
    }

    public String getFirstCharacter(String area) {

        Boolean match = false;
        int i = 0;
        String firstChar = "";

        //Get the first character but not S and U
        // The reason for this, is we use the station and not area, a lot of station begins with S/U
        //TODO: Check for funky characters, ie. slash, dot etc..
        while (!match) {
            if (!(area.charAt(i) + "").equals("S") || !(area.charAt(i) + "").equals("S")) {
                firstChar = area.charAt(i) + "";
                match = true;
            }
            i++;
        }
        return firstChar;
    }

    public String readableTimestamp(long time) {
        //The differene between the post date and now;
        long diff = currentTimeMillis() - time;

        //Convert to minutes and double due to Math.ceil()
        double ago = diff / 60000;

        //Round up
        ago = Math.ceil(ago);
        int agoInt = ((int) ago);

        //If the difference was less or equal to 1 minute , the type Just now
        if (agoInt <= 1) {
            return "Just now";
        }

        //Return the nearest minutes ago
        return agoInt + " minutes ago";
    }
}
