package com.github.kevin.library.utils;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.github.kevin.library.listener.CallbackListener;
import com.github.kevin.library.request.IHttpRequest;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable, Delayed{
    private IHttpRequest mIHttpRequest;

    public HttpTask(T requestData, String url, IHttpRequest httpRequest, CallbackListener callbackListener) {
        mIHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(callbackListener);
        String content = JSON.toJSONString(requestData);//使用阿里的JSON工具将泛型转换为String类型
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            mIHttpRequest.excute();
        } catch (Exception e) {
            e.printStackTrace();
            //将失败的任务添加到重试队列中
            ThreadPoolManager.getInstance().addDelayTask(this);
        }

    }

    /**
     * 该方法返回当前元素还需要延时多长时间。
     * @param unit
     * @return
     */
    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    /**
     * 实现compareTo方法来指定元素的顺序。例如，让延时时间最长的放在队列的末尾。
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NonNull Delayed o) {
        return 0;
    }

    private long delayTime;
    private int retryCount;

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        //设置延迟时间 3000毫秒
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

}
