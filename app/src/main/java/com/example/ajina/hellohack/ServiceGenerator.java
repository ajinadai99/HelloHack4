package com.example.ajina.hellohack;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ajina on 2017/08/11.
 */

public class ServiceGenerator {

    // ここでベースURLを指定する
    public static final String API_BASE_URL = "http://ik1-316-18424.vs.sakura.ne.jp:8001/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}