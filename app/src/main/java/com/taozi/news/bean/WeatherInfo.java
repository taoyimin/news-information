package com.taozi.news.bean;

/**
 * Created by Tao Yimin on 2016/10/6.
 * 天气信息实体类
 */
public class WeatherInfo {
    private String city;
    private String low;
    private String high;
    private String type;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
