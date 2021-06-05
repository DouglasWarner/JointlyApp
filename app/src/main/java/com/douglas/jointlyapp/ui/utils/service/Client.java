package com.douglas.jointlyapp.ui.utils.service;

import java.time.Duration;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url)
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //TODO cuidado con el callTimeout, puede ser muy poco tiempo
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .callTimeout(Duration.ofSeconds(5))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
