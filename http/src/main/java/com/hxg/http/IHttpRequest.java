package com.hxg.http;

public interface IHttpRequest {
    void setUrl(String url);
    void serData(byte[] data);
    void setCallback(CallbackListener listener);
    void execute();
}
