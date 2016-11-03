package com.taozi.news.bean;

import com.taozi.news.R;
import com.taozi.news.db.XutilsManager;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tao Yimin on 2016/10/10.
 * 用单例模式管理List<NewsChannel>
 */
public class ChannelManager {
    private static ChannelManager instance;
    //已订阅的频道
    private List<NewsChannel> mSubscibeChannelList;
    //未订阅的频道
    private List<NewsChannel> mNoSubscibeChannelList;
    //推荐的频道
    private List<NewsChannel> mRecommendChanneList;

    private ChannelManager() {
    }

    public static ChannelManager getIntance() {
        if (instance == null) {
            synchronized (ChannelManager.class) {
                if (instance == null) {
                    instance = new ChannelManager();
                    initChannel();
                }
            }
        }
        return instance;
    }

    /**
     * 拿到所有频道的集合
     * @return
     */
    public List<NewsChannel> getChannelList() {
        List<NewsChannel> channelList = new ArrayList<>();
        channelList.addAll(getmSubscibeChannelList());
        channelList.addAll(getmNoSubscibeChannelList());
        return channelList;
    }

    public List<NewsChannel> getmSubscibeChannelList() {
        return mSubscibeChannelList;
    }

    public List<NewsChannel> getmNoSubscibeChannelList() {
        return mNoSubscibeChannelList;
    }

    public void setmSubscibeChannelList(List<NewsChannel> mSubscibeChannelList) {
        this.mSubscibeChannelList = mSubscibeChannelList;
    }

    public void setmNoSubscibeChannelList(List<NewsChannel> mNoSubscibeChannelList) {
        this.mNoSubscibeChannelList = mNoSubscibeChannelList;
    }

    public List<NewsChannel> getmRecommendChanneList() {
        return mRecommendChanneList;
    }

    public void setmRecommendChanneList(List<NewsChannel> mRecommendChanneList) {
        this.mRecommendChanneList = mRecommendChanneList;
    }

