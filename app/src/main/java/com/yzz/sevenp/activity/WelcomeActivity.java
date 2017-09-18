package com.yzz.sevenp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.utils.PreferenceUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {


    Handler hand = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 0x111){
                Log.d("getJsessionId(),", PreferenceUtils.getJsessionId());
                if (PreferenceUtils.getJsessionId().equals("0")) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, LiveHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    };


    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                hand.sendEmptyMessage(0x111);
            }

        },3000);
    }
}
