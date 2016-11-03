package com.taozi.news.modules.commonnews.dao;

import com.taozi.news.i.BaseCallBack;
import com.taozi.news.modules.commonnews.bean.CommonNews;
import com.taozi.news.modules.commonnews.util.ParseCommonNews;
import com.taozi.news.net.HttpUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 新闻数据操作层
 */
public class CommonNewsDao {
    public static void getCommonNewsList(int id,int page,final BaseCallBack callBack){
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id + "");
        params.put("page",page + "");
        String url="http://a121.baopiqi.com/api/mh/getClassificationMakeComplaintsContentFour.php?limit=20&first=1&appname=%E7%89%B9%E5%88%AB%E7%9C%8B%E7%82%B9&pkgname=com.tools.news&imei=000000000000000&os=android&ver=2.0.2&androidid=ffffffff-f617-6c94-ffff-ffff99d603a9&ip=192.168.0.1&timestamp=1476065084&md5=39af81cf0e06ec0abc38d2c038c26cc1&network=WIFI&install=%5B%22%E8%B4%A2%E7%BB%8F%22%2C%22%E6%90%9E%E7%AC%91%22%2C%22%E5%A8%B1%E4%B9%90%22%2C%22%E7%A7%91%E6%8A%80%22%2C%22%E4%BD%93%E8%82%B2%22%5D&viewlist=%5B%22%E4%BD&page="+page+"&id="+id;
        HttpUtil.doHttpReqeustByxUtils("POST", url, params, new BaseCallBack() {
            @Override
            public void success(Object data) {
                //获取联网请求的数据
                List<CommonNews> tempList= ParseCommonNews.getParseCommonNewsList(data.toString());

                if(tempList==null){
                    callBack.failed(0,"请检查网络连接");
                    return;
                }
                callBack.success(tempList);
            }

            @Override
            public void failed(int errorCode, Object data) {
                callBack.failed(errorCode,data);
            }
        });
    }
}
