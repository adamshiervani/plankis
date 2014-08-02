package com.johfornicad.plankis.app.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.johfornicad.plankis.app.R;
import com.johfornicad.plankis.app.utils.Typefaces;

import static java.lang.System.currentTimeMillis;

public class ReportAdapter extends BaseAdapter {
    private final Context context;
    private final JsonArray content;

    public ReportAdapter(Context context, JsonArray jsonArray) {
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
        TextView timestampTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.time) : null);
        TextView rankTxt = (TextView) (rowView != null ? rowView.findViewById(R.id.rank) : null);

        // Set custom fonts on all the textfield
        Typeface latoLight = Typefaces.get(this.context, "fonts/Lato-Lig.ttf");
        Typeface latoReg = Typefaces.get(this.context, "fonts/Lato-Reg.ttf");
        Typeface latoBold = Typefaces.get(this.context, "fonts/Lato-Bol.ttf");



        //Check if the components are null
        if (stationTxt != null &&
                areaTxt != null &&
                timestampTxt != null
            ) {

            // Set fonts
            areaTxt.setTypeface(latoLight);
            stationTxt.setTypeface(latoReg);
            timestampTxt.setTypeface(latoReg);
            rankTxt.setTypeface(latoReg);


            //Declare the Station and area
            String station = content.get(position).getAsJsonObject().get("station").getAsString();
            String area = content.get(position).getAsJsonObject().get("area").getAsString();
            long timestamp = content.get(position).getAsJsonObject().get("timestamp").getAsLong();
            String rank = content.get(position).getAsJsonObject().get("rank").getAsString();


            //Set the text
            stationTxt.setText(station);
            areaTxt.setText(area);
            areaTxt.setText(area);
            timestampTxt.setText(readableTimestamp(timestamp));
            rankTxt.setText("Rank " + rank);
        }

        return rowView;
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
