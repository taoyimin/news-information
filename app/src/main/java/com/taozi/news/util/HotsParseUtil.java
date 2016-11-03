package com.taozi.news.util;

import com.taozi.news.bean.Hots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/11.
 * 解析JSON工具类
 */
public class HotsParseUtil {
    public static List<Hots> getHotsList(String json){
        List<Hots> list = new ArrayList<Hots>();
        try {
            JSONObject jsonObject = new JSONObject(json.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("top_news");
            for (int i = 0; i < jsonArray.length(); i++) {
                Hots hots = new Hots();
                String str =  jsonArray.getString(i);
                JSONObject jsonObj=new JSONObject(str);
                hots.setTitle(jsonObj.getString("title"));
                hots.setUrl(jsonObj.getString("url_detail"));
                list.add(hots);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
