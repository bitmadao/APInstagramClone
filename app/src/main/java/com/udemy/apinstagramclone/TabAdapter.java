package com.udemy.apinstagramclone;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                return  new ProfileTab();

            case 1:
                return new UsersTab();

            case 2:
                return new SharePictureTab();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Profile"; // TODO add to strings.xml

            case 1:
                return "Users";// TODO add to strings.xml

            case 2:
                return "Share Picture";// TODO add to strings.xml

            default:
                return null;

        }
    }
}