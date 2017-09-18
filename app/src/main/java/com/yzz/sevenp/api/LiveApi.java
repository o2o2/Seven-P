package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.Live;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Wookeibun on 2017/8/17.
 */

public interface LiveApi {
    @POST(UrlConfig.HOME_API)
    Call<Live> getLive(@Body FormBody formBody);

}
