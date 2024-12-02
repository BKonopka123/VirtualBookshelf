package com.example.virtualbookshelf.viewmodel.Main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * BookFoundDetailViewModel is a ViewModel class for the BookFoundDetailFragment.
 */
public class BookFoundDetailViewModel extends MainFoundBooksViewModel {

    /**
     * LiveData for navigating back to the previous fragment.
     */
    protected final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();

    /**
     * Constructor for BookFoundDetailViewModel.
     *
     * @param application Application context.
     */
    public BookFoundDetailViewModel(Application application) {
        super(application);
    }

    /**
     * Handles the back button click event.
     */
    public void onBackButtonClicked() {
        navigateBack.setValue(true);
    }

    /**
     * Gets the LiveData for navigating back to the previous fragment.
     *
     * @return LiveData for navigating back.
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Resets the LiveData for navigating back to the previous fragment.
     */
    public void resetNavigationBack() { navigateBack.setValue(false); }
}
