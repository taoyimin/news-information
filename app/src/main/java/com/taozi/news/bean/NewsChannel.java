package com.taozi.news.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 新闻频道实体类
 */
@Table(name = "NewsChannel")
public class NewsChannel implements Serializable {
    @Column(name = "_id", isId = true)
    private int _id;
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "icon")
    private int icon;
    @Column(name = "isSubscibe")
    private boolean isSubscibe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSubscibe() {
        return isSubscibe;
    }

    public void setSubscibe(boolean subscibe) {
        isSubscibe = subscibe;
    }

    public interface ID {
        //热点
        int HOTSPOT = 1;
        //娱乐
        int ENTERTAINMENT = 3;
        //社会
        int SOCIETY = 4;
        //网红
        int WEBCELEBRITY = 5;
        //互联网
        int INTERNET=6;
        //汽车
        int CAR = 7;
        //体育
        int SPORT = 8;
        //财经
        int FINANCE = 9;
        //军事
        int MILITARY=10;
        //搞笑
        int FUNNY = 11;
        //美女
        int BELLE = 12;
        //GIF
        int GIF=13;
        //图片
        int IMAGE=14;
        //情感
        int EMOTION = 15;
        //时尚
        int FASHION = 16;
        //NBA
        int NBA = 17;
        //房产
        int HOUSE=18;
        //国际
        int INTERNATIONAL = 19;
        //历史
        int HISTORY = 20;
        //萌宠
        int CUTEPET = 21;
        //养生
        int HEALTH = 23;
        //星座
        int CONSTELLATION=24;
        //电影
        int MOIVE = 25;
        //育儿
        int PARENTING=26;
        //数码控
        int DIGITAL=27;
        //美食咖
        int FOOD=28;
        //教育
        int EDUCATION=29;
        //趣闻
        int INTERESTING = 30;
        //科技
        int TECHNOLOGY = 31;
        //游戏
        int GAME=32;
        //二次元
        int ACG=33;
        //故事
        int STORY=34;
        //探索
        int EXPLORE=35;
        //美文
        int FICTION=36;
        //交通
        int TRAFFIC = 72;
        //歪楼
        int CROOKEDHOUSE=73;
        //管理
        int MANAGER=74;
    }
}
