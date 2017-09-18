package com.yzz.sevenp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ChoiceAndHotAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> alist;
    private String[] title;
    public ChoiceAndHotAdapter(FragmentManager fm,ArrayList<Fragment> list,String[] titles) {
        super(fm);
        alist = list;
        title = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return alist.get(position);
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
