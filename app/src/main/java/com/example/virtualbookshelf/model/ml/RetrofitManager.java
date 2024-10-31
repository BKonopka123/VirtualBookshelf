package com.example.virtualbookshelf.model.ml;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The RetrofitManager class is responsible for managing the communication with the Google Books API.
 */
public class RetrofitManager {

    /**
     * API key for the Google Books API
     */
    private static final String API_KEY = "AIzaSyAsrcc0VpF-trPAOJDUEbsFMtarPQOGSHE";
    /**
     * GoogleBooksApi object
     */
    private final GoogleBooksApi apiService;
    /**
     * ArrayList of found text from tesseract
     */
    ArrayList<FoundObject> foundTitles;
    /**
     * ArrayList of found books from the Google Books API
     */
    ArrayList<FoundObject> foundBooks;

    /**
     * Constructor for the RetrofitManager class
     * @param foundTitles ArrayList of found text from tesseract
     */
    public RetrofitManager(ArrayList<FoundObject> foundTitles) {
        apiService = RetrofitClient.getClient().create(GoogleBooksApi.class);
        this.foundTitles = foundTitles;
        this.foundBooks = new ArrayList<>();
    }

    /**
     * Method to find books using the Google Books API
     * @param callback Callback<ArrayList<FoundObject>>
     */
    public void findBooks(final Callback<ArrayList<FoundObject>> callback) {
        foundBooks.clear();
        AtomicInteger completedRequests = new AtomicInteger(0);
        for (int i = 0; i < foundTitles.size(); i++) {
            callApi(foundTitles.get(i).getFoundText(), i, callback, completedRequests);
        }
    }

    /**
     * Method to call the Google Books API
     * @param Title String with the book title
     * @param i The current item from the list on which we run the query
     * @param callback Callback<ArrayList<FoundObject>>
     * @param completedRequests AtomicInteger to count the completed requests
     */
    private void callApi(String Title, int i, final Callback<ArrayList<FoundObject>> callback, AtomicInteger completedRequests){
        Call<BookResponse> call = apiService.searchBooks(Title, API_KEY);

        call.enqueue(new Callback<BookResponse>() {
            /**
             * The method is called when a positive response is received from the Google Books API
             * @param call Call<BookResponse>
             * @param response Response<BookResponse>
             */
            @Override
            public void onResponse(@NonNull Call<BookResponse> call, @NonNull Response<BookResponse> response) {
               //Positive response with a body
                if (response.isSuccessful() && response.body() != null) {
                    List<Volume> books = response.body().getItems();
                    if (books != null && !books.isEmpty()) {
                        int counter = 0;
                        for (Volume book : books) {
                            if (counter >= 3)
                                break;

                            VolumeInfo volumeInfo = book.getVolumeInfo();

                            FoundObject foundObject = new FoundObject(foundTitles.get(i).getImage(), foundTitles.get(i).getFoundText(), volumeInfo.getTitle(), volumeInfo.getAuthors(), volumeInfo.getPublishedDate(), volumeInfo.getDescription(), volumeInfo.getCategories(), false);
                            foundBooks.add(foundObject);
                            logFoundText(foundObject);

                            counter++;
                        }
                    }
                // Negative response - too many API calls for today
                } else if (response.code() == 429) {
                    Log.e("RetrofitManager", "Error calling API - too many requests - " + response.message());
                    foundBooks.clear();
                    callback.onFailure(fakeCall, new Exception("CODE_429"));
                    return;
                }
                // Negative response
                else {
                    Log.e("RetrofitManager", "Error calling API - no response - " + response.message());
                }

                // Check if all requests are completed
                if (completedRequests.incrementAndGet() == foundTitles.size()) {
                    Log.d("RetrofitManager", "Found books: " + foundBooks.size());
                    callback.onResponse(fakeCall, Response.success(foundBooks));
                }
            }

            /**
             * The method is called when a negative response is received from the Google Books API
             * @param call Call<BookResponse>
             * @param t Throwable
             */
            @Override
            public void onFailure(@NonNull Call<BookResponse> call, @NonNull Throwable t) {
                Log.e("RetrofitManager", "Error calling API - " + t.getMessage(), t);
                foundBooks.clear();
                callback.onFailure(fakeCall, t);
            }
        });
    }

    /**
     * Method to log the found text
     * @param foundObject FoundObject
     */
    private void logFoundText(FoundObject foundObject) {
        Log.d("RetrofitManager", "--------------------------------------------------------");
        if (foundObject.getImage() != null)
            Log.d("RetrofitManager", "Found image length: " + foundObject.getImage().length);
        else
            Log.d("RetrofitManager", "Found image is null");
        if (foundObject.getFoundText() != null)
            Log.d("RetrofitManager", "Found text: " + foundObject.getFoundText());
        else
            Log.d("RetrofitManager", "Found text is null");
        if (foundObject.getTitle() != null)
            Log.d("RetrofitManager", "Title: " + foundObject.getTitle());
        else
            Log.d("RetrofitManager", "Book Title is null");
        if (foundObject.getAuthors() != null)
            Log.d("RetrofitManager", "Authors: " + String.join(", ", foundObject.getAuthors()));
        else
            Log.d("RetrofitManager", "Authors are null");
        if (foundObject.getPublishedDate() != null)
            Log.d("RetrofitManager", "Published date: " + foundObject.getPublishedDate());
        else
            Log.d("RetrofitManager", "Published date is null");
        if (foundObject.getDescription() != null)
            Log.d("RetrofitManager", "Description: " + foundObject.getDescription());
        else
            Log.d("RetrofitManager", "Description is null");
        if (foundObject.getCategories() != null)
            Log.d("RetrofitManager", "Categories: " + String.join(", ", foundObject.getCategories()));
        else
            Log.d("RetrofitManager", "Categories are null");
        if (foundObject.getIsInDatabase())
            Log.d("RetrofitManager", "Is in database: true");
        else
            Log.d("RetrofitManager", "Is in database: false");
    }

    /**
     * Fake Call object
     */
    Call<ArrayList<FoundObject>> fakeCall = new Call<ArrayList<FoundObject>>() {
        @Override
        public Response<ArrayList<FoundObject>> execute() { return null; }
        @Override
        public void enqueue(@NonNull Callback<ArrayList<FoundObject>> callback) {}
        @Override
        public boolean isExecuted() { return false; }
        @Override
        public void cancel() {}
        @Override
        public boolean isCanceled() { return false; }
        @Override
        public Call<ArrayList<FoundObject>> clone() { return null; }
        @Override
        public Request request() { return null; }
        @Override
        public Timeout timeout() { return null; }
    };
}

