package com.lcsd.examines.fengtai.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.entity.Record;
import com.lcsd.examines.fengtai.http.AppConfig;
import com.lcsd.examines.fengtai.http.AppContext;
import com.lcsd.examines.fengtai.manager.AudioRecordButton;
import com.lcsd.examines.fengtai.manager.MediaManager;
import com.lcsd.examines.fengtai.utils.FileUtils;
import com.lcsd.examines.fengtai.utils.PermissionHelper;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyAcyivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title, tv_tj, tv_lz, tv_bf, tv_time;
    private EditText ed;
    private AudioRecordButton button;
    private PermissionHelper mHelper;
    private Record recordModel;
    private LinearLayout ll, ll_all;
    private List<AnimationDrawable> mAnimationDrawables;
    private Context mContext;
    private String id;
    private File file;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        mContext = this;
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        initView();
        setListener();
        initData();
    }

    @Override
    protected void initView() {
        progressDialog = new ProgressDialog(mContext);
        mAnimationDrawables = new ArrayList<>();
        recordModel = new Record();
        iv_back = (ImageView) findViewById(R.id.titlebar_back);
        tv_title = (TextView) findViewById(R.id.titlebar_title);
        iv_back.setVisibility(View.VISIBLE);
        ed = (EditText) findViewById(R.id.modify_ed);
        tv_tj = (TextView) findViewById(R.id.mod_tv);
        tv_lz = (TextView) findViewById(R.id.mod_lz);
        button = (AudioRecordButton) findViewById(R.id.mod_tv_btn);
        tv_bf = (TextView) findViewById(R.id.mod_tv_bf);
        tv_time = (TextView) findViewById(R.id.mod_tv_time);
        ll = (LinearLayout) findViewById(R.id.mod_ll_yuyin);
        ll_all = (LinearLayout) findViewById(R.id.mod_ll_all);
    }

    @Override
    protected void setListener() {
        iv_back.setOnClickListener(this);
        tv_tj.setOnClickListener(this);
        tv_lz.setOnClickListener(this);
        ll_all.setOnClickListener(this);

        button.setHasRecordPromission(false);
        //        授权处理
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音",
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        button.setHasRecordPromission(true);
                        button.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
                            @Override
                            public void onFinished(float seconds, String filePath) {
                                if (recordModel.getPath() != null && recordModel.getPath().length() > 1) {
                                    try {
                                        FileUtils.delete(recordModel.getPath());
                                        Log.d("TAG", "删除：" + recordModel.toString() + "成功");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                recordModel.setSecond((int) seconds <= 0 ? 1 : (int) seconds);
                                recordModel.setPath(filePath);
                                recordModel.setPlayed(false);

                                Log.d("TAG", "保存录音成功------------->" + recordModel.toString());
                                tv_time.setVisibility(View.VISIBLE);
                                tv_time.setText(recordModel.getSecond() + " ''");
                                button.setVisibility(View.GONE);
                                tv_lz.setVisibility(View.VISIBLE);
                                tv_lz.setText("重新录制");
                                tv_bf.setText("点击回放");
                            }
                        });
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        button.setHasRecordPromission(false);
                        Toast.makeText(ModifyAcyivity.this, "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void initData() {
        tv_title.setText("修改意见");
        tv_bf.setText("录制语音");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                this.finish();
                break;
            case R.id.mod_tv:
                if ((recordModel.getPath() != null && recordModel.getPath().length() > 1) || ed.getText().toString().length() > 0) {
                    request_shno();
                } else {
                    Toast.makeText(mContext, "请输入审核不通过的原因", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mod_lz:
                button.setVisibility(View.VISIBLE);
                break;
            case R.id.mod_ll_all:
                if (recordModel.getPath() != null) {
                    final AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
                    //重置动画
                    resetAnim(animationDrawable);
                    animationDrawable.start();

                    if (recordModel.isPlaying()) {
                        recordModel.setPlaying(false);
                        MediaManager.release();
                        animationDrawable.stop();
                        animationDrawable.selectDrawable(0);//reset
                        return;
                    } else {
                        recordModel.setPlaying(true);
                    }

                    recordModel.setPlaying(true);
                    //播放前重置。
                    MediaManager.release();
                    //开始实质播放
                    MediaManager.playSound(recordModel.getPath(),
                            new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    animationDrawable.selectDrawable(0);//显示动画第一帧
                                    animationDrawable.stop();

                                }
                            });
                } else {
                    button.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //审核不通过
    private void request_shno() {
        Map<String, String> map = new HashMap<>();
        map.put("c", "status");
        map.put("f", "edit");
        map.put("id", id);
        map.put("status", "2");
        if (ed.getText().toString().length() > 0) {
            map.put("note", ed.getText().toString());
        }
        if (recordModel.getPath() != null && recordModel.getPath().length() > 1) {
            Map<String, File> map1 = new HashMap<>();
            file = new File(recordModel.getPath());
            map1.put("audio", file);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置顺序在前，不然不显示
            progressDialog.setTitle("正在上传文件");
            progressDialog.setCancelable(false);
            progressDialog.show();
            AppContext.getInstance().getmMyOkHttp().upload(mContext, AppConfig.mianurl, map, map1, new RawResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    if (response != null) {
                        Log.d("带语音上传上传审核：", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("status").equals("ok")) {
                                Toast.makeText(mContext, "审核成功", Toast.LENGTH_SHORT).show();
                                ModifyAcyivity.this.finish();
                                ExamineActivity.examineActivity.finish();
                            } else {
                                Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onProgress(long currentBytes, long totalBytes) {
                    super.onProgress(currentBytes, totalBytes);
                    progressDialog.setProgressNumberFormat(FileUtils.convertFileSize(currentBytes) + "/" + FileUtils.convertFileSize(totalBytes));
                    progressDialog.setProgress((int) currentBytes);
                    progressDialog.setMax((int) totalBytes);//做百分比更新
                    if (currentBytes == totalBytes) {
                        progressDialog.dismiss();
                    }

                }
            });
        } else {
            AppContext.getInstance().getmMyOkHttp().post(mContext, AppConfig.mianurl, map, new RawResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    if (response != null) {
                        Log.d("无语音上传审核：", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("status").equals("ok")) {
                                Toast.makeText(mContext, "审核成功", Toast.LENGTH_SHORT).show();
                                ModifyAcyivity.this.finish();
                                ExamineActivity.examineActivity.finish();
                            } else {
                                Toast.makeText(mContext, object.getString("content"), Toast.LENGTH_SHORT).show();
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
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        MediaManager.release();//保证在退出该页面时，终止语音播放
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //退出時刪除录制的语音文件
        if (recordModel.getPath() != null && recordModel.getPath().length() > 1) {
            try {
                FileUtils.delete(recordModel.getPath());
                Log.d("TAG", "删除：" + recordModel.toString() + "成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
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
