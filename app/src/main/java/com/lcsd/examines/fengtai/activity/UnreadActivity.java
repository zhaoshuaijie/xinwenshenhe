package com.lcsd.examines.fengtai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.adapter.WeiduAdapter;
import com.lcsd.examines.fengtai.entity.SyFl;
import com.lcsd.examines.fengtai.http.AppConfig;
import com.lcsd.examines.fengtai.http.AppContext;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class UnreadActivity extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_title,tv_empty;
    private PtrClassicFrameLayout ptr;
    private ListView lv;
    private List<SyFl> list;
    private String title, pid;
    private WeiduAdapter adapter;
    private Context mContext;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unread);

        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            pid = getIntent().getStringExtra("pid");
        }
        mContext = this;
        initView();
        setListener();
        initData();
        request_fl(false);
    }


    @Override
    protected void initView() {
        iv_back = (ImageView) findViewById(R.id.titlebar_back);
        tv_title = (TextView) findViewById(R.id.titlebar_title);
        iv_back.setVisibility(View.VISIBLE);
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_weidu);
        lv = (ListView) findViewById(R.id.lv_weidu);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        emptyView = View.inflate(this, R.layout.item_empty, null);
        tv_empty= (TextView) emptyView.findViewById(R.id.empty_tv);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(onClickListener);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(UnreadActivity.this, ExamineListActivity.class).putExtra("pid",pid).putExtra("cateid",list.get(i).getId()));
            }
        });
        ptr.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request_fl(true);
            }


            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
    }

    private void initData() {
        tv_title.setText(title);
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv.setEmptyView(emptyView);
        list = new ArrayList<>();
        adapter = new WeiduAdapter(this, list);
        lv.setAdapter(adapter);
    }

    private void request_fl(final boolean refresh) {
        Map<String, String> map = new HashMap<>();
        map.put("c", "cate");
        map.put("pid", pid);
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("分类信息：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<SyFl> fllist = JSON.parseArray(object.getString("content"), SyFl.class);
                            if (fllist != null && fllist.size() > 0) {
                                if (refresh) {
                                    list.clear();
                                }
                                list.addAll(fllist);
                                adapter.notifyDataSetChanged();
                                if (refresh) {
                                    ptr.refreshComplete();
                                }
                            }else {
                                tv_empty.setText("暂无数据");
                                emptyView.findViewById(R.id.empty_progress).setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UnreadActivity.this.finish();
        }
    };
}
