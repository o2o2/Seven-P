package com.yzz.sevenp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Wookeibun on 2017/8/16.
 */

public class LiveHomeAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> alist;


    public LiveHomeAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        alist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return alist.get(position);

    }

    @Override
    public int getCount() {
        return alist.size();

    }
}
