package com.lcsd.examines.fengtai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.SyFl;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class WeiduAdapter extends BaseAdapter {
    private Context context;
    private List<SyFl> list;

    public WeiduAdapter(Context context, List<SyFl> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_weidu, null);
            viewHodle.tv_title = (TextView) view.findViewById(R.id.item_weidu_title);
            viewHodle.tv_xx = (TextView) view.findViewById(R.id.item_weidu_xiaoxi);
            view.setTag(viewHodle);
        } else {
            viewHodle = (ViewHodle) view.getTag();
        }
        viewHodle.tv_title.setText(list.get(i).getTitle());
        viewHodle.tv_xx.setText("您有"+list.get(i).getTotal()+"条未审核、"+list.get(i).getTotal2()+"条未通过消息");
        return view;
    }

    class ViewHodle {
        private TextView tv_xx, tv_title;
    }
}
