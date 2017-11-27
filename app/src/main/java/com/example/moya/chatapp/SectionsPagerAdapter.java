package com.example.moya.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Moya on 10/21/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return (new ChatFragment());
            case 1:
                return (new BrowseFragment());
            case 2:
                return (new ProfileFragment());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position)
        {
            case 0:
                return "Chat";
            case 1:
                return "Browse";
            case 2:
                return "Profile";
            default:
                return null;
        }
    }
}
