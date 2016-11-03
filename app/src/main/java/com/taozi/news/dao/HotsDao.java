package com.taozi.news.dao;

import com.taozi.news.bean.Hots;
import com.taozi.news.i.BaseCallBack;
import com.taozi.news.net.HttpUtil;
import com.taozi.news.util.HotsParseUtil;

import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/11.
 * 热点新闻数据操作层
 */
public class HotsDao {
    public static void getHotsList(final BaseCallBack callBack) {
        String url = "http://discover.ie.sogou.com/discover_agent?v=1.2.9.2526&t=1476173195&r1=2116&lastindex=0&cmd=getpush&imei=000000000000000&simplejson=1&api=6&phone=1&f=tp&r=2116&h=ffffffff-f5bd-3048-ffff-ffff99d603a9&window_index=-1";
        HttpUtil.doHttpReqeustByxUtils("GET", url, null, new BaseCallBack() {
            @Override
            public void success(Object data) {
                List<Hots> list=HotsParseUtil.getHotsList(data.toString());
                if(list!=null){
                    callBack.success(list);
                }else{
                    callBack.failed(0,"请检查网络连接");
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                callBack.failed(errorCode, data);
            }
        });
    }
}
