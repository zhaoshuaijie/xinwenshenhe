package com.lcsd.examines.fengtai.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.UserInfo;
import com.lcsd.examines.fengtai.http.AppConfig;
import com.lcsd.examines.fengtai.http.AppContext;
import com.lcsd.examines.fengtai.util.StringUtils;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed1, ed2;
    private Button btn_log;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_login, null);
        setContentView(view);
        setSystemBarTransparent();
        mContext = this;
        //渐变展示启动屏
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(1500);
        view.startAnimation(alphaAnimation);
        initView();
        initData();
        setListener();
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void initView() {
        ed1 = (EditText) findViewById(R.id.edit1);
        ed2 = (EditText) findViewById(R.id.edit2);
        btn_log = (Button) findViewById(R.id.btn_log);
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("fengtaishenhe", Context.MODE_PRIVATE);
        ed1.setText(sharedPreferences.getString("user", ""));
        ed2.setText(sharedPreferences.getString("pwd", ""));
    }

    @Override
    protected void setListener() {
        btn_log.setOnClickListener(this);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log:
                if (StringUtils.isEmpty(ed1.getText().toString())) {
                    Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(ed2.getText().toString())) {
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                request_login();
                break;
        }
    }

    //管理员登录
    private void request_login() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "login");
        map.put("user", ed1.getText().toString());
        map.put("pass", ed2.getText().toString());
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("管理员登录：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            request_information();
                            SharedPreferences sharedPreferences = getSharedPreferences("fengtaishenhe", MODE_PRIVATE);
                            SharedPreferences.Editor usereditor = sharedPreferences.edit();
                            usereditor.putString("user", ed1.getText().toString());
                            usereditor.putString("pwd", ed2.getText().toString());
                            usereditor.commit();
                            startActivity(new Intent(mContext, MainActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(mContext, "用户名或密码输入不正确！", Toast.LENGTH_SHORT).show();
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

    //管理员信息
    private void request_information() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "usercp");
        AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("管理员信息：", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            UserInfo info = JSON.parseObject(object.getString("content"), UserInfo.class);
                            AppContext.getInstance().saveUserInfo(info);
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
}
