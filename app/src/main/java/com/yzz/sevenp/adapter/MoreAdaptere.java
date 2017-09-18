package com.yzz.sevenp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yzz.sevenp.bean.Login;

import java.util.ArrayList;

/**
 * Created by Wookeibun on 2017/9/6.
 */

public class MoreAdaptere extends RecyclerView.Adapter<MyViewHolder1>{
    private ArrayList<Login.ResultBean.UserDataBean> alist;

    public MoreAdaptere(ArrayList<Login.ResultBean.UserDataBean> alist) {
        this.alist = alist;
    }


    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder1 holder, int position) {

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
class MyViewHolder1 extends RecyclerView.ViewHolder {


    public MyViewHolder1(View itemView) {
        super(itemView);
    }
}
