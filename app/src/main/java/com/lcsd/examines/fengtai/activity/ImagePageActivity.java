package com.lcsd.examines.fengtai.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.examines.fengtai.R;
import com.lcsd.examines.fengtai.photoview.PhotoView;


public class ImagePageActivity extends BaseActivity {
    private ViewPager vp;
    private TextView tv;
    private String[] img;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);
        setSystemBarTransparent();
        if (getIntent() != null) {
            img = getIntent().getStringArrayExtra("imgs");
            index = getIntent().getIntExtra("index", 0);
        }

        initView();
        initData();
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
        tv = (TextView) findViewById(R.id.img_tv);
        vp = (ViewPager) findViewById(R.id.img_viewpager);
        tv.setText((index + 1) + "/" + img.length);
    }

    @Override
    protected void setListener() {

    }

    private void initData() {

        vp.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv.setText(position + 1 + "/" + img.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView view = new PhotoView(ImagePageActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            String s=img[position].substring(0,4);
                Glide.with(ImagePageActivity.this).load(img[position]).crossFade().into(view);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePageActivity.this.finish();
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.sacle_small);
    }
}
