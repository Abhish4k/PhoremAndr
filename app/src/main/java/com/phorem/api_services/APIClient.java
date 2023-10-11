package com.phorem.api_services;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class APIClient {

    private static Retrofit retrofit = null;

    public static ApiInterface getClient() {

        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newBuilder()
                .addInterceptor(new ApiInterceptor())
                .connectTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.baseUrl)
                .addConverterFactory(factory)
                .client(okHttpClient).build();



        return retrofit.create(ApiInterface.class);
    }

}