package com.lcsd.examines.fengtai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.SyFl;
import com.lcsd.examines.fengtai.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class FenleiAdapter extends BaseAdapter {
    private Context context;
    private List<SyFl> list;

    public FenleiAdapter(Context context, List<SyFl> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodle viewHodle = null;
        if (view == null) {
            viewHodle = new ViewHodle();
            view = LayoutInflater.from(context).inflate(R.layout.item_fenlei, null);
            viewHodle.iv = (CircleImageView) view.findViewById(R.id.item_main_iv);
            viewHodle.tv_title = (TextView) view.findViewById(R.id.item_main_title);
            viewHodle.tv_xx = (TextView) view.findViewById(R.id.item_main_xiaoxi);
            view.setTag(viewHodle);
        } else {
            viewHodle = (ViewHodle) view.getTag();
        }
        viewHodle.tv_title.setText(list.get(i).getTitle());
        Glide.with(context).load(list.get(i).getIco()).placeholder(R.drawable.img_1).into(viewHodle.iv);
        viewHodle.tv_xx.setText("您有"+list.get(i).getTotal()+"条未审核、"+list.get(i).getTotal2()+"条未通过消息");
        return view;
    }

    class ViewHodle {
        private CircleImageView iv;
        private TextView tv_xx, tv_title;
    }
}
