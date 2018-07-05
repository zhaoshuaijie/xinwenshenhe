package com.lcsd.examines.fengtai.http;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.lcsd.examines.fengtai.entity.UserInfo;
import com.lcsd.examines.fengtai.manager.Storage;
import com.lcsd.examines.fengtai.util.StringUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/9/6.
 */
public class AppContext extends Application {
    public static AppContext appContext;
    private static Context context;
    private MyOkHttp mMyOkHttp;

    public static AppContext getInstance() {
        if (appContext == null)
            return new AppContext();
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        context = this.getApplicationContext();
        QbSdk.initX5Environment(context, null);

        //持久化存储cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public MyOkHttp getmMyOkHttp() {
        return mMyOkHttp;
    }

    /**
     * 获取缓存用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return Storage.GetUserInfo() == null ? new UserInfo() : Storage
                .GetUserInfo();
    }

    /**
     * 保存缓存用户信息
     *
     * @param user
     */
    public void saveUserInfo(final UserInfo user) {
        if (user != null) {
            Storage.ClearUserInfo();
            Storage.saveUsersInfo(user);
        }
    }

    /**
     * 用户存在是ture 否则是false
     *
     * @return
     */
    public boolean checkUser() {
        if (StringUtils.isEmpty(getUserInfo().getId())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 清除缓存用户信息
     *
     * @param
     */
    public void cleanUserInfo() {
        Storage.ClearUserInfo();
    }
}
