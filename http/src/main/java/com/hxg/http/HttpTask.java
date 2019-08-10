package com.hxg.http;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest httpRequest;
    private CallbackListener listener;
    private long delayTime;
    private int retryCount = 0;

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, CallbackListener listener) {
        try {
            this.httpRequest = httpRequest;
            this.listener = listener;
            httpRequest.setUrl(url);
            byte[] data = JSON.toJSONString(requestData).getBytes("UTF-8");
            httpRequest.serData(data);
            httpRequest.setCallback(listener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            httpRequest.execute();
        } catch (Exception e) {
            listener.onFailed();
            ThreadPoolManager.getInstance().addDelayTask(this);
            e.printStackTrace();
        }
    }

    public void setDelayTime(long time) {
        delayTime = time + System.currentTimeMillis();
    }

    public long getDelayTime() {
        return delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return 0;
    }
}
