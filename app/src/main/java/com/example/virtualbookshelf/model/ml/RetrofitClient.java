package com.example.virtualbookshelf.model.ml;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * A helper class for the Retrofit library
 */
public class RetrofitClient {

    /**
     * Retrofit object
     */
    private static Retrofit retrofit;
    /**
     * Base URL for the Google Books API
     */
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";

    /**
     * Retrofit object getter
     * @return Retrofit object
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
