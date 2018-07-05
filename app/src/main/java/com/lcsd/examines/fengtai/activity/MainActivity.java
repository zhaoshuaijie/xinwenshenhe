package com.lcsd.examines.fengtai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.adapter.FenleiAdapter;
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


public class MainActivity extends BaseActivity {
    private TextView tv_title, tv_empty;
    private PtrClassicFrameLayout ptr;
    private ListView lv;
    private List<SyFl> list;
    private FenleiAdapter adapter;
    private Context mContext;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        setListener();
        initData();
        request_sy(false);
    }

    @Override
    protected void initView() {
        tv_title = (TextView) findViewById(R.id.titlebar_title);
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_main);
        lv = (ListView) findViewById(R.id.lv_main);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.disableWhenHorizontalMove(true);
        emptyView = View.inflate(this, R.layout.item_empty, null);
        tv_empty = (TextView) emptyView.findViewById(R.id.empty_tv);
    }


    @Override
    protected void setListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).getSon() == 0) {
                    startActivity(new Intent(MainActivity.this, ExamineListActivity.class).putExtra("pid", list.get(i).getId()).putExtra("cateid", "0"));
                } else {
                    startActivity(new Intent(MainActivity.this, UnreadActivity.class).putExtra("title", list.get(i).getTitle()).putExtra("pid", list.get(i).getId()));
                }
            }
        });
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                request_sy(true);
            }


            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }
        });
    }

    private void initData() {
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lv.setEmptyView(emptyView);
        list = new ArrayList<>();
        adapter = new FenleiAdapter(this, list);
        lv.setAdapter(adapter);
        tv_title.setText("分 类");
    }

    //请求数据
    private void request_sy(final boolean refresh) {
        AppContext.getInstance().getmMyOkHttp().get(mContext, AppConfig.mianurl, null, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("首页信息：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            List<SyFl> Sylist = JSON.parseArray(object.getString("content"), SyFl.class);
                            if (Sylist != null & Sylist.size() > 0) {
                                if (refresh) {
                                    list.clear();
                                }
                                list.addAll(Sylist);
                                adapter.notifyDataSetChanged();
                                if (refresh) {
                                    ptr.refreshComplete();
                                }
                            } else {
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

    //管理员退出
    private void request_logout() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "logout");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("退出登录：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            if (AppContext.getInstance().checkUser()) {
                                AppContext.getInstance().cleanUserInfo();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    /**
     * 返回键两次退出程序
     */
    private long mExitTime;
    private boolean isLand;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isLand)
            return false;
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request_logout();
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

}
