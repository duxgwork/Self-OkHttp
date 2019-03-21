package com.github.kevin.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.github.kevin.library.listener.IJsonDataListener;
import com.github.kevin.library.OkHttp;
import com.github.kevin.library.bean.ResponseBean;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String URL = "xxxx";//模拟请求失败，测试重试机制
//    private static final String URL = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendReaquest(View view) {
        OkHttp.sendJsonRequest(null, URL, ResponseBean.class, new IJsonDataListener<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean bean) {
                Toast.makeText(MainActivity.this,"onSuccess：" + bean.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess: " + bean.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
