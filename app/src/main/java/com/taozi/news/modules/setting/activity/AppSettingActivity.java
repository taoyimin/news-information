package com.taozi.news.modules.setting.activity;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.StatueUtil;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 应用推荐界面
 */
public class AppSettingActivity extends BaseActivity {
    private ViewGroup mLayout;
    private View mStatue;
    private TextView mSinaDownload,mTencentDownload;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_setting_app;
    }

    @Override
    protected void findViews() {
        mLayout = (ViewGroup) findViewById(R.id.setting_app_layout);
        mStatue = findViewById(R.id.setting_app_statue);
        mSinaDownload= (TextView) findViewById(R.id.app_btn_sina);
        mTencentDownload= (TextView) findViewById(R.id.app_btn_tencent);
    }

    @Override
    protected void initEvent() {
        mSinaDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout,"功能未实现",Snackbar.LENGTH_SHORT).show();
            }
        });
        mTencentDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout,"功能未实现",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void init() {
        //初始化状态栏
        StatueUtil.initStatue(this, mLayout, mStatue);
    }

    @Override
    protected void loadData() {

    }
}
