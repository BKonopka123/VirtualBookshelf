package com.example.virtualbookshelf.model.ml;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for the Google Books API
 */
public interface GoogleBooksApi {

    /**
     * Search for books
     * @param query String with the search query
     * @param apiKey String with the API key
     * @return Call<BookResponse> with API response
     */
    @GET("volumes")
    Call<BookResponse> searchBooks(@Query("q") String query, @Query("key") String apiKey);
}
