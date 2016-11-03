package com.taozi.news.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tao Yimin on 2016/10/13.
 * 使用单例模式管理用户设置
 */
public class UserSettingManager {
    private static Context mContext;
    private static UserSettingManager instance;
    private int fontSize;
    private int imageMode;
    private boolean notifyPush;
    private boolean pushSound;
    private boolean collectShare;
    private boolean subscibeShare;
    private boolean commentShare;

    private UserSettingManager() {
    }

    public static UserSettingManager getIntance() {
        if (instance == null) {
            synchronized (UserSettingManager.class) {
                if (instance == null) {
                    instance = new UserSettingManager();
                    initUserSetting();
                }
            }
        }
        return instance;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getImageMode() {
        return imageMode;
    }

    public void setImageMode(int imageMode) {
        this.imageMode = imageMode;
    }

    public boolean isNotifyPush() {
        return notifyPush;
    }

    public void setNotifyPush(boolean notifyPush) {
        this.notifyPush = notifyPush;
    }

    public boolean isPushSound() {
        return pushSound;
    }

    public void setPushSound(boolean pushSound) {
        this.pushSound = pushSound;
    }

    public boolean isCollectShare() {
        return collectShare;
    }

    public void setCollectShare(boolean collectShare) {
        this.collectShare = collectShare;
    }

    public boolean isSubscibeShare() {
        return subscibeShare;
    }

    public void setSubscibeShare(boolean subscibeShare) {
        this.subscibeShare = subscibeShare;
    }

    public boolean isCommentShare() {
        return commentShare;
    }

    public void setCommentShare(boolean commentShare) {
        this.commentShare = commentShare;
    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 初始化用户设置
     */
    private static void initUserSetting() {
        SharedPreferences sp = mContext.getSharedPreferences("userSetting", Context.MODE_PRIVATE);
        instance.setFontSize(sp.getInt("fontSize", 1));
        instance.setImageMode(sp.getInt("imageMode", 0));
        instance.setNotifyPush(sp.getBoolean("notifyPush",true));
        instance.setPushSound(sp.getBoolean("pushSound",true));
        instance.setCollectShare(sp.getBoolean("collectShare",false));
        instance.setSubscibeShare(sp.getBoolean("subscibeShare",false));
        instance.setCommentShare(sp.getBoolean("commentShare",false));
    }

    /**
     * 字体大小
     */
    public interface FONT_SIZE {
        //小
        int SAMLL = 0;
        //中
        int MEDIUM = 1;
        //大
        int BIG = 2;
        //超大
        int HUGE = 3;
    }

    /**
     * 图片模式
     */
    public interface IMAGE_MODE {
        //显示图片
        int NORMAL = 0;
        //智能显示
        int ADJUST = 1;
        //不显示图片
        int NO_IMAGE = 2;
        //仅在WIFI下显示图片
        int ONLY_WIFI = 3;
    }
}
