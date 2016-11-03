package com.taozi.news.modules.commonnews.util;

import android.text.TextUtils;

import com.taozi.news.modules.commonnews.bean.CommonNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 解析json工具类
 */
public class ParseCommonNews {
    public static List<CommonNews> getParseCommonNewsList(String json) {
        List<CommonNews> list=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                CommonNews commonNews=new CommonNews();
                JSONObject jsonObj=jsonArray.getJSONObject(i);
                try{
                    commonNews.setTitle(jsonObj.getString("title"));
                    commonNews.setSource(jsonObj.getString("source"));
                    commonNews.setTime(jsonObj.getString("time"));
                    //得到图片的地址
                    String thumbnails=jsonObj.getString("thumbnails");
                    String thumbnails1=jsonObj.getString("thumbnails1");
                    String thumbnails2=jsonObj.getString("thumbnails2");
                    String[] strs = null;
                    if (TextUtils.isEmpty(thumbnails)){
                        String cover=jsonObj.getString("cover");
                        commonNews.setCover(cover);
                    }else if(TextUtils.isEmpty(thumbnails1)&&TextUtils.isEmpty(thumbnails2)){
                        strs= new String[]{thumbnails};
                    }else{
                        strs= new String[]{thumbnails, thumbnails1, thumbnails2};
                    }
                    commonNews.setImgs(strs);
                    commonNews.setUrl(jsonObj.getString("url"));
                    list.add(commonNews);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
