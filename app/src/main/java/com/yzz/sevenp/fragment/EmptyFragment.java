package com.yzz.sevenp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yzz.sevenp.R;
import com.yzz.sevenp.fragment.base.BaseFragment;


/**
 * Created by Wookeibun on 2017/8/21.
 */

public class EmptyFragment extends BaseFragment {




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }





    public static EmptyFragment getFragment() {
        return new EmptyFragment();
    }

    @Override
    protected int getContentResId() {
        return R.layout.frg_empty;
    }

}
