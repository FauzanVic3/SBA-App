package com.example.sbaapp.YoutubeIntegration.api;

import android.content.Context;
import android.content.DialogInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    public static String BASE_URL = "http://parkproject.asia/dine-social-api/public/";

    private static Retrofit.Builder retrofit = null;
    public static Context context;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
        }

        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getClient().client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, Context mContext, DialogInterface.OnClickListener callBack) {
        context = mContext;
        OkHttpClient client = httpClient.readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(600, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = getClient().client(client).build();
        return retrofit.create(serviceClass);
    }
}