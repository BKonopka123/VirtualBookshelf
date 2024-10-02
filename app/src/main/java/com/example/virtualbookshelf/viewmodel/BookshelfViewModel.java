package com.example.virtualbookshelf.viewmodel;

import android.app.Application;

/**
 * BookshelfViewModel is a ViewModel responsible for managing bookshelf-related data.
 * It inherits from BaseViewModel and doesn't currently implement any additional logic.
 */
public class BookshelfViewModel extends BaseViewModel {

    /**
     * Constructor for BookshelfViewModel.
     * Initializes the ViewModel by calling the constructor of BaseViewModel.
     *
     * @param application The application context.
     */
    public BookshelfViewModel(Application application) {
        super(application);
    }
}