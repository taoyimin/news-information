package com.taozi.news.modules.userlogin.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.activity.MainActivity;
import com.taozi.news.util.StatueUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by Tao Yimin on 2016/10/11.
 */
public class LoginActivity extends BaseActivity {
    private ViewGroup mLayout;
    private View mStatue;
    private Button mLoginButton;
    private ImageView mWechatImageView, mQQImageView, mSinaImageView, mMiImageView;
    private TextView mTouristBtn, mRegisterBtn;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void findViews() {
        mLayout = (ViewGroup) findViewById(R.id.user_login_layout);
        mStatue = findViewById(R.id.user_login_statue);
        mLoginButton = (Button) findViewById(R.id.user_login_btn);
        mWechatImageView = (ImageView) findViewById(R.id.user_wechat_btn);
        mQQImageView = (ImageView) findViewById(R.id.user_qq_btn);
        mSinaImageView = (ImageView) findViewById(R.id.user_sina_btn);
        mMiImageView = (ImageView) findViewById(R.id.user_mi_btn);
        mTouristBtn = (TextView) findViewById(R.id.user_tourist_btn);
        mRegisterBtn = (TextView) findViewById(R.id.user_register_btn);
    }

    @Override
    protected void initEvent() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout, "无法连接到服务器", Snackbar.LENGTH_SHORT).show();
            }
        });
        mWechatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout, "暂不支持微信登录", Snackbar.LENGTH_SHORT).show();
            }
        });
        mQQImageView.setOnClickListener(new View.OnClickListener() {
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
        mSinaImageView.setOnClickListener(new View.OnClickListener() {
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
        mMiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout, "暂不支持小米登录", Snackbar.LENGTH_SHORT).show();
            }
        });
        mTouristBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout, "无法连接到服务器", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void init() {
        StatueUtil.initStatue(this, mLayout, mStatue);
    }

    @Override
    protected void loadData() {

    }
}
