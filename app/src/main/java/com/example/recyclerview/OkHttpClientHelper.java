package com.example.recyclerview;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpClientHelper {

    private static OkHttpClient sClient;

    public static OkHttpClient getInstance() {
        if (sClient == null) {
            sClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
        }
        return sClient;
    }
}
