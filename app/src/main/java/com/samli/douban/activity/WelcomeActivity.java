package com.samli.douban.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.samli.douban.R;

import java.util.ArrayList;

/**
 * Created by sam.li on 2015/12/4.
 */
public class WelcomeActivity extends BaseActivity {

    private ViewPager viewPager;
    private int currentIndex;
    private ArrayList<View> views;
    private static final int[] pics = { R.mipmap.bg01, R.mipmap.bg02,
            R.mipmap.bg03 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        findViewById();
        initView();
        initData();

    }

    @Override
    protected void findViewById() {
        viewPager= (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        views=new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for(int i=0;i<pics.length;i++) {
            ImageView im = new ImageView(this);
            im.setLayoutParams(mParams);
            im.setScaleType(ImageView.ScaleType.FIT_XY);
            im.setImageResource(pics[i]);
            views.add(im);
        }
        viewPager.setAdapter(new ViewPagerAdapter(views));
    }

    private class ViewPagerAdapter extends PagerAdapter{

        private ArrayList<View> views;
        public ViewPagerAdapter(ArrayList<View> views){
            this.views=views;
        }

        /**
         * 获得当前界面数
         */
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        /**
         * 判断是否由对象生成界面
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }


        /**
         * 销毁position位置的界面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        /**
         * 初始化position位置的界面
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }
    }
}
