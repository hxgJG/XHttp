package com.hxg.http;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallback<T> implements CallbackListener {
    private Class<T> responseClass;
    private IJsonDataListener<T> listener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallback(Class<T> clazz, IJsonDataListener<T> listener) {
        responseClass = clazz;
        this.listener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String content = getContent(inputStream);
        final T obj = JSON.parseObject(content, responseClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(obj);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onFailed() {
        listener.onFailure();
    }
}
