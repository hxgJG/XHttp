package com.hxg.http;

public class HttpUtils {
    public static <T, R> void  sendJsonRequest(T requestData, String url, Class<R> responseClass, IJsonDataListener<R> listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallback<>(responseClass, listener);
        HttpTask task = new HttpTask<>(url, requestData, httpRequest, callbackListener);
        ThreadPoolManager.getInstance().addTask(task);
    }
}
