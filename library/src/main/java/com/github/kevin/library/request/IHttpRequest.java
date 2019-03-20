package com.github.kevin.library.request;

import com.github.kevin.library.listener.CallbackListener;

/**
 * 封装请求接口
 */
public interface IHttpRequest {

    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallbackListener callbackListener);

    void excute();

}
