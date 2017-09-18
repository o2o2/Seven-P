package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.MyLive;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Administrator on 2017/9/1.
 */

public interface MyLiveApi {
    @POST(UrlConfig.API_V1_0_TEST6)
    Call<MyLive> postMyLive(@Body FormBody formBody);
}


