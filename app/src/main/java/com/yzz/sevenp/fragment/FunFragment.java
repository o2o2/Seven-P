package com.yzz.sevenp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.PlayerVideoActivity;
import com.yzz.sevenp.adapter.Play_Gift_Grid_Adp;
import com.yzz.sevenp.adapter.Play_Gift_Pager_Adp;
import com.yzz.sevenp.adapter.Play_Recycler_Hor;
import com.yzz.sevenp.adapter.Play_Recycler_Ver;
import com.yzz.sevenp.bean.ChatContent;
import com.yzz.sevenp.bean.Gift;
import com.yzz.sevenp.fragment.base.BaseFragment;
import com.yzz.sevenp.heart.HeartLayout;
import com.yzz.sevenp.utils.PreferenceUtils;
import com.yzz.sevenp.utils.TimeUtil;

import org.dync.giftlibrary.widget.CustormAnim;
import org.dync.giftlibrary.widget.GiftControl;
import org.dync.giftlibrary.widget.GiftModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.hyphenate.EMError.EM_NO_ERROR;
import static com.hyphenate.EMError.USER_ALREADY_EXIST;
import static com.hyphenate.EMError.USER_ALREADY_LOGIN;
import static com.hyphenate.EMError.USER_AUTHENTICATION_FAILED;
import static com.hyphenate.EMError.USER_REG_FAILED;


/**
 * Created by Wookeibun on 2017/8/21.
 */

