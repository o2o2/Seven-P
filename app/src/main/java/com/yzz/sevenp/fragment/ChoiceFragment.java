package com.yzz.sevenp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.PlayerVideoActivity;
import com.yzz.sevenp.adapter.ChoiceAdapter;
import com.yzz.sevenp.api.LiveApi;
import com.yzz.sevenp.bean.Live;
import com.yzz.sevenp.bean.OnItemClickListener;
import com.yzz.sevenp.fragment.base.BaseNetFragment;
import com.yzz.sevenp.network.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.FormBody;
import retrofit2.Call;


/**
 * Created by Administrator on 2017/8/15.
 */

public class ChoiceFragment extends BaseNetFragment<Live> {


    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    Unbinder unbinder1;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;
    Unbinder unbinder2;
    List<Live.ResultBean.ListBean> list =new ArrayList<>();
    private ChoiceAdapter choiceAdapter;
    private int STATE;
    private final int NORM = 0;
    private final int PULL = 1;
    private final int UP = 2;
    private int page = 1;


    @Override
    protected int getContentResId() {
        return R.layout.fragment_choice;
    }

    @Override
    protected void initViews() {
        recyclerview1.setLayoutManager(new LinearLayoutManager(getActivity()));
        choiceAdapter = new ChoiceAdapter(list, getActivity());

        recyclerview1.setAdapter(choiceAdapter);
        choiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), PlayerVideoActivity.class);
                intent.putExtra("live_name",list.get(i).getData().getLive_name());
                intent.putExtra("id",list.get(i).getId());
                intent.putExtra("sign",list.get(i).getUser().getUser_data().getSign());
                intent.putExtra("avatar",list.get(i).getUser().getUser_data().getAvatar());
                intent.putExtra("status",list.get(i).getData().getStatus());
                startActivity(intent);
            }
        });
    }
    @Override
    protected void loadData() {


        LiveApi liveApi = RetrofitManager.getTestRetrofit().create(LiveApi.class);
        FormBody formBody = new FormBody.Builder()
                .add("type", "1")
                .add("page", page+"")
                .build();
        Call<Live> liveCall = liveApi.getLive(formBody);
        liveCall.enqueue(this);

    }

    @Override
    protected void processData(Live live) {
        goneLoading();
        init(live);

    }


    private void init(Live live) {
        list = live.getResult().getList();
        if (list.size() == 0) {
            return;
        }
        switch (STATE) {
            case NORM:
                choiceAdapter.refreshData(list);
                refresh.finishRefresh();
                break;
            case PULL:
                choiceAdapter.refreshData(list);
                refresh.finishRefresh();
                break;
            case UP:
                choiceAdapter.moreData(list);
                refresh.finishRefreshLoadMore();
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder2.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder2 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresh.setLoadMore(true);//设置支持上拉加载
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                choiceAdapter.chearData();
                loadData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                page ++;
                STATE = UP;
                loadData();
            }
        });
    }
}
