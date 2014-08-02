package com.johfornicad.plankis.app.splash.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.johfornicad.plankis.app.MainActivity;
import com.johfornicad.plankis.app.R;
import com.johfornicad.plankis.app.splash.SplashScreen;
import com.johfornicad.plankis.app.utils.Typefaces;

/**
 * Created by adam on 18.07.14.
 */
public class SplashScreenFragmentStep3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_splash_step_3, container, false);



        TextView top_text = (TextView) rootView.findViewById(R.id.top_text_explain);
        TextView middle_text = (TextView) rootView.findViewById(R.id.middle_text_action);
        TextView sub_text = (TextView) rootView.findViewById(R.id.sub_text);


        // Get and Set Lato
        Typeface lato = Typefaces.get(getActivity(), "fonts/Lato-Lig.ttf");
        top_text.setTypeface(lato);
        middle_text.setTypeface(lato);
        sub_text.setTypeface(lato);

        return rootView;
    }
}
