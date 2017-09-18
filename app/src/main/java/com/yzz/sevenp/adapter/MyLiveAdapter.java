package com.yzz.sevenp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yzz.sevenp.R;
import com.yzz.sevenp.bean.MyLive;

import java.util.ArrayList;
import java.util.List;

import static com.yzz.sevenp.utils.PhoneInfoUtils.context;


/**
 * Created by Administrator on 2017/9/4.
 */
public abstract class MyLiveAdapter extends RecyclerView.Adapter<LiveHolder1> {
    Context mContext;
    List<MyLive.ResultBean.ListBean> list;

    //用于传参：上下文对象，数据
    public MyLiveAdapter(Context context) {
        this.mContext = context;
        this.list = new ArrayList<>();
    }

    @Override
    public LiveHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.myliveadapter, parent, false);
        LiveHolder1 liveHolder = new LiveHolder1(view);
        return liveHolder;
    }

    @Override
    public void onBindViewHolder(LiveHolder1 holder, final int position) {
        if (holder instanceof LiveHolder1) {
            //这是最主要的代码，显示内容
            holder.liveTvLiveName.setText(list.get(position)
                    .getData().getLive_name());
            holder.liveTvUserName.setText(list.get(position)
                    .getUser().getUser_data().getUser_name());
            if (list.get(position).getData().getStatus() == 0) {
                holder.liveTvStatus.setText("直播");
            }
            if (list.get(position).getData().getStatus() == 1) {
                holder.liveTvStatus.setText("录播");
            }
            String liveIvAvatar_url = list.get(position).getUser().getUser_data().getAvatar();
            String liveIvPic_url = list.get(position).getData().getPic();

            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            Picasso.with(mContext)
                    .load(liveIvAvatar_url)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.liveIvAvatar);
            Picasso.with(mContext)
                    .load(liveIvPic_url)
                    .into(holder.liveIvPic);

            //item点击事件监听
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickLiveItem(position);
                }
            });
            //item点击事件监听
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickLiveItem(position);
                    return true;
                }
            });
        }
    }

    //点击的抽象方法
    public abstract void clickLiveItem(int position);

    //长按的抽象方法
    public abstract void longClickLiveItem(int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    //本地清空
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    //刷新
    public void refreshData(List<MyLive.ResultBean.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //加载
    public void moreData(List<MyLive.ResultBean.ListBean> list) {
        this.list.addAll(list);
        notifyItemChanged(this.list.size());
    }
}

class LiveHolder1 extends RecyclerView.ViewHolder {
    de.hdodenhof.circleimageview.CircleImageView liveIvAvatar;//头像
    TextView liveTvLiveName;//直播名字
    TextView liveTvUserName;//名字
    ImageView liveIvPic;//图片
    TextView liveTvStatus;//直播状态

    public LiveHolder1(View itemView) {
        super(itemView);
        liveIvAvatar = itemView.findViewById(R.id.iv_mylive_photo);
        liveTvLiveName = itemView.findViewById(R.id.tv_mylive_user_name);
        liveTvUserName = itemView.findViewById(R.id.tv_mylive_content);
        liveIvPic = itemView.findViewById(R.id.iv_mylive_audio);
        liveTvStatus = itemView.findViewById(R.id.tv_mylive_play);
    }
}