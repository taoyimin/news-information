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

import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/12.
 * 推荐频道列表适配器
 */
public class RecommendGridRecyclerAdapter extends RecyclerView.Adapter<RecommendGridRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<NewsChannel> list;
    private OnItemClickListener onItemClickListener;
    //动画播放的延时
    private int delay = 0;
    //用于记录item入场的个数
    private int sum = 0;
    //用于记录第一批入场的item的总个数
    private int listSize;

    public RecommendGridRecyclerAdapter(Context context, List<NewsChannel> list) {
        this.context = context;
        this.list = list;
        listSize = list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dragrecycler_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
        }else{
            //第一批之后入场的item做放大动画
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
        //设置itemView的点击和长按监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(v, pos);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(v, position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.drag_recycler_tv);
            imageView = (ImageView) itemView.findViewById(R.id.drag_recycler_iv);
        }
    }

    /**
     * item的点击事件和长按事件回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
