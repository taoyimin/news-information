package com.taozi.news.modules.navigation.adatper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taozi.news.R;
import com.taozi.news.bean.NewsChannel;
import com.taozi.news.modules.navigation.i.MyItemTouchCallback;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 可拖拽的网格RecyclerView适配器
 */
public class DragGridRecyclerAdapter extends RecyclerView.Adapter<DragGridRecyclerAdapter.MyViewHolder> implements MyItemTouchCallback.ItemTouchAdapter{

    private Context context;
    private List<NewsChannel> list;
    //item动画播放的延时
    private int delay = 0;
    //纪录item入场的个数
    private int sum = 0;
    //第一批入场的item总个数
    private int listSize;

    public DragGridRecyclerAdapter(Context context,List<NewsChannel> list){
        this.context=context;
        this.list = list;
        listSize=list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dragrecycler_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        sum++;
        holder.itemView.setAlpha(0);
        holder.imageView.setImageResource(list.get(position).getIcon());
        holder.textView.setText(list.get(position).getName());
        if (sum <= listSize) {
            //第一批入场的item做左移动画
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float progress = animation.getAnimatedFraction();
                    holder.itemView.setAlpha(progress);
                    holder.itemView.setTranslationX(200 - 200 * progress);
                }
            });
            animator.setStartDelay(delay);
            delay += 100;
            animator.start();
        }else {
            //非第一批入场的item做放大动画
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float progress = animation.getAnimatedFraction();
                    holder.itemView.setScaleX(progress);
                    holder.itemView.setScaleY(progress);
                }
            });
            animator.start();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 在拖拽过程中会不停回掉该方法
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.drag_recycler_tv);
            imageView = (ImageView) itemView.findViewById(R.id.drag_recycler_iv);
        }
    }
}
