package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.Remove;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface RemoveApi {
    @POST(UrlConfig.REMOVE)
    Call<Remove> postRemove(@Body FormBody formBody);
}