public class FunFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.play_ll_person_info)
    LinearLayout playLlPersonInfo;
    @BindView(R.id.play_recycler_hor)
    RecyclerView playRecyclerHor;
    @BindView(R.id.play_tv_number)
    TextView playTvNumber;
    @BindView(R.id.play_tv_duration)
    Chronometer playTvDuration;
    @BindView(R.id.play_tv_date)
    TextView playTvDate;
    @BindView(R.id.play_recycler_ver)
    RecyclerView playRecyclerVer;
    @BindView(R.id.play_fun_iv_pub)
    ImageView playFunIvPub;
    @BindView(R.id.play_fun_iv_gift)
    ImageView playFunIvGift;
    @BindView(R.id.play_fun_iv_music)
    ImageView playFunIvMusic;
    @BindView(R.id.play_fun_iv_close)
    ImageView playFunIvClose;
    @BindView(R.id.play_fun_rl)
    RelativeLayout playFunRl;
    Unbinder unbinder;
    PlayerVideoActivity playerVideoActivity;

    GridView gridView;
    PopupWindow window;
    ViewPager viewPager;
    View popupView;
    GiftControl giftControl;

    List<Gift.GiftListBean> list;
    List<View> gridViewList;
    List<ChatContent> list0;

    Play_Gift_Pager_Adp play_gift_pager_adp;
    Play_Recycler_Ver play_recycler_ver;
    SharedPreferences sp;

    int page;
    String Et;
    @BindView(R.id.frg_pla_fun_img)
    CircleImageView frgPlaFunImg;
    @BindView(R.id.frg_pla_fun_name)
    TextView frgPlaFunName;
    @BindView(R.id.frg_pla_fun_sign)
    TextView frgPlaFunSign;
    @BindView(R.id.heart_layout)
    HeartLayout heartLayout;

    private Context mcontext;
    double pageSize = 9.0;

    @BindView(R.id.play_fun_ll)
    LinearLayout playFunLl;
    @BindView(R.id.play_fun_et)
    EditText playFunEt;
    @BindView(R.id.play_fun_bt)
    Button playFunBt;
    private ChatContent chatContent;
    private Timer mTimer;


    private boolean isPressed;

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.e("TAG", "收到消息" + messages.size());
            for (int i = 0; i < messages.size(); i++) {
                try {
                    int type = messages.get(i).getIntAttribute("type");
                    if (type == 1) {
                        String user_name = messages.get(i).getStringAttribute("user_name");
                        Log.d("user_name", user_name);
                        String content = messages.get(i).getStringAttribute("content");
                        Log.d("content", content);
                        chatContent = new ChatContent(user_name, content);
                        chatContent.setUsername(user_name);
                        chatContent.setContent(content);
                        play_recycler_ver.addData(chatContent);
                    } else if (type == 2) {
                        String user_name = messages.get(i).getStringAttribute("username");
                        Log.d("user_name", user_name);
                        String user_id = messages.get(i).getStringAttribute("user_id");
                        Log.d("user_id", user_id);
                        String avatar = messages.get(i).getStringAttribute("avatar");
                        Log.d("avatar", avatar);
                        String gift_pic = messages.get(i).getStringAttribute("gift_pic");
                        Log.d("gift_pic", gift_pic);
                        String gift_id = messages.get(i).getStringAttribute("gift_id");
                        Log.d("gift_id", gift_id);
                        int gift_count = messages.get(i).getIntAttribute("gift_count");

                        GiftModel giftModel = new GiftModel();
                        giftModel.setGiftId(gift_id);
                        giftModel.setGiftName(gift_id);
                        giftModel.setGiftCount(gift_count);
                        giftModel.setGiftPic(gift_pic);
                        giftModel.setGiftPrice("1");
                        giftModel.setSendUserName(user_name);
                        giftModel.setSendUserPic(avatar);
                        giftModel.setCurrentStart(false);
                        giftModel.setSendUserId(user_id);
                        giftModel.setSendGiftTime(System.currentTimeMillis());
                        giftModel.setHitCombo(0);
                        giftControl.loadGift(giftModel);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };
    private EMMessage message;

    //环信操作
    public void initIM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(PreferenceUtils.getJsessionId(), "111111");//同步方法
                    Log.d("aaaa", "注册成功" + PreferenceUtils.getJsessionId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("TAG", "" + e.getErrorCode());
                    if (e.getErrorCode() == USER_REG_FAILED) {//208:注册失败
                        Log.e("TAG", "环信账号注册失败" + PreferenceUtils.getJsessionId() + "111111");
                    }
                    if (e.getErrorCode() == USER_ALREADY_EXIST || e.getErrorCode() == EM_NO_ERROR) {//203:用户已经存在
                        Log.e("TAG", "环信账号用户已经存在" + PreferenceUtils.getJsessionId() + "111111");
                        //登录环信
                        EMClient.getInstance().login(PreferenceUtils.getJsessionId(), "111111", new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("main", "登录聊天服务器成功！");
                                //加入聊天室
                                //roomId为聊天室ID
                                EMClient.getInstance().chatroomManager()
                                        .joinChatRoom("25861179899905", new EMValueCallBack<EMChatRoom>() {
                                            @Override
                                            public void onSuccess(EMChatRoom value) {
                                                //加入聊天室成功
                                                Log.e("TAG", "加入聊天室成功");
                                                EMClient.getInstance().chatManager().addMessageListener(msgListener);

                                            }

                                            @Override
                                            public void onError(final int error, String errorMsg) {
                                                //加入聊天室失败
                                                Log.e("TAG", "加入聊天室失败" + errorMsg);
                                                Log.e("TAG", "加入聊天室失败" + error);
                                            }
                                        });
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("main", "登录聊天服务器失败！");
                                Log.d("main", "=======" + message);
                                Log.d("main", "=======" + code);
                                if (code == USER_ALREADY_LOGIN) {//200
                                    Log.e("TAG", "用户已登录");
                                } else {
                                }
                                if (code == USER_AUTHENTICATION_FAILED) {//202
                                    Log.e("TAG", "用户id或密码错误");
                                }
                            }
                        });
                    }
                }
            }

        }).start();

    }

    public static FunFragment getFragment() {
        return new FunFragment();
    }

    @Override
    protected int getContentResId() {
        return R.layout.frg_fun;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        play_recycler_ver = new Play_Recycler_Ver(mcontext, list0);
        playRecyclerHor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        playRecyclerHor.setAdapter(new Play_Recycler_Hor());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        playRecyclerVer.setLayoutManager(linearLayoutManager);
        playRecyclerVer.setAdapter(play_recycler_ver);
        playRecyclerVer.smoothScrollToPosition(list0.size());
        playFunIvPub.setOnClickListener(this);
        playFunIvGift.setOnClickListener(this);
        playFunIvMusic.setOnClickListener(this);
        playFunIvClose.setOnClickListener(this);
        playFunBt.setOnClickListener(this);
        mcontext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        playerVideoActivity = (PlayerVideoActivity) getActivity();

        showMusic();

        if (!TextUtils.isEmpty(playerVideoActivity.getpic())
                || !((PlayerVideoActivity) getActivity()).getpic().endsWith(".jpg")
                || !((PlayerVideoActivity) getActivity()).getpic().endsWith(".png")
                || !((PlayerVideoActivity) getActivity()).getpic().endsWith(".jpeg")) {
            Glide.with(getActivity()).load(((PlayerVideoActivity) getActivity()).getpic()).into(frgPlaFunImg);
        } else {
            Glide.with(getActivity()).load(R.mipmap.ic_image_loadfail).into(frgPlaFunImg);
        }
        initIM();
        frgPlaFunName.setText(((PlayerVideoActivity) getActivity()).getlive_name());
        frgPlaFunSign.setText(((PlayerVideoActivity) getActivity()).getsign());
        giftControl = new GiftControl(getActivity());
        giftControl.setGiftLayout(false, playFunLl, 3);
        playTvDuration.setBase(SystemClock.elapsedRealtime());//计时器清零
        playTvDuration.start();
        giftControl.setCustormAnim(new CustormAnim());
        playTvDate.setText(TimeUtil.getNowDate());
        sp = getActivity().getSharedPreferences("user_id", MODE_PRIVATE);
        list0 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ChatContent chatContent = new ChatContent();
            chatContent.setUsername("游照洲");
            chatContent.setContent(":666!");
            list0.add(chatContent);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_fun_iv_pub://弹出弹幕
                showEdit();
                break;
            case R.id.play_fun_iv_gift://礼物
                showgift();
                break;
            case R.id.play_fun_iv_music://点赞
                heartLayout.addHeart(randomColor());
                break;
            case R.id.play_fun_iv_close://退出
                showClosedialog();
                break;
            case R.id.play_fun_bt://发送弹幕
                add();
                break;
        }
    }

    /**
     * 随机颜色
     * @return
     */
    private int randomColor() {
        return Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
    }

    private void showMusic() {
        //每500毫秒生成一个心
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (heartLayout!= null) {
                    heartLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            heartLayout.addHeart(randomColor());
                        }
                    });
                }
            }
        }, 500, 500);

