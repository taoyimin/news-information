package com.taozi.news.util;

import android.view.View;

/**
 * Created by Tao Yimin on 2016/10/8.
 * 用于获取缩放动画交点的工具类
 */
public class ScaleUtil {
    /**
     * 获得交点的X轴坐标
     *
     * @param view    需要做动画的View
     * @param beforeX 缩放前的X轴坐标
     * @param afterX  缩放后的X轴坐标
     * @param from    X轴缩放前的比例
     * @param to      X轴缩放后的比例
     * @return
     */
    public static float getPoivtX(View view, float beforeX, float afterX, float from, float to) {
        float poivtX = 0f;
        poivtX = (to / from * beforeX - afterX) / (to / from - 1f) - view.getLeft();
        return poivtX;
    }

    /**
     * 获得交点的Y轴坐标
     *
     * @param view    需要做动画的View
     * @param beforeY 缩放前的Y轴坐标
     * @param afterY  缩放后的Y轴坐标
     * @param from    Y轴缩放前的比例
     * @param to      Y轴缩放后的比例
     * @return
     */
    public static float getPoivtY(View view, float beforeY, float afterY, float from, float to) {
        float poivtY = 0f;
        poivtY = (to / from * beforeY - afterY) / (to / from - 1f) - view.getTop();
        return poivtY;
    }
}
