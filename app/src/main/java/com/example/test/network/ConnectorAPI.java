package com.example.test.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectorAPI {

    public static final String BASE_URL_API = "http://192.168.1.87/api/";

    public static BaseAPIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL_API).create(BaseAPIService.class);

    }
}
