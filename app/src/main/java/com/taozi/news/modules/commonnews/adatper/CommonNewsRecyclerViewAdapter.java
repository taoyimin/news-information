package com.taozi.news.modules.commonnews.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taozi.news.R;
import com.taozi.news.bean.UserSettingManager;
import com.taozi.news.modules.commonnews.bean.CommonNews;
import com.taozi.news.modules.commonnews.util.FormatDataUtil;
import com.taozi.news.util.ImageLoaderUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 新闻列表的适配器
 */
public class CommonNewsRecyclerViewAdapter extends RecyclerView.Adapter<CommonNewsRecyclerViewAdapter.BaseViewHolder> {
    private Context mContext;
    private List<CommonNews> mList;
    private Random mRandom = new Random();
    //用于记录屏幕上最后一个可见item的下标
    private int lastPosition = -1;
    //item的回调接口
    private OnItemClickListener onItemClickListener;
    //item中删除按钮的回调接口
    private OnDeleteClickListener onDeleteClickListener;
    //recycler的滑动加载更多回调接口
    private OnScrollLoadMoreListener onScrollLoadMoreListener;
    private int fontSize;
    private int imageMode;

    /**
     * 构造方法
     *
     * @param mContext
     * @param mList
     */
    public CommonNewsRecyclerViewAdapter(Context mContext, List<CommonNews> mList) {
        this.mContext = mContext;
        this.mList = mList;
        fontSize= UserSettingManager.getIntance().getFontSize();
        imageMode=UserSettingManager.getIntance().getImageMode();
    }

    /**
     * 创建itemView和ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                //实例化一个视频新闻的ViewHolder
                itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_video, parent, false);
                viewHolder = new NoImageViewHolder(itemView);
                break;
            case 1:
                //随机生产一个0或1的数
                if (mRandom.nextInt(2) == 0) {
                    //为0时实例化一个图片在左边的ViewHolder
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_left_image, parent, false);
                } else {
                    //为1时实例化一个图片在右边的ViewHolder
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_right_image, parent, false);
                }
                viewHolder = new SingleImageViewHolder(itemView);
                break;
            case 3:
                //实例化一个三张图片的ViewHolder
                itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_three_image, parent, false);
                viewHolder = new ThreeImageViewHolder(itemView);
                break;
            default:
                //其他情况下,实例化一个视频新闻的ViewHolder
                itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycler_video, parent, false);
                viewHolder = new NoImageViewHolder(itemView);
                break;

        }
        return viewHolder;
    }

    /**
     * 填充界面
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        CommonNews commonNews = mList.get(position);
        int type = -1;
        //避免空指针,解析的json对象为视频新闻时,图片数组为空
        if (commonNews.getImgs() == null) {
            //视频新闻
            type = 0;
        } else {
            //根据图片数组的长度返回新闻的类型
            type = commonNews.getImgs().length;
        }
        //根据不同的新闻类型,采用相应的填充数据方法
        switch (type) {
            case 0:
                fillDataVideo(holder, commonNews);
                break;
            case 1:
                fillDataSingleImage(holder, commonNews);
                break;
            case 3:
                fillDataThreeImage(holder, commonNews);
                break;
            default:
                fillDataVideo(holder, commonNews);
                break;
        }
        if (onScrollLoadMoreListener != null) {
            //将当前滑动到的item的position回调给HeadlineFragment
            onScrollLoadMoreListener.onScrollLoadMore(position);
        }
        //设置item中删除按钮的点击事件
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    // 获取最新item的位置
                    int pos = holder.getLayoutPosition();
                    onDeleteClickListener.onDeleteClick(v, pos);
                }
            }
        });
        //设置item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    // 获取最新item的位置
                    int pos = holder.getLayoutPosition();
                    ImageView iv=null;
                    if(holder instanceof NoImageViewHolder){
                        iv=((NoImageViewHolder) holder).iv;
                    }else if(holder instanceof SingleImageViewHolder){
                        iv=((SingleImageViewHolder) holder).iv;
                    }else if(holder instanceof ThreeImageViewHolder){
                        iv=((ThreeImageViewHolder) holder).iv_left;
                    }
                    onItemClickListener.onItemClick(v, pos,iv);
                }
            }
        });
        //设置item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    // 获取最新item的位置
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(v, pos);
                }
                // 消费事件
                return true;
            }
        });
        //设置item入场动画
        setAnimation(holder.recyclerView_item, position);
    }

    /**
     * 三张图片界面的填充
     */
    private void fillDataThreeImage(BaseViewHolder holder, CommonNews commonNews) {
        ThreeImageViewHolder viewHolder = (ThreeImageViewHolder) holder;
        viewHolder.tv_title.setText(commonNews.getTitle());
        viewHolder.tv_source.setText(commonNews.getSource());
        viewHolder.tv_time.setText(FormatDataUtil.FormatData(commonNews.getTime()));
        if(imageMode==UserSettingManager.IMAGE_MODE.NO_IMAGE){
            return;
        }
        String[] imgs = commonNews.getImgs();
        ImageLoader.getInstance().displayImage(imgs[0], viewHolder.iv_left, ImageLoaderUtil.getDefaultOption());
        ImageLoader.getInstance().displayImage(imgs[1], viewHolder.iv_center, ImageLoaderUtil.getDefaultOption());
        ImageLoader.getInstance().displayImage(imgs[2], viewHolder.iv_right, ImageLoaderUtil.getDefaultOption());
    }

