package com.taozi.news.modules.commonnews.activity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taozi.news.R;
import com.taozi.news.activity.BaseActivity;
import com.taozi.news.util.ImageLoaderUtil;

/**
 * Created by Tao Yimin on 2016/10/2.
 * 新闻详情页
 */
public class CommonNewsActivity extends BaseActivity {
    private WebView mWebView;
    private ImageView mImageView;
    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton mFab;
    private NestedScrollView mScrollView;
    private boolean isAnimation;

    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 允许使用transitions
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // 设置一个exit transition
            //getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Fade());
        }
        return R.layout.activity_common_news;
    }

    @Override
    protected void findViews() {
        mWebView = (WebView) findViewById(R.id.web_view);
        mImageView = (ImageView) findViewById(R.id.backdrop);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.news_fab);
        mScrollView = (NestedScrollView) findViewById(R.id.news_scrollView);
    }

    @Override
    protected void initEvent() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "收藏成功", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void init() {
        //初始化状态栏
        initStatue();
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        ImageLoader.getInstance().displayImage(imageUrl, mImageView, ImageLoaderUtil.getDefaultOption());
        mToolbarLayout.setTitle(title);
        // 设置js可用
        mWebView.getSettings().setJavaScriptEnabled(true);
        initWeb();
        mWebView.loadUrl(url);
        fabAnimation(true);
    }

    /**
     * 初始化状态栏
     */
    private void initStatue() {
        if (Build.VERSION.SDK_INT >= 21) {
            //系统版本大于5.0则启用沉浸式状态栏
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 初始化WebView
     */
    private void initWeb() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //开始加载页面的回调
                mScrollView.setAlpha(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //结束加载页面的回调
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //返回加载的进度
                if (newProgress > 10 && !isAnimation) {
                    isAnimation = true;
                    Animation animation = AnimationUtils.loadAnimation(CommonNewsActivity.this, R.anim.news_content_bottom_in);
                    mScrollView.setAlpha(1);
                    mScrollView.startAnimation(animation);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        mWebView.setAlpha(0);
        fabAnimation(false);
        super.onBackPressed();
    }

    public void fabAnimation(final boolean isEnter) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = animation.getAnimatedFraction();
                if (isEnter) {
                    mFab.setTranslationX(300 - 300 * progress);
                } else {
                    mFab.setTranslationX(300 - 300 * (1 - progress));
                }
            }
        });
        if (isEnter) {
            animator.setInterpolator(new OvershootInterpolator());
            animator.setStartDelay(300);
            mFab.setTranslationX(300);
        } else {
            animator.setInterpolator(new AccelerateInterpolator());
        }
        animator.start();
    }
}
