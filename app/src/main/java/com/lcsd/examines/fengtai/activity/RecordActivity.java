package com.lcsd.examines.fengtai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.adapter.RecordAdapter;
import com.lcsd.examines.fengtai.entity.RecordList;
import com.lcsd.examines.fengtai.http.AppConfig;
import com.lcsd.examines.fengtai.http.AppContext;
import com.lcsd.examines.fengtai.manager.MediaManager;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class RecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title, tv_empty;
    private String id;
    private Context mContext;
    private PtrClassicFrameLayout ptr;
    private ListView lv;
    private View emptyView;
    private Integer pageid = 1, total = 0;
    private List<RecordList.TRslist> list;
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mContext = this;
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        initView();
        setListener();
        initData();
        request_record(0);
    }

    @Override
    protected void initView() {
        iv_back = (ImageView) findViewById(R.id.titlebar_back);
        tv_title = (TextView) findViewById(R.id.titlebar_title);
        iv_back.setVisibility(View.VISIBLE);
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_recrod);
        lv = (ListView) findViewById(R.id.lv_recrod);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        emptyView = View.inflate(this, R.layout.item_empty, null);
        tv_empty = (TextView) emptyView.findViewById(R.id.empty_tv);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                request_record(2);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request_record(1);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (pageid < total) {
                    return super.checkCanDoLoadMore(frame, lv, footer);
                } else {
                    return false;
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
    }

    private void initData() {
        tv_title.setText("审核记录");
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv.setEmptyView(emptyView);
        list = new ArrayList<>();
        adapter = new RecordAdapter(mContext, list);
        lv.setAdapter(adapter);
    }

    private void request_record(final int i) {
        if (i == 2) {
            if (pageid < total) {
                pageid++;
            } else {
                ptr.refreshComplete();
                return;
            }
        }
        if (i == 1) {
            pageid = 1;
        }
        Map<String, String> map = new HashMap<>();
        map.put("c", "status");
        map.put("id", id);
        map.put("pageid", pageid + "");
        map.put("psize", 10 + "");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("审核记录：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            RecordList recordList = JSON.parseObject(object.getString("content"), RecordList.class);
                            total = recordList.getTotal_page();
                            if (recordList.getRslist() != null) {
                                if (i == 1) {
                                    list.clear();
                                }
                                list.addAll(recordList.getRslist());
                                adapter.notifyDataSetChanged();
                            }
                            if (list.size() == 0) {
                                tv_empty.setText("暂无数据");
                                emptyView.findViewById(R.id.empty_progress).setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (i == 1 || i == 2) {
                        ptr.refreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        MediaManager.release();
        super.onPause();
    }
}
