package com.github.kevin.library.utils;

import com.alibaba.fastjson.JSON;
import com.github.kevin.library.listener.CallbackListener;
import com.github.kevin.library.request.IHttpRequest;

import java.io.UnsupportedEncodingException;

public class HttpTask<T> implements Runnable {
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
        mIHttpRequest.excute();

    }


}
