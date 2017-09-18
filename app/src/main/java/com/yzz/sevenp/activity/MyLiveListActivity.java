package com.yzz.sevenp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.adapter.MyLiveAdapter;
import com.yzz.sevenp.api.MyLiveApi;
import com.yzz.sevenp.api.RemoveApi;
import com.yzz.sevenp.bean.MyLive;
import com.yzz.sevenp.bean.Remove;
import com.yzz.sevenp.network.RetrofitManager;
import com.yzz.sevenp.utils.PreferenceUtils;
import com.yzz.sevenp.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyLiveListActivity  extends BaseActivity implements Callback<MyLive> {
    @BindView(R.id.mylive_rv)
    RecyclerView siftRv;
    @BindView(R.id.refresh_mylive)
    MaterialRefreshLayout siftRefresh;
    private List<MyLive.ResultBean.ListBean> list;
    private int page = 1;
    private MyLiveAdapter myLiveAdapter;

    private final int ONE = 1;
    private final int TWO = 2;
    private final int THREE = 3;
    private int STATE = ONE;//状态

    protected int getContentResId() {
        return R.layout.activity_my_live_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: add setContentView(...) invocation
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initViews();
        initData();
        loadData();
    }

    private void initData() {
        siftRefresh.setLoadMore(true);//支持加载
        siftRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override//更新
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                STATE = TWO;
                page = 0;
                loadData();
            }

            @Override//加载跟多
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                STATE = THREE;
                page++;
                loadData();
            }
        });
    }

    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    protected void initViews() {
        siftRv.setLayoutManager(new LinearLayoutManager(MyLiveListActivity.this));
        myLiveAdapter = new MyLiveAdapter(MyLiveListActivity.this) {
            @Override
            public void clickLiveItem(int position) {
                Intent intent = new Intent();
                intent.setClass(MyLiveListActivity.this, CameraActivity.class);
                intent.putExtra("state", 0);
                intent.putExtra("Id",list.get(position).getId()+"");
                Log.e("TAG", list.get(position).getId() + "");
                startActivity(intent);
            }

            @Override
            public void longClickLiveItem(final int position) {
                //长按删除，属于危险操作，提示下
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MyLiveListActivity.this);
               
                normalDialog.setTitle("删除我的直播");
                normalDialog.setMessage("你要删除吗?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RemoveApi removeApi = RetrofitManager.getTestRetrofit().create(RemoveApi.class);
                                FormBody formBody = new FormBody.Builder()
                                        .add("live_id", list.get(position).getId() + "")
                                        .build();
                                Call<Remove> removeBeanCall = removeApi.postRemove(formBody);
                                removeBeanCall.enqueue(new Callback<Remove>() {
                                    @Override
                                    public void onResponse(Call<Remove> call, Response<Remove> response) {
                                        ToastUtils.showLong("删除成功");
                                        list.remove(position);
                                        myLiveAdapter.notifyDataSetChanged();//更新数据，更新UI
                                    }

                                    @Override
                                    public void onFailure(Call<Remove> call, Throwable t) {

                                    }
                                });
                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 显示
                normalDialog.show();
            }
        };
    }


    protected void loadData() {
        String uid = PreferenceUtils.getJsessionId();
        Log.e("TAG", "uid:" + uid);
        Toast.makeText(MyLiveListActivity.this, "获取数据失败"+uid, Toast.LENGTH_SHORT).show();
        MyLiveApi myLiveApi = RetrofitManager.getTestRetrofit().create(MyLiveApi.class);
        FormBody formBody = new FormBody.Builder()
                .add("uid", uid)
                .add("page", page + "")
                .build();
        Call<MyLive> liveCall = myLiveApi.postMyLive(formBody);
        liveCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<MyLive> call, Response<MyLive> response) {
        Log.e("TAG", "成功");
//        Toast.makeText(MyLiveListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        if (response.body() == null) {
            Toast.makeText(MyLiveListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        list = response.body().getResult().getList();
        if (list.size() == 0) {
//            Toast.makeText(MyLiveListActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (STATE) {
            case ONE://状态为第一次加载时
                myLiveAdapter.refreshData(list);//更新数据
                siftRv.setAdapter(myLiveAdapter);//进行RecyclerView适配
                break;
            case TWO://状态为刷新时，
                myLiveAdapter.refreshData(list);//进行更新数据，刷新UI
                siftRefresh.finishRefresh();//刷新完成
                break;
            case THREE://（上拉加载）状态为再次加载数据时，
                myLiveAdapter.moreData(list);//更新数据，更新UI
                siftRefresh.finishRefreshLoadMore();//加载完成
                break;
        }
    }

    @Override
    public void onFailure(Call<MyLive> call, Throwable t) {
        Log.e("TAG", "失败");
    }
}
