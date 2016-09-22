package com.example.nagato.mobiledeveloper.net;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nagato on 2016/9/18.
 */
public class APIService {
    private static volatile APIEndPoint apiService;

    private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40,TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build();

    private static final Gson gson=new GsonBuilder()
            .setDateFormat(com.example.nagato.mobiledeveloper.utils.DateUtils.DATE_FORMAT_ISO)
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();
    private static final Retrofit retrofit=new Retrofit.Builder()
            .baseUrl(APIEndPoint.BASE_URL)
                .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private APIService() {
    }

    public static APIEndPoint getDefaultInstance() {
        if (apiService == null) {
            synchronized (APIService.class) {
                if (apiService == null)
                    apiService = retrofit.create(APIEndPoint.class);
            }
        }
        return apiService;
    }
}
