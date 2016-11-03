package com.taozi.news.modules.commonnews.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tao Yimin on 2016/10/13.
 * 格式化时间工具类
 */
public class FormatDataUtil {
    public static String FormatData(String str) {
        String time = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = new Date();
            long l = now.getTime() - df.parse(str).getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day > 0) {
                time = day + "天前";
            } else if (hour > 0) {
                time = hour + "小时前";
            } else if (min > 0) {
                time = min + "分钟前";
            } else {
                time = "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
