package com.yzz.sevenp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yzz.sevenp.R;
import com.yzz.sevenp.bean.Live;
import com.yzz.sevenp.bean.OnItemClickListener;

import java.util.List;

import butterknife.BindView;



/**
 * Created by Wookeibun on 2017/8/17.
 */

public class ChoiceAdapter extends RecyclerView.Adapter<MyViewHolder> {
    @BindView(R.id.cardView)
    CardView cardView;
    private List<Live.ResultBean.ListBean> list;
    private Context mContext;
    OnItemClickListener listener;

    public ChoiceAdapter(List<Live.ResultBean.ListBean> aList, Context context) {
        this.list = aList;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void refreshData(List<Live.ResultBean.ListBean> aList) {
        this.list = aList;
        notifyDataSetChanged();
    }

    public void chearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public void moreData(List<Live.ResultBean.ListBean> moreData) {
        list.addAll(moreData);
        notifyItemChanged(list.size());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.choice, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(list.get(position).getUser().getUser_data().getAvatar())
                .placeholder(R.mipmap.ic_image_loading)
                .error(R.mipmap.ic_image_loadfail)
                .into(holder.avatar);
        Picasso.with(mContext)
                .load(list.get(position).getData().getPic())
                .placeholder(R.mipmap.ic_image_loading)
                .error(R.mipmap.ic_image_loadfail)
                .into(holder.pic);
        holder.user_name.setText(list.get(position).getUser().getUser_data().getUser_name());
        if (list.get(position).getData().getStatus() == 0){
            holder.status.setText(" 直播 ");
        }else{
            holder.status.setText(" 录播 ");
        }

        holder.sign.setText(list.get(position).getUser().getUser_data().getSign());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    public final ImageView avatar;
    public final TextView user_name;
    public final TextView sign;
    public final ImageView pic;
    public final TextView status;

    public MyViewHolder(View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.avatar);
        user_name = itemView.findViewById(R.id.user_name);
        sign = itemView.findViewById(R.id.sign);
        pic = itemView.findViewById(R.id.pic);
        status = itemView.findViewById(R.id.status);

    }
}
