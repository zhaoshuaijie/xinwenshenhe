package com.lcsd.examines.fengtai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.Shenhelist;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class ExamineAdapter extends BaseAdapter {
    private Context context;
    private List<Shenhelist.TRslist> list;

    public ExamineAdapter(Context context, List<Shenhelist.TRslist> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_examine, null);
            viewHodle.iv = (ImageView) view.findViewById(R.id.item_examine_iv);
            viewHodle.tv_title = (TextView) view.findViewById(R.id.item_examine_title);
            view.setTag(viewHodle);
        } else {
            viewHodle = (ViewHodle) view.getTag();
        }
        viewHodle.tv_title.setText(list.get(i).getTitle());
        if (list.get(i).getStatus() != null) {
            if (list.get(i).getStatus().equals("0")) {
                viewHodle.iv.setImageResource(R.drawable.img_wsh);
            } else if (list.get(i).getStatus().equals("2")) {
                viewHodle.iv.setImageResource(R.drawable.img_wtg);
            }
        }
        return view;
    }

    class ViewHodle {
        private ImageView iv;
        private TextView tv_title;
    }
}
