package com.yzz.sevenp.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.fragment.EmptyFragment;
import com.yzz.sevenp.fragment.FunFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerVideoActivity extends BaseActivity {
    private static final String TAG = "PlayerVideoActivity";
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;
    private Context mContext;
    @BindView(R.id.play_vp)
    ViewPager vpPlay;
    @BindView(R.id.texture_view)
    KSYTextureView mVideoView;
    Long id;
    String live_name;
    String sign;
    String avatar;
    int status;
    Intent intent;
    private List<Fragment> listFragment;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            Log.d("VideoPlayer", "OnPrepared");
            mVideoWidth = mVideoView.getVideoWidth();
            mVideoHeight = mVideoView.getVideoHeight();
            // Set Video Scaling Mode
            mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            //start player
            mVideoView.start();

        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {

        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (width != mVideoWidth || height != mVideoHeight) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();

                    if (mVideoView != null)
                        mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
            }
        }
    };

    public IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG, "Buffering Start.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG, "Buffering End.");
                    break;
                case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Toast.makeText(mContext, "Audio Rendering Start", Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Toast.makeText(mContext, "Video Rendering Start", Toast.LENGTH_SHORT).show();
                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                    Toast.makeText(mContext, "Succeed to reload video.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Succeed to mPlayerReload video.");
                    return false;
            }
            return false;
        }
    };

    private IMediaPlayer.OnMessageListener mOnMessageListener = new IMediaPlayer.OnMessageListener() {
        @Override
        public void onMessage(IMediaPlayer iMediaPlayer, String name, String info, double number) {
            Log.e(TAG, "name:" + name + ",info:" + info + ",number:" + number);
        }
    };

    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_player_video;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this.getApplicationContext();
        mVideoView.setKeepScreenOn(true);
//        mVideoView.setOnTouchListener(mTouchListener);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnSeekCompleteListener(mOnSeekCompletedListener);
        mVideoView.setOnMessageListener(mOnMessageListener);
        mVideoView.setScreenOnWhilePlaying(true);
        intent = getIntent();
        status = intent.getIntExtra("status",1);
        id = intent.getLongExtra("id",0);


        try {
            if (status == 0) {
                mVideoView.setDataSource("rtmp://rtmplive.geekniu.com/live/"+id);
            }else if(status == 1){
                mVideoView.setDataSource("rtmp://live.hkstv.hk.lxdns.com/live/hks");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mVideoView.prepareAsync();

        initVariable();
    }




    public Long getId(){
        return id;
    }

    public  String getpic(){
        avatar = intent.getStringExtra("avatar");
        return avatar;
    }

    public String getsign(){
        sign = intent.getStringExtra("sign");
        return sign;
    }

    public String getlive_name(){
        live_name = intent.getStringExtra("live_name");
        return live_name;
    }

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompletedListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.e(TAG, "onSeekComplete");
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            switch (what) {
                //case KSYVideoView.MEDIA_ERROR_UNKNOWN:
                // Log.e(TAG, "OnErrorListener, Error Unknown:" + what + ",extra:" + extra);
                //  break;
                default:
                    Log.e(TAG, "OnErrorListener, Error:" + what + ",extra:" + extra);
            }
            videoPlayEnd();
            return false;
        }
    };


    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            Toast.makeText(mContext, "OnCompletionListener, play complete.", Toast.LENGTH_LONG).show();
            videoPlayEnd();
        }
    };

    private void videoPlayEnd() {
        if (mVideoView != null) {
            mVideoView.release();
            mVideoView = null;
        }
        finish();
    }

    private void videostop(){
        if(mVideoView ==null){

        }else{
            mVideoView.stop();
        }

    }

    private void initVariable() {
        EmptyFragment emptyFragment = new EmptyFragment();
        FunFragment funFragment = new FunFragment();

        listFragment = new ArrayList<>();
        listFragment.add(emptyFragment);
        listFragment.add(funFragment);
        vpPlay.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        vpPlay.setCurrentItem(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videostop();
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }
    }
}
