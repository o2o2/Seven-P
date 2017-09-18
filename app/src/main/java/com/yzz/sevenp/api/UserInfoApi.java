package com.yzz.sevenp.api;


import com.yzz.sevenp.bean.UserInfoBean;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface UserInfoApi {
    @POST(UrlConfig.UserInfo)
    Call<UserInfoBean> postCreate(@Body FormBody formBody);
}
