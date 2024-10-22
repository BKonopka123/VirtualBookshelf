package com.example.virtualbookshelf.model.ml;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public class RetrofitManager {

    private final Retrofit retrofit;
    private final String BASE_URL = "https://www.googleapis.com/books/v1/";
    private final String API_KEY = "";

    public RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
