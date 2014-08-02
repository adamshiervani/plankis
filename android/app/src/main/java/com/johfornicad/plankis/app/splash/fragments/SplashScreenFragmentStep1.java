package com.johfornicad.plankis.app.splash.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johfornicad.plankis.app.R;
import com.johfornicad.plankis.app.utils.Typefaces;

/**
 * Created by adam on 18.07.14.
 */ // Instances of this class are fragments representing a single
// object in our collection.
public class SplashScreenFragmentStep1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_splash_step_1, container, false);
        //Style the App name
        TextView appName = (TextView) rootView.findViewById(R.id.splash_app_name);
        TextView slide_to_setup = (TextView) rootView.findViewById(R.id.splash_instructions_swipe);


        //Get and Set qwigley
        Typeface qwigley = Typefaces.get(getActivity(), "fonts/Qwigley-Regular.ttf");
        appName.setTypeface(qwigley);

        // Get and Set Lato
        Typeface lato = Typefaces.get(getActivity(), "fonts/Lato-Lig.ttf");
        slide_to_setup.setTypeface(lato);


        return rootView;
    }
}
