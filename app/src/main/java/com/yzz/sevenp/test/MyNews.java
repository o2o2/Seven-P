package com.yzz.sevenp.test;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Wookeibun on 2017/8/15.
 */

public interface MyNews {
    @GET("index")
    Call<TopNews.ResultBean.DataBean> getNews (@Query("type") String type,@Query("key") String key);
    @GET("index/{id}")
    Call<TopNews.ResultBean.DataBean> getNews1 (@Path("id") String id);

    @POST("index")
    Call<TopNews.ResultBean.DataBean> postNew(@Body FormBody formBody);


}