    /**
     * 单张图片界面的填充
     *
     * @param holder
     * @param commonNews
     */
    private void fillDataSingleImage(BaseViewHolder holder, CommonNews commonNews) {
        SingleImageViewHolder viewHolder = (SingleImageViewHolder) holder;
        viewHolder.tv_title.setText(commonNews.getTitle());
        viewHolder.tv_source.setText(commonNews.getSource());
        viewHolder.tv_time.setText(FormatDataUtil.FormatData(commonNews.getTime()));
        if(imageMode==UserSettingManager.IMAGE_MODE.NO_IMAGE){
            return;
        }
        String[] imgs = commonNews.getImgs();
        ImageLoader.getInstance().displayImage(imgs[0], viewHolder.iv, ImageLoaderUtil.getDefaultOption());
    }

    /**
     * 视频新闻界面的填充
     *
     * @param holder
     * @param commonNews
     */
    private void fillDataVideo(BaseViewHolder holder, CommonNews commonNews) {
        NoImageViewHolder viewHolder = (NoImageViewHolder) holder;
        viewHolder.tv_title.setText(commonNews.getTitle());
        viewHolder.tv_source.setText("视频");
        viewHolder.tv_time.setText("刚刚");
        if(imageMode==UserSettingManager.IMAGE_MODE.NO_IMAGE){
            return;
        }
        ImageLoader.getInstance().displayImage(commonNews.getCover(),viewHolder.iv,ImageLoaderUtil.getDefaultOption());
    }

    /**
     * 返回需要显示的行数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 返回布局类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //避免空指针,解析的json对象为新闻对象时,图片数组为空
        if (mList.get(position).getImgs() == null) {
            //0代表视频新闻
            return 0;
        }
        //图片数组的长度代表新闻类型
        return mList.get(position).getImgs().length;
    }

    /**
     * item入场动画
     *
     * @param viewToAnimate
     * @param position
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            //手指上滑
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            //手指下滑
            lastPosition--;
        }
    }

    /**
     * item滑出屏幕时清空动画
     *
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.recyclerView_item.clearAnimation();
    }

    /**
     * 所有ViewHolder的基类
     */
    class BaseViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup recyclerView_item;
        private TextView tv_delete;

