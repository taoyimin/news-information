package com.taozi.news.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.se7en.utils.DeviceUtils;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 初始化状态栏工具类
 */
public class StatueUtil {
    /**
     * 初始化状态栏
     *
     * @param activity activity实例
     * @param root     根布局
     * @param statue   状态栏布局
     * @return true表示没有启用沉浸式状态栏,false表示启用了沉浸式状态栏
     */
    public static boolean initStatue(Activity activity, ViewGroup root, View statue) {
        //获取系统状态栏的高度
        int statueHeight = DeviceUtils.getStatuBarHeight();
        //将mStatue布局的高度设置为系统状态栏的高度
        ViewGroup.LayoutParams layoutParams = statue.getLayoutParams();
        layoutParams.height = statueHeight;
        statue.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= 21) {
            //系统版本大于5.0则启用沉浸式状态栏
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            return false;
        } else {
            //否则不启用沉浸式状态栏,并将mStatue移除
            root.removeView(statue);
            return true;
        }
    }
}
