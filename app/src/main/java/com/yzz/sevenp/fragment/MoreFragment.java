package com.yzz.sevenp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.LoginActivity;
import com.yzz.sevenp.activity.MyLiveListActivity;
import com.yzz.sevenp.api.UserInfoApi;
import com.yzz.sevenp.bean.Login;
import com.yzz.sevenp.bean.UserInfoBean;
import com.yzz.sevenp.network.RetrofitManager;
import com.yzz.sevenp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wookeibun on 2017/8/16.
 */

public class MoreFragment extends Fragment {
    @BindView(R.id.back_login)
    RelativeLayout backLogin;
    Unbinder unbinder;
    @BindView(R.id.iv_more_photo)
    CircleImageView ivMorePhoto;
    @BindView(R.id.more_my_live)
    RelativeLayout moreMyLive;
    @BindView(R.id.tv_more_user_name)
    TextView tvMoreUserName;
    @BindView(R.id.tv_more_sign)
    TextView tvMoreSign;
    private List<Login.ResultBean.UserDataBean> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_more, null);
        unbinder = ButterKnife.bind(this, view);
        initTopData();
        return view;
    }

    //用户头部信息
    private void initTopData() {
        //通过id得到User信息
        UserInfoApi userInfoApi = RetrofitManager.getTestRetrofit().create(UserInfoApi.class);
        FormBody formBody = new FormBody.Builder()//用户信息参数
                .add("uid", PreferenceUtils.getJsessionId())//userID
                .build();
        Call<UserInfoBean> userInfoBeanCall = userInfoApi.postCreate(formBody);
        userInfoBeanCall.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                tvMoreUserName.setText(response.body().getResult().getList().get(0).getUser_data().getUser_name());
                tvMoreSign.setText(response.body().getResult().getList().get(0).getUser_data().getSign());
                String avatarUrl = response.body().getResult().getList().get(0).getUser_data().getAvatar();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(avatarUrl, ivMorePhoto);
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                Toast.makeText(getActivity(), "网络连接失败或参数错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.back_login, R.id.more_my_live})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_login:

                PreferenceUtils.putJsessionId(null);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.more_my_live:
                Intent intent = new Intent(getActivity(), MyLiveListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
