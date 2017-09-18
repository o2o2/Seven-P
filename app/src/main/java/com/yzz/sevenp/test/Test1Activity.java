package com.yzz.sevenp.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yzz.sevenp.R;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        init();
    }

    /*
    * 这是get的请求方法
    * */
    private void init() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://v.juhe.cn/toutiao/")
                .build();
        MyNews myNews = retrofit.create(MyNews.class);
        Call<TopNews.ResultBean.DataBean> top = myNews.getNews("top", "562402375cb93590c7eec9ade024dffe");
        top.enqueue(new Callback<TopNews.ResultBean.DataBean>() {
            @Override
            public void onResponse(Call<TopNews.ResultBean.DataBean> call, Response<TopNews.ResultBean.DataBean> response) {
                Log.d("Test1Activity", "response::" + response.body().getAuthor_name());
            }

            @Override
            public void onFailure(Call<TopNews.ResultBean.DataBean> call, Throwable t) {
                Toast.makeText(Test1Activity.this, "网络连接失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    * 这个api不支持post
    * 这是post的请求方法
    * */
    private void init1() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://v.juhe.cn/toutiao/")
                .build();
        MyNews myNews = retrofit.create(MyNews.class);
        FormBody formBody = new FormBody.Builder()
                .add("type", "top")
                .add("key", "562402375cb93590c7eec9ade024dffe")
                .build();
        Call<TopNews.ResultBean.DataBean> topNewsCall = myNews.postNew(formBody);
        topNewsCall.enqueue(new Callback<TopNews.ResultBean.DataBean>() {
            @Override
            public void onResponse(Call<TopNews.ResultBean.DataBean> call, Response<TopNews.ResultBean.DataBean> response) {
                Log.d("Test1Activity", "response::" + response.body().getAuthor_name());
            }

            @Override
            public void onFailure(Call<TopNews.ResultBean.DataBean> call, Throwable t) {
                Toast.makeText(Test1Activity.this, "网络连接失败!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
