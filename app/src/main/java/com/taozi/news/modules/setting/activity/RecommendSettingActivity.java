package com.taozi.news.modules.setting.activity;

import android.view.View;
import android.view.ViewGroup;

import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.StatueUtil;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 推荐好友界面
 */
public class RecommendSettingActivity extends BaseActivity {
    private ViewGroup mLayout;
    private View mStatue;
    private ViewGroup mFriendsLayout,mWechatLayout,mQQLayout,mTencentLayout,mSinaLayout,mQzoneLayout,mMailLayout,mSmsLayout;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_setting_recommend;
    }

    @Override
    protected void findViews() {
        mLayout = (ViewGroup) findViewById(R.id.setting_recommend_layout);
        mStatue = findViewById(R.id.setting_recommend_statue);
        mFriendsLayout= (ViewGroup) findViewById(R.id.recommend_btn_friends);
        mWechatLayout= (ViewGroup) findViewById(R.id.recommend_btn_wechat);
        mQQLayout= (ViewGroup) findViewById(R.id.recommend_btn_qq);
        mTencentLayout= (ViewGroup) findViewById(R.id.recommend_btn_tencent);
        mSinaLayout= (ViewGroup) findViewById(R.id.recommend_btn_sina);
        mQzoneLayout= (ViewGroup) findViewById(R.id.recommend_btn_qzone);
        mMailLayout= (ViewGroup) findViewById(R.id.recommend_btn_mail);
        mSmsLayout= (ViewGroup) findViewById(R.id.recommend_btn_sms);
    }

    @Override
    protected void initEvent() {
        mFriendsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform wechatmoments = ShareSDK.getPlatform(WechatMoments.NAME);
                // 执行图文分享
                wechatmoments.share(sp);
            }
        });
        mWechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                // 执行图文分享
                wechat.share(sp);
            }
        });
        mQQLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQ.ShareParams sp = new QQ.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                // 执行图文分享
                qq.share(sp);
            }
        });
        mTencentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TencentWeibo.ShareParams sp=new TencentWeibo.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform weibo=ShareSDK.getPlatform(TencentWeibo.NAME);
                weibo.share(sp);
            }
        });
        mSinaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                // 执行图文分享
                weibo.share(sp);
            }
        });
        mQzoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QZone.ShareParams sp = new QZone.ShareParams();
                sp.setTitle("测试分享的标题");
                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                sp.setSite("发布分享的网站名称");
                sp.setSiteUrl("发布分享网站的地址");
                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
                // 执行图文分享
                qzone.share(sp);
            }
        });
        mMailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.ShareParams sp = new Email.ShareParams();
                sp.setText("测试分享的文本");
                sp.setImageUrl("http://img2.imgtn.bdimg.com/it/u=1996540642,2883341744&fm=21&gp=0.jpg");
                Platform email = ShareSDK.getPlatform(Email.NAME);
                // 执行图文分享
                email.share(sp);
            }
        });
        mSmsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortMessage.ShareParams sp = new ShortMessage.ShareParams();
                sp.setText("测试分享的文本");
                Platform message = ShareSDK.getPlatform(ShortMessage.NAME);
                // 执行图文分享
                message.share(sp);
            }
        });
    }

    @Override
    protected void init() {
        //初始化状态栏
        StatueUtil.initStatue(this, mLayout, mStatue);
        ShareSDK.initSDK(this);
    }

    @Override
    protected void loadData() {

    }
}
