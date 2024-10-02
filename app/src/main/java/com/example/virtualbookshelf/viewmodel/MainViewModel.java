package com.example.virtualbookshelf.viewmodel;

import android.app.Application;

/**
 * MainViewModel is a ViewModel responsible for managing data and actions in the main activity.
 * It inherits from BaseViewModel and doesn't currently implement any additional logic.
 */
public class MainViewModel extends BaseViewModel {

    /**
     * Constructor for MainViewModel.
     * Initializes the ViewModel by calling the constructor of BaseViewModel.
     *
     * @param application The application context.
     */
    public MainViewModel(Application application) {
        super(application);
    }
}