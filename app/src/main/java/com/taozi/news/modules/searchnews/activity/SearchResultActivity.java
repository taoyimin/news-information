package com.taozi.news.modules.searchnews.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.StatueUtil;

/**
 * Created by Tao Yimin on 2016/10/11.
 * 搜索结果页面
 */
public class SearchResultActivity extends BaseActivity {
    private ViewGroup mLayout;
    private View mStatue;
    private TextView mTextView;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void findViews() {
        mLayout = (ViewGroup) findViewById(R.id.search_result_layout);
        mStatue = findViewById(R.id.search_result_statue);
        mTextView= (TextView) findViewById(R.id.search_result_tv);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {
        StatueUtil.initStatue(this, mLayout, mStatue);
        mTextView.setText("没有找到和“"+getIntent().getStringExtra("text")+"”相关的结果");
    }

    @Override
    protected void loadData() {

    }
}
