package com.yzz.sevenp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.adapter.LiveHomeAdapter;
import com.yzz.sevenp.fragment.HomeFragment;
import com.yzz.sevenp.fragment.MoreFragment;

import java.util.ArrayList;

public class LiveHomeActivity extends BaseActivity {
    private ViewPager vp_home;
    private RadioGroup rg_home;
    private RadioButton rb_home_live;
    private ImageView iv_home_photo;
    private RadioButton rb_home_more;
    private HomeFragment homeFragment;
    private MoreFragment moreFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        //初始化控件
        initView();

        //初始化数据
        initData();

    }

    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    private void initData() {
        //获取管理工具
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        //实例化fragment
        homeFragment = new HomeFragment();
        moreFragment = new MoreFragment();

        ArrayList<Fragment> list = new ArrayList<>();
        list.add(homeFragment);
        list.add(moreFragment);

        rg_home.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_home_live:
                        vp_home.setCurrentItem(0);
                        break;
                    case R.id.rb_home_more:
                        vp_home.setCurrentItem(1);
                        break;
                }
            }
        });

        LiveHomeAdapter homeAdapter = new LiveHomeAdapter(getSupportFragmentManager(), list);
        vp_home.setAdapter(homeAdapter);

        rg_home.check(R.id.rb_home_live);

        vp_home.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               switch (position){
                    case 0:
                        rg_home.check(R.id.rb_home_live);
                        break;
                    case 1:
                        rg_home.check(R.id.rb_home_more);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iv_home_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LiveHomeActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        vp_home = (ViewPager) findViewById(R.id.vp_home);
        rg_home = (RadioGroup) findViewById(R.id.rg_home);
        rb_home_live = (RadioButton) findViewById(R.id.rb_home_live);
        iv_home_photo = (ImageView) findViewById(R.id.iv_home_photo);
        rb_home_more = (RadioButton) findViewById(R.id.rb_home_more);
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_live_home;
    }

}

