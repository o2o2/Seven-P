package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.Register;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Wookeibun on 2017/8/25.
 */

public interface RegisterApi {
    @POST(UrlConfig.REGISTER_API)
    Call<Register> getLive(@Body FormBody formBody);
}
