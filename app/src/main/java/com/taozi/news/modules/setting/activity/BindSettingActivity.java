package com.taozi.news.modules.setting.activity;

import android.view.View;
import android.view.ViewGroup;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.StatueUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 绑定社交账号界面
 */
public class BindSettingActivity extends BaseActivity {
    private ViewGroup mLayout;
    private View mStatue;
    private ViewGroup mQQLayout,mSinaLayout;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_setting_bind;
    }

    @Override
    protected void findViews() {
        mLayout = (ViewGroup) findViewById(R.id.setting_bind_layout);
        mStatue = findViewById(R.id.setting_bind_statue);
        mQQLayout= (ViewGroup) findViewById(R.id.bind_btn_qq);
        mSinaLayout= (ViewGroup) findViewById(R.id.bind_btn_sina);
    }

    @Override
    protected void initEvent() {
        mQQLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });
                //authorize与showUser单独调用一个即可
                //qq.authorize();//单独授权,OnComplete返回的hashmap是空的
                qq.showUser(null);//授权并获取用户信息
                //移除授权
                //weibo.removeAccount(true);
            }
        });
        mSinaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });
                //authorize与showUser单独调用一个即可
                //weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
                weibo.showUser(null);//授权并获取用户信息
                //移除授权
                //weibo.removeAccount(true);
            }
        });
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
