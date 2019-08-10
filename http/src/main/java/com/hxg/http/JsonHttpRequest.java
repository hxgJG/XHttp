package com.hxg.http;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpRequest implements IHttpRequest {
    private String url;
    private byte[] data;
    private CallbackListener listener;
    private HttpURLConnection urlConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void serData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setCallback(CallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void execute() {
        URL url = null;

        try {
            ///////////////请求设置//////////////////
            url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();


            ///////////////使用字节流发送数据////////////////////
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bos.write(data);
            bos.flush();
            outputStream.close();
            bos.close();

            ///////////////字符流写入数据/////////////////
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                listener.onSuccess(inputStream);
            } else {
                throw new RuntimeException("请求失败");
            }
        }catch (Exception e) {
            throw new RuntimeException("请求失败");
        }finally {
           urlConnection.disconnect();
        }
    }
}
