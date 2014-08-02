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
import com.johfornicad.plankis.app.utils.Typefaces;

/**
 * Created by adam on 18.07.14.
 */
public class SplashScreenFragmentStep4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_splash_step_4, container, false);

        TextView subText = (TextView) rootView.findViewById(R.id.splash_screen_4_sub_text);

        // Get and Set Lato
        Typeface lato = Typefaces.get(getActivity(), "fonts/Lato-Lig.ttf");
        subText.setTypeface(lato);

        Button btn = (Button) rootView.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(myIntent);

                return;
            }
        });


        return rootView;
    }
}
