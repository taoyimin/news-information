package com.taozi.news.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.se7en.utils.DeviceUtils;
import com.taozi.news.R;
import com.taozi.news.adapter.CommonFragmentPagerAdapter;
import com.taozi.news.bean.ChannelManager;
import com.taozi.news.bean.Hots;
import com.taozi.news.bean.NewsChannel;
import com.taozi.news.bean.WeatherInfo;
import com.taozi.news.dao.HotsDao;
import com.taozi.news.dao.WeatherInfoDao;
import com.taozi.news.i.BaseCallBack;
import com.taozi.news.modules.commonnews.activity.CommonNewsFragment;
import com.taozi.news.modules.map.activity.MapActivity;
import com.taozi.news.modules.searchnews.activity.SearchFragment;
import com.taozi.news.util.ImageLoaderUtil;
import com.taozi.news.util.StatueUtil;
import com.taozi.news.widget.ColorFlipPagerTitleView;
import com.zxing.activity.CaptureActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Tao Yimin on 2016/10/5.
 * 主界面Fragment
 */
public class MainFragment extends BaseFragment {
    //搜索栏轮播数据集
    //private static final String[] SEARCHS = new String[]{"三星Note7停售","和一元纸币说再见", "电影票房", "扎娜为张翰庆生", "楼市", "女主播邻居报警", "1米8重30公斤", "幻城"};
    // private List<String> mSearchList = Arrays.asList(SEARCHS);
    private List<Hots> mHotsList;
    //ViewPager中的Fragment集合
    private List<Fragment> mFragmentList;
    //ViewPager适配器
    private CommonFragmentPagerAdapter mAdapter;
    //用来展示新闻列表的ViewPager
    private ViewPager mViewPager;
    //状态栏视图
    private View mStatue;
    //搜索栏视图
    private RelativeLayout mSearchView;
    //MainFragment根布局
    private ViewGroup mLayout;
    //工具栏
    private Toolbar mToolbar;
    //导航栏指示器
    private MagicIndicator magicIndicator;
    //fab菜单
    private FloatingActionsMenu mFabMenu;
    //fab菜单中的按钮
    private FloatingActionButton mFab1, mFab2, mFab3;
    //导航栏按钮
    private Button mButton;
    //头像
    public ImageView mImageView, mQrcodeBtn;
    //搜索栏和天气栏的文字显示
    private TextView mTextView1, mTextView2, mTextViewHint, mTextViewTop, mTextViewBottom;
    //用来记录搜索栏中应该显示哪个TextView
    private int flag = 0;
    //用来记录有没有状态栏
    public boolean haveStatue;
    private ViewGroup mMap;
    private int fabMenuTranslationY;
    //回掉监听接口
    private OnButtonClickListener onButtonClickListener;
    private OnSearchClickListener onSearchClickListener;
    private OnImageViewClickListener onImageViewClickListener;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int REQUEST_CODE_START_QRCODE=0;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void findViews(View view) {
        mImageView = (ImageView) view.findViewById(R.id.main_head);
        mQrcodeBtn = (ImageView) view.findViewById(R.id.main_qrcode);
        mButton = (Button) view.findViewById(R.id.main_navigation);
        magicIndicator = (MagicIndicator) view.findViewById(R.id.main_magic_indicator);
        mToolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        mLayout = (ViewGroup) view.findViewById(R.id.main_layout);
        mStatue = view.findViewById(R.id.main_statue);
        mViewPager = (ViewPager) view.findViewById(R.id.main_view_pager);
        mSearchView = (RelativeLayout) view.findViewById(R.id.main_search);
        mTextView1 = (TextView) view.findViewById(R.id.main_search_tv1);
        mTextView2 = (TextView) view.findViewById(R.id.main_search_tv2);
        mTextViewHint = (TextView) view.findViewById(R.id.main_search_hint);
        mTextViewTop = (TextView) view.findViewById(R.id.main_weather_top);
        mTextViewBottom = (TextView) view.findViewById(R.id.main_weather_bottom);
        mFabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        mFab1 = (FloatingActionButton) view.findViewById(R.id.fab_1);
        mFab2 = (FloatingActionButton) view.findViewById(R.id.fab_2);
        mFab3 = (FloatingActionButton) view.findViewById(R.id.fab_3);
        mMap = (ViewGroup) view.findViewById(R.id.main_map);
    }

