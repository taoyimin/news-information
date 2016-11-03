package com.taozi.news.modules.navigation.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taozi.news.R;
import com.taozi.news.activity.BaseFragment;
import com.taozi.news.activity.MainFragment;
import com.taozi.news.bean.ChannelManager;
import com.taozi.news.bean.NewsChannel;
import com.taozi.news.modules.navigation.adatper.DragGridRecyclerAdapter;
import com.taozi.news.modules.navigation.adatper.RecommendGridRecyclerAdapter;
import com.taozi.news.modules.navigation.i.MyItemTouchCallback;
import com.taozi.news.modules.navigation.i.OnRecyclerItemClickListener;
import com.taozi.news.modules.navigation.util.VibratorUtil;
import com.taozi.news.util.StatueUtil;

/**
 * Created by Tao Yimin on 2016/10/5.
 * 自定义导航栏Fragment
 */
public class NavigationFragment extends BaseFragment implements MyItemTouchCallback.OnDragListener {
    private View mStatue;
    private ViewGroup mLayout, mChangeChannel;
    private ImageView mBackImageView;
    //我的频道
    private RecyclerView mRecyclerView;
    //推荐频道
    private RecyclerView mRecyclerView2;
    private DragGridRecyclerAdapter mAdapter;
    private RecommendGridRecyclerAdapter mAdapter2;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void findViews(View view) {
        mStatue = view.findViewById(R.id.statue);
        mLayout = (ViewGroup) view.findViewById(R.id.navigation_layout);
        mBackImageView = (ImageView) view.findViewById(R.id.navigation_close);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_channel_recycler);
        mRecyclerView2 = (RecyclerView) view.findViewById(R.id.recommend_channel_recycler);
        mChangeChannel = (ViewGroup) view.findViewById(R.id.navigation_change_channel);
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mChangeChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变推荐频道的内容
                ChannelManager.getIntance().changeRecommendChannelList();
                mAdapter2 = new RecommendGridRecyclerAdapter(getActivity(), ChannelManager.getIntance().getmRecommendChanneList());
                setAdapterListener(mAdapter2);
                mRecyclerView2.setAdapter(mAdapter2);
            }
        });
    }

    @Override
    protected void init() {
        //初始化状态栏
        StatueUtil.initStatue(getActivity(), mLayout, mStatue);

        mAdapter = new DragGridRecyclerAdapter(getActivity(), ChannelManager.getIntance().getmSubscibeChannelList());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mAdapter).setOnDragListener(this));
        //将mItemTouchHelper和mRecyclerView进行关联
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        //添加监听
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                mItemTouchHelper.startDrag(vh);
                //震动70ms
                VibratorUtil.Vibrate(getActivity(), 70);
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //取消订阅
                int pos=vh.getLayoutPosition();
                NewsChannel newsChannel=ChannelManager.getIntance().getmSubscibeChannelList().get(pos);
                ChannelManager.getIntance().unSubscibeChannel(newsChannel);
                mAdapter.notifyItemRemoved(pos);
                mAdapter2.notifyItemInserted(ChannelManager.getIntance().getmRecommendChanneList().size()-1);
                onFinishDrag();
            }
        });

        mRecyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ChannelManager.getIntance().changeRecommendChannelList();
        mAdapter2 = new RecommendGridRecyclerAdapter(getActivity(), ChannelManager.getIntance().getmRecommendChanneList());
        setAdapterListener(mAdapter2);
        mRecyclerView2.setAdapter(mAdapter2);
    }

    @Override
    protected void loadData() {

    }

    /**
     * 拖拽完成后的回掉
     */
    @Override
    public void onFinishDrag() {
        //通知MainFragment更新
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("mainFragment");
        ((MainFragment) fragment).initViewPager();
        ((MainFragment) fragment).initMagicIndicator();
        //存入缓存
        ChannelManager.getIntance().saveToDb();
    }

    /**
     * 设置适配器的监听
     * @param adapter
     */
    private void setAdapterListener(final RecommendGridRecyclerAdapter adapter) {
        adapter.setOnItemClickListener(new RecommendGridRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //订阅频道
                NewsChannel newsChannel = ChannelManager.getIntance().getmRecommendChanneList().get(position);
                ChannelManager.getIntance().subscibeChannel(newsChannel);
                adapter.notifyItemRemoved(position);
                mAdapter.notifyItemInserted(ChannelManager.getIntance().getmSubscibeChannelList().size()-1);
                onFinishDrag();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }
}