//        frameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                heartLayout.addHeart(randomColor());
//
//            }
//        });
    }

    //礼物popupwindow
    public void showgift() {
        // TODO: 2016/5/17 构建一个popupwindow的布局
        popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_gift, null);
        viewPager = popupView.findViewById(R.id.play_gift_vp);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, 400);
        // TODO: 2016/5/17 设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态

        window.update();
        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.frg_fun, null);
        window.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        initData();
        setGridview();
    }

    private void setGridview() {
        page = (int) Math.ceil(list.size() / pageSize);
        gridViewList = new ArrayList<View>();
//        message.setChatType(EMMessage.ChatType.ChatRoom);
        for (int i = 0; i < page; i++) {
            gridView = new GridView(mcontext);
            gridView.setAdapter(new Play_Gift_Grid_Adp(mcontext, list, i, pageSize));
            gridView.setGravity(Gravity.CENTER);
            gridView.setClickable(true);
            gridView.setFocusable(true);
            gridView.setNumColumns(3);
            gridView.setHorizontalSpacing(10);
            gridView.setVerticalSpacing(10);
            gridViewList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    final Gift.GiftListBean gift = list.get(i);

                    EMMessage message = EMMessage.createTxtSendMessage("1", "25861179899905");
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    // 增加自己特定的属性
                    message.setAttribute("user_name", sp.getString("user_name", "游照洲"));
                    message.setAttribute("user_id", PreferenceUtils.getJsessionId());
                    message.setAttribute("avatar", sp.getString("avatar", "https://raw.githubusercontent.com/DyncKathline/LiveGiftLayout/master/giftlibrary/src/main/assets/p/000.png"));
                    message.setAttribute("gift_pic", gift.getGiftPic());
                    message.setAttribute("gift_id", gift.getGiftName());
                    message.setAttribute("gift_count", 1);
                    message.setAttribute("type", 2);

                    EMClient.getInstance().chatManager().sendMessage(message);


                    Log.d("aaa", "listener");
                    GiftModel giftModel = new GiftModel();
                    giftModel.setGiftId(list.get(i).getGiftName())
                            .setGiftName(list.get(i).getGiftName())
                            .setGiftCount(1)
                            .setGiftPic(list.get(i).getGiftPic())
                            .setGiftPrice(list.get(i).getGiftPrice())
                            .setSendUserName(sp.getString("user_name", "游照洲"))
                            .setSendUserPic(sp.getString("avatar", "https://raw.githubusercontent.com/DyncKathline/LiveGiftLayout/master/giftlibrary/src/main/assets/p/000.png"))
                            .setCurrentStart(false)
                            .setSendUserId(PreferenceUtils.getJsessionId())
                            .setSendGiftTime(System.currentTimeMillis())
                            .setHitCombo(0);
                    giftControl.loadGift(giftModel);

                }
            });
        }
        play_gift_pager_adp = new Play_Gift_Pager_Adp(gridViewList);
        viewPager.setAdapter(play_gift_pager_adp);
    }

    //加载礼物数据
    public void initData() {
        try {
            InputStream inputStream = getActivity().getAssets().open("gift.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Gift gift = gson.fromJson(inputStreamReader, Gift.class);
            list = new ArrayList<>();
            list = gift.getGiftList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //弹出弹幕
    public void showEdit() {
        playFunRl.setVisibility(View.VISIBLE);
    }

    //发送弹幕
    public void add() {
        Et = playFunEt.getText().toString();
        if (!Et.equals("")) {
            String username = sp.getString("user_name", "游照洲");
            String content = ":" + Et;
            ChatContent chatContent = new ChatContent();
            chatContent.setUsername(username);
            chatContent.setContent(content);
            play_recycler_ver.addData(chatContent);
            playRecyclerVer.smoothScrollToPosition(play_recycler_ver.getData().size());
            message = EMMessage.createTxtSendMessage(".", "25861179899905");
            message.setChatType(EMMessage.ChatType.GroupChat);
            message.setAttribute("user_name", username);
            message.setAttribute("content", content);
            message.setAttribute("type", 1);
            EMClient.getInstance().chatManager().sendMessage(message);

            playFunEt.setText("");
            playFunRl.setVisibility(View.GONE);
        } else {
            Toast.makeText(mcontext, "输入的弹幕不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    //退出
    public void showClosedialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");
        builder.setMessage("确定结束观看？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();

            }
        });
        builder.setCancelable(false);
        builder.show();

    }

}
