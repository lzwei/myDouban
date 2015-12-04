package com.samli.douban.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.samli.douban.R;
import com.samli.douban.netUtils.MyNetUtil;
import com.samli.douban.netUtils.ResultCallback;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        //把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.leftmenu);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void test(View view){
        Map<String,String> params=new HashMap<String,String>();
        params.put("email","sam.li@accentrix.com");
        params.put("password","123456");
        params.put("language","ZH_CN");
        params.put("providerId","1027ab3c-4072-4829-b83c-d64c35de84ec");

        MyNetUtil.getInstance().getUser(params, new MyResultCallback<String>() {
        });
    }


    public abstract class MyResultCallback<T> extends ResultCallback<String>{
        @Override
        public void onError(Request request, Exception e) {
            Log.e("TAG", "onError , e = " + e.getMessage());
        }

        @Override
        public void onResponse(String response) {
            Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAfter() {
            super.onAfter();
            tv.setText("onAfter");
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            tv.setText("onBefore");
        }
    }

}
