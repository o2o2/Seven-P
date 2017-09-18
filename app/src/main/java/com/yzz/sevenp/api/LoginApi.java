package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.Login;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Wookeibun on 2017/8/28.
 */

public interface LoginApi {
    @POST(UrlConfig.LOGIN_API)
    Call<Login> getLive(@Body FormBody formBody);
}
