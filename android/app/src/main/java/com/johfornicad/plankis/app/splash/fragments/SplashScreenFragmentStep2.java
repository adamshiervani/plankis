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
 */
public class SplashScreenFragmentStep2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_splash_step_2, container, false);

        TextView subText = (TextView) rootView.findViewById(R.id.splash_screen_2_sub_text);

        // Get and Set Lato
        Typeface lato = Typefaces.get(getActivity(), "fonts/Lato-Lig.ttf");
        subText.setTypeface(lato);

        return rootView;
    }
}
