package com.yzz.sevenp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Play_Gift_Pager_Adp extends PagerAdapter{

    private List<View> gridViewList;

    public Play_Gift_Pager_Adp(List<View> gridViewList) {

        this.gridViewList = gridViewList;
    }

    @Override
    public int getCount() {
        return gridViewList == null?0:gridViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(gridViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(gridViewList.get(position));
        return gridViewList.get(position);
    }
}
