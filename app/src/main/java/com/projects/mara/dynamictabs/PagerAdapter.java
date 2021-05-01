package com.projects.mara.dynamictabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Yashasvi on 23-06-2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    ArrayList<String> array;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public void setArray(ArrayList<String> arr){
        array = new ArrayList<>();
        array = arr;
    }
    @Override
    public Fragment getItem(int position) {

        WebFragment webFragment = new WebFragment();
        webFragment.setURL(array.get(position));
        return webFragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
