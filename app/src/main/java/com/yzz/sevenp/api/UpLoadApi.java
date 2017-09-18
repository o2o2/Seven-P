package com.yzz.sevenp.api;

import com.yzz.sevenp.bean.UploadBean;
import com.yzz.sevenp.config.UrlConfig;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Wookeibun on 2017/8/25.
 */

public interface UpLoadApi {
    @Multipart
    @POST(UrlConfig.UTIL_FILE_UPLOAD)
    Call<UploadBean> upload(@Part MultipartBody.Part filepart);
}
