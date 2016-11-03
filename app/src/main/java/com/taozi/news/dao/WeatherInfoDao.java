package com.taozi.news.dao;

import com.taozi.news.bean.WeatherInfo;
import com.taozi.news.i.BaseCallBack;
import com.taozi.news.net.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tao Yimin on 2016/10/6.
 * 天气信息数据操作层
 */
public class WeatherInfoDao {
    public static void getWeatherInfo(final String city, final BaseCallBack callBack) {
        String url = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
        HttpUtil.doHttpReqeustByxUtils("GET", url, null, new BaseCallBack() {
            @Override
            public void success(Object data) {
                WeatherInfo weatherInfo = new WeatherInfo();
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    jsonObject = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("forecast");
                    JSONObject jsonObj = jsonArray.getJSONObject(0);
                    weatherInfo.setCity(city);
                    weatherInfo.setLow(jsonObj.getString("low"));
                    weatherInfo.setHigh(jsonObj.getString("high"));
                    weatherInfo.setType(jsonObj.getString("type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.success(weatherInfo);
            }

            @Override
            public void failed(int errorCode, Object data) {
                callBack.failed(errorCode, data);
            }
        });
    }
}
