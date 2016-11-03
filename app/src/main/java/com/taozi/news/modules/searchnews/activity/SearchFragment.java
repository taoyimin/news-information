package com.taozi.news.modules.searchnews.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taozi.news.R;
import com.taozi.news.activity.BaseFragment;
import com.taozi.news.bean.Hots;
import com.taozi.news.modules.commonnews.activity.CommonNewsActivity;
import com.taozi.news.util.StatueUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/5.
 */
public class SearchFragment extends BaseFragment {
    private List<Hots> mHotsList;
    private View mStatue;
    private ViewGroup mLayout;
    private TextView rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8;
    private ViewGroup btnRank1, btnRank2, btnRank3, btnRank4, btnRank5, btnRank6, btnRank7, btnRank8;
    private List<TextView> mRankList;
    private List<ViewGroup> mBtnRankList;
    private EditText mEditText;
    private TextView mSearchTextView;
    private ImageView mBackImageView;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_searchnews;
    }

    @Override
    protected void findViews(View view) {
        mStatue = view.findViewById(R.id.statue);
        mLayout = (ViewGroup) view.findViewById(R.id.search_layout);
        mEditText = (EditText) view.findViewById(R.id.searchnews_edit);
        mSearchTextView = (TextView) view.findViewById(R.id.search_btn_search);
        mBackImageView= (ImageView) view.findViewById(R.id.search_back_iv);
        rank1 = (TextView) view.findViewById(R.id.search_rank1);
        rank2 = (TextView) view.findViewById(R.id.search_rank2);
        rank3 = (TextView) view.findViewById(R.id.search_rank3);
        rank4 = (TextView) view.findViewById(R.id.search_rank4);
        rank5 = (TextView) view.findViewById(R.id.search_rank5);
        rank6 = (TextView) view.findViewById(R.id.search_rank6);
        rank7 = (TextView) view.findViewById(R.id.search_rank7);
        rank8 = (TextView) view.findViewById(R.id.search_rank8);
        btnRank1= (ViewGroup) view.findViewById(R.id.search_btn_rank1);
        btnRank2= (ViewGroup) view.findViewById(R.id.search_btn_rank2);
        btnRank3= (ViewGroup) view.findViewById(R.id.search_btn_rank3);
        btnRank4= (ViewGroup) view.findViewById(R.id.search_btn_rank4);
        btnRank5= (ViewGroup) view.findViewById(R.id.search_btn_rank5);
        btnRank6= (ViewGroup) view.findViewById(R.id.search_btn_rank6);
        btnRank7= (ViewGroup) view.findViewById(R.id.search_btn_rank7);
        btnRank8= (ViewGroup) view.findViewById(R.id.search_btn_rank8);
    }

    @Override
    protected void initEvent() {
        //设置搜索按钮的点击事件
        mSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mBtnRankList=new ArrayList<>();
        mBtnRankList.add(btnRank1);
        mBtnRankList.add(btnRank2);
        mBtnRankList.add(btnRank3);
        mBtnRankList.add(btnRank4);
        mBtnRankList.add(btnRank5);
        mBtnRankList.add(btnRank6);
        mBtnRankList.add(btnRank7);
        mBtnRankList.add(btnRank8);
        //设置排行榜按钮的点击事件
        for (int i = 0; i < mBtnRankList.size(); i++) {
            final int finalI = i;
            mBtnRankList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHotsList != null) {
                        Intent intent = new Intent(getActivity(), CommonNewsActivity.class);
                        intent.putExtra("title", mHotsList.get(finalI).getTitle());
                        String[] strs=mHotsList.get(finalI).getUrl().replace("sogouhotspot://","").replace("%3A",":").split("&");
                        intent.putExtra("url", strs[0]);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    protected void init() {
        //初始化状态栏
        StatueUtil.initStatue(getActivity(), mLayout, mStatue);
    }

    @Override
    protected void loadData() {

    }

    /**
     * 弹出软键盘
     */
    public void showSoftInput() {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 清空EditText中的内容
     */
    public void clearEditText() {
        mEditText.setText("");
    }

    /**
     * 初始化排行榜内容
     * @param list
     */
    public void initRank(List<Hots> list) {
        mHotsList = list;
        mRankList = new ArrayList<>();
        mRankList.add(rank1);
        mRankList.add(rank2);
        mRankList.add(rank3);
        mRankList.add(rank4);
        mRankList.add(rank5);
        mRankList.add(rank6);
        mRankList.add(rank7);
        mRankList.add(rank8);
        for (int i = 0; i < 8; i++) {
            mRankList.get(i).setText(mHotsList.get(i).getTitle());
        }
    }
}
