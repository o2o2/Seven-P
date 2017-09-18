package com.yzz.sevenp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzz.sevenp.R;
import com.yzz.sevenp.adapter.ChoiceAndHotAdapter;

import java.util.ArrayList;

/**
 * Created by Wookeibun on 2017/8/16.
 */

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        String[] titles = new String[]{"精选","热门"};

        ArrayList<Fragment> list = new ArrayList<>();
        ChoiceFragment choiceFragment = new ChoiceFragment();
        HotFragment hotFragment = new HotFragment();
        list.add(choiceFragment);
        list.add(hotFragment);

        viewPager.setAdapter(new ChoiceAndHotAdapter(getChildFragmentManager(),list,titles));

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
