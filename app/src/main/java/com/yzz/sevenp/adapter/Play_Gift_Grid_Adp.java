package com.yzz.sevenp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzz.sevenp.R;
import com.yzz.sevenp.bean.Gift;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Play_Gift_Grid_Adp extends BaseAdapter {
    private Context context;
    private List<Gift.GiftListBean> list;
    private double COUNT;
    int num = 0;

    public Play_Gift_Grid_Adp(Context mcontext, List<Gift.GiftListBean> list, int i1, double i) {
        this.context = mcontext;
        this.list = list;
        this.num = i1;
        this.COUNT = i;
    }

    @Override
    public int getCount() {
        return (int) (list.size() > (num + 1) * COUNT ? COUNT : (list.size() - num * COUNT));
    }

    @Override
    public Object getItem(int i) {
        return list.get((int) (i + num * COUNT));
    }

    @Override
    public long getItemId(int i) {
        return (long) (i + num * COUNT);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context,R.layout.play_gift_gird_child, null);
            viewHolder.gird_child_iv =
                    (ImageView) view.findViewById(R.id.gird_child_iv);
            viewHolder.gird_child_tv =
                    (TextView) view.findViewById(R.id.gird_child_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        int pos = (int) (i + num * COUNT);
        Glide.with(context).load(list.get(pos).getGiftPic()).into(viewHolder.gird_child_iv);
        viewHolder.gird_child_tv.setText(list.get(pos).getGiftName());

        return view;
    }

    public class ViewHolder {
        public ImageView gird_child_iv;
        public TextView gird_child_tv;
    }
}
