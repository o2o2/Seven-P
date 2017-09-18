package com.yzz.sevenp.api;


import com.yzz.sevenp.bean.Create;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface CreateApi {
    @POST(UrlConfig.CREATE)
    Call<Create> postCreate(@Body FormBody formBody);
}
