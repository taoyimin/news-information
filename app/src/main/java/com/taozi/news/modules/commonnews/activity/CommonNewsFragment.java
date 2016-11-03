package com.taozi.news.modules.commonnews.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taozi.news.R;
import com.taozi.news.activity.BaseFragment;
import com.taozi.news.bean.UserSettingManager;
import com.taozi.news.i.BaseCallBack;
import com.taozi.news.modules.commonnews.adatper.CommonNewsRecyclerViewAdapter;
import com.taozi.news.modules.commonnews.bean.CommonNews;
import com.taozi.news.modules.commonnews.dao.CommonNewsDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 新闻列表页
 */
public class CommonNewsFragment extends BaseFragment {
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresh;
    private CommonNewsRecyclerViewAdapter mAdapter;
    //用于记录页数
    private int page = 0;
    //数据集
    private List<CommonNews> mList;
    //用于记录是刷新还是加载跟多
    private boolean isRefresh = true;
    //用于记录是否正在加载
    private boolean isLoading = false;
    //当剩下的item小于或等于REMAIN个时加载更多
    private static final int REMAIN = 2;
    //用于记录新闻频道
    private int id;
    private int lastDy;
    //用于记录是否需要关闭fam
    public boolean isCollapseFam;
    //字体大小和图片显示模式
    private int fontSzie;
    private int imageMode;
    private OnRecyclerViewScrollListener onRecyclerViewScrollListener;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_common_recycler;
    }

    @Override
    protected void findViews(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.content_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    }

    @Override
    protected void initEvent() {
        mList = new ArrayList<>();
        mAdapter = new CommonNewsRecyclerViewAdapter(getActivity(), mList);
        //设置适配器的监听
        setAdapterListener(mAdapter);
        //设置mRefresh的刷新监听
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page++;
                loadData();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == recyclerView.SCROLL_STATE_DRAGGING) {
                    isCollapseFam = true;
                } else {
                    isCollapseFam = false;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onRecyclerViewScrollListener != null) {
                    onRecyclerViewScrollListener.onRecyclerScroll(dy - lastDy);
                    dy = lastDy;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void init() {
        //读取用户设置信息
        SharedPreferences sp = getActivity().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
        fontSzie = sp.getInt("fontSize", UserSettingManager.FONT_SIZE.MEDIUM);
        imageMode = sp.getInt("imageMode", UserSettingManager.IMAGE_MODE.NORMAL);
        mRefresh.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView.setAdapter(mAdapter);
        //设置为列表布局
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void loadData() {
        //如果用户设置改变了,重新设置recycler的适配器
        if (settingIsChange()) {
            mAdapter = new CommonNewsRecyclerViewAdapter(getActivity(), mList);
            setAdapterListener(mAdapter);
            mRecyclerView.setAdapter(mAdapter);
        }
        if (isLoading) {
            return;
        }
        isLoading = true;
        mRefresh.setRefreshing(true);
        //id代表频道
        id = getArguments().getInt("id");
        //请求数据
        CommonNewsDao.getCommonNewsList(id, page, new BaseCallBack() {
            @Override
            public void success(Object data) {
                isLoading = false;
                mRefresh.setRefreshing(false);
                if (data != null) {
                    List<CommonNews> tempList = (List<CommonNews>) data;
                    if (isRefresh) {
                        //如果是刷新操作,清空数据集
                        mList.clear();
                    }
                    mList.addAll(tempList);
                    if (isRefresh) {
                        isRefresh = false;
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //用这种方式刷新数据更加平滑
                        mAdapter.notifyItemRangeInserted(mList.size() - 1, tempList.size());
                    }
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                isLoading = false;
                mRefresh.setRefreshing(false);
           /*     if(mLinearLayout!=null){
                    SnackbarUtil.ShortSnackbar(mLinearLayout, "请检查网络连接", Color.WHITE, Color.parseColor("#BBEA3344")).show();
                }*/
                if (mLinearLayout != null) {
                    Snackbar.make(mLinearLayout, "请检查网络连接", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 判断用户设置是否改变
     *
     * @return
     */
    private boolean settingIsChange() {
        SharedPreferences sp = getActivity().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
        int mFontSzie = sp.getInt("fontSize", UserSettingManager.FONT_SIZE.MEDIUM);
        int mImageMode = sp.getInt("imageMode", UserSettingManager.IMAGE_MODE.NORMAL);
        if (mFontSzie != fontSzie || mImageMode != imageMode) {
            fontSzie = mFontSzie;
            imageMode = mImageMode;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置适配器的监听
     *
     * @param adapter
     */
    private void setAdapterListener(final CommonNewsRecyclerViewAdapter adapter) {
        //设置item中删除按钮的点击事件
        adapter.setOnDeleteClickListener(new CommonNewsRecyclerViewAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final String[] str = new String[]{"标题党", "色情、暴力", "内容质量差", "不感兴趣"};
                dialog.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();
            }
        });
        //设置item的点击事件和长按事件
        adapter.setOnItemClickListener(new CommonNewsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ImageView iv) {
                Intent intent = new Intent(getActivity(), CommonNewsActivity.class);
                intent.putExtra("url", mList.get(position).getUrl());
                intent.putExtra("title", mList.get(position).getTitle());
                String imageUrl = "";
                if (mList.get(position).getImgs() != null) {
                    imageUrl = mList.get(position).getImgs()[0];
                } else {
                    imageUrl = mList.get(position).getCover();
                }
                intent.putExtra("imageUrl", imageUrl);
                if (Build.VERSION.SDK_INT >= 21) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), Pair.create((View) iv, "news_image"));
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View v, final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final String[] str = new String[]{"标题党", "色情、暴力", "内容质量差", "不感兴趣"};
                dialog.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();
            }
        });
        //设置mRecycler的滑动监听
        adapter.setOnScrollLoadMoreListener(new CommonNewsRecyclerViewAdapter.OnScrollLoadMoreListener() {
            @Override
            public void onScrollLoadMore(int position) {
                //当还剩余REMAIN个item时加载更多
                if (position >= mList.size() - REMAIN - 1) {
                    page++;
                    loadData();
                }
            }
        });
    }

    //以下是对外公开的方法和接口

    /**
     * 刷新数据
     */
    public void refreshData() {
        isRefresh = true;
        page++;
        if (mList.size() <= 39) {
            mRecyclerView.smoothScrollToPosition(0);
        }
        loadData();
    }

    /**
     * 回到顶部
     */
    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    public interface OnRecyclerViewScrollListener {
        void onRecyclerScroll(int dy);
    }

    public void setOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        this.onRecyclerViewScrollListener = onRecyclerViewScrollListener;
    }
}
