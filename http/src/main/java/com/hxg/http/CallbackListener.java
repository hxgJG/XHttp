package com.hxg.http;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailed();
}
