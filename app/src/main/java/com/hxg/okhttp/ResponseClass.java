package com.hxg.okhttp;

public class ResponseClass {
    private String resultcode;
    private String error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    @Override
    public String toString() {
        return "ResponseClass{" +
                "resultcode='" + resultcode + '\'' +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}
