package com.taozi.news.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.se7en.utils.DeviceUtils;

/**
 * Created by Tao Yimin on 2016/10/7.
 * 自定义FloatingActionsMenu的Behavior
 */
public class FamBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {

    public FamBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        //如果dependency是Snackbar.SnackbarLayout的实例，说明它就是我们所需要的Dependency
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    //每次dependency位置发生变化，都会执行onDependentViewChanged方法
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu fam, View dependency) {
        //根据dependency的位置，设置FloatingActionsMenu的位置
        float y = dependency.getTranslationY();
        float famTranslationY=fam.getTranslationY();
        if(famTranslationY> DeviceUtils.dip2px(20)&&famTranslationY<=300)
            return true;
        fam.setTranslationY(y - dependency.getHeight());
        return true;
    }
}