        public BaseViewHolder(View itemView) {
            super(itemView);
            recyclerView_item = (ViewGroup) itemView.findViewById(R.id.recyclerView_item);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    /**
     * 视频新闻显示的ViewHolder
     */
    class NoImageViewHolder extends BaseViewHolder {
        private TextView tv_title,tv_source,tv_time;
        private ImageView iv;
        private ViewGroup iv_group;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv= (ImageView) itemView.findViewById(R.id.item_iv);
            iv_group= (ViewGroup) itemView.findViewById(R.id.iv_group);
            initFontSize(tv_title);
            initImageMode(iv_group);
        }
    }

    /**
     * 显示一张图片的ViewHolder
     * 因为图片在左边显示和在右边显示只是布局不同,布局内的控件完全相同,所以两种不同的布局可以用同一个ViewHolder
     */
    class SingleImageViewHolder extends BaseViewHolder {
        private ImageView iv;
        private TextView tv_title;
        private TextView tv_source;
        private TextView tv_time;


        public SingleImageViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_iv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            initFontSize(tv_title);
            initImageMode(iv);
        }
    }

    /**
     * 显示三张图片的ViewHolder
     */
    class ThreeImageViewHolder extends BaseViewHolder {
        private ImageView iv_left, iv_center, iv_right;
        private TextView tv_title;
        private TextView tv_source;
        private TextView tv_time;
        private ViewGroup iv_group;

        public ThreeImageViewHolder(View itemView) {
            super(itemView);
            iv_center = (ImageView) itemView.findViewById(R.id.iv_center);
            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            iv_right = (ImageView) itemView.findViewById(R.id.iv_right);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_group= (ViewGroup) itemView.findViewById(R.id.iv_group);
            initFontSize(tv_title);
            initImageMode(iv_group);
        }
    }

    /**
     * 初始化字体的大小
     * @param textView
     */
    private void initFontSize(TextView textView){
        switch (fontSize){
            case UserSettingManager.FONT_SIZE.SAMLL:
                textView.setTextSize(13);
                break;
            case UserSettingManager.FONT_SIZE.MEDIUM:
                textView.setTextSize(15);
                break;
            case UserSettingManager.FONT_SIZE.BIG:
                textView.setTextSize(17);
                break;
            case UserSettingManager.FONT_SIZE.HUGE:
                textView.setTextSize(19);
                break;
            default:
                textView.setTextSize(15);
                break;
        }
    }

    /**
     * 初始化图片的显示模式
     * @param view
     */
    private void initImageMode(View view){
        switch (imageMode){
            case UserSettingManager.IMAGE_MODE.NORMAL:
                view.setVisibility(View.VISIBLE);
                break;
            case UserSettingManager.IMAGE_MODE.ADJUST:
                //待完善
                view.setVisibility(View.VISIBLE);
                break;
            case UserSettingManager.IMAGE_MODE.NO_IMAGE:
                view.setVisibility(View.GONE);
                break;
            case UserSettingManager.IMAGE_MODE.ONLY_WIFI:
                //待完善
                view.setVisibility(View.VISIBLE);
                break;
            default:
                view.setVisibility(View.VISIBLE);
                break;
        }
    }

    //以下是对外公开的方法

    /**
     * item的点击事件和长按事件回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position,ImageView iv);

        void onItemLongClick(View v, int position);
    }

    /**
     * 删除按钮的点击事件回调接口
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(View v, int position);
    }

    /**
     * 滑动加载更多回调接口
     */
    public interface OnScrollLoadMoreListener {
        void onScrollLoadMore(int position);
    }

    /**
     * 设置item点击事件的接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置删除按钮点击事件的接口
     */
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    /**
     * 设置滑动加载更多监听的接口
     */
    public void setOnScrollLoadMoreListener(OnScrollLoadMoreListener onScrollLoadMoreListener) {
        this.onScrollLoadMoreListener = onScrollLoadMoreListener;
    }
}
