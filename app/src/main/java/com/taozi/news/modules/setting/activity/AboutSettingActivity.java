package com.taozi.news.modules.setting.activity;

import android.view.View;
import android.view.ViewGroup;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.StatueUtil;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 关于界面
 */
public class AboutSettingActivity extends BaseActivity{
    private ViewGroup mLayout;
    private View mStatue;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_setting_about;
    }

    @Override
    protected void findViews() {
        mLayout= (ViewGroup) findViewById(R.id.setting_about_layout);
        mStatue=findViewById(R.id.setting_about_statue);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {
        //初始化状态栏
        StatueUtil.initStatue(this,mLayout,mStatue);
    }

    @Override
    protected void loadData() {

    }
}
