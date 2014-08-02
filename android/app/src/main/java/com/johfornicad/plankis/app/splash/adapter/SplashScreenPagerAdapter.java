package com.johfornicad.plankis.app.splash.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.johfornicad.plankis.app.splash.fragments.SplashScreenFragmentStep1;
import com.johfornicad.plankis.app.splash.fragments.SplashScreenFragmentStep2;
import com.johfornicad.plankis.app.splash.fragments.SplashScreenFragmentStep3;
import com.johfornicad.plankis.app.splash.fragments.SplashScreenFragmentStep4;

/**
 * Created by adam on 18.07.14.
 */ // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class SplashScreenPagerAdapter extends FragmentStatePagerAdapter {
    public SplashScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;


        switch (i){
            case 0:
                fragment =  new SplashScreenFragmentStep1();
            break;
            case 1:
                fragment =  new SplashScreenFragmentStep2();
            break;
            case 2:
                fragment =  new SplashScreenFragmentStep3();
            break;
            case 3:
                fragment =  new SplashScreenFragmentStep4();
            break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
