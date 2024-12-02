package com.example.virtualbookshelf.viewmodel.Book;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.virtualbookshelf.model.Book;

/**
 * ViewModel for the BookshelfDetailActivity.
 */
public class BookshelfDetailViewModel extends BookshelfViewModel {

    /**
     * LiveData to handle navigation to the main view.
     */
    protected final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();

    /**
     * Constructor for the BookshelfDetailViewModel.
     *
     * @param application The application context.
     */
    public BookshelfDetailViewModel(Application application) {
        super(application);
    }

    /**
     * Handles the click event on the back button.
     */
    public void onBackButtonClicked() {
        navigateBack.setValue(true);
    }

    /**
     * Gets the LiveData for navigation to the bookshelf view.
     *
     * @return The LiveData for navigation.
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Resets the navigation to the main view.
     */
    public void resetNavigationBack() { navigateBack.setValue(false); }

    /**
     * Updates the book in the database.
     *
     * @param book The book to be updated.
     */
    public void updateBook(Book book) {
        dbManager.updateBook(book);
    }
}
