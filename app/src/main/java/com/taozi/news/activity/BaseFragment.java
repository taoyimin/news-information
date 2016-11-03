package com.taozi.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 所有fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentViewId(), container, false);
        findViews(view);
        initEvent();
        init();
        loadData();
        return view;
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int setContentViewId();

    /**
     * 初始化控件
     *
     * @param view
     */
    protected abstract void findViews(View view);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 初始化界面
     */
    protected abstract void init();

    /**
     * 加载数据
     */
    protected abstract void loadData();

}

