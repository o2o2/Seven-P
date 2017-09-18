package com.yzz.sevenp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.api.CreateApi;
import com.yzz.sevenp.api.UpDateStatusApi;
import com.yzz.sevenp.bean.Create;
import com.yzz.sevenp.bean.UpDateStatus;
import com.yzz.sevenp.network.RetrofitManager;
import com.yzz.sevenp.utils.PreferenceUtils;

import java.io.File;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends BaseActivity {

    @BindView(R.id.camera_preview)
    GLSurfaceView mCameraPreview;
    @BindView(R.id.switch_cam)
    ImageView switchCam;
    @BindView(R.id.flash)
    ImageView flash;
    @BindView(R.id.exposure)
    ImageView exposure;
    @BindView(R.id.camera_et)
    EditText cameraEt;
    @BindView(R.id.camera_bt)
    Button cameraBt;
    @BindView(R.id.camera_close)
    ImageView cameraClo;
    @BindView(R.id.camera_show)
    Button cameraShow;
    @BindView(R.id.root)
    RelativeLayout root;
    private KSYStreamer mStreamer;
    private boolean mIsFlashOpened = false;
    private boolean STATE;
    String livetitle;
    String live_id;
    private int status = 1;
    private Uri imageUri;
    private String mLogoPath = "asdf";

    //调用系统相册-选择图片
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private String thumbnail_pic = "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123925693.jpg";//小图地址
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        state = getIntent().getIntExtra("state",1);
        Log.d("sss", "state++++++" + state);
        if (state == 0) {
            cameraEt.setVisibility(View.GONE);
            cameraShow.setVisibility(View.VISIBLE);
        } else if (state ==1) {
            cameraEt.setVisibility(View.VISIBLE);
            cameraShow.setVisibility(View.GONE);
        }
        initData();
        initInfo();

    }

    public void onContinueLive(){

        cameraBt.setVisibility(View.GONE);

        cameraClo.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        live_id = intent.getStringExtra("Id");
        Log.d("Id","+++++++++++++++"+live_id);
        mStreamer.setUrl("rtmp://uplive.geekniu.com/live/"+live_id);
        mStreamer.startStream();
        updateStatus(live_id);

    }

    //开启直播
    private void
    onStartLive() {
        livetitle = cameraEt.getText().toString().trim();
        if (TextUtils.isEmpty(livetitle)) {
            Toast.makeText(this, "请输入直播标题!", Toast.LENGTH_SHORT).show();
        } else {
            cameraBt.setVisibility(View.GONE);
            cameraEt.setVisibility(View.GONE);
            cameraClo.setVisibility(View.VISIBLE);

            Log.d("PreferenceUtil", "" + PreferenceUtils.getJsessionId());
            final CreateApi createApi = RetrofitManager.getTestRetrofit().create(CreateApi.class);
            FormBody formBody = new FormBody.Builder()
                    .add("uid", PreferenceUtils.getJsessionId())
                    .add("pic", thumbnail_pic)
                    .add("live_name", livetitle)
                    .add("live_type", String.valueOf(new Random().nextInt(2)))
                    .build();
            Call<Create> CreateCall = createApi.postCreate(formBody);
            CreateCall.enqueue(new Callback<Create>() {
                @Override
                public void onResponse(Call<Create> call, Response<Create> response) {
                    live_id = String.valueOf(response.body().getResult().getId());
//                Log.d("live_id",live_id);
                    //开始推流
//                 设置推流url（需要向相关人员申请，测试地址并不稳定！）
                    mStreamer.setUrl("rtmp://uplive.geekniu.com/live/" + live_id);
                    mStreamer.startStream();
                    updateStatus(live_id);
                }

                @Override
                public void onFailure(Call<Create> call, Throwable t) {
                    Toast.makeText(CameraActivity.this, "创建直播失败了，重新创建一下吧。", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void updateStatus(String live_id) {

        if (status == 1) {
            final UpDateStatusApi upDateStatusApi = RetrofitManager.getTestRetrofit().create(UpDateStatusApi.class);
            FormBody formBody = new FormBody.Builder()
                    .add("live_id", live_id)
                    .add("status", "0")
                    .build();
            Call<UpDateStatus> upDateStatusCall = upDateStatusApi.postUpDateStatus(formBody);
            upDateStatusCall.enqueue(new Callback<UpDateStatus>() {
                @Override
                public void onResponse(Call<UpDateStatus> call, Response<UpDateStatus> response) {
                    status = response.body().getResult().get(0).getData().getStatus();
                }

                @Override
                public void onFailure(Call<UpDateStatus> call, Throwable t) {

                }
            });
        } else if (status == 0) {
            final UpDateStatusApi upDateStatusApi = RetrofitManager.getTestRetrofit().create(UpDateStatusApi.class);
            FormBody formBody = new FormBody.Builder()
                    .add("live_id", live_id)
                    .add("status", "1")
                    .build();
            Call<UpDateStatus> upDateStatusCall = upDateStatusApi.postUpDateStatus(formBody);
            upDateStatusCall.enqueue(new Callback<UpDateStatus>() {
                @Override
                public void onResponse(Call<UpDateStatus> call, Response<UpDateStatus> response) {
                    status = response.body().getResult().get(0).getData().getStatus();
                }

                @Override
                public void onFailure(Call<UpDateStatus> call, Throwable t) {

                }
            });
        }
    }

    public void setAvatar() {
        String[] choice = new String[]{"相机", "相册", "取消"};
        new AlertDialog.Builder(CameraActivity.this)
                .setTitle("请选择你的直播封面")
                .setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //拍照选择
                                chooseFromCamera();
                                break;
                            case 1:
                                //从相册选取
                                chooseFromGallery();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .show();
    }

    //退出
    public void onStopLive() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定结束本次直播？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                updateStatus(live_id);
                //结束推流
                mStreamer.stopStream();
                cameraBt.setVisibility(View.VISIBLE);
                cameraEt.setVisibility(View.VISIBLE);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    // 从相册选择图片
    private void chooseFromGallery() {

        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         */
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, GALLERY_CODE);
    }

    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
        // 来自相册

    }

    //美颜方法
    private void onBeautyChecked() {
        if (!STATE) {
            //设置美颜
            mStreamer.getImgTexFilterMgt().setFilter(
                    mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO);
            Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
            if (parameters != null) {
                int minValue = parameters.getMinExposureCompensation();
                int maxValue = parameters.getMaxExposureCompensation();
                int range = 100 / (maxValue - minValue);
                parameters.setExposureCompensation(12);
            }
            mStreamer.getCameraCapture().setCameraParameters(parameters);
            parameters.setExposureCompensation(10);
            mStreamer.getCameraCapture().setCameraParameters(parameters);
            List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
            final ImgFilterBase filter = filters.get(0);
            filter.setGrindRatio(99 / 100.f);
            filter.setWhitenRatio(99);
            filter.setRuddyRatio(2 / 50.f - 1.0f);

            final ImgFilterBase filter1 = filters.get(0);
            filter1.setGrindRatio(80 / 100.f);
            filter1.setWhitenRatio(80);
            filter1.setRuddyRatio(0 / 50.f - 1.0f);


        } else {
            //关闭美颜
            List<ImgFilterBase> filters1 = mStreamer.getImgTexFilterMgt().getFilter();
            final ImgFilterBase filter3 = filters1.get(0);
            filter3.setGrindRatio(10 / 100.f);
            filter3.setWhitenRatio(10);
            filter3.setRuddyRatio(0 / 50.f - 1.0f);
            Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
            parameters.setExposureCompensation(-2);
            mStreamer.getCameraCapture().setCameraParameters(parameters);

        }
        STATE = !STATE;
    }

    //开启闪光灯方法
    private void onFlashClick() {
        if (mIsFlashOpened) {
            mStreamer.toggleTorch(false);
            mIsFlashOpened = false;
        } else {
            mStreamer.toggleTorch(true);
            mIsFlashOpened = true;
        }
    }

    //翻转摄像头
    @Override
    public void onResume() {
        super.onResume();
        // 一般可以在onResume中开启摄像头预览
        mStreamer.startCameraPreview();
        // 调用KSYStreamer的onResume接口
        mStreamer.onResume();
        // 如果onPause中切到了DummyAudio模块，可以在此恢复
        mStreamer.setUseDummyAudioCapture(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
        // 一般在这里停止摄像头采集
        mStreamer.stopCameraPreview();
        // 如果希望App切后台后，停止录制主播端的声音，可以在此切换为DummyAudio采集，
        // 该模块会代替mic采集模块产生静音数据，同时释放占用的mic资源
        mStreamer.setUseDummyAudioCapture(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清理相关资源
        mStreamer.release();
    }


    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_camera;
    }


    private void initData() {
        // 创建KSYStreamer实例
        mStreamer = new KSYStreamer(this);
// 设置预览View
        mStreamer.setDisplayPreview(mCameraPreview);
// 设置推流url（需要向相关人员申请，测试地址并不稳定！）
        mStreamer.setUrl("rtmp://test.uplive.ksyun.com/live/{streamName}");
// 设置预览分辨率, 当一边为0时，SDK会根据另一边及实际预览View的尺寸进行计算
        mStreamer.setPreviewResolution(480, 0);
// 设置推流分辨率，可以不同于预览分辨率（不应大于预览分辨率，否则推流会有画质损失）
        mStreamer.setTargetResolution(480, 0);
// 设置预览帧率
        mStreamer.setPreviewFps(15);
// 设置推流帧率，当预览帧率大于推流帧率时，编码模块会自动丢帧以适应设定的推流帧率
        mStreamer.setTargetFps(15);
// 设置视频码率，分别为初始平均码率、最高平均码率、最低平均码率，单位为kbps，另有setVideoBitrate接口，单位为bps
        mStreamer.setVideoKBitrate(600, 800, 400);
// 设置音频采样率
        mStreamer.setAudioSampleRate(44100);
// 设置音频码率，单位为kbps，另有setAudioBitrate接口，单位为bps
        mStreamer.setAudioKBitrate(48);
/**
 * 设置编码模式(软编、硬编)，请根据白名单和系统版本来设置软硬编模式，不要全部设成软编或者硬编,白名单可以联系金山云商务:
 * StreamerConstants.ENCODE_METHOD_SOFTWARE
 * StreamerConstants.ENCODE_METHOD_HARDWARE
 */
        mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
// 设置屏幕的旋转角度，支持 0, 90, 180, 270
        mStreamer.setRotateDegrees(0);
// 设置开始预览使用前置还是后置摄像头
        mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);


        // 设置Info回调，可以收到相关通知信息
        mStreamer.setOnInfoListener(new KSYStreamer.OnInfoListener() {
            @Override
            public void onInfo(int what, int msg1, int msg2) {
            }
        });
        // 设置错误回调，收到该回调后，一般是发生了严重错误，比如网络断开等，
        // SDK内部会停止推流，APP可以在这里根据回调类型及需求添加重试逻辑。
        mStreamer.setOnErrorListener(new KSYStreamer.OnErrorListener() {
            @Override
            public void onError(int what, int msg1, int msg2) {

            }
        });

        showWaterMark();
        // 切换前后摄像头
        mStreamer.switchCamera();
        // 开关闪光灯
        mStreamer.toggleTorch(true);
        setAvatar();
    }


    private void showWaterMark() {
        mStreamer.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
        mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.WHITE, 1.0f);
    }

    private void initInfo() {
// 设置Info回调，可以收到相关通知信息
        mStreamer.setOnInfoListener(new KSYStreamer.OnInfoListener() {
            @Override
            public void onInfo(int what, int msg1, int msg2) {
                // ...
            }
        });
// 设置错误回调，收到该回调后，一般是发生了严重错误，比如网络断开等，
// SDK内部会停止推流，APP可以在这里根据回调类型及需求添加重试逻辑。
        mStreamer.setOnErrorListener(new KSYStreamer.OnErrorListener() {
            @Override
            public void onError(int what, int msg1, int msg2) {
                // ...
            }
        });
    }

    private void initLister() {
        mStreamer.startStream();


// 开关闪光灯
        mStreamer.toggleTorch(true);
// 设置美颜滤镜，关于美颜滤镜的具体说明请参见专题说明
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE);

        mStreamer.stopStream();
    }

    @OnClick({R.id.switch_cam, R.id.flash, R.id.exposure, R.id.camera_bt, R.id.camera_close,R.id.camera_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_cam:
                // 切换前后摄像头
                mStreamer.switchCamera();
                break;
            case R.id.flash:
                //开启闪光灯
                onFlashClick();
                break;
            case R.id.exposure:
                //开启美颜
                onBeautyChecked();
                break;
            case R.id.camera_bt:
                //开启直播
                onStartLive();
                break;
            case R.id.camera_close:
                //结束直播
                onStopLive();
                break;
            case R.id.camera_show:
                if(state == 0){
                    cameraShow.setVisibility(View.GONE);
                    onContinueLive();
                }else if(state ==1){
                    onStartLive();
                }
                break;
        }
    }
}
