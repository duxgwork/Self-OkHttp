package com.github.kevin.library;

import com.github.kevin.library.listener.CallbackListener;
import com.github.kevin.library.listener.IJsonDataListener;
import com.github.kevin.library.listener.impl.JsonCallbackListener;
import com.github.kevin.library.request.IHttpRequest;
import com.github.kevin.library.request.JsonHttpRequest;
import com.github.kevin.library.utils.HttpTask;
import com.github.kevin.library.utils.ThreadPoolManager;

public class OkHttp {

    public static <T,M> void sendJsonRequest(T requestData, String url, Class<M> response, IJsonDataListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response, listener);
        HttpTask httpTask = new HttpTask(requestData,url,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTast(httpTask);
    }

}
