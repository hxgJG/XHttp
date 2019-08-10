package com.hxg.okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.hxg.http.HttpUtils;
import com.hxg.http.IJsonDataListener;

public class MainActivity extends AppCompatActivity {

    private String url = "uiuhjhfgdggdf";//"http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpUtils.sendJsonRequest(null, url, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass responseClass) {
                Log.i("hxg", responseClass.toString());
            }

            @Override
            public void onFailure() {
                Log.e("hxg", "请求失败");
            }
        });

    }
}
