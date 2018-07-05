package com.lcsd.examines.fengtai.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.RecordList;
import com.lcsd.examines.fengtai.manager.MediaManager;
import com.lcsd.examines.fengtai.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */
public class RecordAdapter extends BaseAdapter {
    private Context context;
    private List<RecordList.TRslist> list;
    List<AnimationDrawable> mAnimationDrawables;
    int pos = -1;//标记当前录音索引，默认没有播放任何一个

    public RecordAdapter(Context context, List<RecordList.TRslist> list) {
        this.list = list;
        this.context = context;
        mAnimationDrawables = new ArrayList<>();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHodle viewHodle = null;
        if (view == null) {
            viewHodle = new ViewHodle();
            view = LayoutInflater.from(context).inflate(R.layout.item_recordlist, null);
            viewHodle.tv_name = (TextView) view.findViewById(R.id.recordlist_name);
            viewHodle.tv_time = (TextView) view.findViewById(R.id.recordlist_time);
            viewHodle.tv_note = (TextView) view.findViewById(R.id.recordlist_note);
            viewHodle.tv_bf = (TextView) view.findViewById(R.id.record_tv_bf);
            viewHodle.ll_all = (LinearLayout) view.findViewById(R.id.record_ll_all);
            viewHodle.ll_bf = (LinearLayout) view.findViewById(R.id.record_ll_yuyin);
            view.setTag(viewHodle);
        } else {
            viewHodle = (ViewHodle) view.getTag();
        }
        //开始设置监听
        final LinearLayout ieaLlSinger = viewHodle.ll_bf;

        viewHodle.tv_name.setText("管理员：" + list.get(i).getAdmin());
        viewHodle.tv_time.setText(StringUtils.timeStamp2Date(list.get(i).getDateline()));
        if (list.get(i).getNote() != null) {
            viewHodle.tv_note.setText(list.get(i).getNote().equals("通过审核") ? "通过审核" : "未通过原因：" + list.get(i).getNote());
        }
        if (list.get(i).getAudio() != null && list.get(i).getAudio().length() > 1) {
            viewHodle.ll_all.setVisibility(View.VISIBLE);
        } else {
            viewHodle.ll_all.setVisibility(View.GONE);
        }
        viewHodle.ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AnimationDrawable animationDrawable = (AnimationDrawable) ieaLlSinger.getBackground();
                //重置动画
                resetAnim(animationDrawable);
                animationDrawable.start();

                //处理点击正在播放的语音时，可以停止；再次点击时重新播放。
                if (pos == i) {
                    if (list.get(i).isPlaying()) {
                        list.get(i).setPlaying(false);
                        MediaManager.release();
                        animationDrawable.stop();
                        animationDrawable.selectDrawable(0);//reset
                        return;
                    } else {
                        list.get(i).setPlaying(true);
                    }
                }
                //记录当前位置正在播放。
                pos = i;
                list.get(i).setPlaying(true);

                //播放前重置。
                MediaManager.release();
                //开始实质播放
                MediaManager.playSound(list.get(i).getAudio(),
                        new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                animationDrawable.selectDrawable(0);//显示动画第一帧
                                animationDrawable.stop();

                                //播放完毕，当前播放索引置为-1。
                                pos = -1;
                            }
                        });
            }
        });
        return view;
    }

    class ViewHodle {
        private TextView tv_name, tv_time, tv_note, tv_bf;
        private LinearLayout ll_all, ll_bf;
    }

    private void resetAnim(AnimationDrawable animationDrawable) {
        if (!mAnimationDrawables.contains(animationDrawable)) {
            mAnimationDrawables.add(animationDrawable);
        }
        for (AnimationDrawable ad : mAnimationDrawables) {
            ad.selectDrawable(0);
            ad.stop();
        }
    }
}