    @Override
    protected void initEvent() {
        //设置导航栏按钮的点击事件
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonClick();
                }
            }
        });
        //设置搜索栏的点击事件
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchClickListener != null) {
                    onSearchClickListener.onSearchClick();
                }
            }
        });
        //设置fab的点击事件
        mFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonNewsFragment commonNewsFragment = (CommonNewsFragment) mFragmentList.get(mViewPager.getCurrentItem());
                commonNewsFragment.scrollToTop();
                mFabMenu.collapse();
            }
        });
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonNewsFragment commonNewsFragment = (CommonNewsFragment) mFragmentList.get(mViewPager.getCurrentItem());
                commonNewsFragment.refreshData();
                mFabMenu.collapse();
            }
        });
        mFab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
                mFabMenu.collapse();
            }
        });
        //设置头像的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageViewClickListener != null) {
                    onImageViewClickListener.onImageViewClick(v);
                }
            }
        });
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        mQrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    applyPermission();
                } else {
                    startQrcode();
                }
            }
        });
    }

    @Override
    protected void init() {
        //用mToolbar取代ActionBar
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //初始化状态栏
        haveStatue = StatueUtil.initStatue(getActivity(), mLayout, mStatue);
        //初始化mViewPager
        initViewPager();
        //初始化导航栏指示器
        initMagicIndicator();
        mTextView1.setAlpha(0);
        mTextViewTop.setAlpha(0);
        mTextViewBottom.setAlpha(0);
        initHead(false);
    }

    @Override
    protected void loadData() {
        //请求天气数据
        String city = "深圳";
        WeatherInfoDao.getWeatherInfo(city, new BaseCallBack() {
            @Override
            public void success(Object data) {
                WeatherInfo mWeatherInfo = (WeatherInfo) data;
                mTextViewTop.setText(mWeatherInfo.getCity() + "   " + mWeatherInfo.getType());
                if (mWeatherInfo.getLow() != null && mWeatherInfo.getHigh() != null) {
                    mTextViewBottom.setText(mWeatherInfo.getLow().replace("低温 ", "") + "~" + mWeatherInfo.getHigh().replace("高温 ", ""));
                }
                //渐显动画
                ObjectAnimator mAnimator1 = ObjectAnimator.ofFloat(mTextViewTop, "alpha", 0, 1).setDuration(500);
                ObjectAnimator mAnimator2 = ObjectAnimator.ofFloat(mTextViewBottom, "alpha", 0, 1).setDuration(500);
                mAnimator1.start();
                mAnimator2.start();
            }

            @Override
            public void failed(int errorCode, Object data) {

            }
        });

        HotsDao.getHotsList(new BaseCallBack() {
            @Override
            public void success(Object data) {
                mHotsList = (List<Hots>) data;
                //开启搜索栏动画
                startSearchAnimation(-1);
                SearchFragment searchFragment = (SearchFragment) getActivity().getSupportFragmentManager().findFragmentByTag("searchFragment");
                searchFragment.initRank(mHotsList);
            }

            @Override
            public void failed(int errorCode, Object data) {

            }
        });
    }

    /**
     * 跳转到二维码扫描界面
     */
    public void startQrcode() {
        Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(openCameraIntent, REQUEST_CODE_START_QRCODE);
    }

    /**
     * 申请相机权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void applyPermission() {
        int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        startQrcode();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_START_QRCODE) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Snackbar.make(mMap, scanResult, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 申请结果的回掉
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    startQrcode();
                } else {
                    // Permission Denied
                    Snackbar.make(mLayout, "拒绝授权可能会影响您的体验", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 开启搜索栏的轮播动画
     *
     * @param index 数据集中的下标,采用递归的方法让搜索栏一直轮播,-1表示第一次进入该方法
     */
    private void startSearchAnimation(int index) {
        if (index == -1) {
            //第一次进来时将mTextViewHint中的文字变成指定文字
            ObjectAnimator mAnimator = ObjectAnimator.ofFloat(mTextViewHint, "alpha", 0, 1).setDuration(500);
            mAnimator.setStartDelay(2000);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mTextViewHint.setText("大家都在搜:");
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimator.start();
        }
        index++;
        //播完一轮后把index设为0
        if (index > mHotsList.size() - 1) {
            index = 0;
        }
        //需要播放的文字
        String str = mHotsList.get(index).getTitle();
        //flag=0表示展示mTextView1,flag=1表示展示mTextView2
        if (flag == 0) {
            //flag设置为1,下次让mTextView2展示
            flag = 1;
            mTextView1.setText(str);
            //mTextView1渐显,mTextView2渐隐
            ObjectAnimator mAnimator1 = ObjectAnimator.ofFloat(mTextView1, "alpha", 0, 1).setDuration(500);
            ObjectAnimator mAnimator2 = ObjectAnimator.ofFloat(mTextView2, "alpha", 1, 0).setDuration(500);
            final int finalIndex = index;
            mAnimator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //递归调用
                    startSearchAnimation(finalIndex);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimator1.setStartDelay(2000);
            mAnimator2.setStartDelay(2000);
            mAnimator1.start();
            mAnimator2.start();
        } else if (flag == 1) {
            //flag设置为0,下次让mTextView1展示
            flag = 0;
            mTextView2.setText(str);
            //mTextView2渐显,mTextView1渐隐
            ObjectAnimator mAnimator1 = ObjectAnimator.ofFloat(mTextView1, "alpha", 1, 0).setDuration(500);
            ObjectAnimator mAnimator2 = ObjectAnimator.ofFloat(mTextView2, "alpha", 0, 1).setDuration(500);
            final int finalIndex = index;
            mAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //递归调用
                    startSearchAnimation(finalIndex);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimator1.setStartDelay(2000);
            mAnimator2.setStartDelay(2000);
            mAnimator1.start();
            mAnimator2.start();
        }
    }

    /**
     * 弹出分享菜单
     */
    private void showShare() {
        //ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("测试文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.baidu.com");
        // 启动分享GUI
        oks.show(getContext());
    }

    //以下是对外公开的方法与接口

    /**
     * 初始化导航栏指示器
     */
    public void initMagicIndicator() {
        List<NewsChannel> mChannelList = ChannelManager.getIntance().getmSubscibeChannelList();
        //设置指示器颜色
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        //初始化导航栏
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.65f);
        //导航栏可滑动
        commonNavigator.setAdjustMode(false);
        //设置导航栏适配器
        final List<NewsChannel> finalMChannelList = mChannelList;
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                //导航栏栏目个数
                return finalMChannelList == null ? 0 : finalMChannelList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                //导航栏文字
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                //文字设置为粗体
                simplePagerTitleView.getPaint().setFakeBoldText(true);
                //设置文字
                simplePagerTitleView.setText(finalMChannelList.get(index).getName());
                //设置选中和未选中状态的文字颜色
                simplePagerTitleView.setNormalColor(Color.parseColor("#55ffffff"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));
                //设置监听
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //初始化指示器
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //指示器模式
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                //指示器高度
                indicator.setLineHeight(DeviceUtils.dip2px(4));
                //指示器宽度
                indicator.setLineWidth(DeviceUtils.dip2px(20));
                //指示器圆角
                indicator.setRoundRadius(DeviceUtils.dip2px(2));
                //指示器颜色
                indicator.setColors(Color.parseColor("#ffffff"));
                //给指示器设置插值器
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                return indicator;
            }
        });
        //和mViewPager绑定
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
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

    /**
     * 初始化mViewPager
     */
    public void initViewPager() {
        mFragmentList = new ArrayList<>();
        List<NewsChannel> mChannelList = ChannelManager.getIntance().getmSubscibeChannelList();
        for (int i = 0; i < mChannelList.size(); i++) {
            NewsChannel newsChannel = mChannelList.get(i);
            final CommonNewsFragment commonNewsFragment = new CommonNewsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", newsChannel.getId());
            commonNewsFragment.setArguments(bundle);
            commonNewsFragment.setOnRecyclerViewScrollListener(new CommonNewsFragment.OnRecyclerViewScrollListener() {
                @Override
                public void onRecyclerScroll(int dy) {
                    //判断是否收起fabMenu
                    if (commonNewsFragment.isCollapseFam && mFabMenu.isExpanded()) {
                        mFabMenu.collapse();
                    }
                    fabMenuTranslationY += dy;
                    //让fabMenu一起滑动
                    if (fabMenuTranslationY >= 0 && fabMenuTranslationY <= 300) {
                        mFabMenu.setTranslationY(fabMenuTranslationY);
                    } else if (fabMenuTranslationY > 300) {
                        fabMenuTranslationY = 300;
                        mFabMenu.setTranslationY(fabMenuTranslationY);
                    } else if (fabMenuTranslationY < 0) {
                        fabMenuTranslationY = 0;
                        mFabMenu.setTranslationY(fabMenuTranslationY);
                    }
                }
            });
            mFragmentList.add(commonNewsFragment);
        }
        mAdapter = new CommonFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 导航栏按钮点击事件的回掉接口
     */
    public interface OnButtonClickListener {
        void onButtonClick();
    }

    /**
     * 设置导航栏按钮的点击监听
     *
     * @param onButtonClickListener
     */
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    /**
     * 搜索栏点击事件的回掉接口
     */
    public interface OnSearchClickListener {
        void onSearchClick();
    }

    /**
     * 设置搜索栏的点击监听
     *
     * @param onSearchClickListener
     */
    public void setOnSearchClickListener(OnSearchClickListener onSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener;
    }

    /**
     * mImageView点击事件的回掉接口
     */
    public interface OnImageViewClickListener {
        void onImageViewClick(View view);
    }

    /**
     * 设置mImageView的点击事件监听
     *
     * @param onImageViewClickListener
     */
    public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
        this.onImageViewClickListener = onImageViewClickListener;
    }

    /**
     * 将头像隐藏
     */
    public void hideHeadImageView() {
        mImageView.setVisibility(View.INVISIBLE);
    }

    /**
     * 将头像显示
     */
    public void showHeadImageView() {
        mImageView.setVisibility(View.VISIBLE);
    }
}

