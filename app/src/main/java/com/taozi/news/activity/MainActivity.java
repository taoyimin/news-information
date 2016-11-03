package com.taozi.news.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.se7en.utils.DeviceUtils;
import com.taozi.news.R;
import com.taozi.news.modules.navigation.activity.NavigationFragment;
import com.taozi.news.modules.searchnews.activity.SearchFragment;
import com.taozi.news.modules.userinfo.activity.UserinfoFragment;
import com.taozi.news.util.ImageLoaderUtil;
import com.taozi.news.util.ScaleUtil;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseActivity {
    //Fragment管理器
    private FragmentManager mManager;
    //Fragment事务
    private FragmentTransaction mTransaction;
    //主页面Fragment
    private MainFragment mainFragment;
    //搜索页Fragment
    private SearchFragment searchFragment;
    //增删导航栏Fragment
    private NavigationFragment navigationFragment;
    //用户信息页Fragment
    private UserinfoFragment userinfoFragment;
    //用来记录按返回键时,是要退出程序还是返回到Fragment
    public boolean isExit = true;
    //用来记录当前正在展示的Fragment
    private Fragment currentFragment;
    //用来记录物理返回键按下的时间点
    private long exitTime = 0;
    //用户头像
    private ImageView mImageView;
    //用户头像视图的坐标
    private float x, y;


    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 允许使用transitions
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // 设置一个exit transition
            getWindow().setExitTransition(new Fade());
            //getWindow().setEnterTransition(new Explode());
        }
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        //初始化MainActivity中的Fragment
        initFragment();
        mImageView = (ImageView) findViewById(R.id.main_activity_head);
    }

    @Override
    protected void initEvent() {
        //设置导航栏按钮的点击事件
        mainFragment.setOnButtonClickListener(new MainFragment.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                changeFragment(mainFragment, navigationFragment);
                isExit = false;
            }
        });
        //设置搜索栏的点击事件按钮
        mainFragment.setOnSearchClickListener(new MainFragment.OnSearchClickListener() {
            @Override
            public void onSearchClick() {
                changeFragment(mainFragment, searchFragment);
                isExit = false;
            }
        });
        //设置头像的点击事件
        mainFragment.setOnImageViewClickListener(new MainFragment.OnImageViewClickListener() {
            @Override
            public void onImageViewClick(View view) {
                //获得MainFragment中头像的坐标
                x = view.getX() + view.getWidth() / 2;
                y = view.getY() + view.getHeight() / 2;
                //设置MainActivity中的头像坐标
                if (mainFragment.haveStatue) {
                    mImageView.setY(view.getY());
                } else {
                    mImageView.setY(view.getY() + DeviceUtils.getStatuBarHeight());
                }
                mImageView.setX(view.getX());
                //开启放大动画
                if (userinfoFragment.haveStatue) {
                    startImageViewAnimation(x, y, DeviceUtils.getScreenWdith() / 2, DeviceUtils.dip2px(118) + DeviceUtils.getStatuBarHeight(), 1f, 2f, false);
                } else {
                    startImageViewAnimation(x, y, DeviceUtils.getScreenWdith() / 2, DeviceUtils.dip2px(118) - DeviceUtils.getStatuBarHeight(), 1f, 2f, false);
                }
                changeFragment(mainFragment, userinfoFragment);
                isExit = false;
            }
        });
    }

    /**
     * 头像缩放动画
     *
     * @param beforeX 缩放前的X轴坐标
     * @param beforeY 缩放前的Y轴坐标
     * @param afterX  缩放后的X轴坐标
     * @param afterY  缩放后的Y轴坐标
     * @param from    缩放前的比例
     * @param to      缩放后的比例
     * @param hide    是否在动画结束后隐藏MainActivity中的头像
     */
    private void startImageViewAnimation(float beforeX, float beforeY, float afterX, float afterY, float from, float to, final boolean hide) {
        //构建动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(from, to, from, to, Animation.ABSOLUTE,
                ScaleUtil.getPoivtX(mImageView, beforeX, afterX, from, to), Animation.ABSOLUTE,
                ScaleUtil.getPoivtY(mImageView, beforeY, afterY, from, to));
        //和Fragment切换动画的时间保持一致
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!hide) {
                    //将MainActivity中的头像显示出来,MainFragment中的头像隐藏
                    mImageView.setVisibility(View.VISIBLE);
                    mainFragment.hideHeadImageView();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hide) {
                    mImageView.clearAnimation();
                    mImageView.setVisibility(View.INVISIBLE);
                    mainFragment.showHeadImageView();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(scaleAnimation);
    }

    @Override
    protected void init() {
        ShareSDK.initSDK(this);
        //初始化用户头像
        initHead(false);
    }

    @Override
    protected void loadData() {

    }

    /**
     * 初始化MainActivity中的Fragment
     */
    private void initFragment() {
        mainFragment = new MainFragment();
        searchFragment = new SearchFragment();
        navigationFragment = new NavigationFragment();
        userinfoFragment = new UserinfoFragment();
        mManager = getSupportFragmentManager();
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, userinfoFragment, "userinfoFragment");
        mTransaction.hide(userinfoFragment);
        mTransaction.add(R.id.fragment_container, navigationFragment, "navigationFragment");
        mTransaction.hide(navigationFragment);
        mTransaction.add(R.id.fragment_container, searchFragment, "searchFragment");
        mTransaction.hide(searchFragment);
        mTransaction.add(R.id.fragment_container, mainFragment, "mainFragment");
        currentFragment = mainFragment;
        mTransaction.commit();
    }

