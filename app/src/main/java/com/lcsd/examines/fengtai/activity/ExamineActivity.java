package com.lcsd.examines.fengtai.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.http.AppConfig;
import com.lcsd.examines.fengtai.http.AppContext;
import com.lcsd.examines.fengtai.view.VideoEnabledWebChromeClient;
import com.lcsd.examines.fengtai.view.VideoEnabledWebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExamineActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back, iv_right;
    private TextView tv_title;
    private VideoEnabledWebView webView;
    private TextView tv_yes, tv_no;
    private String url, id;
    private ProgressBar mProgressBar;
    private int currentProgress;
    private boolean isAnimStart;
    public static ExamineActivity examineActivity;
    private ArrayList<String> images = new ArrayList<>();
    private LinearLayout parent;
    private FrameLayout videoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        examineActivity = this;
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            id = getIntent().getStringExtra("id");
        }
        initView();
        setListener();
        initData();
    }

    @Override
    protected void initView() {
        iv_right = (ImageView) findViewById(R.id.titlebar_right);
        iv_back = (ImageView) findViewById(R.id.titlebar_back);
        tv_title = (TextView) findViewById(R.id.titlebar_title);
        iv_right.setImageResource(R.drawable.img_titlebar_jilu);
        iv_back.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.VISIBLE);
        webView = (VideoEnabledWebView) findViewById(R.id.examine_webview);
        tv_yes = (TextView) findViewById(R.id.examine_yes);
        tv_no = (TextView) findViewById(R.id.examine_no);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        parent = (LinearLayout) findViewById(R.id.parent);
        videoLayout = (FrameLayout) findViewById(R.id.videoLayout);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    private void initData() {
        tv_title.setText("审核状态");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getLoadWithOverviewMode();
        webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
        //载入js
        webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        webView.setWebChromeClient(new VideoEnabledWebChromeClient(this, parent, videoLayout){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                currentProgress = mProgressBar.getProgress();
                if (i >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    mProgressBar.setProgress(i);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(mProgressBar.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(i);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            // 网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("url",url);
                super.onPageFinished(view, url);
                // html加载完成之后，添加监听图片的点击js函数
                addImageClickListner();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                this.finish();
                break;
            case R.id.examine_yes:
                request_tg();
                break;
            case R.id.examine_no:
                startActivity(new Intent(this, ModifyAcyivity.class).putExtra("id", id));
                break;
            case R.id.titlebar_right:
                startActivity(new Intent(this, RecordActivity.class).putExtra("id", id));
                break;
        }
    }

    //审核通过
    private void request_tg() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "status");
        map.put("f", "edit");
        map.put("id", id);
        map.put("status", 1 + "");
        AppContext.getInstance().getmMyOkHttp().post(this, AppConfig.mianurl, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    try {
                        Log.d("修改审核：", response);
                        JSONObject object = new JSONObject(response);
                        if (object.getString("status").equals("ok")) {
                            Toast.makeText(ExamineActivity.this, "审核成功", Toast.LENGTH_SHORT).show();
                            ExamineActivity.this.finish();

                        } else {
                            Toast.makeText(ExamineActivity.this, object.getString("content"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Toast.makeText(ExamineActivity.this, error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                mProgressBar.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }
    // 注入js函数监听
    private void addImageClickListner() {
        //遍历页面中所有img的节点，因为节点里面的图片的url即objs[i].src，保存所有图片的src.
        //为每个图片设置点击事件，objs[i].onclick
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{" +
                "window.imagelistner.readImageUrl(objs[i].src);  " +
                " objs[i].onclick=function()  " +
                " {  " +
                " window.imagelistner.openImage(this.src);  " +
                "  }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void readImageUrl(String img) {     //把所有图片的url保存在ArrayList<String>中
            images.add(img);
        }

        @android.webkit.JavascriptInterface  //对于targetSdkVersion>=17的，要加这个声明
        public void openImage(String clickimg)//点击图片所调用到的函数
        {
            int index = 0;
            ArrayList<String> list = addImages();
            for (String url : list)
                if (url.equals(clickimg)) index = list.indexOf(clickimg);//获取点击图片在整个页面图片中的位置
            String[] imgs = new String[list.size()];
            for (int i = 0; i < imgs.length; i++) {
                imgs[i] = list.get(i);
            }
            Intent intent = new Intent();
            intent.putExtra("imgs", imgs);
            intent.putExtra("index", index);
            intent.setClass(context, ImagePageActivity.class);
            context.startActivity(intent);//启动ViewPagerActivity,用于显示图片
        }
    }


    //去重复
    private ArrayList<String> addImages() {
        ArrayList<String> list = new ArrayList<>();
        Set set = new HashSet();
        for (String cd : images) {
            if (set.add(cd)) {
                list.add(cd);
            }
        }
        return list;
    }
}