    /**
     * 将所有的频道集合存入数据库
     */
    public void saveToDb(){
        try {
            XutilsManager.getInstance().getNewsDb().delete(NewsChannel.class);
            XutilsManager.getInstance().getNewsDb().save(ChannelManager.getIntance().getChannelList());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变推荐集合的内容
     */
    public void changeRecommendChannelList() {
        List<NewsChannel> list = new ArrayList<>();
        Random random = new Random();
        int n=9;
        if(getmNoSubscibeChannelList().size()<9){
            n=getmNoSubscibeChannelList().size();
        }
        for (int i = 0; i < n; ) {
            NewsChannel newsChannel = getmNoSubscibeChannelList().get(random.nextInt(getmNoSubscibeChannelList().size()));
            if (!list.contains(newsChannel)) {
                list.add(newsChannel);
                i++;
            }
        }
        setmRecommendChanneList(list);
    }

    /**
     * 订阅频道
     * @param newsChannel
     */
    public void subscibeChannel(NewsChannel newsChannel) {
        getmRecommendChanneList().remove(newsChannel);
        getmNoSubscibeChannelList().remove(newsChannel);
        newsChannel.setSubscibe(true);
        getmSubscibeChannelList().add(newsChannel);
    }

    /**
     * 取消订阅
     * @param newsChannel
     */
    public void unSubscibeChannel(NewsChannel newsChannel) {
        getmSubscibeChannelList().remove(newsChannel);
        newsChannel.setSubscibe(false);
        getmRecommendChanneList().add(newsChannel);
        getmNoSubscibeChannelList().add(newsChannel);
    }

    /**
     * 初始化新闻频道
     */

    public static void initChannel() {
        List<NewsChannel> list = new ArrayList<>();
        DbManager db = XutilsManager.getInstance().getNewsDb();
        try {
            //从数据库中读取
            list = db.findAll(NewsChannel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list == null || list.size() == 0) {
            //如果数据库中不存在则初始化一个默认的频道集合
            list = initDefaultChannel();
            try {
                //保存到数据库
                db.save(list);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        List<NewsChannel> mSubscibeChannelList = new ArrayList<>();
        List<NewsChannel> mNoSubscibeChannelList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            NewsChannel newsChannel = list.get(i);
            if (newsChannel.isSubscibe()) {
                mSubscibeChannelList.add(newsChannel);
            } else {
                mNoSubscibeChannelList.add(newsChannel);
            }
        }
        instance.setmSubscibeChannelList(mSubscibeChannelList);
        instance.setmNoSubscibeChannelList(mNoSubscibeChannelList);
    }

    /**
     * 初始化默认新闻频道
     *
     * @return
     */
    public static List<NewsChannel> initDefaultChannel() {
        List<NewsChannel> mChannelList = new ArrayList<>();

        NewsChannel newsChannel1 = new NewsChannel();
        newsChannel1.setId(NewsChannel.ID.HOTSPOT);
        newsChannel1.setIcon(R.mipmap.icon_news_hotspot);
        newsChannel1.setName("热点");
        newsChannel1.setSubscibe(true);

        NewsChannel newsChannel2 = new NewsChannel();
        newsChannel2.setId(NewsChannel.ID.FUNNY);
        newsChannel2.setIcon(R.mipmap.icon_news_funny);
        newsChannel2.setName("搞笑");
        newsChannel2.setSubscibe(true);

        NewsChannel newsChannel3 = new NewsChannel();
        newsChannel3.setId(NewsChannel.ID.INTERNATIONAL);
        newsChannel3.setIcon(R.mipmap.icon_news_international);
        newsChannel3.setName("国际");
        newsChannel3.setSubscibe(true);

        NewsChannel newsChannel4 = new NewsChannel();
        newsChannel4.setId(NewsChannel.ID.BELLE);
        newsChannel4.setIcon(R.mipmap.icon_news_belle);
        newsChannel4.setName("美女");
        newsChannel4.setSubscibe(true);

        NewsChannel newsChannel5 = new NewsChannel();
        newsChannel5.setId(NewsChannel.ID.FINANCE);
        newsChannel5.setIcon(R.mipmap.icon_news_finance);
        newsChannel5.setName("财经");
        newsChannel5.setSubscibe(true);

        NewsChannel newsChannel6 = new NewsChannel();
        newsChannel6.setId(NewsChannel.ID.ENTERTAINMENT);
        newsChannel6.setIcon(R.mipmap.icon_news_enterainment);
        newsChannel6.setName("娱乐");
        newsChannel6.setSubscibe(true);

        NewsChannel newsChannel7 = new NewsChannel();
        newsChannel7.setId(NewsChannel.ID.SPORT);
        newsChannel7.setIcon(R.mipmap.icon_news_sport);
        newsChannel7.setName("体育");
        newsChannel7.setSubscibe(true);

        NewsChannel newsChannel8 = new NewsChannel();
        newsChannel8.setId(NewsChannel.ID.TECHNOLOGY);
        newsChannel8.setIcon(R.mipmap.icon_news_technology);
        newsChannel8.setName("科技");
        newsChannel8.setSubscibe(true);

        NewsChannel newsChannel9 = new NewsChannel();
        newsChannel9.setId(NewsChannel.ID.CUTEPET);
        newsChannel9.setIcon(R.mipmap.icon_news_cutepet);
        newsChannel9.setName("萌宠");
        newsChannel9.setSubscibe(true);

        NewsChannel newsChannel10 = new NewsChannel();
        newsChannel10.setId(NewsChannel.ID.CAR);
        newsChannel10.setIcon(R.mipmap.icon_news_car);
        newsChannel10.setName("汽车");
        newsChannel10.setSubscibe(true);

        NewsChannel newsChannel11 = new NewsChannel();
        newsChannel11.setId(NewsChannel.ID.FASHION);
        newsChannel11.setIcon(R.mipmap.icon_news_fashion);
        newsChannel11.setName("时尚");
        newsChannel11.setSubscibe(true);

        NewsChannel newsChannel12 = new NewsChannel();
        newsChannel12.setId(NewsChannel.ID.EMOTION);
        newsChannel12.setIcon(R.mipmap.icon_news_emotion);
        newsChannel12.setName("情感");
        newsChannel12.setSubscibe(false);

        NewsChannel newsChannel13 = new NewsChannel();
        newsChannel13.setId(NewsChannel.ID.WEBCELEBRITY);
        newsChannel13.setIcon(R.mipmap.icon_news_webcelebrity);
        newsChannel13.setName("网红");
        newsChannel13.setSubscibe(false);

        NewsChannel newsChannel14 = new NewsChannel();
        newsChannel14.setId(NewsChannel.ID.MOIVE);
        newsChannel14.setIcon(R.mipmap.icon_news_movie);
        newsChannel14.setName("电影");
        newsChannel14.setSubscibe(false);

        NewsChannel newsChannel15 = new NewsChannel();
        newsChannel15.setId(NewsChannel.ID.HISTORY);
        newsChannel15.setIcon(R.mipmap.icon_news_history);
        newsChannel15.setName("历史");
        newsChannel15.setSubscibe(false);

        NewsChannel newsChannel16 = new NewsChannel();
        newsChannel16.setId(NewsChannel.ID.INTERESTING);
        newsChannel16.setIcon(R.mipmap.icon_news_interesting);
        newsChannel16.setName("趣闻");
        newsChannel16.setSubscibe(false);

        NewsChannel newsChannel17 = new NewsChannel();
        newsChannel17.setId(NewsChannel.ID.HEALTH);
        newsChannel17.setIcon(R.mipmap.icon_news_health);
        newsChannel17.setName("养生");
        newsChannel17.setSubscibe(false);

        NewsChannel newsChannel18 = new NewsChannel();
        newsChannel18.setId(NewsChannel.ID.NBA);
        newsChannel18.setIcon(R.mipmap.icon_news_nba);
        newsChannel18.setName("NBA");
        newsChannel18.setSubscibe(false);

        NewsChannel newsChannel19 = new NewsChannel();
        newsChannel19.setId(NewsChannel.ID.SOCIETY);
        newsChannel19.setIcon(R.mipmap.icon_news_society);
        newsChannel19.setName("社会");
        newsChannel19.setSubscibe(false);

        NewsChannel newsChannel20 = new NewsChannel();
        newsChannel20.setId(NewsChannel.ID.TRAFFIC);
        newsChannel20.setIcon(R.mipmap.icon_news_traffic);
        newsChannel20.setName("交通");
        newsChannel20.setSubscibe(false);

        NewsChannel newsChannel21 = new NewsChannel();
        newsChannel21.setId(NewsChannel.ID.CONSTELLATION);
        newsChannel21.setIcon(R.mipmap.icon_news_constellation);
        newsChannel21.setName("星座");
        newsChannel21.setSubscibe(false);

        NewsChannel newsChannel22 = new NewsChannel();
        newsChannel22.setId(NewsChannel.ID.FICTION);
        newsChannel22.setIcon(R.mipmap.icon_news_fiction);
        newsChannel22.setName("美文");
        newsChannel22.setSubscibe(false);

        NewsChannel newsChannel23 = new NewsChannel();
        newsChannel23.setId(NewsChannel.ID.MILITARY);
        newsChannel23.setIcon(R.mipmap.icon_news_military);
        newsChannel23.setName("军事");
        newsChannel23.setSubscibe(false);

        NewsChannel newsChannel24 = new NewsChannel();
        newsChannel24.setId(NewsChannel.ID.INTERNET);
        newsChannel24.setIcon(R.mipmap.icon_news_internet);
        newsChannel24.setName("互联网");
        newsChannel24.setSubscibe(false);

        NewsChannel newsChannel25 = new NewsChannel();
        newsChannel25.setId(NewsChannel.ID.IMAGE);
        newsChannel25.setIcon(R.mipmap.icon_news_image);
        newsChannel25.setName("图片");
        newsChannel25.setSubscibe(false);

        NewsChannel newsChannel26 = new NewsChannel();
        newsChannel26.setId(NewsChannel.ID.GAME);
        newsChannel26.setIcon(R.mipmap.icon_news_game);
        newsChannel26.setName("游戏");
        newsChannel26.setSubscibe(false);

        NewsChannel newsChannel27 = new NewsChannel();
        newsChannel27.setId(NewsChannel.ID.PARENTING);
        newsChannel27.setIcon(R.mipmap.icon_news_parenting);
        newsChannel27.setName("育儿");
        newsChannel27.setSubscibe(false);

        NewsChannel newsChannel28 = new NewsChannel();
        newsChannel28.setId(NewsChannel.ID.DIGITAL);
        newsChannel28.setIcon(R.mipmap.icon_news_digital);
        newsChannel28.setName("数码控");
        newsChannel28.setSubscibe(false);

        NewsChannel newsChannel29 = new NewsChannel();
        newsChannel29.setId(NewsChannel.ID.FOOD);
        newsChannel29.setIcon(R.mipmap.icon_news_food);
        newsChannel29.setName("美食咖");
        newsChannel29.setSubscibe(false);

        NewsChannel newsChannel30 = new NewsChannel();
        newsChannel30.setId(NewsChannel.ID.HOUSE);
        newsChannel30.setIcon(R.mipmap.icon_news_house);
        newsChannel30.setName("房产");
        newsChannel30.setSubscibe(false);

        NewsChannel newsChannel31 = new NewsChannel();
        newsChannel31.setId(NewsChannel.ID.EDUCATION);
        newsChannel31.setIcon(R.mipmap.icon_news_education);
        newsChannel31.setName("教育");
        newsChannel31.setSubscibe(false);

        NewsChannel newsChannel32 = new NewsChannel();
        newsChannel32.setId(NewsChannel.ID.CROOKEDHOUSE);
        newsChannel32.setIcon(R.mipmap.icon_news_crookedhouse);
        newsChannel32.setName("歪楼");
        newsChannel32.setSubscibe(false);

        NewsChannel newsChannel33 = new NewsChannel();
        newsChannel33.setId(NewsChannel.ID.MANAGER);
        newsChannel33.setIcon(R.mipmap.icon_news_manager);
        newsChannel33.setName("管理");
        newsChannel33.setSubscibe(false);

        NewsChannel newsChannel34 = new NewsChannel();
        newsChannel34.setId(NewsChannel.ID.ACG);
        newsChannel34.setIcon(R.mipmap.icon_news_acg);
        newsChannel34.setName("二次元");
        newsChannel34.setSubscibe(false);

        NewsChannel newsChannel35 = new NewsChannel();
        newsChannel35.setId(NewsChannel.ID.STORY);
        newsChannel35.setIcon(R.mipmap.icon_news_story);
        newsChannel35.setName("故事");
        newsChannel35.setSubscibe(false);

        NewsChannel newsChannel36 = new NewsChannel();
        newsChannel36.setId(NewsChannel.ID.EXPLORE);
        newsChannel36.setIcon(R.mipmap.icon_news_explore);
        newsChannel36.setName("探索");
        newsChannel36.setSubscibe(false);

        NewsChannel newsChannel37 = new NewsChannel();
        newsChannel37.setId(NewsChannel.ID.GIF);
        newsChannel37.setIcon(R.mipmap.icon_news_gif);
        newsChannel37.setName("GIF");
        newsChannel37.setSubscibe(false);

        mChannelList.add(newsChannel1);
        mChannelList.add(newsChannel2);
        mChannelList.add(newsChannel3);
        mChannelList.add(newsChannel4);
        mChannelList.add(newsChannel5);
        mChannelList.add(newsChannel6);
        mChannelList.add(newsChannel7);
        mChannelList.add(newsChannel8);
        mChannelList.add(newsChannel9);
        mChannelList.add(newsChannel10);
        mChannelList.add(newsChannel11);
        mChannelList.add(newsChannel12);
        mChannelList.add(newsChannel13);
        mChannelList.add(newsChannel14);
        mChannelList.add(newsChannel15);
        mChannelList.add(newsChannel16);
        mChannelList.add(newsChannel17);
        mChannelList.add(newsChannel18);
        mChannelList.add(newsChannel19);
        mChannelList.add(newsChannel20);
        mChannelList.add(newsChannel21);
        mChannelList.add(newsChannel22);
        mChannelList.add(newsChannel23);
        mChannelList.add(newsChannel24);
        mChannelList.add(newsChannel25);
        mChannelList.add(newsChannel26);
        mChannelList.add(newsChannel27);
        mChannelList.add(newsChannel28);
        mChannelList.add(newsChannel29);
        mChannelList.add(newsChannel30);
        mChannelList.add(newsChannel31);
        mChannelList.add(newsChannel32);
        mChannelList.add(newsChannel33);
        mChannelList.add(newsChannel34);
        mChannelList.add(newsChannel35);
        mChannelList.add(newsChannel36);
        mChannelList.add(newsChannel37);

        return mChannelList;
    }
}
