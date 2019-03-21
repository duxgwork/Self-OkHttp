package com.github.kevin.library.request;

import com.github.kevin.library.listener.CallbackListener;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest {
    private String url;
    private byte[] data;
    private CallbackListener callbackListener;
    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    public void excute() {
        try {
            URL url = new URL(this.url);
            // 打开Http连接
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            //是成员函数，仅作用于当前函数，设置这个连接是否可以被重定向
            urlConnection.setInstanceFollowRedirects(true);
            //响应超时时间
            urlConnection.setReadTimeout(3000);
            // 发送POST请求必须设置如下两行
            urlConnection.setDoOutput( true);//设置这个连接是否可以写入数据
            urlConnection.setDoInput( true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");
            // 设置消息的类型
            urlConnection.setRequestProperty( "Content-Type", "application/json;charset=UTF-8" );
            urlConnection.connect();//建立实际的连接

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bos.write(data);
            bos.flush();//刷新缓冲区，发送数据
            outputStream.close();
            bos.close();
            // 根据ResponseCode判断连接是否成功
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                callbackListener.onSuccess(inputStream);
            } else {
                callbackListener.onFailure();
                throw new RuntimeException("请求失败！");
            }
        } catch (Exception e) {
            System. out.println("发送 POST 请求出现异常！" +e);
            e.printStackTrace();
        } finally {
            //断开Http连接
            urlConnection.disconnect();
        }
    }

}
