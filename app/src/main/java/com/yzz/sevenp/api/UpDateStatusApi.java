package com.yzz.sevenp.api;


import com.yzz.sevenp.bean.UpDateStatus;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface UpDateStatusApi {
    @POST(UrlConfig.UPDATESTATUS)
    Call<UpDateStatus> postUpDateStatus(@Body FormBody formBody);
}