/*    *//**
     * 创建Toolbar的菜单
     *
     * @param menu
     * @return
     *//*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    *//**
     * Toolbar菜单的监听
     *
     * @param item
     * @return
     *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            mainFragment.mImageView.performClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * 重写MainActivity的onBackPressed方法
     */
    @Override
    public void onBackPressed() {
        if (isExit) {
            //两秒之内连按两次返回则退出程序
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                View view = mainFragment.getView().findViewById(R.id.main_coordinatorLayout);
                Snackbar.make(view, "再按一次退出程序", Snackbar.LENGTH_SHORT).setAction("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            //从当前Fragment返回到mainFragment
            changeFragment(currentFragment, mainFragment);
            isExit = true;
        }
    }

    /**
     * 切换Fragment
     *
     * @param oldFragment 旧的Fragment实例
     * @param newFragment 新的Fragment实例
     */
    public void changeFragment(Fragment oldFragment, Fragment newFragment) {
        mManager = getSupportFragmentManager();
        mTransaction = mManager.beginTransaction();
        mTransaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        mTransaction.hide(oldFragment);
        mTransaction.show(newFragment);
        mTransaction.commit();
        currentFragment = newFragment;
        //如果newFragment是UserinfoFragment的实例,则在切换时要更新缓存大小
        if (newFragment instanceof UserinfoFragment) {
            userinfoFragment.upDataCacheSize();
        }
        //如果oldFragment是UserinfoFragment的实例,则在切换Fragment的同时要开始头像缩小的动画
        if (oldFragment instanceof UserinfoFragment) {
            if (userinfoFragment.haveStatue) {
                startImageViewAnimation(DeviceUtils.getScreenWdith() / 2, DeviceUtils.dip2px(118) + DeviceUtils.getStatuBarHeight(), x, y, 2f, 1f, true);
            } else {
                startImageViewAnimation(DeviceUtils.getScreenWdith() / 2, DeviceUtils.dip2px(118) - DeviceUtils.getStatuBarHeight(), x, y, 2f, 1f, true);
            }
        }
        if (newFragment instanceof SearchFragment) {
            searchFragment.showSoftInput();
        }
        if (oldFragment instanceof SearchFragment) {
            searchFragment.clearEditText();
        }
    }

    /**
     * 初始化头像
     * @param change 头像是否改变
     */
    public void initHead(boolean change){
        File outputImages = new File(Environment.getExternalStorageDirectory() + "/News", "head.png");
        if(outputImages.exists()) {
            if (change) {
                DiskCacheUtils.removeFromCache("file://" + outputImages.getAbsolutePath(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache("file://" + outputImages.getAbsolutePath(), ImageLoader.getInstance().getMemoryCache());
            }
            ImageLoader.getInstance().displayImage("file://"+outputImages.getAbsolutePath(),mImageView, ImageLoaderUtil.getCircleBitmapOption(Color.WHITE, 5));
        }else{
            mImageView.setImageResource(R.mipmap.navi_profile_default);
        }
    }
}
