package com.hxg.http;

public interface IJsonDataListener<T> {
    void onSuccess(T t);
    void onFailure();
}
