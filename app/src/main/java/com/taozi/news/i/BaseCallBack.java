package com.taozi.news.i;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 基础异步回调类
 */
public abstract class BaseCallBack
{
    /**
     * 请求成功
     *
     */
    public void success()
    {
    }

    /**
     *
     * 成功
     *
     * @param data
     *            Object 对象
     */
    public abstract void success(Object data);

    /**
     * 进度
     *
     * @param cur
     *            当前进度
     * @param total
     *            总大小
     * @param data
     *            消息实体
     */
    public void progress(long cur, long total, Object data)
    {

    }

    /**
     *
     * 请求成功
     *
     * @param data
     *            Object 对象数组
     */
    public void success(Object... data)
    {
    }

    /**
     * 失败
     *
     * @param errorCode
     *            错误码
     * @param data
     *            消息实体
     */
    public abstract void failed(int errorCode, Object data);

    /**
     * 服务器失败(包含400、401、403等服务器访问错误)
     *
     * @param data
     *            失败消息实体{@link com.android.volley.VolleyError}
     */
    public void failed(Object data)
    {
    };

}
